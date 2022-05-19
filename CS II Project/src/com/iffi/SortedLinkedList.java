package com.iffi;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Custom linked list implementation that maintains an order according to
 * the passed in comparator
 * @author ebrown
 *
 * @param <T>
 */
public class SortedLinkedList<T> implements Iterable<T>{

	private Node<T> head;
	private Comparator<T> comp;
	private int size;
	
	/**
	 * Constructor that accepts comparator to maintain sorted order
	 */
	public SortedLinkedList(Comparator<T> comparator) {
		this.head = null;
		this.comp = comparator;
		this.size = 0;
	}
	
	/**
	 * Will add an element to the correct node in this list
	 * as specified by the comparator 
	 */
	public void addElement(T element) {
		
		Node<T> newNode = new Node<T>(element);
		Node<T> current = this.head;
		Node<T> temp = null;
		
		if (this.isEmpty()) {
			addToHead(element);
			size++;
			return;
		}
		while ((current != null) && (comp.compare(element, current.getElement()) >= 0)) {
			temp = current;
			current = current.getNext();
		}
		if (current == this.head) {
			addToHead(element);
		}
		newNode.setNext(current);
		if (temp != null) {
		temp.setNext(newNode);
		}
		else {
		 return;
		}
		size++;
	}
	
	/**
	 * Private method that adds the element to the head/start of the linked list
	 * Used internally, don't want to allow outside world to addToHead because 
	 * order wouldn't be maintained
	 * @param element
	 */
	private void addToHead(T element) {
		Node<T> nHead = new Node<T>(element);
		nHead.setNext(this.head);
		this.head = nHead;
	}
	
	/**
	 * Returns the size of this linked list 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Determines whether the linked list is empty (contains no elements)
	 * @return
	 */
	public boolean isEmpty() {
		if (this.getSize() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the node in this list given the position. It is private and unaccessible.
	 * @param position
	 * @return
	 */
	private Node<T> getNode(int position) {
		Node<T> current = this.head;
		for (int i = 0; i < position; i++ ) {
			current = current.getNext();
		}
		return current;
	}
	
	/**
	 * Remove an item from the linked list given its position
	 * @param position
	 */
	public void removeFromPosition(int position) {
		if (position == 0) {
			this.head = this.head.getNext();
			return;
		}
		Node<T> previous = this.getNode(position-1);
		Node<T> current = previous.getNext();
		Node<T> next = current.getNext();
		previous.setNext(next);
		size--;
	}
	
	/**
	 * Returns an element from a list given its position
	 * @param position
	 * @return
	 */
	public T getElement(int position) {
		Node<T> current = this.head;
		for (int i = 0; i < position; i++) {
			current = current.getNext();
		}
		return current.getElement();
	}
	
	/**
	 * Prints the list
	 */
	public void print() {
		if (this.head == null) {
			System.out.println("Empty");
		}
		else {
			StringBuilder sb = new StringBuilder();
			Node<T> current = this.head;
			while (current.getNext() != null) {
				sb.append(current.getElement() + ",");
				current = current.getNext();
			}
			sb.append(current.getElement() + ",");
			System.out.println(sb);
		}
	}

	/**
	 * Iterator implementation that allows the list to be iterated over
	 * using an enhanced for loop
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			Node<T> current = head;
			
			@Override
			public boolean hasNext() {
				return (this.current != null);
			}

			@Override
			public T next() {
				T e = this.current.getElement();
				this.current = this.current.getNext();
				return e;
			}
			
		};
	}
}
