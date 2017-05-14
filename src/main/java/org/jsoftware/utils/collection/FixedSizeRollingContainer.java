package org.jsoftware.utils.collection;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;


/**
 * Container that always holds <tt>size</tt> number of elements. It uses supplier to fill the container.
 * Elements can be removed by {@link #shift()} method.
 * <p>This class is not thread-safe.</p>
 * @param <T> the type of elements in this container
 * @author m-szalik
 */
public class FixedSizeRollingContainer<T> implements Serializable {
	private static final long serialVersionUID = -3777873725549224549L;
	private final Object[] objects;
	private final Supplier<T> supplier;
	private boolean initFinished;


	/**
	 * @param size size of a container
	 * @param supplier used to fetch new object whenever it is needed.
	 * @see FixedSizeRollingContainer
	 */
	@SuppressWarnings("unchecked")
	public FixedSizeRollingContainer(int size, Supplier<T> supplier) {
		if (size < 2) {
			throw new IllegalArgumentException("Argument size must be equal to 2 or more.");
		}
		if (supplier == null) {
			throw new IllegalArgumentException("Supplier cannot be null!");
		}
		this.supplier = supplier;
		this.objects = new Object[size];
	}


	/**
	 * Must be invoked 
	 */
	private void init() {
		if (!initFinished) {
			for (int i = 0; i < objects.length; i++) {
				objects[i] = fetchNew();
			}
			initFinished = true;
		}
	}

	private T fetchNew() {
		return supplier.get();
	}


	/**
	 * Remove last element ans switch others.
	 */
	public void shift() {
		init();
        System.arraycopy(objects, 1, objects, 0, objects.length - 1);
		objects[objects.length - 1] = fetchNew();
	}


	/**
	 * Get element at index
	 * @param index index
	 * @return an element
	 * @throws IndexOutOfBoundsException if index is less then zero or equal or more then <tt>size</tt>.
	 */
	public T get(int index) {
		init();
		if (index < 0 || index >= objects.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is not between <0;" + (objects.length-1) + ">.");
		}
		return (T) objects[index];
	}


	/**
	 * @return max length of container's elements. 
	 */
	public int length() {
		return objects.length;
	}


	/**
	 * @return length of container's elements without <tt>null</tt>s.
	 */
	public int getActiveSize() {
		init();
		int a = 0;
		for (Object obj : objects) {
			if (obj != null) {
				a++;
			}
		}
		return a;
	}


	/**
	 * Remove element at position <code>index</code> and replace it with new one.
	 * @param index index of element to replace
	 * @return old value
	 * @see #fetchNew()
	 */
	public T replaceWithNew(int index) {
		init();
		T old = get(index);
		objects[index] = fetchNew();
		return old;
	}


	/**
	 * Swap elements
	 * @param index0 index of element to swap
	 * @param index1 index of element to swap
	 */
	public void swap(int index0, int index1) {
		init();
		T t0 = get(index0);
		T t1 = get(index1);
		objects[index1] = t0;
		objects[index0] = t1;
	}

	
	/**
	 * Get current state as {@link List}
	 * @return current state as {@link List}
	 */
	public List<T> getAsList() {
		init();
		LinkedList<T> list = new LinkedList<>();
		for(Object o : objects) {
			list.add((T) o);
		}
		return list;
	}

}
