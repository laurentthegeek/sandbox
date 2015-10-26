package commons.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ProxyOutputStream;
import org.apache.commons.io.output.ThresholdingOutputStream;

public class MyOutputStream extends ThresholdingOutputStream {

    private static class ProxyOutputStreamImpl extends ProxyOutputStream {

        private final int reached;

        public ProxyOutputStreamImpl(int reached) {
            super(System.out);
            this.reached = reached;

        }

        @Override
        protected void beforeWrite(int n) {
            System.out.printf("%d:", reached);

        }

        @Override
        protected void afterWrite(int n) {
            System.out.println();

        }
    }
    
    private int reached = 0;
    private OutputStream output = new ProxyOutputStreamImpl(reached);

    public MyOutputStream(int count) {
        super(count);
    }

    @Override
    protected OutputStream getStream() throws IOException {
        return output;
    }

    @Override
    protected void thresholdReached() throws IOException {
        output = new ProxyOutputStreamImpl(++reached);
        this.resetByteCount();
    }
}
