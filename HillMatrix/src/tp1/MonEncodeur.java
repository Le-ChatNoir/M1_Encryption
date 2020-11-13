package tp1;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey; 

public class MonEncodeur {  
	
	public static String encodeCesar(String text, int key) {   
		String res = "";
		
		for(int i = 0; i<text.length(); i++) {
			if(Character.isUpperCase(text.charAt(i))) {
				res += (char) (((int)text.charAt(i) + key - 65) % 26 + 65);
			} else {
				res += text.charAt(i);
			}
		}
		
		return res;
	}    
	
	public static String decodeCesar(String text, int key) {   
		String res = "";
		
		for(int i = 0; i<text.length(); i++) {
			if(Character.isUpperCase(text.charAt(i))) {
				res += (char) (((int)text.charAt(i) - key - 65 + 26) % 26 + 65);
			} else {
				res += text.charAt(i);
			}
		}
		
		return res;
	}
	
	public static String encodeVigenere(String text, String key) {
		String res = "";
		int lengthKey = key.length();
		int indexKey = 0;
		
		for(int i = 0; i<text.length(); i++) {
			if(Character.isUpperCase(text.charAt(i))) {
				res += (char) (((int)text.charAt(i)-65 + (int)key.charAt(indexKey) - 65) % 26 + 65);
				indexKey = (indexKey+1) % lengthKey;
			} else {
				res += text.charAt(i);
			}
		}
		
		return res;
	}
	
	public static String decodeVigenere(String text, String key) {
		String res = "";
		int lengthKey = key.length();
		int indexKey = 0;
		
		for(int i = 0; i<text.length(); i++) {
			if(Character.isUpperCase(text.charAt(i))) {
				res += (char) ((((int)text.charAt(i) - 65) - ((int)key.charAt(indexKey) - 65) + 26) % 26 + 65);
				indexKey = (indexKey+1) % lengthKey;
			} else {
				res += text.charAt(i);
			}
		}
		
		return res;
	}
	
	public static String encodeHill(String text, int keySize, String key) {
		String res = "";
		int[] resMatrix = new int[keySize];
		
		//Generating the Matrix using the key word and key size
		int keyMatrix[][] = new int[keySize][keySize];
		HillMatrixGen.getKeyMatrix(key, keySize, keyMatrix);
		
		System.out.println("Matrix key:");
		HillMatrixGen.printMatrix(keyMatrix);
		
		//Cutting text to ecrypt in packets of size keySize
		String[] cutText = HillMatrixGen.getMessageCut(text, keySize);
		
		//Matrix multiplying
		for(String token: cutText) {
			for(int i = 0; i < keySize; i++) {
				for(int j = 0; j < keySize; j++) {
					//System.out.println("Doing char : " +  token.charAt(j) + "(" + (int)(token.charAt(j) -65) + ") * " + keyMatrix[i][j] + " = " + ((token.charAt(j) - 65) * keyMatrix[i][j]));
					resMatrix[i] += ((token.charAt(j) - 65) * keyMatrix[i][j]);
				}
			}
			
			//Doing % 26 and forming the encoded message
			for(int i = 0; i < resMatrix.length; i++) {
				resMatrix[i] = resMatrix[i] % 26;
				res += (char) (resMatrix[i] + 65);
				//Cleaning matrix
				resMatrix[i] = 0;
			}
			
		}
		
		return res;
	}
	
	public static String decodeHill(String text, int keySize, String key) {
		String res = "";
		int[] resMatrix = new int[keySize];
		
		//Generating the Matrix using the key word and key size
		int keyMatrix[][] = new int[keySize][keySize];
		HillMatrixGen.getKeyMatrix(key, keySize, keyMatrix);
		
		//Generating invert Matrix
		float[][] invertMatrix = new float[keySize][keySize];
		HillMatrixGen.getInverse(keyMatrix, invertMatrix, keySize);
		
		//Making the matrix % 26
		for(int i =0; i<keySize; i++) {
			for(int j =0; j<keySize; j++) {
				invertMatrix[i][j] = (invertMatrix[i][j] + 26)%26;
				
				//Chech if matrix valid for decyphering
				if(invertMatrix[i][j] % 1 != 0.0) {
					System.out.println("Matrix unfit for decyphering. Not coprime with 26.");
					return "";
				}
			}
		}
		System.out.println("Inverted matrix key:");
		HillMatrixGen.printMatrix(invertMatrix);
		
		//Cutting text to ecrypt in packets of size keySize
		String[] cutText = HillMatrixGen.getMessageCut(text, keySize);
				
		//Matrix multiplying
		for(String token: cutText) {
			for(int i = 0; i < keySize; i++) {
				for(int j = 0; j < keySize; j++) {
					//System.out.println("Doing char : " +  token.charAt(j) + "(" + (int)(token.charAt(j) -65) + ") * " + keyMatrix[i][j] + " = " + ((token.charAt(j) - 65) * keyMatrix[i][j]));
					resMatrix[i] += ((token.charAt(j) - 65) * invertMatrix[i][j]);
				}
			}
					
			//Doing % 26 and forming the encoded message
			for(int i = 0; i < resMatrix.length; i++) {
				resMatrix[i] = resMatrix[i] % 26;
				res += (char) (resMatrix[i] + 65);
				//Cleaning matrix
				resMatrix[i] = 0;
			}
					
		}
		
		return res;
	}
	
