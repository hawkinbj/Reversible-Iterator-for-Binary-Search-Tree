package project3;

public class SortedBST<T extends Comparable> implements SortedADT<T> {
	private PublicBTNode<T> root;
	private int size;

	public SortedBST() {
		root = null;
		size = 0;
	}

	// return the maximum element
	// O(logn)
	public T findMax() {
		if (size == 0)
			return null;
		PublicBTNode<T> node = root;
		while (node.right != null) {
			node = node.right;
		}
		return node.element;
	}

	// return the minimum element
	// O(logn)
	public T findMin() {
		if (size == 0)
			return null;
		PublicBTNode<T> node = root;
		while (node.left != null) {
			node = node.left;
		}
		return node.element;
	}

	// added this - returns minimum element node
	// O(logn)
	public PublicBTNode<T> findMinNode() {
		if (size == 0)
			return null;
		PublicBTNode<T> node = root;
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	// added this - returns max element node
	public PublicBTNode<T> findMaxNode() {
		if (size == 0)
			return null;
		PublicBTNode<T> node = root;
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	// Adds the element, returns true if successful
	// O(logn)
	public boolean add(T element) {
		PublicBTNode<T> newnode = new PublicBTNode<T>(element);
		if (size == 0) {
			// newnode is the root
			root = newnode;
		} else {
			// newnode goes down in the tree
			PublicBTNode<T> current = root;
			PublicBTNode<T> parent = null;
			while (current != null) {
				parent = current;
				if (element.compareTo(current.element) < 0) {
					current = current.left;
				} else {
					current = current.right;
				}
			}
			if (element.compareTo(parent.element) < 0) {
				parent.left = newnode;
			} else {
				parent.right = newnode;
			}
			newnode.parent = parent;
		}
		size++;
		return true;
	}

	// Returns true if the element is in the collection
	// O(logn)
	public boolean contains(T elt) {
		if (size == 0)
			return false;
		PublicBTNode<T> current = root;
		while (current != null) {
			if (elt.equals(current.element))
				return true;
			if (elt.compareTo(current.element) < 0) {
				current = current.left;
			} else
				current = current.right;
		}
		return false;
	}

	// Removes the element, returns true if successful
	// O(logn)
	public boolean remove(T elt) {
		if (size == 0)
			return false;
		PublicBTNode<T> current = root;
		while (current != null && !elt.equals(current.element)) {
			if (elt.compareTo(current.element) < 0) {
				current = current.left;
			} else
				current = current.right;
		}
		if (current == null)
			return false;

		// identify node to promote: 4 cases
		PublicBTNode<T> promote = null;
		if (current.left == null && current.right == null) {
			promote = null; // nothing to promote
		} else if (current.left == null) {
			promote = current.right;
		} else if (current.right == null) {
			promote = current.left;
		} else {
			// find minimum node on right side
			PublicBTNode<T> node = current.right;
			while (node.left != null) {
				node = node.left;
			}
			// link node's child with node's parent
			if (node.right != null)
				node.right.parent = node.parent;

			// link node's parent to node's child
			if (node == node.parent.left)
				node.parent.left = node.right;
			else
				node.parent.right = node.right;

			promote = node;

			// change references so promote becomes the parent to current's
			// child
			promote.left = current.left;
			promote.right = current.right;
			promote.left.parent = promote;
			if (null != promote.right)
				promote.right.parent = promote;
		}
		// we have a node to promote
		// it's children have been set up as needed
		// just need to fix parent reference
		if (current == root) {
			if (promote != null)
				promote.parent = null;
			root = promote;
		} else {
			if (promote != null)
				promote.parent = current.parent;
			if (current == current.parent.left)
				current.parent.left = promote;
			else
				current.parent.right = promote;
		}
		size--;
		return true;
	}

	// Returns an iterator over all the elements
	// O(1)
	public ReversibleIterator<T> iterator() {
		return new RIForBST<T>(this);
	}

	// return an Iterator that starts with the first element
	// that is greater than or equal to start
	// O(1)
	public ReversibleIterator<T> iterator(T start) {
		return new RIForBST<T>(this, start);
	}

	// Returns a string representation of the collection
	public String toString() {
		String result = "elements: ";
		result += toString(root);
		return result;
	}

	// Recursive inorder traversal
	public String toString(PublicBTNode<T> node) {
		if (node == null)
			return "";
		String result = "";
		if (node.left != null || node.right != null) {
			result += "[";
		}
		if (node.left != null) {
			result += toString(node.left) + " ";
		} else {
			result += toString(node.left);
		}
		result += node.element;
		if (node.right != null) {
			result += " " + toString(node.right);
		} else {
			result += toString(node.right);
		}
		if (node.right != null || node.left != null) {
			result += "]";
		}
		return result;
	}

	// returns the size of the tree
	// O(1)
	public int size() {
		return size;
	}

}
