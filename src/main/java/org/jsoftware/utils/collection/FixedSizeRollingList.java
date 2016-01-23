package org.jsoftware.utils.collection;

import java.util.*;

/**
 * {@link List} that always holds max <tt>fixedSize</tt> of elements <tt>E</tt> if more elements added the first one is automatically removed from {@link List}.
 * @author szalik
 * @param <E> list elements
 * <p>This is not thread-safe class.</p>
 */
public class FixedSizeRollingList<E> implements List<E> {
	private final List<E> list;
	private final int fixedSize;
	
	/**
	 * @param fixedSize size of a container
	 * @see FixedSizeRollingList
	 */
	public FixedSizeRollingList(int fixedSize) {
		this.list = new LinkedList<E>();
		this.fixedSize = fixedSize;
	}
	
	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	public synchronized final boolean add(E e) {
		if (list.size() >= fixedSize) {
			list.remove(0);
		}
		return list.add(e);
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		for(E e : c) {
			add(e);
		}
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	public void clear() {
		list.clear();
	}

	public boolean equals(Object o) {
		return list.equals(o);
	}

	public int hashCode() {
		return list.hashCode();
	}

	public E get(int index) {
		return list.get(index);
	}

	public E set(int index, E element) {
		return list.set(index, element);
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public E remove(int index) {
		return list.remove(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return list.toString();
	}

}
