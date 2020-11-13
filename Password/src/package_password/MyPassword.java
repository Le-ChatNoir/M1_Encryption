package package_password;

import java.util.Scanner;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyPassword {
	
	private String password;
	private static final String SEL = "#G4D!éR$*+JD%Z";
	
	public MyPassword(String pass) {
		this.password = pass;
	}
	
	public String toString() {
		return "Mot de passe stocké: " + password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public static String hacheSha256(String pMessage) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		StringBuffer sb = new StringBuffer();
		byte[] lMessageUTF8;
		try {
			// On convertit de le message de UNICODE vers UTF-8
			lMessageUTF8 = pMessage.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "Erreur encodage";
		}
		
		for(byte b : md.digest( lMessageUTF8 ) ) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
			 
	}
	
	public static String saler (String pPass) {
		return pPass + SEL;
	}
	
	public boolean equals(Object pPass) {
		return this.getPassword().equals( ((MyPassword) pPass).getPassword());
	}
	
	public boolean controleAcces (String pPass) throws NoSuchAlgorithmException {
		MyPassword myPasswordControleAccess = new MyPassword(hacheSha256(saler(pPass)));
		
		if(this.equals(myPasswordControleAccess))
			return true;
		else
			return false;
	}
	
	public static void main(String[] argv) throws NoSuchAlgorithmException {
		System.out.println("Veuillez définir un mot de passe: ");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String passString = scanner.nextLine();
		
		MyPassword myPassword = new MyPassword(hacheSha256(saler(passString)));
		
		System.out.println("Confirmez votre mot de passe:");
		String confirmation = scanner.nextLine();
		boolean control = myPassword.controleAcces(confirmation);
		
		if(control)
			System.out.println("Succès");
		else
			System.out.println("Echec");
	}

}
