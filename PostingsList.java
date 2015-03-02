// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn2

import java.util.ArrayList;
import java.util.Vector;
import java.lang.Comparable;


/**
 * an implementation of postings list
 * your implementation must use a singly linked list for efficiency
 * 
 * a postings list is created everytime a term is added to dict
 * anytime a document is found to contain the term, the docID will be added
 * to the term's postings list
 * 
 * 
 * @author dkauchak
 *
 */
public class PostingsList{
	// save document IDs matching search query to the postings list
	private SinglyLinkedList<Integer> postingslist;

	/**
	 * constructor creates a new postings list for this term
	 */
	public PostingsList(){
		postingslist = new SinglyLinkedList<Integer>();
	}

	/**
	 * add a document ID to this posting list
	 * in sorted order (head<-- descending | ascending --> tail)
	 * @param docID the docID of the document being added
	 */
	public boolean addDoc(int docID){
		// make an integer object for comparing
		Integer currdocID = new Integer(docID);
		Node<Integer> newnode = new Node<Integer>(); // create a new node
		newnode.setElement(currdocID); // set the element of the node

		if(docID == 0){
			// don't do anything
		}
		else{

			// if head is null, then simply add the docID as the new head
			if (postingslist.getFirst() ==null){
				postingslist.addFirst(newnode); // add to the head of the list
			}
			// if there's only a head in the list
			else if(!postingslist.getFirst().hasNext()){
				Node<Integer> head = postingslist.getFirst();
				// if the currdocID > head's doc ID, insert it at the end
				if(currdocID.compareTo(head.getElement())>0){
					postingslist.addLast(newnode);
				}
				// if the docID = head's doc ID, don't insert a duplicate index
				else if (currdocID.compareTo(head.getElement())==0){
					// do nothing
				}
				// if the currdocID < head's doc ID, insert it at the beginning
				else{
					postingslist.addFirst(newnode);
				}
			}
			// if there are more than 1 nodes in the list
			else{
				// starting with the head,
				Node<Integer> head = postingslist.getFirst();
				Node<Integer> currNode = head;
				Node<Integer> prevNode = head;
				// traverse the singly linked list nodes until the new docID >= node's docID
				while(currNode.hasNext()){
					// if our new docID > current node, keep traversing
					if(currdocID.compareTo(currNode.getElement())>0){
						// update to the next node
						prevNode = currNode;
						currNode = currNode.getNext();
					}
					// if our new docID < next node, insert this after the previous node
					else if(currdocID.compareTo(currNode.getElement())<0){
						// add it after the node we went to just before this
						postingslist.addAfter(prevNode, newnode);
						// update to the next node
						prevNode = currNode;
						currNode = currNode.getNext();
						break;
					}
					// don't add a node that already exists in the postings list
					else{
						currNode = currNode.getNext();
					}
				}
				// if we make it out of the while loop, the currdocID might be a number
				// greater than the tail of the list
				if(postingslist.getLast().getElement().compareTo(currdocID)<0){
					postingslist.addLast(newnode);
				}

			}
		}
		return true;
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
		PostingsList answer = new PostingsList(); // create a new list to return

		// because we know that the docIDs are numbered in ascending order from 1 to dictionary size,
		// we can simply return a list from 1 to maxDocID (or dictionary size) filled with
		// all the Integers not in the passed postingslist

		// get all the ids that we will exclude from the new postings list
		int[] notIDs = list.getIDs();

		int notpt = 0; // pointer for the NOT list, which we are removing
		int pt = 1; // pointer for the list we are removing NOT from
		int notptmax = notIDs.length;

		// while the value of this list pointer is <= maxDocID
		// AND the notpt pointer has not exceeded the length of the NOT list
		while((pt <=maxDocID)&&(notpt<notptmax)){
			// if the entry of the NOT list = current point,
			if(notIDs[notpt] == pt){
				// don't add this pt to the answer list
				// increment both pointers
				pt ++;
				notpt++;
			}
			// if the entry of the NOT list > current point,
			else if(notIDs[notpt] > pt){
				// save the currentn point in the vector
				Integer passedpt = new Integer(pt);
				answer.addDoc(passedpt);
				// increment the current point
				pt ++;
			}
			// if the entry of the NOT list < current point
			else{
				// increment the NOT list point
				notpt++;
			}


		}
		// if we exit this while loop, we've either iterated through the maxDocID (we're done),
		// or the NOT list has been exhausted (not quite done)
		// if we're not quite done, simple add the rest of the list to the answer
		if(pt<=maxDocID){
			// starting at the point, iterate to the maxDocID,
			// filling the answer list
			for(int i = pt; i<maxDocID+1; i++){
				Integer newDocID = new Integer(i);
				answer.addDoc(newDocID);
			}
		}

		// test print the final answer
/*		int[] finalans = answer.getIDs();
		String finalstr = "";
		for(int i =0; i<finalans.length; i++){
			finalstr=finalstr+finalans[i]+", ";
		}
		System.out.println("NOT: "+finalstr);*/


		return answer;
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
		PostingsList answer = new PostingsList(); // return this
		PostingsList shorterPL; // shorter postings list
		PostingsList longerPL; // longer postings list

		// the relative lengths of the postings list will help us
		// make the walk through more efficient
		if(posting1.size()>posting2.size()){
			longerPL = posting1;
			shorterPL = posting2;
		}
		else{
			longerPL = posting2;
			shorterPL = posting1;
		}

		// get the array representations to walk through
		int[] shorter = shorterPL.getIDs();
		int[] longer = longerPL.getIDs();

		// pointers and pointer limits
		int pt = 0; // pointer for the shorter
		int pt1 = 0; // pointer for the longer list
		int ptmax = shorterPL.size();
		int pt1max = longerPL.size();
		
		// walk through the lists until the shorter one is done
		while((pt<ptmax)&&(pt1<pt1max)){
			// if the entries of the two lists match, 
			if(shorter[pt] == longer[pt1]){
				// add to answers list!
				Integer newDocID = new Integer(shorter[pt]);
				answer.addDoc(newDocID);
				// increment both pointers
				pt ++;
				pt1++;
			}
			// if the entry of the longer[pt1] > shorter[pt]
			else if(longer[pt1]>shorter[pt]){
				// increment the pointer for short list
				pt ++;
			}
			// if the entry of the NOT list < current point
			else{
				// increment the NOT list point
				pt1++;
			}			
		}

		// test print the final answer
/*		int[] finalans = answer.getIDs();
		String finalstr = "";
		for(int i =0; i<finalans.length; i++){
			finalstr=finalstr+finalans[i]+", ";
		}
		System.out.println("ANDMerge(): "+finalstr);*/

		return answer;
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
		PostingsList answer = new PostingsList(); // return this
		PostingsList shorterPL; // shorter postings list
		PostingsList longerPL; // longer postings list

		// the relative lengths of the postings list will help us
		// make the walk through more efficient
		if(posting1.size()>posting2.size()){
			longerPL = posting1;
			shorterPL = posting2;
		}
		else{
			longerPL = posting2;
			shorterPL = posting1;
		}

		// get the array representations to walk through
		int[] shorter = shorterPL.getIDs();
		int[] longer = longerPL.getIDs();

		// pointers and pointer limits
		int pt = 0; // pointer for the shorter
		int pt1 = 0; // pointer for the longer list
		int ptmax = shorterPL.size();
		int pt1max = longerPL.size();

		// walk through the lists until the shorter one is done
		while((pt<ptmax)&&(pt1<pt1max)){
			// if the entries of the two lists match, 
			if(shorter[pt] == longer[pt1]){
				// add to answers list!
				Integer newDocID = new Integer(shorter[pt]);
				answer.addDoc(newDocID);
				// increment both pointers
				pt ++;
				pt1++;
			}
			// if the entry of the longer[pt1] > shorter[pt]
			else if(longer[pt1]>shorter[pt]){
				// add the entry that is the lesser value
				Integer newDocID = new Integer(shorter[pt]);
				answer.addDoc(newDocID);
				// increment the pointer for short list
				pt ++;
			}
			// if the entry of the NOT list < current point
			else{
				// add the entry that is the lesser value
				Integer newDocID = new Integer(longer[pt1]);
				answer.addDoc(newDocID);
				// increment the NOT list point
				pt1++;
			}
		}

		// once we leave the while loop, we might not be done
		// (there may be more numbers to add from the longer list)
		// add everything else in the longer list

		if(pt1<=longer.length){
			// starting at the point, iterate to the maxDocID,
			// filling the answer list
			for(int i = pt1; i<longer.length; i++){
				Integer newDocID = new Integer(longer[i]);
				answer.addDoc(newDocID);
			}
		}

		// test print the final answer
/*		int[] finalans = answer.getIDs();
		String finalstr = "";
		for(int i =0; i<finalans.length; i++){
			finalstr=finalstr+finalans[i]+", ";
		}
		System.out.println("ORMerge(): "+finalstr);*/

		return answer;
	}

	/**
	 * @return the number of docIDs for this posting list
	 */
	public int size(){
		return (int)postingslist.getSize();
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
		int[] docIDArray = new int[size()]; // store the ints in here
		int ct = 0; // counter
		// starting with the head of the list, add the docIDs into the array
		Node<Integer> currNode = postingslist.getFirst(); 
		
		if(!(currNode==null)){
			docIDArray[ct] = (int)currNode.getElement();
			while(currNode.hasNext()){
				ct++; //increment cound
				docIDArray[ct] = (int)currNode.getNext().getElement(); // fill index
				currNode = currNode.getNext(); // advance the pointer
			}
		}
		return docIDArray;
	}
}
