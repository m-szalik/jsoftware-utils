package org.jsoftware.utils.collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
		Assert.assertFalse(list.isEmpty());
		list.clear();
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	public void testSize() throws Exception {
		Assert.assertEquals(2, list.size());
		list.add(9);
		Assert.assertEquals(2, list.size());
		list.clear();
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void testIndexOf() throws Exception {
		int index = list.indexOf(2);
		Assert.assertEquals(1, index);
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
		Assert.assertArrayEquals(new Object[] { 1, 2 }, ret);
	}

	@Test
	public void testToArrayGeneric() throws Exception {
		Integer[] ret = list.toArray(new Integer[0]);
		Assert.assertArrayEquals(new Integer[] { 1, 2 }, ret);
	}

	@Test
	public void testHashcode() throws Exception {
		int hc1 = list.hashCode();
		int hc2 = Arrays.asList(1, 2).hashCode();
		Assert.assertEquals(hc2, hc1);
	}

	@Test
	public void testRemove() throws Exception {
		Assert.assertEquals(2, list.size());
		list.remove(1);
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testGet() throws Exception {
		Integer i = list.get(1);
		Assert.assertEquals(Integer.valueOf(2), i);
	}

	@Test
	public void testRemoveObject() throws Exception {
		Object o = Integer.valueOf(1);
		list.remove(o);
		assertEquals(Arrays.asList(2), list);
	}
}
