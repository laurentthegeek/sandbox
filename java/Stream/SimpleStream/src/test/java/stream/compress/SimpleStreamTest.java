package stream.compress;

import java.util.Random;
import java.util.concurrent.Callable;
import org.junit.Test;

public class SimpleStreamTest {

    private static final int ENTRY_MAX = 1000;

    private static class Randomizer implements Callable<byte[]> {

        private final Random RANDOM = new Random();

        public byte[] call() throws Exception {
            byte[] buffer = new byte[500];
            RANDOM.nextBytes(buffer);
            return buffer;
        }
    }

    private static class Xmlizer implements Callable<byte[]> {

        private int i = 0;

        public byte[] call() throws Exception {
            String value = String.format(
                    "<entry><uniqueKey>%010d</uniqueKey><foreignKey>123456789</foreignKey></entry>", ++i);
            return value.getBytes();
        }
    }

    @Test
    public void testRandomGZip() throws Exception {
        Compressor c = new Compressor("target/random.gz");
        c.gzip(ENTRY_MAX, new Randomizer());
        System.out.printf("length=%d bytes\n", c.length());
    }

    @Test
    public void testRandomTgz() throws Exception {
        Compressor c = new Compressor("target/random.tar.gz");
        c.targz(ENTRY_MAX, "e%d.bin", new Randomizer());
        System.out.printf("length=%d bytes\n", c.length());
    }

    @Test
    public void testRandomZip() throws Exception {
        Compressor c = new Compressor("target/random.zip");
        c.zip(ENTRY_MAX, "e%d.bin", new Randomizer());
        System.out.printf("length=%d bytes\n", c.length());
    }

    @Test
    public void testXmlGZip() throws Exception {
        Compressor c = new Compressor("target/xml.gz");
        c.gzip(ENTRY_MAX, new Xmlizer());
        System.out.printf("length=%d bytes\n", c.length());
    }

    @Test
    public void testXmlTgz() throws Exception {
        Compressor c = new Compressor("target/xml.tar.gz");
        c.targz(ENTRY_MAX, "e%d.xml", new Xmlizer());
        System.out.printf("length=%d bytes\n", c.length());
    }

    @Test
    public void testXmlZip() throws Exception {
        Compressor c = new Compressor("target/xml.zip");
        c.zip(ENTRY_MAX, "e%d.xml", new Xmlizer());
        System.out.printf("length=%d bytes\n", c.length());
    }
}