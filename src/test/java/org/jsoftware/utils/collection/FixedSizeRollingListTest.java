package org.jsoftware.utils.collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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


}
