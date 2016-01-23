package org.jsoftware.utils.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;


/**
 * Container that always holds <tt>size</tt> number of elements.
 * Elements can be removed by {@link #shift()} method.
 * Method {@link #fetchNew()} is used to get new elements if required.
 * <p>Elements can be <tt>null</tt>s if there is no more elements to get via {@link #fetchNew()} method.</p>
 * <p>This is not thread-safe class.</p>
 * @author szalik
 * @param <T>
 */
public abstract class AbstractFixedSizeRollingContainer<T> implements Serializable {
	private static final long serialVersionUID = -3777873725549224549L;
	private T[] objects;
	private boolean inited;


	/**
	 * @see AbstractFixedSizeRollingContainer
	 */
	@SuppressWarnings("unchecked")
	public AbstractFixedSizeRollingContainer(int size) {
		if (size < 2) {
			throw new IllegalArgumentException("Argument size must be 2 or more.");
		}
		Class<?> parametrizedClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; // so this is class of "T"
		objects = (T[]) Array.newInstance(parametrizedClass, size); // array of "T"
	}


	/**
	 * Must be invoked 
	 */
	private void init() {
		if (! inited) {
			for (int i = 0; i < objects.length; i++) {
				objects[i] = fetchNew();
			}
			inited = true;
		}
	}


	/**
	 * Remove last element ans switch others.
	 */
	public void shift() {
		init();
        System.arraycopy(objects, 1, objects, 0, objects.length - 1);
		objects[objects.length - 1] = fetchNew();
	}



	public T get(int index) {
		init();
		if (index < 0 || index >= objects.length) {
			return null;
		}
		return objects[index];
	}


	/**
	 * @return max length of container's elements. 
	 */
	public int length() {
		return objects.length;
	}


	/**
	 * @return @return length of container's elements without <tt>null</tt>s.
	 */
	public int getActiveSize() {
		init();
		int a = 0;
		for (T t : objects) {
			if (t != null) {
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
	 * Switch elements between positions 
	 * @param dstInd destination index
	 * @param srcInd source index
	 */
	public void switchWith(int dstInd, int srcInd) {
		init();
		T t = get(srcInd);
		objects[dstInd] = t;
	}

	
	/**
	 * Get current state as {@link List}
	 * @return current state as {@link List}
	 */
	public List<T> getAsList() {
		init();
		return Arrays.asList(objects);
	}


	/**
	 * Fetch new element when it is needed.
	 * @return new element
	 */
	protected abstract T fetchNew();

}
