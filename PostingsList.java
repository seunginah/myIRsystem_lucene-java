import java.util.ArrayList;



/**
 * an implementation of postings list
 * your implementation must use a singly linked list for efficiency
 * 
 * @author dkauchak
 *
 */
public class PostingsList{
	// save document IDs matching search query to the postings list
	private ArrayList<Integer> postingslist;
	// during boolean search, we might need to know the total collection of doc ID#s
	private ArrayList<String> totallist; 
	
	/**
	 * constructor
	 */
	public PostingsList(){
		
	}
	
	/**
	 * add a document ID to this posting list
	 *
	 * @param docID the docID of the document being added
	 */
	public void addDoc(int docID){
		// if it isn't already contained in the postings list, add the doc ID
		if(!postingslist.contains(docID)){
			postingslist.add(new Integer(docID));
		}
	}
	
	/**
	 * Given a postings list, return the NOT of the postings list, i.e.
	 * a postings list that contains all document ids not in "list"
	 * 
	 * document IDs should range from 0 to maxDocID
	 * 
	 * @param list the postings list to NOT
	 * @param maxDocID the maximum allowable document ID
	 * @return not of the posting list
	 */
	public static PostingsList not(PostingsList list, int maxDocID){
		// do a binary search to save time (?)
		return null;
	}
	
	/**
	 * Given two postings lists, return a new postings list that contains the AND
	 * of the postings, i.e. all the docIDs that occur in both posting1 and posting2
	 * 
	 * @param posting1
	 * @param posting2
	 * @return the AND of the postings lists
	 */
	public static PostingsList andMerge(PostingsList posting1, PostingsList posting2){
		return null;
	}
	
	/**
	 * Given two postings lists, return a new postings list that contains the OR
	 * of the postings, i.e. all those docIDs that occur in either posting1 and posting2
	 * 
	 * @param posting1
	 * @param posting2
	 * @return the OR of the postings lists
	 */
	public static PostingsList orMerge(PostingsList posting1, PostingsList posting2){
		return null;
	}
		
	/**
	 * @return the number of docIDs for this posting list
	 */
	public int size(){
		return -1;
	}
	
	/**
	 * From the linked list structure, generate an integer array containing 
	 * all of the document ids.  This will make our life easy when we want to 
	 * print out the ids.  (another option would have been to write an iterator, but
	 * this is easier).
	 * 
	 * @return
	 */
	public int[] getIDs(){
		return null;
	}
}
