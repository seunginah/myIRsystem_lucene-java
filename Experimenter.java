// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn2

import java.io.IOException;
import java.util.HashMap;

public class Experimenter {

	public Experimenter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * test 2 different queries
	 * @param rdr
	 * @param idx
	 */
	public void testQuery(TDTReader rdr, Index idx){
		HashMap<String, PostingsList> map = idx.getMap();
		idx.genAllDocsPL();
		PostingsList all = idx.getAllDocs();
		
		// test query
		String q = "!korea AND !help";
		System.out.println("\nTesting the query: "+q);
		// what should the result for !korea, !help be?
		PostingsList koreaPL = map.get("korea");
		PostingsList NOTkoreaPL = all.not(koreaPL, all.size());
		int[] final1 = NOTkoreaPL.getIDs();
		String finalstr = "";
		for(int i =0; i<final1.length; i++){
			finalstr=finalstr+final1[i]+", ";
		}
		System.out.println("!korea should be: "+finalstr);
		
		PostingsList helpPL = map.get("help"); 
		PostingsList NOThelpPL = all.not(helpPL, all.size());
		int[] final2 = NOThelpPL.getIDs();
		finalstr = "";
		for(int i =0; i<final1.length; i++){
			finalstr=finalstr+final1[i]+", ";
		}
		System.out.println("!help should be:"+finalstr);
		
		
		PostingsList NOTkoreaNOThelpPL = all.andMerge(NOTkoreaPL, NOThelpPL);
		int[] final3= NOTkoreaNOThelpPL.getIDs();
		finalstr = "";
		for(int i =0; i<final3.length; i++){
			finalstr=finalstr+final3[i]+", ";
		}
		System.out.println("\n!korea AND !help should be: "+finalstr);
		
		idx.booleanQuery(q);
		
		// try an or
		System.out.println("\nTesting the query: !korea OR !help");
		PostingsList NOTkoreaORNOThelpPL = all.orMerge(NOThelpPL, NOTkoreaPL);
		int[] final4 = NOTkoreaORNOThelpPL.getIDs();
		finalstr = "";
		for(int i =0; i<final4.length; i++){
			finalstr=finalstr+final4[i]+", ";
		}
		System.out.println("\n!korea OR !help should be: "+finalstr);
		idx.booleanQuery("!korea or !help");
		
		
		
	}

	/**
	 * an example of how to test the OR operation
	 * @param rdr
	 * @param idx
	 * @return
	 */
	public PostingsList testORMerge(TDTReader rdr, Index idx){
		HashMap<String, PostingsList> map = idx.getMap();
		PostingsList testPL = new PostingsList();
		testPL.addDoc(new Integer(1));
		testPL.addDoc(new Integer(2));
		testPL.addDoc(new Integer(3));
		testPL.addDoc(new Integer(15));
		testPL.addDoc(new Integer(20));
		
		
		String testnotkey = "help"; // help: 1, 3, 8, 14
		PostingsList orPL = map.get(testnotkey); 
		
		return testPL.orMerge(testPL, orPL);
	}
	
	/**
	 * and example of how to test the AND operation
	 * @param rdr
	 * @param idx
	 * @return
	 */
	public PostingsList testANDMerge(TDTReader rdr, Index idx){
		HashMap<String, PostingsList> map = idx.getMap();
		
		// test postings list 
		String testkey = "</text>"; // 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
		String testnotkey = "help"; // help: 1, 3, 8, 14
		// 
		PostingsList testPL = map.get(testkey);  
		PostingsList andPL = map.get(testnotkey); 
		
		// result should be 1,3,8,14
		return testPL.andMerge(testPL, andPL);
		
	}
	
	/**
	 * an example of how to test the NOT operation
	 * @param rdr
	 * @param idx
	 * @return
	 */
	public PostingsList testNot(TDTReader rdr, Index idx){
		HashMap<String, PostingsList> map = idx.getMap();
		
		// test postings list 
		String testkey = "</text>";
		String testnotkey = "help"; // help: 1, 3, 8, 14
		// this is the list to return the NOT of
		PostingsList testPL = map.get(testkey);  
		PostingsList notPL = map.get(testnotkey); 
		
		// 
		return testPL.not(notPL, testPL.size() );
		
	}
	
	/**
	 * answer to problem 5 for write-up
	 * @param rdr
	 * @param idx
	 */
	public void problem5(){
		// mapping WITHOUT lowercasing
		// set up TDT reader w tokenizer
		TDTReader r = new TDTReader("ap89.txt");
		r.setTokenizer(new ImprovedTokenizer());
		// token processor settings-- do no settings
/*		TokenProcessor tknproc = new TokenProcessor();
		tknproc.setFoldNumbers(true);
		tknproc.setLowercase(true);
		tknproc.setStem(true);
		tknproc.setStopList("stoplist.txt");*/
		r.read(); // read
		Index i = new Index(r); // index
		int nolowercasing = i.getMap().size();
		
		// mapping WITH lowercasing
		TDTReader r2 = new TDTReader("ap89.txt");
		r2.setTokenizer(new ImprovedTokenizer());
		// token processor settings-- do no settings
		TokenProcessor tknproc = new TokenProcessor();
		//tknproc.setFoldNumbers(true);
		tknproc.setLowercase(true);
		//tknproc.setStem(true);
		//tknproc.setStopList("stoplist.txt");
		r2.setTokenProcessor(tknproc);
		r2.read(); // read
		Index i2 = new Index(r2);
		int lowercasing = i2.getMap().size();
		
		int diff = nolowercasing - lowercasing;
		
		System.out.println("without lowercasing, we have "+nolowercasing+
				"entries in the dictionary\nwithlowercasing, we have "+lowercasing+
				"entries in the dictionary\n the difference is "+diff+"words");
	}
	
	/**
	 * SOLUTION for 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException{
		Experimenter exp = new Experimenter();
		
		System.out.println("running some tests with a shortened version of the ap89.txt file");
		// set up new TDT reader
		TDTReader reader = new TDTReader("ap89_short.txt");
		reader.setTokenizer(new ImprovedTokenizer());

		// token processor settings
		TokenProcessor tknproc = new TokenProcessor();
		tknproc.setFoldNumbers(true);
		tknproc.setLowercase(true);
		//tknproc.setStem(true);
		//tknproc.setStopList("stoplist.txt");
		reader.setTokenProcessor(tknproc);
		
		// process documents
		reader.read();
		// do indexing
		Index idx = new Index(reader);
		
		// test boolean operations
		//exp.testNot(reader, idx);
		//exp.testANDMerge(reader, idx);
		//exp.testORMerge(reader,idx);
		
		// test queries
		exp.testQuery(reader,idx);
		
		System.out.println("solving problem 5 with the ap89.txt file");

		// do problem 5
		exp.problem5();
	}

}
