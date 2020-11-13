package package_password;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MyFirstSignature {

	private String message;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public MyFirstSignature(String message) throws NoSuchAlgorithmException {
		this.message = message;
		genereBiCleRsa();
		System.out.println("Clé publique: "+ this.publicKey.toString());
		System.out.println("Clé privée: "+ this.privateKey.toString() + "\n");
	}
	
	private void genereBiCleRsa() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}
	
	private byte[] chiffrer(String pMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, this.privateKey);
		byte[] res = pMessage.getBytes("UTF-8");
		res = c.doFinal(res);
		return res;
	}
	
	public byte[] signe() {
		String hashed = new String();
		byte[] res = null;
		try {
			hashed = MyPassword.hacheSha256(this.message);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			res = chiffrer(hashed);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String dechiffrer(byte pCondensat[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String res = new String();
		byte[] decrypted = null;
		
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, this.publicKey);
		decrypted = c.doFinal(pCondensat);
		res = new String(decrypted);
		
		System.out.println("Le déchiffrement est \n" + res + "\n");
		return res;
	}
	
	public boolean verifierSignature(byte pCondensat[]) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		return (MyPassword.hacheSha256(this.message).equals(dechiffrer(pCondensat)));
	}
	
	public static void main(String[] argv) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		MyFirstSignature signature = new MyFirstSignature("Je signe un message électroniquement");
		byte[] sig = signature.signe();
		String crypted = new String(sig);
		System.out.println("La signature du message [" + signature.message + "] est\n" + crypted + "\n");
		
		if(signature.verifierSignature(sig))
			System.out.println("La signature est valide");
	}
}
