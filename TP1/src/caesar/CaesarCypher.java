package caesar;

public class CaesarCypher {
	
	public static String cypherText(String text, int key) {
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
	
	public static String decypherText(String text, int key) {
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
	
	public static void main(String[] args) {
		System.out.println("====Encoding====");
		String secretMessage = "THIS IS A very VERY SECRET MESSAGE!!";
		String encoded = "";
		encoded = cypherText(secretMessage, 6);
		System.out.println("Base message : \"" + secretMessage + "\"`\nEncoded message : \"" + encoded + "\"");
		
		System.out.println("\n\n====Decoding====");
		secretMessage = "GZZGIQ GZ UTIK ABCDE!!";
		String decoded = decypherText(secretMessage, 6);
		System.out.println("Base encoded message : \"" + secretMessage + "\"`\nDecoded message : \"" + decoded + "\"");
	}
	
}
