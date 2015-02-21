// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Experimenter {

	public Experimenter() {
		// TODO Auto-generated constructor stub
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
	 * SOLUTION for 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException{
		// set up new experiment, with TDT reader
		Experimenter exp = new Experimenter();
		TDTReader reader = new TDTReader("ap89_short.txt");
		// set tokenizer
		Tokenizer tknzr = new ImprovedTokenizer();
		reader.setTokenizer(tknzr);
		// set token processor
		TokenProcessor tknproc = new TokenProcessor();
		reader.setTokenProcessor(tknproc);
		
		// read documents
		String doctext = reader.rea
		
	}

}
