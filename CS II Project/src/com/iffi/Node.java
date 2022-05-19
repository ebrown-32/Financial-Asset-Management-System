package com.iffi;

/**
 * Represents a node, or link in the
 * SortedLinkedList class
 * @author ebrown
 *
 * @param <T>
 */
public class Node<T> {

	private T element;
	private Node<T> next;

	public Node(T element) {
		super();
		this.element = element;
		this.next = null;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T getElement() {
		return element;
	}

	public Node<T> getNext() {
		return next;
	}
	
	public boolean hasNext() {
		return (this.next != null);
	}
	
	
	public String toString() {
		if (this.element == null) {
			return "(null)";
		}
		else {
			return this.element.toString();
		}
	}
}
