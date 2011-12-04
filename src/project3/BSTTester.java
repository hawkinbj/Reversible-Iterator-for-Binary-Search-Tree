package project3;

import java.util.*;

public class TomsBSTTester {
	public static void main(String[] args) {

		System.out
				.println("Lab written by Brandon Hawkins. Test code written by Dr. Bylander");
		// test on Integers
		Integer[] ints = { 21, 15, 18, 37, 28, 45, 9, 22, 17 };
		Integer[] moreints = new Integer[51];
		for (int i = 0; i < 51; i++)
			moreints[i] = i;
		testRI(ints, moreints, true);

		// test on Strings
		String[] strings = { "to", "be", "or", "not", "to", "be", "to", "be",
				"or", "not", "to", "be" };
		String[] moreStrings = new String[62];
		int index = 0;
		for (int i = 0; i < 62; i++) {
			if (i < 10)
				moreStrings[i] = "" + (char) (i + 48);
			else if (i < 36)
				moreStrings[i] = "" + (char) (i - 10 + 65);
			else
				moreStrings[i] = "" + (char) (i - 36 + 97);
		}
		testRI(strings, moreStrings, true);

		// test on Doubles
		Double[] doubles = new Double[1000];
		Double[] moreDoubles = new Double[1000];
		for (int i = 0; i < 1000; i++) {
			doubles[i] = Math.random();
			moreDoubles[i] = Math.random();
		}
		testRI(doubles, moreDoubles, false);

		// Go out for a few random walks
		System.out.println("\nTesting ReversibleIterator with random walks");
		randomWalkRI(ints);
		randomWalkRI(strings);
		randomWalkRI(doubles);
	}

