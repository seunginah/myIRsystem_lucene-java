

/**
 * adapted from old data structures assignment
 */
 
public class Node<V> {
    private V element;
    private Node<V> next;
 
    // constructor w/ no parameters
    public Node() {
        this(null, null); 
    }
 
    // constructor next node pointer specified
    public Node(V element, Node<V> next) {
        this.element = element;
        this.next = next;
    }
 
    public V getElement() {
        return element;
    }
    
    public boolean hasNext(){
    	if(next == null){
    		return false;
    	}
    	return true;
    }
 
    public Node<V> getNext() {
        return next;
    }
 
    public void setElement(V element) {
        this.element = element;
    }
 
    public void setNext(Node<V> next) {
        this.next = next;
    }
 
}