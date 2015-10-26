package stream.compress;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

public class Compressor {

    private final File FILE;

    public long length() {
        return FILE.length();
    }

    public Compressor(String filename) {
        FILE = new File(filename);
    }

    public void gzip(int nbEntry, Callable<byte[]> value) throws Exception {
        System.out.printf("Create gzip file '%s' with %d entries\n", FILE.getAbsoluteFile(), nbEntry);

        FileOutputStream file = new FileOutputStream(FILE);
        GZIPOutputStream gzip = new GZIPOutputStream(file);

        for (int i = 0; i < nbEntry; ++i) {
            gzip.write(value.call());
        }

        gzip.close();
        file.close();
    }

    public void targz(int nbEntry, String entryName, Callable<byte[]> value) throws Exception {
        System.out.printf("Create gzip file '%s' with %d entries\n", FILE.getAbsoluteFile(), nbEntry);

        FileOutputStream file = new FileOutputStream(FILE);
        GZIPOutputStream gzip = new GZIPOutputStream(file);
        TarOutputStream tar = new TarOutputStream(gzip);

        for (int i = 0; i < nbEntry; ++i) {
            byte[] buffer = value.call();
            TarEntry entry = new TarEntry(String.format(entryName, i));
            entry.setSize(buffer.length);
            tar.putNextEntry(entry);
            tar.write(buffer);
            tar.closeEntry();
        }

        tar.close();
        gzip.close();
        file.close();
    }

    public void zip(int nbEntry, String entryName, Callable<byte[]> value) throws Exception {
        System.out.printf("Create tar.gz file '%s' with %d entries\n", FILE.getAbsoluteFile(), nbEntry);

        FileOutputStream file = new FileOutputStream(FILE);
        ZipOutputStream zip = new ZipOutputStream(file);

        for (int i = 0; i < nbEntry; ++i) {
            zip.putNextEntry(new ZipEntry(String.format(entryName, i)));
            zip.write(value.call());
            zip.closeEntry();
        }

        zip.close();
        file.close();
    }
}
