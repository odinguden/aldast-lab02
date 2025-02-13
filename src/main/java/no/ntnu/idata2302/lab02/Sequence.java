/*
 * This file is part of NTNU's IDATA2302 Lab02.
 *
 * Copyright (C) NTNU 2022
 * All rights reserved.
 *
 */
package no.ntnu.idata2302.lab02;

/**
 * Implement the Sequence ADT from Lecture 2.2
 *
 * The items in the sequence are indexed from 1 (as opposed to Java arrays that
 * are indexed from 0)
 */
public class Sequence {

	private static final int INITIAL_CAPACITY = 100;

	private int capacity;
	private int length;
	private int[] items;

	public Sequence() {
		this(INITIAL_CAPACITY, new int[]{});
	}

	public Sequence(int capacity, int[] items) {
		if (capacity < items.length) {
			throw new IllegalArgumentException("Capacity must be greater than item count");
		}
		this.capacity = capacity;
		this.length = items.length;
		this.items = new int[capacity];
		for (int i=0 ; i<items.length ; i++) {
			this.items[i] = items[i];
		}
	}

	/**
	 * @return The number of items in the sequence
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * @return the number of "buckets" currently allocated
	 */
	int getCapacity() {
		return this.capacity;
	}

	/**
	 * Return the item stored at the given index
	 *
	 * @param index the index of the desired item, starting at 1
	 * @return the item at the given index.
	 */
	public int get(int index) {
		if (index < 1 || index > length) {
			throw new IllegalArgumentException("Invalid index!");
		}
		return this.items[index - 1];
	}

	private void multiplyCapacity(float multiplier) {
		int newCapacity = (int) (capacity * multiplier);
		int[] clone = new int[newCapacity];

		for (int index = 0; index < length; index++) {
			clone[index] = items[index];
		}

		this.items = clone;
		this.capacity = newCapacity;
	}

	private int[] cloneArray() {
		int[] clonedArray = new int[capacity];
		
		for (int i = 0; i < items.length; i++) {
			clonedArray[i] = items[i];
		}

		return clonedArray;
	}

	private void makeSpace(int index) {
		for (int i = length; i > index; i--) {
			items[i] = items[i - 1];
		}

		items[index] = 0;
	}

	private void takeSpace(int index) {
		for (int i = index; i < length - 1; i++) {
			items[i] = items[i + 1];
		}
	}

	/**
	 * Append the given item at the end of the sequence
	 *
	 * @param item the item that must be inserted
	 */
	public void insert(int item, int index) {
		if (index < 1 || index > length + 1) {
			throw new IllegalArgumentException("Invalid index!");
		}

		index--;
		
		if (index > capacity || length + 1 >= capacity) {
			multiplyCapacity(2);
		}

		if (index <= length) {
			makeSpace(index);
		}

		items[index] = item;
		length++;
	}

	/**
	 * Remove the index at the given index
	 *
	 * @param index the index that must be removed.
	 */
	public void remove(int index) {
		if (index < 1 || index > length) {
			throw new IllegalArgumentException("Invalid index!");
		}
		index--;

		this.takeSpace(index);
		this.length--;

		if (this.length <= (this.capacity / 4)) {
			this.multiplyCapacity(0.5f);
		}
	}

	private int[] selectionSort() {
		int[] sortedArray = this.cloneArray();

		
		int i = 0;
		while (i < length) {
			int minimumIndex = i;
			for (int index = i; index < length; index++) {
				if (sortedArray[index] < sortedArray[minimumIndex]) {
					minimumIndex = index;
				}
			}
			int tempHold = sortedArray[i];
			sortedArray[i] = sortedArray[minimumIndex];
			sortedArray[minimumIndex] = tempHold;

			i++;
		}

		return sortedArray;
	}

	/**
	 * Find a index where the given item can be found. Returns 0 if that item cannot
	 * be found.
	 *
	 * @param item the item whose index must be found
	 * @return an
	 */
	public int search(int item) {
		if (length <= 0) {
			return 0;
		}

		boolean foundFlag = false;
		int foundIndex = 0;

		int index = 0;
		while (!foundFlag && index < length) {
			int current = items[index];

			if (current == item) {
				foundIndex = index + 1;
				foundFlag = true;
			}

			index++;
		}

		return foundIndex;
	}

	/**
	 * Find both the smallest and the largest items in the sequence.
	 *
	 * @return an array of length two where the first entry is the minimum and the
	 *         second the maximum
	 */
	public int[] extrema() {
		if (length == 0){
			throw new IllegalStateException();
		}
		int[] sorted = selectionSort();
		int minimum = sorted[0];
		int maximum = sorted[length - 1];
		return new int[]{minimum,maximum};
	}

	/**
	 * Check whether the given sequence contains any duplicate item
	 *
	 * @return true if the sequence has the the same items at multiple indices
	 */
	public boolean hasDuplicate() {
		boolean containsADuplicate = false;
		int[] sorted = selectionSort();
		
		int index = 1;

		while (index < length && !containsADuplicate) {
			if (sorted[index] == sorted[index - 1]) {
				containsADuplicate = true;
			}
			index++;
		}

		return containsADuplicate;
	}


	/**
	 * Convert the sequence into an Java array
	 */
	public int[] toArray() {
		return items;
	}

}
