

/**
 * this holds the actual index and also provides the interface to the search
 * engine for querying the index
 * 
 * @author dkauchak
 */ 
public class Index {
	/**
	 * @param reader a reader that will be used to read the corpus
	 */
	public Index(DocumentReader reader){
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
		return null;
	}
}
