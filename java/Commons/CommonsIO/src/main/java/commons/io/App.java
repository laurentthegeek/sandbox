package commons.io;

import java.io.IOException;
import java.io.OutputStream;

public class App
{
    public static void main( String[] args ) throws IOException
    {
        OutputStream out = new MyOutputStream(10);
        for (int i=0;i<1000;++i) {
            out.write(String.format("%d",i).getBytes());
        }
    }
}
