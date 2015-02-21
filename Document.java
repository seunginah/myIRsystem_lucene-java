// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1

package assignment1;


import java.util.ArrayList;

/**
 * A representation of the documents that will be stored in our
 * data collection.
 * 
 * @author dkauchak
 */
public class Document{
	private int docID;
	private ArrayList<String> text;
	private Dictionary words;
	
	/**
	 * Create a new document
	 * 
	 * @param docID the unique ID associated with this document
	 * @param text the words in the document
	 */
	public Document(int docID, ArrayList<String> text){
		this.docID = docID;
		this.text = text;
		this.words = new Dictionary();
	}

	
	/**
	 * Get the document ID
	 * 
	 * @return the docID
	 */
	public int getDocID() {
		return docID;
	}

	/**
	 * Set the document ID
	 * 
	 * @param docID
	 */
	public void setDocID(int docID) {
		this.docID = docID;
	}

	/**
	 * Get the text (i.e. words) that make up this document. (all tokens)
	 * 
	 * @return
	 */
	public ArrayList<String> getText() {
		return text;
	}
	
	/**
	 * Set the text (i.e. words) that make up this document.
	 * 
	 * @param text
	 */
	public void setText(ArrayList<String> text) {
		this.text = text;
	}
	
	/**
	 * Populate the dictionary from the test ArrayList
	 * 
	 */
	public void setDict(){
		// if the array list text isn't empty, use it to populate the dictionary
		if (!text.isEmpty()){
			//
			for(int i = 0; i<text.size(); i++){
				this.words.addWord(text.get(i));
			}
		}
	}
	
	/**
	 * Get the dictionary of words (all types)
	 * 
	 * @return
	 */
	public Dictionary getDict(){
		return words;
	}
}
