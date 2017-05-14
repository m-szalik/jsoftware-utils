package org.jsoftware.utils.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link List} that always holds max <tt>fixedSize</tt> of elements <tt>E</tt> if more elements are added the first one is automatically removed from {@link List}.
 * @author m-szalik
 * @param <E> list elements
 * <p>This class is not thread-safe.</p>
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

	public boolean containsAll(Collection<?> collection) {
		return list.containsAll(collection);
	}

	public boolean addAll(Collection<? extends E> collection) {
		for(E e : collection) {
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
		if (o == this) {
			return true;
		}
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
		return getClass().getSimpleName() + "[fixedSize:" + fixedSize + "]";
	}

}
