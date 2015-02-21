// Grace Seungin Yoo
// CS336 Intelligent IR, SP'15
// Assn1

package assignment1;

import java.util.ArrayList;

/**
 * An improved tokenizer class that uses the following tokenization rules:
 * - tokens are delimited by whitespace
 * - Single quotes at the beginning and end of words should be separate tokens
 * - Numbers should stay together.  A number can start with a `+' or a `-', 
 *   can have any number of digits, commas and periods interspersed, but must 
 *   end in a digit (note this is a more general definition that accepts things like ``192.168.1" 
 *   and other things like ``-129.,24.34").
 * - An abbreviation is any single letter followed by a period repeated 2 or more times.  
 *   In regex terms, ``(\w\.){2,}".  For example, ``I.B.M.", ``S.A.T." and ``p.m." are all 
 *   valid abbreviations, while ``Mr." or ``I.B" are not.  All abbreviations should have 
 *   the periods removed, i.e. ``I.B.M" becomes ``IBM".
 * - Finally, ``. , ? : ; " ` ( ) % $" should all be treated as separate tokens, regardless of 
 *   where they appear (as long as they don't conflict with the above rules).  So ``$10,000" 
 *   becomes two tokens ``$" and ``10,000" and ``I wondered,is this a test?" becomes 8 
 *   tokens, with the ``," and ``?" as separate tokens.
 * 
 * @author dkauchak
 *
 */
public class ImprovedTokenizer implements Tokenizer{
	// Given text, tokenize the text into individual tokens
	// and return an ArrayList with those tokens
	ArrayList<String> tokens = new ArrayList<String>();

	/**
	 * Given text, tokenize the text into individual tokens and 
	 * return and ArrayList with those tokens
	 */
	public ArrayList<String> tokenize(String text){	
		//attern p = p.compile("$");

		// I was having  a lot of trouble splitting the text with whitespace as well as special characters
		// I tried things like text.split("[( )+;:]") but whenever 
		// 		i combined the whitespace split with another character, it wouldn't work
		// Instead, i decided to replace special characters with the same chacter 
		// 		but also surrounded by spaces, then let the whitespace delimiter split the string
		// these are special characters i'm supposed to make into their own tokens:   \,?:;"`()%$    
		// these are the special characters i haven't gotten yet:   \?()$
		String specChar = text.replaceAll("\"", " \" ").replaceAll(",", " , ").replaceAll("%", " % ").replaceAll("`", " ` ").replaceAll(";", " ; ").replaceAll(":", " : ");
		//System.out.println("specChar: "+specChar);
		// the abbreviation replacement is really complicated so do it in its own step
		// this abbreviation replace gets rid of non-abbreviation periods...
		String abbv = specChar.replaceAll(
	            "(?<=(^|[.])[\\S&&\\D])[.](?=[\\S&&\\D]([.]|$))", "").replace(".", "");
		
		//System.out.println("abbv: "+abbv);
		
		// now, with all the remaining periods, replace with a period surrounded in whtiespace
		//String per = abbv.replaceAll("\.", " \. ");
		
		// split the line with whitespace
		String[] whitespace = abbv.split("( )+");
		
		int starthere = 0;
		// some tests showed that leading whitespace doesn't get removed by the split
		if(whitespace[0].isEmpty()){
			// then start the tokenizing after this index
			starthere = 1;
		}
		
		// test print all the things we'll be tokenizing!
		for(int i = starthere; i<whitespace.length; i++){
			//System.out.println("i:"+i+" "+whitespace[i]);
			tokens.add(whitespace[i]);
		}
		//System.out.println("\n");

		
		// this works for tests
		// but gets buggy with actual text -- uncomment to check it out
		
/*		// Let's handle apostrophes (that aren't in conjunctions, but surrounding word boundaries)
		for(int i = starthere; i<whitespace.length; i++){
			String s = whitespace[i];
			
			// handle 'this kind of statement', as well as  '''these'''
			if(!s.isEmpty()){
				// if starts with '
				if(s.substring(0, 1).equals("'")){
					// stack for apostrophes
					Stack<String> astack = new Stack<String>();
					// while the first letter of this token is an apostrophe
					while(s.substring(0,1).equals("'")){
						// push each apostrophe to the stack
						astack.push("'");
						// shorten the string
						s =s.substring(1);
					}
					// now pop these from stack into the tokens list
					while(!astack.isEmpty()){
						tokens.add(astack.pop());
					}
					// BEFORE we add the rest of this string back in, let's make sure that it doesnt
					// also END in an apostrophe. if it passes the next IF loop, it will be added
				}
				// now at this point, we still have whatever was attached to the apostrophes
				// if this word also ends with apostrophes, do the same stack thing but for the end
				if(s.substring(s.length()-1, s.length()).equals("'")){
					// stack for apostrophes
					Stack<String> astack = new Stack<String>();
					// while the first letter of this token is an apostrophe
					while(s.substring(s.length()-1, s.length()).equals("'")){
						// push each apostrophe to the stack
						astack.push("'");
						// shorten the string
						s =s.substring(0, s.length()-1);
						if(s.isEmpty()){
							System.out.println("break s: "+s);
							break;
						}
					}
					// now, pop the string back before popping the apostrophes
					tokens.add(s);
					// now pop these from stack into the tokens list
					while(!astack.isEmpty()){
						tokens.add(astack.pop());
					}
				}
				// if it wasn't handled by the above, add the string
				else{
					tokens.add(s);
				}
			}
		}*/
		
		//String printTokens = tokens.toString();
		//System.out.println(printTokens);
		return tokens;
	}

