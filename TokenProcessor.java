// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1

package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class for performing various data normalization techniques
 * on tokens.
 * 
 * @author dkauchak
 *
 */
public class TokenProcessor{
	
	private boolean lower;
	private boolean stem;
	private boolean num;
	private ArrayList<String> stops;
	
	/**
	 * 
	 */
	public TokenProcessor(){
		this.lower = false;
		this.stem = false;
		this.num = false;
		this.stops = new ArrayList<String>();
	}
	
	/**
	 * Set whether or not to lowercase the tokens.
	 * 
	 * @param b
	 */
	public void setLowercase(boolean b){
		this.lower = b;
	}
	
	/**
	 * Set whether or not to stem the tokens using the Porter stemmer
	 * @param b
	 */
	public void setStem(boolean b){
		this.stem = b;
	}

	/**
	 * Set whether to replace numbers with <NUM> when processing
	 * 
	 * @param b
	 */
	public void setFoldNumbers(boolean b){
		this.num = b;
	}
	
	public ArrayList<String> getStoplist(){
		return stops;
	}
	
	/**
	 * Set the list of words to use as a stoplist.  If the setStopList method is
	 * not called, then don't do any stoplisting.
	 * 
	 * @param list The list of stop words
	 */
	public void setStopList(ArrayList<String> list){
		this.stops = list;
		//System.out.println("just set: "+list.size());
	}
	
	public ArrayList<String> importStopList() throws FileNotFoundException{
		//ArrayList<String> stoplist = new ArrayList<String>();
		this.stops = new ArrayList<String>();
		// use a scanner to read the document, using a whitespace delimiter
		Scanner read = new Scanner (new File("stoplist.txt"));
		read.useDelimiter(" ");

		// add all lines of textfile to stop list
		while (read.hasNext())
		{
			String line = read.nextLine();
			this.stops.add(line);
		}
		read.close();
		System.out.println(stops.size());
		return stops;
	}

	/**
	 * Go through the strings in "tokens", apply all normalization techniques
	 * that are enabled and return the new set of tokens.
	 * 
	 * @param tokens
	 * @return The normalized tokens
	 */
	public ArrayList<String> process(ArrayList<String> tokens){
		ArrayList<String> processed = tokens;
		//System.out.println(tokens.toString());
		
		// if the stop list isn't empty
		if (this.stops ==null){
		}
		else{
			if (!(this.stops.size()==0)){
			System.out.println("stoplist size: "+stops.size()+" tokenslist size: "+processed.size());
			
			for(int i =0; i<processed.size(); i++){
				if(stops.contains(processed.get(i))){
					processed.remove(i);
				}
			}}
			else{
				System.out.println(stops.isEmpty());
			}
		}
		
		// if lowercasing
		if(lower){
			for(int i =0; i<tokens.size(); i++){
				processed.get(i).toLowerCase();
			}
		}
		
		// if stemming
		if(stem){
			Porter porter = new Porter();
			ArrayList<String> stemmed = new ArrayList<String>();
			// for each word in the array list, stem it and save it in the new array list
			for(int i =0; i<processed.size(); i++){
				stemmed.add(porter.stem(processed.get(i)));
			}
			// clear processed
			processed.clear();
			// set processed to the stemmed
			processed = stemmed;
		}
		
		// if number folding
		// please note that this doesn't recognize a number with a + in front of it
		if(num){
			// define a regex for number matching
			for(int i =0; i<processed.size(); i++){
				processed.get(i).replaceAll("(-*)?[0-9]+(.[0-9]*)?(,[0-9]*)?", "<NUM>");
			}
			
		}		
		return processed;
	}
}
