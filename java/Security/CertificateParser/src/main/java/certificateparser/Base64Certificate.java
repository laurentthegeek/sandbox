package certificateparser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.util.encoders.Base64;

public class Base64Certificate
{
    public static void main(String[] args)
    {
        try
        {
            if (args.length == 0)
                throw new IllegalArgumentException("missing argument 'filename'");
            
            // reads the file as a stream of base 64 bytes (assuming ASCII file)
            File file = new File(args[0]);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String strB64Cert = reader.readLine();
            reader.close();

            // decodes base64 into binary DER and generates certificates
            byte[] binCert = Base64.decode(strB64Cert);
            ByteArrayInputStream istream = new ByteArrayInputStream(binCert);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(istream);
        
            // converts certificate into a single line base64 encoded value
            byte[] derCert = cert.getEncoded();
            byte[] b64Cert = Base64.encode(derCert);

            // digests the base64 encoded certificate
            MessageDigest digestEncoder = MessageDigest.getInstance("SHA-256");
            byte[] digest = digestEncoder.digest(b64Cert);
            byte[] b64Digest = Base64.encode(digest);

            // prints the digest
            String s = new String(b64Digest);
            System.out.printf("SHA256(%s)=%s\n", file.getName(), s);
        }
        catch (Exception ex)
        {
            Logger.getLogger(PemOrDerCertificate.class.getName()).log(Level.SEVERE, "caught exception", ex);
        }
    }
}
