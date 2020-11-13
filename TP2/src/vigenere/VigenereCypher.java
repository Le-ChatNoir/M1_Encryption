package vigenere;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import caesar.CaesarCypher;

public class VigenereCypher {
	
	//Cuts up the text in m substrings
	public static String[] cutTextGroup(String text, int m) {
		
		text = text.replaceAll(" ", "");
		
		//Adding "." m times for the regex
		String separator = "";
		for(int i = 0; i<m; i++) {
			separator += ".";
		}
		
		//Splitting text using regex
		String[] res = text.split("(?<=\\G" + separator + ")");
		System.out.println("Number of substrings : " + res.length);
		
		return res;
	}
	
	//Cuts up the text in m substrings
		public static String[] cutTextIndex(String text, int m) {
			
			String[] res = new String[m];
			text = text.replaceAll(" ", "");
			String word = "";
			int counter = 0;
			
			
			//Cutting words to clump every m-th letter together
			while(counter != m) {
				word = "";
				for(int i = 0; i<text.length(); i++) {
					//System.out.println(text.charAt(i));
					if((i-counter)%m == 0) {
						word += text.charAt(i);
					}
				}
				res[counter] = word;
				counter++;
			}
			
			return res;
		}
	
	
	//Returns the letter with the most frequent occurences
	public static double getIC(String text) {
		HashMap<String, Integer> allLetters = new HashMap<String, Integer>();
		Character currLetter;
		int count;
		int nbLetters = 0;
		
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
				nbLetters++;
			}
		}
		//System.out.println(allLetters.toString());
		
		//Calculate IC
		double res = 0;
		
		for (Entry<String, Integer> entry : allLetters.entrySet()) {  // Iterate through the hashmap
			//System.out.println("Valeur " + entry.getKey() + " nb occu : " + entry.getValue());
            res +=  1.0 * (entry.getValue()*(entry.getValue()-1))/(nbLetters*(nbLetters-1));
            //System.out.println(res);
        }
		
		return res;
	}
	
	public static int getMFromWord(HashMap<String, Integer> allStrings, String word) {
		for (Entry<String, Integer> entry : allStrings.entrySet()) {  // Iterate through the hashmap
            if(entry.getKey() == word)
            	return entry.getValue();
        }
		return 0;
	}
	
	public static int findM(String secretMessage) {
		
		HashMap<String, Double> ICList = new HashMap<String, Double>();
		HashMap<String, Integer> allStrings = new HashMap<String, Integer>();
		HashMap<Integer, Double> allMinimum = new HashMap<Integer, Double>();
		String[] strArray;
		int chosenM = 0;
		System.out.println("==== FINDING M ====");
		
		//Add all possible lists from m=2 to m=12
		for(int i=2; i<12; i++) {
			strArray = cutTextIndex(secretMessage, i);
			for(String token : strArray) {
				allStrings.put(token, i);
		    } 
			
			//Initialize all minimum to very high
			allMinimum.put(i, 100000.0);
		}
		
		
		//Put all strings with their IC
		for (Entry<String, Integer> entry : allStrings.entrySet()) {  // Iterate through the hashmap
            ICList.put(entry.getKey(), getIC(entry.getKey()));
        }
		
		//Find closest to 0.0778
		for (Entry<String, Double> entry : ICList.entrySet()) {  // Iterate through the hashmap
            ICList.put(entry.getKey(), getIC(entry.getKey()));
            
            //Get the lowest value between current value and previously registered lowest value    
            //If new value  for abs(0.0778 - IC(m)) closer to zero, then put the new minimum associated to that m
            if(Math.abs(0.0778 - entry.getValue()) < allMinimum.get(getMFromWord(allStrings, entry.getKey()))){
            	allMinimum.put(getMFromWord(allStrings, entry.getKey()), entry.getValue());
            }
        }

		//List all minimum IC
		for (Entry<Integer, Double> entry : allMinimum.entrySet()) {  // Iterate through the hashmap
            System.out.println("M : " + entry.getKey() + " -> " + entry.getValue());
            allMinimum.put(entry.getKey(), Math.abs(0.0778-entry.getValue()));
            if(Collections.min(allMinimum.values()) == entry.getValue()) {
            	chosenM = entry.getKey();
            }
        }
		
		//List smallest m found
		System.out.println("\nM closest to 0.0778 : " + chosenM + " with IC = " + (allMinimum.get(chosenM) + 0.0778) + "\n");

		return chosenM;
	}
	
	public static String decypherText(String text, int m) {
		String res = "";
		String mostFreqLetter;
		int key;
		
		//Cutting the text as intended using the correct m
		String[] strArray = cutTextIndex(text, m);
		ArrayList<String> decodedList = new ArrayList<String>();
		
		//Using Caesar cypher on each token of the text
		for(String token: strArray) {
			mostFreqLetter = CaesarCypher.getMostFrequent(token);
			key = CaesarCypher.getKey(mostFreqLetter);
			decodedList.add(CaesarCypher.decypherText(token, key));
		}
		
		
		int wordSize = decodedList.get(0).length();
		
		for(int i = 0; i<wordSize; i++) {
			for(String tokenLetter : decodedList) {
				if(i < tokenLetter.length()) {
					res += tokenLetter.charAt(i);
				}
			}
		}
		
		return res;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		System.out.println("\n\n====Decoding====");
		//Recuperation du fichier a decoder
		String secretMessage = "";
		FileReader fr = new FileReader("./vigenere.txt");
		int i;
		while((i=fr.read()) != -1) {
			secretMessage += (char) i;
		}

		int m = findM(secretMessage);
		String res = decypherText(secretMessage, m);
		System.out.println(res);
		
	}
	
}