	public static String encodeAES(String s, SecretKey secretKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		
		Cipher codeur = Cipher.getInstance("AES");
		codeur.init(Cipher.ENCRYPT_MODE, secretKey);
		
		byte[] texteCode = codeur.doFinal(s.getBytes());
		
		return Base64.getEncoder().encodeToString(texteCode);
	}
	
	public static String decodeAES(String s, SecretKey secretKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher codeur = Cipher.getInstance("AES");
		codeur.init(Cipher.DECRYPT_MODE, secretKey);
		
		byte[] texteNonCode = codeur.doFinal(Base64.getDecoder().decode(s.getBytes()));
		return new String(texteNonCode);
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		
		//CESAR
		System.out.println("######## CESAR ########");
		System.out.println("====Encoding====");
		String secretMessage = "THIS IS A VERY SECRET MESSAGE!!";
		String encoded = "";
		encoded = MonEncodeur.encodeCesar(secretMessage, 6);
		System.out.println("Base message : \"" + secretMessage + "\"\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n====Decoding====");
		secretMessage = "GZZGIQ GZ UTIK ABCDE!!";
		String decoded = MonEncodeur.decodeCesar(secretMessage, 6);
		System.out.println("Base encoded message : \"" + secretMessage + "\"\nDecoded message : \"" + decoded + "\"\n\n");
		
		//VIGENERE
		System.out.println("######## VIGENERE ########");
		System.out.println("====Encoding====");
		secretMessage = "THE LEGION IS AS GOOD AS DEAD!!";
		String key = "HOOVER";
		encoded = "";
		encoded = MonEncodeur.encodeVigenere(secretMessage, key);
		System.out.println("Base message : \"" + secretMessage + "\"\nKey : " + key + "\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n====Decoding====");
		secretMessage = "POPRIZSE SDX YRG GHZ CYZD!!";
		key = "NAVARRO";
		decoded = MonEncodeur.decodeVigenere(secretMessage, key);
		System.out.println("Base encoded message : \"" + secretMessage + "\"\nKey : " + key + "\nDecoded message : \"" + decoded + "\"\n\n");
		
		//HILL
		System.out.println("######## HILL ########");
		System.out.println("====Encoding====");
		secretMessage = "BONJOUR";
		key = "CDFH";
		int keySize = 2;
		encoded = "";
		encoded = MonEncodeur.encodeHill(secretMessage, keySize, key);
		System.out.println("Base message : \"" + secretMessage + "\"\nKey : " + key + "\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n====Decoding====");
		secretMessage = "HOPTAKGIYRKMRUGOSCSDHOLBZPIHYG";
		decoded = MonEncodeur.decodeHill(secretMessage, keySize, key);
		System.out.println("Base encoded message : \"" + secretMessage + "\"\nKey : " + key + "\nDecoded message : \"" + decoded + "\"\n\n");
		
		//AES
		KeyGenerator cle = KeyGenerator.getInstance("AES");
		cle.init(128);
		SecretKey secretKey = cle.generateKey();
		
		System.out.println("######## AES ########");
		System.out.println("====Encoding====");
		secretMessage = "NO WAY CAESAR IS GETTING HIS HANDS ON THIS MESSAGE!!";
		encoded = MonEncodeur.encodeAES(secretMessage, secretKey);
		System.out.println("Base message : \"" + secretMessage + "\"\nKey : " + secretKey.toString() + "\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n====Decoding====");
		secretMessage = encoded;
		decoded = MonEncodeur.decodeAES(secretMessage, secretKey);
		System.out.println("Base encoded message : \"" + secretMessage + "\"\nKey : " + secretKey.toString() + "\nDecoded message : \"" + decoded + "\"\n\n");
	} 
	
}


