package laurent.bouncycastle;

import javax.security.cert.X509Certificate;
import org.junit.Test;

public class TesX509Certificate2 {

    @Test
    public void testX509Certificate() throws Exception {

        X509Certificate cert = X509Certificate.getInstance(
                getClass().getResourceAsStream(TestX509Certificate.CERT_FILE));

        System.out.printf("==== javax.security.cert.X509Certificate ====\n");
        System.out.printf("IssuerDN='%s'\n", cert.getIssuerDN());
        System.out.printf("SubjectDN='%s'\n", cert.getSubjectDN());
    }
}
