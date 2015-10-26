package xmlsec;

import java.util.ArrayList;
import java.util.Collections;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class XMLDSig {

    public static void main(String[] args) {
        try {
            new XMLDSig().run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void run() throws Exception {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(System.in);

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        DigestMethod digestMethod = fac.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null);
        Reference ref = fac.newReference("#10", digestMethod);
        ArrayList refList = new ArrayList();
        refList.add(ref);
        CanonicalizationMethod cm = fac.newCanonicalizationMethod(
                "http://www.w3.org/2001/10/xml-exc-c14n#", new ExcC14NParameterSpec());
        SignatureMethod sm = fac.newSignatureMethod(
                "http://www.w3.org/2000/09/xmldsig#rsa-sha1", null);
        SignedInfo signedInfo = fac.newSignedInfo(cm, sm, refList);
        DOMSignContext signContext = null;
        signContext = new DOMSignContext(privKey, doc.getParentNode());
        signContext.setURIDereferencer(new URIResolverImpl());
        KeyInfoFactory keyFactory = KeyInfoFactory.getInstance();
        DOMStructure domKeyInfo = new DOMStructure(tokenReference);
        KeyInfo keyInfo =
                keyFactory.newKeyInfo(Collections.singletonList(domKeyInfo));
        XMLSignature signature = fac.newXMLSignature(signedInfo, keyInfo);
        signature.sign(signContext);
    }
}
