package xmlsec;

//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.util.ArrayList;
//import java.util.Collections;
//import javax.crypto.*;
//import javax.xml.crypto.dom.*;
//import javax.xml.crypto.dsig.*;
//import javax.xml.crypto.dom.*;
//import javax.xml.crypto.dsig.keyinfo.*;
//import javax.xml.crypto.enc.*;
//import javax.xml.crypto.enc.dom.*;
//import javax.xml.crypto.enc.keyinfo.*;
//import org.w3c.dom.Element;
//
///**
// * This is an example of encrypting an XML element.
// */
public class XMLEnc {
//
//    public static void main(String[] args) throws Exception {
//
//	/* Create DOM factory */
//	XMLEncryptionFactory fac = XMLEncryptionFactory.getInstance();
//
//	EncryptionMethod em = fac.newEncryptionMethod
//	    (EncryptionMethod.AES128_CBC, new Integer(128), null);
//
//        // Create an AES Key
//        KeyGenerator kg = KeyGenerator.getInstance("AES");
//	kg.init(128);
//        SecretKey key = kg.generateKey();
//
//	// Create KeyInfo with RetrievalMethod and KeyName
//	KeyInfoFactory kfac = KeyInfoFactory.getInstance();
//	RetrievalMethod rm = kfac.newRetrievalMethod
//	    ("#EK", EncryptedKey.TYPE, null);
//	KeyName kn = kfac.newKeyName("Alice");
//	ArrayList kiTypes = new ArrayList();
//	kiTypes.add(rm);
//	kiTypes.add(kn);
//	KeyInfo ki = kfac.newKeyInfo(kiTypes);
//
//	// Create EncryptedKey
//	EncryptionMethod ekem = fac.newEncryptionMethod
//	    (EncryptionMethod.RSA_1_5, null, null);
//	KeyName ekn = kfac.newKeyName("Bob");
//	KeyInfo eki = kfac.newKeyInfo(Collections.singletonList(ekn));
//	DataReference dr = fac.newDataReference("#ED", null);
//
//        ToBeEncryptedKey tbeKey = new ToBeEncryptedKey(key);
//
//	EncryptedKey ek = fac.newEncryptedKey
//	    (tbeKey, ekem, eki, null, Collections.singletonList(dr), "Alice", "Bob", "EK");
//
//	// Create RSA KeyPair
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//        KeyPair kp = kpg.generateKeyPair();
//
//	// Create key encryption context
//	Element parent = null; //XXX - code missing to identify parent node
//			       //XXX - of to be marshalled EncryptedKey
//	XMLEncryptContext ekxec = new DOMEncryptContext(kp.getPublic(), parent);
//
//	// Encrypt key
//	ek.encrypt(ekxec);
//
//	/* Create encryption context for encrypted data */
//	XMLEncryptContext xec = new DOMEncryptContext(key);
//
//        Element elem = null; // Element to be encrypted
//
//        ToBeEncrypted tbeElement = new DOMToBeEncryptedXML(elem, null);
//
//        // Encrypt element (as CipherValue)
//        /* Create EncryptedData */
//	EncryptedData ed = fac.newEncryptedData(tbeElement, em, ki, null, "ED");
//        ed.encrypt(xec);
//
//        // Alternatively, encrypt element as CipherReference
//        /*
//	EncryptedData ed = fac.newEncryptedData(tbeElement, em, ki, null, "ED",
//	    EncryptedType.ELEMENT, null, null, fac.newCipherReference("#cipherReference", null));
//        ed.encrypt(xec);
//        InputStream cipherText = ed.getCipherText();
//        // the cipherText octet sequence should then be placed in the location which
//        // dereferencing "#cipherReference" will result to
//        */
//
//    }
}