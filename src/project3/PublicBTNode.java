package project3;

public class PublicBTNode<T> {
	public T element;
	public PublicBTNode<T> left, right, parent;

	public PublicBTNode(T elt) {
		element = elt;
		left = right = parent = null;
	}
}
