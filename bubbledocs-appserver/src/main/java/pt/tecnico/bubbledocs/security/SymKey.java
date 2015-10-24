package pt.tecnico.bubbledocs.security;

//provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Program to read and write symmetric key file
 */
public class SymKey {

	public static void write(String keyPath) throws Exception {
		// get a DES private key
		System.out.println("Generating DES key ...");
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		Key key = keyGen.generateKey();
		System.out.println("Finish generating DES key");
		byte[] encoded = key.getEncoded();
		System.out.println("Key:");
		System.out.println(printHexBinary(encoded));

		System.out.println("Writing key to '" + keyPath + "' ...");

		FileOutputStream fos = new FileOutputStream(keyPath);
		fos.write(encoded);
		fos.close();
	}

	public static void writeKeyToFile(Key key) throws Exception {
		String keyPath = (System.getProperty("user.dir") + "/src/main/resources/clientStoreKey.key");
		FileOutputStream fos = new FileOutputStream(keyPath);
		byte[] encoded = key.getEncoded();
		fos.write(encoded);
		fos.close();
	}

	public static Key read(String keyPath) throws Exception {
		System.out.println("Reading key from file " + keyPath + " ...");
		FileInputStream fis = new FileInputStream(keyPath);
		byte[] encoded = new byte[fis.available()];
		fis.read(encoded);
		fis.close();

		DESKeySpec keySpec = new DESKeySpec(encoded);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Key key = keyFactory.generateSecret(keySpec);
		return key;
	}

	public static byte[] encryptBytesWithKey(Key key, byte[] plainBytes)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		System.out.println("Ciphering ...");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherBytes = cipher.doFinal(plainBytes);
		System.out.println("Result:");
		System.out.println(printHexBinary(cipherBytes));
		// return printHexBinary(cipherBytes);
		return cipherBytes;
	}

	public static String decryptBytesWithKey(Key key, byte[] cipherBytes)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		System.out.println("Deciphering...");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] newPlainBytes = cipher.doFinal(cipherBytes);
		System.out.println("Result:");
		System.out.println(printHexBinary(newPlainBytes));

		System.out.println("Text:");
		String newPlainText = new String(newPlainBytes);
		System.out.println(newPlainText);

		return newPlainText;
	}

	public static byte[] digestUsingMD5(byte[] toDigest) throws Exception {

		// get a message digest object using the MD5 algorithm
		MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance("MD5");
		System.out.println(messageDigest.getProvider().getInfo());

		System.out.println("Computing digest ...");
		messageDigest.update(toDigest);
		return messageDigest.digest();

	}

	public static String createTicket(String userID, String service, String n,
			String key) {
		Date date = new Date();

		return userID + ";" + service + ";" + date.getHours() + ";" + n + ";"
				+ key;
	}

	public static String createSessionKey(String key, String time) {
		return key + ";" + time;
	}

	public static Key createKeyFromString(byte[] keyByte) throws Exception {
		DESKeySpec keySpec = new DESKeySpec(keyByte);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Key key = keyFactory.generateSecret(keySpec);
		return key;
	}

}
