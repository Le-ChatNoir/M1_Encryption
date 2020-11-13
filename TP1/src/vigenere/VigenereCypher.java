package vigenere;

public class VigenereCypher {

	public static String cypherText(String text, String key) {
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
	
	public static String decypherText(String text, String key) {
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
	
	public static void main(String[] args) {
		System.out.println("====Encoding====");
		String secretMessage = "THE LEGION IS AS GOOD AS DEAD!!";
		String key = "HOOVER";
		String encoded = "";
		encoded = cypherText(secretMessage, key);
		System.out.println("Base message : \"" + secretMessage + "\"`\nKey : " + key + "\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n\n====Decoding====");
		secretMessage = "COURRIER SIX HAS THE CHIP!!";
		key = "NAVARRO";
		String decoded = decypherText(secretMessage, key);
		System.out.println("Base encoded message : \"" + secretMessage + "\"`\nKey : " + key + "\nDecoded message : \"" + decoded + "\"");
	}
	
}
