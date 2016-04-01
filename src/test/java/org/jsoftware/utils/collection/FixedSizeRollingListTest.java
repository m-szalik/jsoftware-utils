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
		list = new FixedSizeRollingList<Integer>(2);
		list.add(1);
		list.add(2);
	}

	@Test
	public void rollingTest() {
		assertEquals(Arrays.asList(1, 2), list);
		list.add(3);
		assertEquals(Arrays.asList(2, 3), list);
	}

	@Test
	public void testSizeClearEmpty() throws Exception {
		Assert.assertEquals(2, list.size());
		list.clear();
		Assert.assertTrue(list.isEmpty());
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
	public void testAddAll() throws Exception {
		list.addAll(Arrays.asList(9, 10, 11));
		assertEquals(Arrays.asList(10, 11), list);
	}
}
