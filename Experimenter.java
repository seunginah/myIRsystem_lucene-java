// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1

package assignment1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Experimenter {

	public Experimenter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * read all documents from a document queue
	 * @param tdtreader
	 */
	public void readAllDocs(TDTReader tdtreader){
		TDTReader reader = tdtreader;
		// uncomment this code to get analytics on every document
		while (reader.hasNext()){
			// for every document that was found in this file, 
			Document d= reader.getDocQ().remove();
			// populate the dictionaries
			d.setDict();
			// print the number of tokens (the size of the arraylist used to construct to document)
			// print the number of types (the size of the dictionary associated with the document)
			System.out.println("doc#"+d.getDocID()+ " tokens: "+d.getText().size()+ " types: "+ d.getDict().size());
		}

	}
	
	/**
	 * add everything from an array list into a dictionary
	 */
	public void addToDict(ArrayList<String> list, Dictionary dict){
		for(int i = 0; i<list.size(); i++){
			dict.addWord(list.get(i));
		}
	}
	
	/**	
	 * problem 2:
	 * create a new TDTReader on a text file found in the assn1 directory, using the simple tokenizer
	 * iterate through
	 * 
	 * @throws IOException
	 */
	public void simpleTokenizer() throws IOException{
		// for the purposes of analyzing document 1, i used a shortened version of the txt file
		// this was simply to save time
		TDTReader reader1 = new TDTReader("ap89_short.txt", new SimpleTokenizer());
		System.out.println("Grace S Yoo   Assn 1"
				+ "\nplease note that because all docIDs began with '890', "
				+ "\ni removed '890' from all docIDs");
		System.out.println("\nProblem 2. Using the SimpleTokenizer: ");
		

		// use this code to get analytics on only the document #1
		Document d= reader1.getDocQ().remove();
		// populate the dictionaries
		d.setDict();
		// print the number of tokens (the size of the arraylist used to construct to document)
		// print the number of types (the size of the dictionary associated with the document)
		System.out.println(reader1.getTokens().size());
		System.out.println("doc#"+d.getDocID()+ " tokens: "+d.getText().size()+ " types: "+ d.getDict().size());
	}
	
	/**
	 * problem 3:
	 * create a new TDTReader on a text file found in the assn1 directory, using the simple tokenizer
	 * iterate through
	 * @throws IOException
	 */
	public void improvedTokenizer() throws IOException{
		TDTReader reader = new TDTReader("ap89_short.txt", new ImprovedTokenizer());
		System.out.println("\nProblem 3. Using the ImprovedTokenizer: ");

		// use this code to get analytics on only document 1, with improved tokenizing
		// for every document that was found in this file, 
		Document d= reader.getDocQ().remove();
		// populate the dictionaries
		d.setDict();
		// print the number of tokens (the size of the arraylist used to construct to document)
		// print the number of types (the size of the dictionary associated with the document)
		System.out.println("doc#"+d.getDocID()+ " tokens: "+d.getText().size()+ " types: "+ d.getDict().size());
	}
	
	/**
	 * number folding only
	 * returns arraylist -- which could be further processed or added to a dict
	 * 
	 */
	public ArrayList<String> numFold(TDTReader r){
		System.out.println("number folding only:");
		// number folding only
		r.getThisProcessor().setFoldNumbers(true);
		r.getThisProcessor().setLowercase(false);
		r.getThisProcessor().setStem(false);
		// make the stoplist null
		r.getThisProcessor().setStopList(new ArrayList<String>());

		return r.getThisProcessor().process(r.getTokens());
	}
	
	/**
	 * number folding only
	 * returns arraylist -- which could be further processed or added to a dict
	 * 
	 */
	public ArrayList<String> lowercase(TDTReader r){
		System.out.println("lowercasing only:");
		// lowercasing only
		r.getThisProcessor().setFoldNumbers(false);
		r.getThisProcessor().setLowercase(true);
		r.getThisProcessor().setStem(false);
		// make the stoplist empty
		r.getThisProcessor().setStopList(new ArrayList<String>());

		return r.getThisProcessor().process(r.getTokens());
	}
	
	/**
	 * number folding only
	 * returns arraylist -- which could be further processed or added to a dict
	 * 
	 */
	public ArrayList<String> stem(TDTReader r){
		System.out.println("stemming only:");
		// stemming only
		r.getThisProcessor().setFoldNumbers(false);
		r.getThisProcessor().setLowercase(false);
		r.getThisProcessor().setStem(true);
		// make the stoplist empty
		r.getThisProcessor().setStopList(new ArrayList<String>());

		return r.getThisProcessor().process(r.getTokens());
	}
	
	/**
	 * number folding only
	 * returns arraylist -- which could be further processed or added to a dict
	 * @throws FileNotFoundException 
	 * 
	 */
	public ArrayList<String> stoplist(TDTReader r, int stoplistindex) throws FileNotFoundException{
		System.out.println("stoplist removal only:");
		// stop list only
		r.getThisProcessor().setFoldNumbers(false);
		r.getThisProcessor().setLowercase(false);
		r.getThisProcessor().setStem(false);
		// get the imported stop list
		ArrayList<String> stoplist = r.getThisProcessor().importStopList();
		r.getThisProcessor().setStopList(stoplist);

		return r.getThisProcessor().process(r.getTokens());
	}
	
	/**
	 * all of the processors
	 * @throws IOException
	 */
	public void tokenProcessorAll() throws IOException{
		Experimenter exp = new Experimenter();
		TDTReader reader = new TDTReader("ap89_short.txt", new ImprovedTokenizer());
		Dictionary mainDict = new Dictionary();
		// set the processor
		reader.setTokenProcessor(new TokenProcessor());

		// number folding 
		ArrayList<String> numOnly = exp.numFold(reader);
		exp.addToDict(numOnly, mainDict);
		System.out.println("size of dict after numberfolding "+mainDict.size());
		
		// lowercase 
		ArrayList<String> lowerOnly = exp.lowercase(reader);
		exp.addToDict(lowerOnly, mainDict);
		System.out.println("size of dict after lowercaseing "+mainDict.size());
		
		// uncomment this to add stemming back into
/*		// stemming 
		ArrayList<String> stemOnly = exp.stem(reader);
		exp.addToDict(stemOnly, mainDict);
		System.out.println("size of dict after stemming "+mainDict.size());*/
		
		// stop list 
		ArrayList<String> stopOnly = exp.stoplist(reader, 0);
		exp.addToDict(stopOnly, mainDict);
		System.out.println("size of dict after stoplist "+mainDict.size()+" "+stopOnly.size() );
		
		
	}

	/**
	 * SOLUTION for 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException{
		Experimenter exp = new Experimenter();
		TDTReader reader = new TDTReader("ap89_short.txt", new ImprovedTokenizer());
		// set the processor
		reader.setTokenProcessor(new TokenProcessor());
		
		// problem 5.
		// number folding only
		ArrayList<String> numOnly = exp.numFold(reader);
		Dictionary numDict = new Dictionary();
		exp.addToDict(numOnly, numDict);
		System.out.println("size of dict after numberfolding "+numDict.size());
		
		// lowercase only
		ArrayList<String> lowerOnly = exp.lowercase(reader);
		Dictionary lowerDict = new Dictionary();
		exp.addToDict(lowerOnly, lowerDict);
		System.out.println("size of dict after lowercaseing "+lowerDict.size());
		
		// stemming only
		ArrayList<String> stemOnly = exp.stem(reader);
		Dictionary stemDict = new Dictionary();
		exp.addToDict(stemOnly, stemDict);
		System.out.println("size of dict after stemming "+stemDict.size());
		
		// stop list only
		ArrayList<String> stopOnly = exp.stoplist(reader, 0);
		// number folded dictionary
		Dictionary stopDict = new Dictionary();
		exp.addToDict(stopOnly, stopDict);
		System.out.println("size of dict after stoplist "+stopDict.size()+" "+stopOnly.size() +"\n\n");
		
		// all of the processing
		System.out.println("all of the normalizing");
		exp.tokenProcessorAll();

		
	}

}
