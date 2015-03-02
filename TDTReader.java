

import java.io.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Queue;


/**
 * A document reader for the TDT corpus.
 * 
 * @author dkauchak
 * 
 */
public class TDTReader implements DocumentReader{

	private Tokenizer tokenizer = null;
	private TokenProcessor tokenProcessor = null;
	private BufferedReader in;
	private String nextDocText;
	private int nextDocID = 0;
	private Queue<Document> docQ; // queue for doucments
	private String thisFile;
	
	/**
	 * The text file containing the TDT data with documents delimited
	 * by <DOC> ... </DOC>
	 * 
	 * @param documentFile
	 */
	public TDTReader(String documentFile){
		docQ = new LinkedList<Document>(); // save documents in a linked list
		this.thisFile = documentFile;
	}
	
	/**
	 * this method reads through all the documents found in the documentFile
	 * it will populate a document queue, which stores all the documents
	 * 
	 */
	public void read(){
		try{
			in = new BufferedReader(new FileReader(thisFile));
			nextDocText = readNextDocText();
			nextDocID ++;
			Document doc1 = new Document(nextDocID, tokenizer.tokenize(nextDocText));
			
			// while there are more documents, read them and put them into a queue
			while(hasNext()){
				Document doc = next();
				int docID = doc.getDocID();
				docQ.add(doc);

				nextDocText = readNextDocText();
			}
			
		}catch(IOException e){
			throw new RuntimeException("Problems opening file: " + thisFile + "\n" + e.toString());
		}
	}
	
	/**
	 * returns the queued documents
	 * @return
	 */
	public Queue<Document> getDocQueue(){
		return docQ;
	}
	
	/**
	 * Set the tokenizer for this reader
	 * 
	 * @param tokenizer
	 */
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
	
	/**
	 * set the token processor for this reader
	 * 
	 * @param tokenProcessor
	 */
	public void setTokenProcessor(TokenProcessor tokenProcessor) {
		this.tokenProcessor = tokenProcessor;
	}

	/**
	 * Are there more documents to be read?
	 */
	public boolean hasNext() {
		return nextDocText != null;
	}

	/**
	 * Get the next document.
	 */
	public Document next(){
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		if( tokenizer == null ){
			throw new RuntimeException("TDTReader::next() - call without setting tokenizer");
		}
		
		ArrayList<String> tokens = tokenizer.tokenize(nextDocText);
		
		if( tokenProcessor != null ){
			tokens = tokenProcessor.process(tokens);
		}
		
		Document returnMe = new Document(nextDocID, tokens);
		nextDocID++;
		
/*		try{
			nextDocText = readNextDocText();
		}catch(IOException e){
			throw new RuntimeException("Problems reading file\n" + e.toString());
		}*/
		
		return returnMe;
	}

	/**
	 * Read through the file and extract the text between the next <DOC> and </DOC> tags
	 * @return The text between the next DOC tags or null if no more documents exist
	 * @throws IOException
	 */
	private String readNextDocText() throws IOException{
		String line = in.readLine();
		
		// find the beginning of the document
		while( line != null &&
			   !line.equals("<TEXT>") ){
			line = in.readLine();
			//System.out.println("line not null "+line);
		}
		
		if( line == null ){
			//System.out.println("line is null");
			return null;
		}else{
			StringBuffer buffer = new StringBuffer();
		
			line = in.readLine();
			
			// grab all the text between <DOC> and </DOC>
			while( line != null &&
				   !line.contains("</DOC>") ){
				// get the doc ID if it is seen
				if (line.contains("<DOCNO>")){
					// split this line with whitespace to get the docID#
					String[] newDocNum = line.split(" ");
					// save the second index
					String docIDStr = newDocNum[1];
					//System.out.println("docID: "+docIDStr);
				}
				else{
				
				buffer.append(" " + line);
				line = in.readLine();
				//System.out.println("line not null 2: "+line);
				}
			}
		
			return buffer.toString();
		}
	}
	
	public void remove() {
		// method is optional
	}

}