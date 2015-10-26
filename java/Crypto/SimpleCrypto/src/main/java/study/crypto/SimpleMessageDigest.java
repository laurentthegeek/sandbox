package study.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;

public class SimpleMessageDigest {

    private static final int[] FILE_SIZES = {10}; //, 50, 100, 500, 1000};
    private static final String[][] DIGEST_ALGO = new String[][]{
        {"MD2", "md2"},
        {"MD5", "md5"},
        {"SHA-1", "sha1"},
        {"SHA-256", "sha256"},
        {"SHA-384", "sha384"},
        {"SHA-512", "sha512"}
    };

    public static void test() {

        System.out.println("MessageDigest benchmark started");


        try {
            final int ELAPSE_SEC = 10;

            prepare();

            for (String[] algo : DIGEST_ALGO) {
                int mbs = benchDigestMem(algo[0], ELAPSE_SEC * 1000);

                File file = findFile(ELAPSE_SEC * mbs);

                benchFileRead(file);
                benchDigestFile(algo[0], file);
                benchOpenSSLDigest(algo[1], file);
            }

        //cleanup();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Bye!");
    }

    private static File benchFileWrite(int mb) throws IOException {

        File file = openFile(mb);

        System.out.printf("Benchmarking file write (size %d Mb)...\n", mb);

        long begin = System.currentTimeMillis();

        file.createNewFile();

        Random rand = new Random(0);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[8192];
        int max = (mb * 1024 * 1024) / buf.length;
        for (int i = 0; i < max; ++i) {
            rand.nextBytes(buf);
            fos.write(buf);
        }

        fos.close();
        long end = System.currentTimeMillis();
        int mbs = (mb * 1000) / (int) (end - begin);

        System.out.println(file.getName());
        System.err.printf("Benchmark: FILE-WRITE, %dMb/s (%dMb done)\n", mbs, mb);

        return file;
    }

    private static int benchFileRead(File file) throws IOException {

        System.out.printf("Benchmarking file read (size %d Mb)...\n", file.length());

        long begin = System.currentTimeMillis();

        InputStream is = new FileInputStream(file);
        byte[] buf = new byte[8192];
        long read = 0;
        int len;
        while ((len = is.read(buf)) != -1) {
            read += len;
        }
        is.close();

        assert read == file.length();

        long end = System.currentTimeMillis();
        int mb = (int) (read / (1024 * 1024)), mbs = (mb * 1000) / (int) (end - begin);

        System.out.println(file.getName());
        System.err.printf("Benchmark: FILE-READ, %dMb/s (%dMb done)\n", mbs, mb);

        return mbs;
    }

    private static int benchDigestMem(String algo, int msec) throws NoSuchAlgorithmException {

        System.out.printf("Benchmarking %s digest memory...\n", algo);

        long begin = System.currentTimeMillis();
        Random rand = new Random(0);
        MessageDigest md = MessageDigest.getInstance(algo);

        // Digest 1MB of random bytes until enough elapse times spent
        int mb = 0;
        do {
            byte[] buf = new byte[1024];
            rand.nextBytes(buf);

            for (int i = 0; i < 1024; ++i) {
                md.update(buf);
            }
            ++mb;
        } while (System.currentTimeMillis() - begin < msec);

        byte[] digest = md.digest();

        long end = System.currentTimeMillis();
        int mbs = (mb * 1000) / (int) (end - begin);

        String hex = new String(Hex.encodeHex(digest));
        System.out.printf("%s(RANDOM)=%s\n", algo, hex);
        System.err.printf("Benchmark: DIGEST-%s-MEM, %dMb/s (%dMb done)\n", algo, mbs, mb);

        return mbs;
    }

    private static int benchDigestFile(String algo, File file) throws IOException, NoSuchAlgorithmException {

        System.out.printf("Benchmarking %s digest file...\n", algo);

        long begin = System.currentTimeMillis();
        MessageDigest md = MessageDigest.getInstance(algo);

        InputStream is = new FileInputStream(file);
        byte[] buf = new byte[8192];
        long b = 0;

        for (;;) {
            int read = is.read(buf);
            if (read == -1) {
                break;
            }
            md.update(buf, 0, read);
            b += read;
        }

        byte[] digest = md.digest();

        long end = System.currentTimeMillis();
        int mb = (int) (b / (1024 * 1024));
        int mbs = (mb * 1000) / (int) (end - begin);

        String hex = new String(Hex.encodeHex(digest));
        System.out.printf("%s(%s)=%s\n", algo, file.getName(), hex);
        System.err.printf("Benchmark: DIGEST-%s-FILE, %dMb/s (%dMb done)\n", algo, mbs, mb);

        return mbs;
    }

    private static int benchOpenSSLDigest(String algo, File file) throws IOException, NoSuchAlgorithmException, InterruptedException {

        System.out.printf("Benchmarking %s openssl digest...\n", algo);

        long begin = System.currentTimeMillis();

        Runtime rt = Runtime.getRuntime();
        String cmd = String.format("openssl dgst -%s %s\n", algo, file);
        System.out.print(cmd);
        Process proc = rt.exec(cmd);
        proc.waitFor();
        String out = toString(proc.getInputStream());

        long end = System.currentTimeMillis();
        int mb = (int) (file.length() / (1024 * 1024));
        int mbs = (mb * 1000) / (int) (end - begin);

        System.out.print(out);
        System.err.printf("Benchmark: OPENSSL-DIGEST-%s, %dMb/s (%dMb done)\n", algo, mbs, mb);

        return mbs;
    }

    private static void cleanup() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static File findFile(int mb) {

        int found = -1;

        for (int size : FILE_SIZES) {
            if (size >= mb) {
                found = size;
                break;
            }
        }

        return openFile(found != -1 ? found : FILE_SIZES[FILE_SIZES.length - 1]);
    }

    private static File openFile(int mb) {
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpdir, String.format("jbench-%dMB.tmp", mb));

        assert file.exists();

        return file;
    }

    private static void prepare() throws IOException {

        System.out.printf("Preparing benchmark environment...\n");

        for (int mb : FILE_SIZES) {

            File file = openFile(mb);
            if (file.exists()) {
                if (file.length() / (1024 * 1024) == mb) {
                    System.out.printf("file %s already exists: reuse it!\n", file.getName());
                    continue;
                }

                file.delete();
            }

            benchFileWrite(mb);
        }

        System.out.printf("Done prepare!\n");

    }

    private static String toString(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is);
        StringBuilder builder = new StringBuilder();

        char[] cbuf = new char[1024];
        int len;
        while ((len = reader.read(cbuf)) != -1) {
            builder.append(cbuf, 0, len);
        }

        return builder.toString();
    }
}
