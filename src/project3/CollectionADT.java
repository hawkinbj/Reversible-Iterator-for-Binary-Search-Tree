package project3;

import java.util.*;

public interface CollectionADT<T> extends Iterable<T> {
	// Adds the element, returns true if successful
	public boolean add(T element);

	// Removes the element, returns true if successful
	public boolean remove(T element);

	// Returns true if the element is in the collection
	public boolean contains(T element);

	// Returns the number of elements in the collection
	public int size();

	// Returns an iterator over all the elements
	public Iterator<T> iterator();

	// Returns a string representation of the collection
	public String toString();
}