	/**
	 * This is just here to help you test some examples.
	 * You may remove it if you want, but I encourage you to write similar tests.
	 */
	public void test(){
		String test = "The N.Y.S.E. is up $10,000 or 1%.";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("The");
		answer.add("NYSE");
		answer.add("is");
		answer.add("up");
		answer.add("$");
		answer.add("10,000");
		answer.add("or");
		answer.add("1");
		answer.add("%");
		answer.add(".");
		runTest(test, answer);

		test = "1,000,000.00";
		answer = new ArrayList<String>();
		answer.add(test);
		runTest(test, answer);

		test = "0.1234";
		answer = new ArrayList<String>();
		answer.add(test);
		runTest(test, answer);	
	}
	/**
	 * test basic whitespace parsing
	 * special considerations were made for LEADING whitespace
	 */
	public void testWhiteSpace(){
		String test = "  there is    more    than  one   whitespace ";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("there");
		answer.add("is");
		answer.add("more");
		answer.add("than");
		answer.add("one");
		answer.add("whitespace");
		runTest(test, answer);
	}
	/**
	 * test the special character parsing
	 * the following characters have been handled: , ? : ; " `
	 * the following characters haven't been handled: \ ( ) % $
	 */
	public void testSpecChar(){
		String test = "$1,%`?;:\"";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("$");
		answer.add("1");
		answer.add(",");
		answer.add("%");
		answer.add("`");
		answer.add("?");
		answer.add(";");
		answer.add(":");
		answer.add("\""); // double quote
		runTest(test, answer);
	}
	
	/**
	 * test the 'quote' and ''quote'' parsing
	 * things to consider: 
	 * 		multiple quotes around 1 words or phrase:  ''multiple'' 
	 * 		entire phrases contained in 1 set of quotes: 'grace yoo'
	 * 		not separating apostrophes in conjunctions
	 */
	public void testQuotes(){
		String test = "let's test 'how well' this '''parses'''";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("let's");
		answer.add("test");
		answer.add("'");
		answer.add("how");
		answer.add("well");
		answer.add("'");
		answer.add("this");
		answer.add("'");
		answer.add("'");
		answer.add("'");
		answer.add("parses");
		answer.add("'");
		answer.add("'");
		answer.add("'");
		runTest(test, answer);
	}
	
	/**
	 * test the abbreviation handling
	 * things to consider:
	 * 		when periods denote and don't denote abbreviations
	 * 		the abbreviations work but the periods get deleted
	 */
	public void testAbbreviation(){
		String test = "I am a student at M.H.C. and a C.S. student.";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("I");
		answer.add("am");
		answer.add("a");
		answer.add("student");
		answer.add("at");
		answer.add("MHC");
		answer.add("and");
		answer.add("a");
		answer.add("CS");
		answer.add("student");
		answer.add(".");
		runTest(test, answer);
	}
	
	/**
	 * things to consider:
	 * 		numbers with decimals (multiple interspersed)
	 * 		numbers with commas
	 * 		numbers with positive or negative signs
	 * 
	 * things that don't work:
	 * 		numbers with positive signs (can't escape???)
	 * 		numbers with multiple interspersed commons and periods
	 */
	public void testNumbers(){
		String test = "1.2 +100 -100 -300,000 100.00";
		String numtest = test.replaceAll("(-*)?[0-9]+(.[0-9]*)?(,[0-9]*)?([0-9]*)?", "<NUM>");
		System.out.println("numtset "+numtest);
	}
	
	/**
	 * 
	 * 
	 */

	/**
	 * Test helper.  Tokenizes the test string and compares
	 * the result to the answer.  Outputs pass or fail.
	 * 
	 * @param test test string
	 * @param answer the correct tokenization of test
	 */
	private void runTest(String test, ArrayList<String> answer){
		// tokenize the test string
		ArrayList<String> result = tokenize(test);

		if( !checkResult(result, answer) ){
			System.out.println("Failed: " + test);

			for( String s: result ){
				System.out.print(s + " |");
				System.out.println();
			}

			System.out.println("size result"+result.size());
		}else{
			System.out.println("Passed: " + test);
		}
		// clear arraylist for next test
		result.clear();
	}

	/**
	 * Compares two ArrayLists of strings to see if they're the same.
	 * 
	 * @param result
	 * @param answer
	 * @return true if the ArrayLists are the same, false otherwise
	 */
	private boolean checkResult(ArrayList<String> result, ArrayList<String> answer){
		if( result.size() != answer.size() ){
			return false;
		}else{
			for( int i = 0; i < result.size(); i++ ){
				if( !result.get(i).equals(answer.get(i)) ){
					return false;
				}
			}

			return true;
		}
	}

/*	public static void main(String[] args){
		ImprovedTokenizer tknzr= new ImprovedTokenizer();
		tknzr.testWhiteSpace();
		tknzr.testSpecChar();
		tknzr.testQuotes();
		tknzr.testAbbreviation(); 
		tknzr.testNumbers();
		//tknzr.tokenize("ap89_short.txt");
	}*/
}
