// Grace Yoo
// CS 336, SP'15
// Assn 3

/**
 * basic singly linked list from data strutures
 * this data structure was written with generics
 * our implementations of singly linked lists will probably use Integers
 * 
 * @author Seunginah
 *
 * @param <V>
 */
public class SinglyLinkedList<V> {
	protected Node<V> head;
	protected Node<V> tail;
	protected long size;

	/**
	 * constructor with no parameters
	 */
	public SinglyLinkedList() {
		head = null;
		tail = null;
		size = 0;
	} 

	/**
	 * add  note to beginning of list
	 * @param node
	 */
	public void addFirst(Node<V> node) {
		// set tail if this is the first node
		if (tail == null)
			tail = node;
		node.setNext(head); // make this point to the head
		head = node;

		// increment the size
		size++;
	} 

	/**
	 * add a node after specified node
	 * @param currentN
	 * @param newN
	 */
	public void addAfter(Node<V> currentN, Node<V> newN) {
		if(currentN==null){
			System.out.println(currentN.getElement()+" is null");
		}
		// if we're adding after the tail
		else if (currentN == tail){
			// update the current tail's next
			currentN.setNext(newN);
			// update the tail to this new tail
			this.tail = newN;
		}
		// otherwise
		// i think there is something buggy with tail settings...
		else{
			// if the current node HAS a next node,
			if(currentN.hasNext()){

				// update the current tail's next
				currentN.setNext(newN);
				// update the tail to this new tail
				this.tail = newN;

			}
			// if it doesnt, the current node seems to be the tail..
			else{		
				// set the new node's next to the current node's next
				newN.setNext(currentN.getNext());
				// then, set the current node's next to the new node
				currentN.setNext(newN);

			}
		}

		// increment size
		size++;
	} 

	/**
	 * add node to end of list
	 * @param node
	 */
	public void addLast(Node<V> node) {
		// has no next pointer
		node.setNext(null);
		// set current tail's next to this new tail
		tail.setNext(node);
		// update tail to new tail
		tail = node;
		// increment size
		size++;
	}

	/**
	 * remove the first node from the list, returning the node
	 * @return
	 */
	public Node<V> removeFirst() {
		if (head == null){
			System.err.println("list is empty");
		}

		// save the one to return
		Node<V> temp = head;

		// do reference manipulation
		head = head.getNext();
		temp.setNext(null);
		// decrement size
		size--;

		return temp;

	} 


	/**
	 * traverse to the last node, removing and returning it
	 * @return
	 */
	public Node<V> removeLast() {
		Node<V> prevN;
		Node<V> removeN;

		// throw error if list is empty
		if (size == 0)
			System.err.println("list is empty");

		// start with the head
		prevN = getFirst();
		// get to the last node
		for (int count = 0; count < size - 2; count++)
			prevN = prevN.getNext();
		// save last node
		removeN = tail;

		// update pointer
		prevN.setNext(null);
		tail = prevN;
		// decrement list size
		size--;

		return removeN;
	} 

	/**
	 * remove a specified node from the list
	 * @param removeN
	 */
	public void remove(Node<V> removeN) {
		Node<V> prevN;
		Node<V> currentN;

		// throw error for empty list
		if (size == 0)
			System.err.println("list is empty");

		// check if the head or tail of the list is the one to remove
		currentN = getFirst();
		if (currentN == removeN)
			removeFirst();
		currentN = getLast();
		if (currentN == removeN)
			removeLast();

		// if it wasn't head or tail, check all the other nodes
		if (size - 2 > 0) {
			prevN = getFirst();
			currentN = getFirst().getNext();
			for (int count = 0; count < size - 2; count++) {
				if (currentN ==removeN) {
					// update pointer, update size
					prevN.setNext(currentN.getNext());
					size--;
					break;
				}

				// update pointer
				prevN = currentN;
				currentN = currentN.getNext();
			} 
		} 

	}

	/**
	 * get head of list
	 * @return
	 */
	public Node<V> getFirst() {
		return head;
	}
	/**
	 * get tail of list
	 * @return
	 */
	public Node<V> getLast() {
		return tail;
	}
	/**
	 * get size of list
	 * @return
	 */
	public long getSize() {
		return size;
	}

}