
import java.io.*;

import java.util.Hashtable;

/**
 * A basic boolean search class that will allow you to run queries
 * against the TDT corpus once you get your your postings list
 * and index construction working properly.
 * 
 * @author dkauchak
 *
 */
public class BooleanSearch{
	private Index index;
	private Hashtable<Integer,Document> id2doc = null;
	
	public static void main(String[] args){
		TDTReader reader = new TDTReader("corpus_filename");
		ImprovedTokenizer tokenizer = new ImprovedTokenizer();
		TokenProcessor processor = new TokenProcessor();				
		processor.setLowercase(true);
		reader.setTokenizer(tokenizer);
		reader.setTokenProcessor(processor);
		
		BooleanSearch searchSystem = new BooleanSearch(reader);
		
		/* Subsitituting the code below for the line above would result in documents being returned rather
		 * than document ids
		 * 
		 * TDTReader reader2 = new TDTReader("/Users/dave/classes/cs160/assignments/assign1/sample_data/tdt-corpus.sample");
		 * reader2.setTokenizer(tokenizer);
		 * reader2.setTokenProcessor(processor);
		 * 
		 * BooleanSearch system = new BooleanSearch(reader, reader2);*/
		
		searchSystem.run();
	}
	
	/**
	 * Use this constructor if you want to have document IDs returned
	 * 
	 * @param reader
	 */
	public BooleanSearch(DocumentReader reader){
		index = new Index(reader);		
	}
	
	/**
	 * Use this constructor if you want to have the actual document text returned.
	 * You should pass two instances of the reader, but that are of the same type
	 * and read from the same file.
	 * NOTE: For now, this is a bit of a hack, but it's a simple choice for now
	 * that works without making life too complicated
	 * 
	 * @param reader
	 * @param reader2
	 */
	// Y
	public BooleanSearch(DocumentReader reader, DocumentReader reader2){
		index = new Index(reader);
		
		id2doc = new Hashtable<Integer,Document>();
		
		while( reader2.hasNext() ){
			Document next = reader2.next();
			id2doc.put(next.getDocID(), next);
		}
	}
	
	/**
	 * Run the boolean query interaction system.  The system
	 * waits for a query to be issued and then prints out the result.
	 * 
	 * If the user types "exit" the program ends, otherwise it will continue
	 * to accept queries.
	 */
	public void run(){
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			String query = in.readLine();
		
			while( !query.equals("exit") ){
				PostingsList result = index.booleanQuery(query);
				
				if( result.size() == 0 ){
					System.out.println("No results");
				}else{
					for( int id: result.getIDs() ){
						if( id2doc == null ){
							System.out.print(id + " ");
						}else{
							System.out.println(id2doc.get(id));
						}
					}
				
						System.out.println();
				}
				
				
				query = in.readLine();
			}
		}catch(IOException e){
			throw new RuntimeException(e.toString());
		}
	}
}
