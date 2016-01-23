package org.jsoftware.utils.collection;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FixedSizeRollingListTest {

	@Test
	public void rollingTest() {
		FixedSizeRollingList<Integer> list = new FixedSizeRollingList<Integer>(2);
		list.add(1);
		list.add(2);
		assertEquals(Arrays.asList(1, 2), list);
		list.add(3);
		assertEquals(Arrays.asList(2, 3), list);
	}

}
