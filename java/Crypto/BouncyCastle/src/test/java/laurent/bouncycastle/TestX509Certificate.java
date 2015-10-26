package laurent.bouncycastle;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestX509Certificate {

    public static final String CERT_FILE = "/valicert.cer";
    public static final String[] X500_PRINCIPAL_FORMATS = {
        X500Principal.CANONICAL,
        X500Principal.RFC1779,
        X500Principal.RFC2253
    };

    @Test
    public void testX509Certificate() throws Exception {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(
                getClass().getResourceAsStream(CERT_FILE));

        System.out.printf("==== java.security.cert.X509Certificate ====\n");
        System.out.printf("IssuerDN='%s'\n", cert.getIssuerDN());
        System.out.printf("SubjectDN='%s'\n", cert.getSubjectDN());
        for (String f : X500_PRINCIPAL_FORMATS) {
            System.out.printf("X500Principal.Name(%s)='%s'\n",
                    f, cert.getSubjectX500Principal().getName(f));
        }
        System.out.println();

        System.out.printf("==== org.bouncycastle.jce.X509Principal ====\n");
        X509Principal principal = PrincipalUtil.getSubjectX509Principal(cert);
        System.out.printf("Name='%s'\n", principal.getName());
        System.out.printf("CN(%s)='%s'\n", X509Name.CN, principal.getValues(X509Name.CN));
        System.out.printf("E(%s)='%s'\n", X509Name.E, principal.getValues(X509Name.E));

        Vector<?> values = principal.getValues();
        Vector<?> oids = principal.getOIDs();
        assertEquals(values.size(), oids.size());
        for (int i = 0; i < values.size(); ++i) {
            System.out.printf("OID(%s)='%s'\n", oids.get(i), values.get(i));
        }
    }
}
