package project3;

import java.util.*;

public class RIForBST<T extends Comparable> implements ReversibleIterator<T> {

	private PublicBTNode<T> currentnode, nextnode, previousnode, node;
	// flags to indicate first calls to next() and/or previous()
	private boolean firstNextCall, firstPreviousCall, bugFlag;
	private SortedBST<T> list;
	// keeps track of bounds
	private int count;

	// constructor for basic RI over list
	public RIForBST(SortedBST<T> list) {
		this.list = list;
		firstNextCall = firstPreviousCall = true;
		bugFlag = false;
		currentnode = list.findMinNode();
		previousnode = node = null;
		nextnode = list.findMinNode();
		count = 0;
	}

	// constructor for RI >= start
	public RIForBST(SortedBST<T> list, T start) {
		this.list = list;
		if (start.compareTo(list.findMax()) > 0) {
			throw new IndexOutOfBoundsException();
		}
		firstNextCall = firstPreviousCall = true;
		currentnode = previousnode = node = null;
		nextnode = this.nextGreatest(start);
		if (!this.hasNext() && nextnode.element.compareTo(list.findMax()) <= 0) {
			bugFlag = true;
			count--;
		}
	}

	public boolean hasNext() {
		return count < list.size() - 1;
	}

	// helper method to find next element >= start
	private PublicBTNode<T> nextGreatest(T start) {
		if (list.findMinNode() == null)
			throw new NoSuchElementException();
		nextnode = list.findMinNode();
		// call next() until nextnode is >= start
		while (nextnode.element.compareTo(start) < 0) {
			this.next();
		}
		// reset flags
		firstNextCall = true;
		firstPreviousCall = true;
		return nextnode;
	}

	// returns next element
	public T next() {
		if (nextnode == null)
			throw new NoSuchElementException();
		// This less-than-elegant segment addresses the exception/bug that
		// occurs when calling iterator(start) where start is > the 2nd-to-last
		// element in the tree but still <= the last element.
		// Lowers the count/bound so next can execute and output the
		// last element - and then raises it back up.
		if (bugFlag) {
			bugFlag = false;
			count++;
			return nextnode.element;
		}
		// don't update any nodes first time called
		if (firstNextCall) {
			firstNextCall = false;
			return nextnode.element;
		}
		currentnode = nextnode;
		if (currentnode.right != null) {
			node = currentnode.right;
			while (node.left != null) {
				node = node.left;
			}
			previousnode = node.parent;
			nextnode = node;
		} else {
			PublicBTNode<T> child = currentnode;
			PublicBTNode<T> parent = currentnode.parent;
			while (parent != null && child == parent.right) {
				child = parent;
				parent = parent.parent;
			}
			previousnode = child;
			nextnode = parent;
		}
		// update bounds and nodes
		count++;
		previousnode = currentnode;
		return nextnode.element;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public boolean hasPrevious() {
		return count > 1;
	}

	// returns previous element
	public T previous() {
		if (previousnode == null) {
			throw new NoSuchElementException();
		}
		// don't update any nodes first time called
		if (firstPreviousCall) {
			firstPreviousCall = false;
			return previousnode.element;
		}
		currentnode = previousnode;
		if (currentnode.left != null) {
			node = currentnode.left;
			while (node.right != null) {
				node = node.right;
			}
			nextnode = node.parent;
			previousnode = node;
		} else {
			PublicBTNode<T> child = currentnode;
			PublicBTNode<T> parent = currentnode.parent;
			while (parent != null && child == parent.left) {
				child = parent;
				parent = parent.parent;
			}
			nextnode = child;
			previousnode = parent;
		}
		// update bounds and nodes
		count--;
		nextnode = currentnode;
		return previousnode.element;
	}
}
