// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1

package assignment1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


/**
 * A document reader for the TDT corpus.
 * 
 * @author dkauchak
 * 
 */
public class TDTReader implements DocumentReader{

	// save the documents in a queue
	private Queue<Document> docQ;
	// select a tokenizer
	private Tokenizer thisTokenizer;
	private TokenProcessor thisProcessor;
	private ArrayList<String> tokens;
	/**
	 * The text file containing the TDT data with documents delimited
	 * by <DOC> ... </DOC>
	 * 
	 * the constructor parses the text file, creates a Document for each document found
	 * after a Document is instantiated, a Dictionary can be constructed if the Document.setDict() method is called
	 * 
	 * @param documentFile
	 * @throws IOException 
	 */
	public TDTReader(String documentFile, Tokenizer tokenizer) throws IOException{
		// set tokenizer
		setTokenizer(tokenizer);

		// instantiate instance fields
		setDocQ(new LinkedList<Document>()); // save documents in a linked list
		String docText = ""; // save the text (to be tokenized) in this string
		String docIDStr = "";// while reading, store the current docID until the doc is ready to be written

		// use a scanner to read the document, using a whitespace delimiter
		Scanner read = new Scanner (new File(documentFile));
		read.useDelimiter(" ");
		// only collect string info when this boolean is true
		boolean startDocRead = false;

		while (read.hasNext())
		{
			// let's read the line
			String line = read.nextLine();

			// this series of if statements should work if the txt file is in a good format
			// for ex., if </TEXT> occurred before the first instance of <TEXT>, the docID#s would be shifted 

			// get the doc ID if it is seen
			if (line.contains("<DOCNO>")){
				// split this line with whitespace to get the docID#
				String[] newDocNum = line.split(" ");
				// save the second index
				docIDStr = newDocNum[1];
			}
			// "turn on" saving strings once the <TEXT> indicator is seen
			else if(line.contains("<TEXT>")){
				startDocRead = true;
			}
			// if we're done reading this Document's text
			else if (line.contains("</TEXT>")){
				// convert the text id to an int id 
				// first get rid of the characters. also get rid of the first 3 numbers
				// (i found that all docIDs begin with "890" so let's ignore that)
				String currentdocID = docIDStr.replaceAll("[^0-9]", "").substring(3);
				int docID = Integer.parseInt(currentdocID);

				// now add the new document into the document Queue with the docID# and the arraylist of tokens
				this.setTokens(thisTokenizer.tokenize(docText));
				getDocQ().add(new Document(docID, getTokens()));

				// turn off saving lines for document text momentarily
				startDocRead = false;
			}
			// if we aren't saving a new docID# or terminating a document,
			else{
				// just keep adding lines to the Document
				if (startDocRead){
					docText = docText + line;
				}
			}
		}
		read.close(); // close the reader
	}

	/**
	 * Set the tokenizer for this reader
	 * 
	 * @param tokenizer
	 */
	public void setTokenizer(Tokenizer tokenizer) {
		this.thisTokenizer = tokenizer;		
	}

	/**
	 * set the token processor for this reader
	 * 
	 * @param tokenProcessor
	 */
	public void setTokenProcessor(TokenProcessor tokenProcessor) {
		this.setThisProcessor(tokenProcessor);
	}

	/**
	 * Are there more documents to be read?
	 */
	public boolean hasNext() {
		// if the document queue isn't empty, there are more documents 
		if(getDocQ().isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Get the next document.
	 */
	public Document next(){
		// if there are documents in the queue
		if (hasNext()){
			// return the next one, removing from queue
			return this.getDocQ().remove();
		}
		else{
			return null;
		}
	}

	public void remove() {
		// method is optional, so don't worry about it
	}

	public Queue<Document> getDocQ() {
		return docQ;
	}

	public void setDocQ(Queue<Document> docQ) {
		this.docQ = docQ;
	}

	public TokenProcessor getThisProcessor() {
		return thisProcessor;
	}

	public void setThisProcessor(TokenProcessor thisProcessor) {
		this.thisProcessor = thisProcessor;
	}

	public ArrayList<String> getTokens() {
		return tokens;
	}

	public void setTokens(ArrayList<String> tokens) {
		this.tokens = tokens;
	}


}
