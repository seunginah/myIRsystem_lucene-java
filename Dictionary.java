// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1



// java util
import java.util.ArrayList;

/**
 * This class keeps track of the number of unique strings added to the dictionary
 *  
 * @author dkauchak
 *
 */
public class Dictionary{
	private ArrayList<String> dict;
	
	/**
	 * constructor instantiates data structures
	 */
	public Dictionary(){
		this.dict = new ArrayList<String>();
	}
	
	/**
	 * Adds a word to the dictionary
	 * 
	 * @param word the word to be added
	 */
	public void addWord(String word){
		// if the dictionary doesn't contain the word, add it
		if (!this.dict.contains(word)){
			this.dict.add(word);
			// create a postings list for this word
			PostingsList postings = new PostingsList();
			//postings.addDoc(docID);
		}
		
	}
	
	/**
	 * Get the size of the dictionary
	 * 
	 * @return the dictionary size
	 */
	public int size(){
		// get the size of the dictionary
		return this.dict.size();
	}
}