	// Add and remove elements from addlist into a binary search tree.
	// Test ReversibleIterator and other methods.
	// testlist should include additional elements to test.
	// If verbose is true, more output is produced.
	public static <T extends Comparable> SortedADT<T> testRI(T[] addlist,
			T[] testlist, boolean verbose) {

		T[] sortedAddlist = Arrays.copyOf(addlist, addlist.length);
		Arrays.sort(sortedAddlist);
		T[] sortedTestlist = Arrays.copyOf(testlist, testlist.length);
		Arrays.sort(sortedTestlist);

		// test add method
		SortedADT<T> tree = new SortedBST<T>();
		System.out.println("\nAdding " + addlist.length + " elements:");
		for (int i = 0; i < addlist.length; i++) {
			tree.add(addlist[i]);
			if (verbose)
				System.out.println("add(" + addlist[i] + "): \t" + tree);
		}
		if (!verbose)
			System.out.println("\tsize is " + tree.size());

		// test contains method
		// ignore elements in testlist that duplicate elements in addlist
		System.out.print("\nTesting contains:");
		int addi = 0, testi = 0, count = 0;
		while (addi < addlist.length && testi < testlist.length) {
			T elt = null;
			if (sortedAddlist[addi].compareTo(sortedTestlist[testi]) < 0) {
				elt = sortedAddlist[addi++];
			} else if (sortedAddlist[addi].compareTo(sortedTestlist[testi]) > 0) {
				elt = sortedTestlist[testi++];
			} else {
				while (testi < testlist.length
						&& sortedAddlist[addi].compareTo(sortedTestlist[testi]) == 0)
					testi++;
				elt = sortedAddlist[addi++];
			}
			if (tree.contains(elt)) {
				count++;
				if (verbose)
					System.out.print(" " + elt);
			}
		}
		while (addi < addlist.length) {
			T elt = sortedAddlist[addi++];
			if (tree.contains(elt)) {
				count++;
				if (verbose)
					System.out.print(" " + elt);
			}
		}
		while (testi < testlist.length) {
			T elt = sortedTestlist[testi++];
			if (tree.contains(elt)) {
				count++;
				if (verbose)
					System.out.print(" " + elt);
			}
		}
		if (verbose)
			System.out.println();
		System.out.println("\tcontains was true " + count + " times");

		// test findMin and findMax method
		System.out.println("\nTesting findMin and findMax:");
		System.out.println("\t" + tree.findMin() + " should be equal to "
				+ sortedAddlist[0]);
		System.out.println("\t" + tree.findMax() + " should be equal to "
				+ sortedAddlist[addlist.length - 1]);

		// test remove method
		System.out.println("\nRemoving all elements:");
		for (int i = 0; i < addlist.length; i++) {
			tree.remove(addlist[i]);
			if (verbose)
				System.out.println("remove(" + addlist[i] + "):\t" + tree);
		}
		if (!verbose)
			System.out.println("size is " + tree.size());
		for (int i = 0; i < addlist.length; i++) {
			if (tree.contains(addlist[i]))
				System.out.println("tree still contains " + addlist[i]);
		}

		// tests for ReversibleIterator
		tree = new SortedBST<T>();
		for (int i = 0; i < addlist.length; i++) {
			tree.add(addlist[i]);
		}

		System.out
				.println("\nTesting ReversibleIterator after adding elements again");
		System.out.println("\nTesting from first to last to first");
		if (verbose) {
			System.out.print("Should print:");
			for (int i = 0; i < addlist.length; i++) {
				System.out.print(" " + sortedAddlist[i]);
			}
			for (int i = addlist.length - 2; i >= 0; i--) {
				System.out.print(" " + sortedAddlist[i]);
			}
			System.out.println();
			System.out.print("             ");
		}
		ReversibleIterator<T> iter = tree.iterator();
		addi = 0;
		count = 0;
		while (iter.hasNext()) {
			T elt = iter.next();
			if (verbose)
				System.out.print(" " + elt);
			if (addi >= addlist.length || !elt.equals(sortedAddlist[addi]))
				count++;
			addi++;
		}
		addi = addlist.length - 2;
		while (iter.hasPrevious()) {
			T elt = iter.previous();
			if (verbose)
				System.out.print(" " + elt);
			if (addi < 0 || !elt.equals(sortedAddlist[addi]))
				count++;
			addi--;
		}
		if (verbose)
			System.out.println();
		if (count > 0) {
			System.out.println("\tIncorrect result " + count + " times");
		} else {
			System.out.println("\tAll results correct");
		}

		// This is intended to guarantee that the elements
		// at middlei-1 and middlei in the sorted addlist
		// have different values. middlei ends up at 0
		// if all values are equal.
		int middlei = addlist.length / 2;
		while (middlei > 0 && middlei < addlist.length
				&& sortedAddlist[middlei - 1].equals(sortedAddlist[middlei]))
			middlei++;

		if (middlei == addlist.length) {
			middlei = addlist.length / 2;
			while (middlei > 0
					&& middlei < addlist.length
					&& sortedAddlist[middlei - 1]
							.equals(sortedAddlist[middlei]))
				middlei--;
		}
		System.out.println("\nMiddle element is " + sortedAddlist[middlei]);

		System.out.println("\nTesting from middle to last to first");
		if (verbose) {
			System.out.print("Should print:");
			for (int i = middlei; i < addlist.length; i++) {
				System.out.print(" " + sortedAddlist[i]);
			}
			for (int i = addlist.length - 2; i >= 0; i--) {
				System.out.print(" " + sortedAddlist[i]);
			}
			System.out.println();

			System.out.print("             ");
		}

		iter = tree.iterator(sortedAddlist[middlei]);
		addi = middlei;
		count = 0;
		while (iter.hasNext()) {
			T elt = iter.next();
			if (verbose)
				System.out.print(" " + elt);
			if (addi >= addlist.length || !elt.equals(sortedAddlist[addi]))
				count++;
			addi++;
		}
		addi = addlist.length - 2;
		while (iter.hasPrevious()) {
			T elt = iter.previous();
			if (verbose)
				System.out.print(" " + elt);
			if (addi < 0 || !elt.equals(sortedAddlist[addi]))
				count++;
			addi--;
		}
		if (verbose)
			System.out.println();
		if (count > 0) {
			System.out.println("\tIncorrect result " + count + " times");
		} else {
			System.out.println("\tAll results correct");
		}

		System.out.println("\nTesting from middle to first to last");
		if (verbose) {
			System.out.print("Should print:");
			for (int i = middlei - 1; i >= 0; i--) {
				System.out.print(" " + sortedAddlist[i]);
			}
			for (int i = Math.min(1, middlei); i < addlist.length; i++) {
				System.out.print(" " + sortedAddlist[i]);
			}
			System.out.println();
			System.out.print("             ");
		}

		iter = tree.iterator(sortedAddlist[middlei]);
		addi = middlei - 1;
		count = 0;
		while (iter.hasPrevious()) {
			T elt = iter.previous();
			if (verbose)
				System.out.print(" " + elt);
			if (addi < 0 || !elt.equals(sortedAddlist[addi]))
				count++;
			addi--;
		}
		addi = 1;
		while (iter.hasNext()) {
			T elt = iter.next();
			if (verbose)
				System.out.print(" " + elt);
			if (addi >= addlist.length || !elt.equals(sortedAddlist[addi]))
				count++;
			addi++;
		}
		if (verbose)
			System.out.println();
		if (count > 0) {
			System.out.println("\tIncorrect result " + count + " times");
		} else {
			System.out.println("\tAll results correct");
		}

		return tree;
	}

	// Take a ReversibleIterator on a random walk
	public static <T extends Comparable> SortedADT<T> randomWalkRI(T[] addlist) {
		T[] sortedAddlist = Arrays.copyOf(addlist, addlist.length);
		Arrays.sort(sortedAddlist);

		SortedADT<T> tree = new SortedBST<T>();
		for (int i = 0; i < addlist.length; i++) {
			tree.add(addlist[i]);
		}

		// This results in the same sequence of "random" numbers every time.
		Random random = new Random(13);
		ReversibleIterator<T> iter = tree.iterator();
		int addi = -1;
		T elt = null;
		for (int i = 0; i < 1000000; i++) {
			if (iter.hasNext() && iter.hasPrevious()) {
				int r = random.nextInt(2);
				if (r == 0) {
					elt = iter.previous();
					addi--;
				} else {
					elt = iter.next();
					addi++;
				}
			} else if (iter.hasPrevious()) {
				elt = iter.previous();
				addi--;
			} else if (iter.hasNext()) {
				elt = iter.next();
				addi++;
			} else {
				System.out.println("Random walk failed on iteration" + (i + 1));
				return tree;
			}
			if (addi < 0 || addi >= addlist.length
					|| !elt.equals(sortedAddlist[addi])) {
				System.out.println("Random walk failed on iteration" + (i + 1));
				return tree;
			}

		}
		System.out.println("Random walk succeeded on 1000000 iterations");

		return tree;

	}
}
