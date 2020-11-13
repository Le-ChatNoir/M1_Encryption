package caesar;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class CaesarCypher {
	
	//Returns the letter with the most frequent occurences
	public static String getMostFrequent(String text) {
		HashMap<String, Integer> allLetters = new HashMap<String, Integer>();
		Character currLetter;
		int count;
		
		//Travel accross the text
		for(int i = 0; i<text.length(); i++) {
			if(Character.isUpperCase(text.charAt(i))) {
				currLetter = text.charAt(i);
				
				//Update HashMap containing a count of all letters
				if(!allLetters.containsKey(currLetter.toString())) {
					allLetters.put(currLetter.toString(), 1);
				} else {
					count = allLetters.get(currLetter.toString());
					allLetters.put(currLetter.toString(), count+1);
				}
				
			}
		}
		
		//Get the letter with biggest count
		int maxValue = (Collections.max(allLetters.values()));
		String res = "";
		
		for (Entry<String, Integer> entry : allLetters.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValue) {
                res = entry.getKey();     // Print the key with max value
            }
        }
		return res;
	}
	
	public static int getKey(String letter) {
		int res = 0;
		char l = letter.charAt(0);
		char e = 'E';
		
		res = (((int)l - e - 65 + 26) % 26 + 65)%26;
		
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
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		System.out.println("\n\n====Decoding====");
		//Recuperation du fichier a decoder
		String secretMessage = "";
		FileReader fr = new FileReader("./cesar.txt");
		int i;
		while((i=fr.read()) != -1) {
			secretMessage += (char) i;
		}

		String mostFreqLetter = getMostFrequent(secretMessage);
		int key = getKey(mostFreqLetter);
		System.out.println("Found most frequent letter: \"" + mostFreqLetter + "\"\nKey : " + key);
		String decoded = decypherText(secretMessage, key);
		System.out.println("Base encoded message : \"" + secretMessage + "\"`\nDecoded message : \"" + decoded + "\"");
	}
	
}
