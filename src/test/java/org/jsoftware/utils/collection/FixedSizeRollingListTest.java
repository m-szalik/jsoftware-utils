package org.jsoftware.utils.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FixedSizeRollingListTest {
	private FixedSizeRollingList<Integer> list;

	@Before
	public void setUp() throws Exception {
		list = new FixedSizeRollingList<>(2);
		list.add(1);
		list.add(2);
	}

	@Test
	public void testRollingValues() {
		assertEquals(Arrays.asList(1, 2), list);
		list.add(3);
		assertEquals(Arrays.asList(2, 3), list);
	}

	@Test
	public void testAddAll() throws Exception {
		FixedSizeRollingList<Integer> list = new FixedSizeRollingList<Integer>(3);
		list.add(1);
		list.add(2);
		list.add(3);
		assertEquals(Arrays.asList(1, 2, 3), list);
		list.addAll(Arrays.asList(4, 5));
		assertEquals(Arrays.asList(3, 4, 5), list);
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertFalse(list.isEmpty());
		list.clear();
		assertTrue(list.isEmpty());
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(2, list.size());
		list.add(9);
		assertEquals(2, list.size());
		list.clear();
		assertEquals(0, list.size());
	}

	@Test
	public void testIndexOf() throws Exception {
		int index = list.indexOf(2);
		assertEquals(1, index);
	}

	@Test
	public void testIterator() throws Exception {
		List<Integer> dst = new LinkedList<>();
		Iterator<Integer> it = list.iterator();
		while(it.hasNext()) {
			dst.add(it.next());
		}
		assertEquals(list, dst);
	}

	@Test
	public void testToArray() throws Exception {
		Object[] ret = list.toArray();
		assertArrayEquals(new Object[]{1, 2}, ret);
	}

	@Test
	public void testToArrayGeneric() throws Exception {
		Integer[] ret = list.toArray(new Integer[0]);
		assertArrayEquals(new Integer[]{1, 2}, ret);
	}

	@Test
	public void testHashcode() throws Exception {
		int hc1 = list.hashCode();
		int hc2 = Arrays.asList(1, 2).hashCode();
		assertEquals(hc2, hc1);
	}

	@Test
	public void testRemove() throws Exception {
		assertEquals(2, list.size());
		list.remove(1);
		assertEquals(1, list.size());
	}

	@Test
	public void testGetValue() throws Exception {
		Integer i = list.get(1);
		assertEquals(Integer.valueOf(2), i);
	}

	@Test
	public void testRemoveObject() throws Exception {
		Object o = Integer.valueOf(1);
		list.remove(o);
		assertEquals(Arrays.asList(2), list);
	}

	@Test
	public void testListIterator() throws Exception {
		Iterator<Integer> it = list.listIterator(1);
		Integer value = it.next();
		assertEquals(Integer.valueOf(2), value);
		assertFalse(it.hasNext());
	}

	@Test
	public void testSetValue() throws Exception {
		list.set(1, 10);
		assertEquals(Arrays.asList(1, 10), list);
	}
}
