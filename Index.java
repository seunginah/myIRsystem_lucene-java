// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn2

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;



/**
 * this holds the actual index and also provides the interface to the search
 * engine for querying the index
 * 
 * @author dkauchak
 */ 
public class Index {
	private DocumentReader rdr;
	private HashMap<String, PostingsList> map;
	private int totalDocs;
	private PostingsList allDocs;
	
	/**
	 * the reader must already be set up: tokenizer, token processor booleans
	 * this index class first goes through 
	 * 
	 * @param reader a reader that will be used to read the corpus
	 */
	public Index(DocumentReader reader){
		// instantiate hashmap
		map = new HashMap<String, PostingsList>();
		
		// create reader with tokenizer, token processor
		this.rdr = reader;
		
		// access the documents Queue
		Queue<Document> docQ = ((TDTReader) rdr).getDocQueue();
		// save the size of the Queue so we know how many documents there were
		totalDocs = docQ.size();
		System.out.println("\n\ntotal docs: "+totalDocs);
		
		// while there are documents in the queue
		// add to index: hashmap<token, posting list>
		while (!docQ.isEmpty()){
			Document doc = docQ.remove(); // get doc
			ArrayList<String> docTokens = doc.getText(); // get text
			int docID = doc.getDocID(); // get ID
			
			// for each token in this document
			for(int i = 0; i<docTokens.size(); i++){
				String token = docTokens.get(i);
				// if a postings list already exists for the term
				if(map.containsKey(token)){
					// update the postings list by adding this doc id to it
					PostingsList updatedPostingsList = map.get(token);
					boolean added = updatedPostingsList.addDoc(docID);
					
					if(added){
					// update the value for this key
					map.put(token, updatedPostingsList);
					}
					else{
						System.out.println("something wrong, "+token+"'s updated posting list was not added to map");
					}
				}
				// if a postings list does not already exist
				else{
					// create a new list, adding this docID
					PostingsList newPostingsList = new PostingsList();
					newPostingsList.addDoc(docID);
					// update the map
					map.put(token, newPostingsList);
				}
			} // finished going through tokens for this document 
			
		} // docQ is now empty
		
		
	}
	
	public HashMap<String, PostingsList> getMap(){
		return map;
	}
	
	public PostingsList getAllDocs(){
		int[] a = allDocs.getIDs();
		return allDocs;
	}
	
	public void printAllPostingsLists(){
		// iteratre over map
		Set<String> tokens = map.keySet();
		Iterator<String> it = tokens.iterator();
		while(it.hasNext()){
			String token = it.next();
			//System.out.println(token);
			PostingsList PL = map.get(token);
		}
		
		String test = "help";
		if(map.containsKey(test)){
			System.out.println("\n testing...\ncontains "+test);
			PostingsList PL = map.get(test);
			System.out.println("got:"+PL+" size:"+PL.size());
			System.out.println(printPL(PL)+"\n\ndone printing");
		}
		else{
			System.out.println("try a diff key");
		}
	}
	
	
	public String printPL(PostingsList pl){
		int[] ids = pl.getIDs();
		System.out.println("length ids[]: "+ids.length);
		String printthis = "";
		for(int i =0; i<ids.length; i++){
			printthis= printthis + ids[i]+", ";
		}
		return printthis;
		
	}
	
	/**
	 * to do a NOT operation, we need to know all of the doc IDs
	 * 
	 */
	public void genAllDocsPL(){
		allDocs = new PostingsList();
		for(int i=1; i<totalDocs+1; i++){
			Integer docID = new Integer(i);
			allDocs.addDoc(docID);
		}
	}

	/**
	 *  Given a boolean query (see the handout for what types of boolean
	 *  queries are valid), return a PostingsList containing the document
	 *  IDs that match the query.  If no documents match, you should still return a
	 *  PostingsList, but it will not have any document ids.
	 * 
	 * @param textQuery
	 * @return
	 */
	public PostingsList booleanQuery(String textQuery){
		String q = textQuery;
		PostingsList answer = new PostingsList(); // return this
		Queue<String> qqueue = new LinkedList<String>(); // queue each term
		Stack<PostingsList> plstack = new Stack<PostingsList>(); // stack each postings list
		
		boolean gotAllDocs = false; // have we populated the postingslist allDocs?
		int evalNext = 0; // is it time to do an AND/OR merge?
		// 1 for AND merge, 2 for OR merge
		
		// split with whitespace
		String delim = " ";
		String[] qs = q.split(delim);
		// add each term to our queue
		for(int i=0; i<qs.length; i++){
			qqueue.add(qs[i]);
		}
		
		// dequeue term, get the posting list, and then push that onto a posting list stack
		while(!qqueue.isEmpty()){
			String dequeued = qqueue.remove();
			
			// check for a not operation on the term
			if(dequeued.contains("!")){
				// if this is the first NOT operator we've come across,
				if(!gotAllDocs){
					// generate the allDocs postings list
					genAllDocsPL();
					gotAllDocs = true;
				}
				// now separate the !
				String[] notsplit = dequeued.split("!");
				String term = notsplit[1].trim();
				
				// get the NOT PL for this term
				PostingsList termPL = map.get(term);
				PostingsList NOTtermPL = allDocs.not(termPL, totalDocs);
				// push it onto the pl stack
				plstack.push(NOTtermPL);
			}
			// don't push AND or OR as terms onto the plstack!
			else if(dequeued.equalsIgnoreCase("AND")|| dequeued.equalsIgnoreCase("OR")){
				// 
			}
			
			// otherwise
			else{
				// get the term's posting list and push it onto the stack
				PostingsList termPL = map.get(dequeued.trim());
				// push it onto the pl stack
				plstack.push(termPL);
				
			}
			
			// if it's time to evaluate using an AND or OR operator,
			if(evalNext>0){
				PostingsList result;
				// pop the top 2 things off the stack, 
				PostingsList pop1 = plstack.pop();
				PostingsList pop2 = plstack.pop();
				
				// evaluate AND operation
				if(evalNext ==1){
					result = allDocs.andMerge(pop1, pop2);
				}
				// evaluate OR operation
				else{
					result = allDocs.orMerge(pop1, pop2);
				}
				// push the result onto the plstack
				plstack.push(result);
				evalNext = 0;
			}
			
			if(dequeued.equalsIgnoreCase("AND")||dequeued.equalsIgnoreCase("OR")){
				// if we get an AND or an OR statement,
				// turn a boolean on so that after the next term is popped,
				// we can evaluate the top 2 on the postings list stack
				if(dequeued.equalsIgnoreCase("AND")){
					evalNext = 1;
				}
				else{
					evalNext = 2;
				}
			}
		}
		// pop that answer 
		answer = plstack.pop();
		
		
		// test print the final answer
		int[] finalans = answer.getIDs();
		String finalstr = "";
		for(int i =0; i<finalans.length; i++){
			finalstr=finalstr+finalans[i]+", ";
		}
		System.out.println("answer: "+finalstr);
		return answer;
	}
}
