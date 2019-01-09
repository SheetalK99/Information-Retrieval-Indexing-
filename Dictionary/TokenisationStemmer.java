package sak170006_hw1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class TokenisationStemmer {

	static HashMap<String, Integer> TokenDictionary = new HashMap<>();
	static HashMap<String, Integer> StemDictionary = new HashMap<>();
	static TreeMap<String, Integer> sorted_Stem_Dictionary = new TreeMap<>();
	static TreeMap<String, Integer> sorted_Token_Dictionary = new TreeMap<>();
	int total_tokens = 0;
	int No_of_docs = 0;
	int No_single_tokens = 0;
	int No_single_stems = 0;

	public static TreeMap<String, Integer> sortByValue(HashMap<String, Integer> hashm) {
		// Create comparator instance
		Comparator<String> cmp = new ValCustomComparator(hashm);

		// Pass custom comparator to treemap
		TreeMap<String, Integer> res = new TreeMap<String, Integer>(cmp);
		res.putAll(hashm);// Add values to treemap
		return res;// sort by values
	}

	public void readFile(String dir, String encoding) throws IOException, FileNotFoundException {

		String line;
		File directory = new File(dir);
		// iterate over each file
		for (File f : directory.listFiles()) {
			if (f.isFile()) {
				No_of_docs++; // count files
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));

				while ((line = br.readLine()) != null) {
					// tokenise line
					tokenise(line);
				}
				br.close();
			} else {
				continue;
			}
		}

	}

	public TreeMap<String, Integer> create_sorted_token_stem_dictionary(HashMap<String, Integer> hashm) {

		TreeMap<String, Integer> sortedMap = sortByValue(hashm);
		return sortedMap;
	}

	public static int count_single_tokens_stems(HashMap<String, Integer> dict) {
		int count = 0;
		for (String key : dict.keySet()) {
			if (dict.get(key) == 1) {
				count++;
			}

		}

		return count;
	}

	public void addToken(String token) {
		// Add further constraints if needed here

		AddtoDictionary(TokenDictionary, token);
	}

	public void AddtoDictionary(HashMap<String, Integer> x, String token) {

		if (x.containsKey(token)) {
			x.put(token, x.get(token) + 1);
		} else {
			x.put(token, 1);
		}

	}

	public void addStem(String token) {
		// Retrieve stem from Stemmer class(open source implementation of Porter
		// Stemmer)
		String stem = getStem(token);

		AddtoDictionary(StemDictionary, stem);

	}

	private String getStem(String token) {

		Stemmer s = new Stemmer();
		s.add(token.toCharArray(), token.length());
		s.stem();
		return s.toString();

	}

	public void tokenise(String line) {
		String words[];

		// remove html tags and split words
		words = line.toLowerCase().replaceAll("<.*?>", "").split("[=()/,\\s-\\\\]+");

		for (int i = 0; i < words.length; i++) {

			// handle apostrophe
			if (words[i].endsWith("'s")) {
				words[i] = words[i].replace("'s", "");

			}
			// remove all except alphabets
			words[i] = words[i].replaceAll("[^a-z]", "");

			// skip if null
			if (words[i].length() > 0) {
				addToken(words[i]);
				total_tokens++;
				addStem(words[i]);
			}

		}

	}

	public void get_frequent_tokens(TreeMap<String, Integer> tm) {

		int count = 0;
		// Get a set of the entries on the sorted map
		Set<Entry<String, Integer>> set = tm.entrySet();

		// Get an iterator
		Iterator<Entry<String, Integer>> i = set.iterator();

		// Display elements
		while (count < 30 && i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
			count++;
		}

	}

	public void printDictionary(HashMap<String, Integer> x) {

		for (String key : x.keySet()) {

			String key1 = key;
			String value = x.get(key).toString();

			System.out.println(key1 + " " + value);

			;
		}

	}

	public void printStats() {
		System.out.println("A: TOKENISATION OUTPUT ");

		// Number of tokens in Cranfield collection
		System.out.println("1. The number of tokens in the Cranfield text collection: " + total_tokens);

		// Unique words
		System.out.println("2.The number of unique words in the Cranfield text collection: " + TokenDictionary.size());

		//
		System.out.println(
				"3. The number of words that occur only once in the Cranfield text collection: " + No_single_tokens);

		// Freq tokens in collection
		System.out.println("4. The 30 most frequent tokens in the Cranfield text collection : : ");
		get_frequent_tokens(sorted_Token_Dictionary);

		// Avg no of tokens
		System.out.println("5. The average number of word tokens per document: " + total_tokens / No_of_docs);

		System.out.println();
		
		System.out.println("B: STEMMING ");
		// distinct stems
		System.out.println("1.The number of distinct stems in the Cranfield text collection: " + StemDictionary.size());

		// single stems

		System.out.println(
				"2. The number of stems that occur only once in the Cranfield text collection: " + No_single_stems);

		// Freq tokens in collection
		System.out.println("3. The 30 most frequent stems in the Cranfield text collection : ");
		get_frequent_tokens(sorted_Stem_Dictionary);

		// Avg no of stems
		System.out.println("4. The average number of word stems per document: " + total_tokens / No_of_docs);

	}

	public static void main(String args[]) throws IOException {

		// start timer
		long startTime = System.currentTimeMillis();
		String dir;
		if(args.length == 0)
	    {
		 dir = "/people/cs/s/sanda/cs6322/Cranfield";
	    }
		else {
			dir=args[0].toString();
		}
		   
		 String encoding = "UTF-8";
		TokenisationStemmer t = new TokenisationStemmer();

		t.readFile(dir, encoding); // start tokenization

		TokenisationStemmer.sorted_Token_Dictionary = t.create_sorted_token_stem_dictionary(TokenDictionary);
		TokenisationStemmer.sorted_Stem_Dictionary = t.create_sorted_token_stem_dictionary(StemDictionary);

		t.No_single_tokens = count_single_tokens_stems(TokenDictionary);
		t.No_single_stems = count_single_tokens_stems(StemDictionary);

		t.printStats();

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println();
		System.out.println("Time of execution: " + totalTime + " milliseconds");

		// Option to print all tokens or stems
		Scanner in = new Scanner(System.in);
		whileloop: while (in.hasNext()) {
			int com = in.nextInt();
			switch (com) {
			case 1:
				System.out.println("Print token dictionary");
				t.printDictionary(TokenisationStemmer.TokenDictionary);
				System.out.print(TokenisationStemmer.TokenDictionary.size());
				break;
			case 2:
				System.out.println("Print stem dictionary");
				t.printDictionary(TokenisationStemmer.StemDictionary);
				System.out.print(TokenisationStemmer.StemDictionary.size());
				break;
			default: // Exit loop
				break whileloop;
			}

		}

	}
}
