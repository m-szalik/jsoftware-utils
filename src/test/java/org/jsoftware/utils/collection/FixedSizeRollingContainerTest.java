package org.jsoftware.utils.collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FixedSizeRollingContainerTest {

	private AbstractFixedSizeRollingContainer<Integer> container;

	@Before
	public void init() {
		container = new AbstractFixedSizeRollingContainer<Integer>(3) {
			private static final long serialVersionUID = -1322807402985281202L;
			private int i = 0;
			@Override
			protected Integer fetchNew() {
				return i++;
			}
		};
	}

	@Test
	public void contentTest() {
		List<Integer> list = container.getAsList();
		Assert.assertEquals(3, container.length());
		Assert.assertEquals(3, list.size());
		Assert.assertEquals(Arrays.asList(0, 1, 2), list);
	}

    @Test
    public void shiftTest() {
        container.shift();
        container.shift();
        List<Integer> list = container.getAsList();
        Assert.assertEquals(Arrays.asList(2, 3, 4), list);
    }

	@Test
	public void testActiveSize() throws Exception {
		Assert.assertEquals(3, container.getActiveSize());
		container.replaceWithNew(1);
		Assert.assertEquals(3, container.getActiveSize());
	}

	@Test
	public void testReplace() throws Exception {
		Assert.assertEquals(Integer.valueOf(0), container.get(0));
		Assert.assertEquals(Integer.valueOf(1), container.get(1));
		Assert.assertEquals(Integer.valueOf(2), container.get(2));
		container.replaceWithNew(1);
		Assert.assertEquals(Integer.valueOf(0), container.get(0));
		Assert.assertEquals(Integer.valueOf(3), container.get(1));
		Assert.assertEquals(Integer.valueOf(2), container.get(2));
	}

	@Test
	public void testSwap() throws Exception {
		Assert.assertEquals(Integer.valueOf(0), container.get(0));
		Assert.assertEquals(Integer.valueOf(1), container.get(1));
		Assert.assertEquals(Integer.valueOf(2), container.get(2));
		container.swap(2, 0);
		Assert.assertEquals(Integer.valueOf(2), container.get(0));
		Assert.assertEquals(Integer.valueOf(1), container.get(1));
		Assert.assertEquals(Integer.valueOf(0), container.get(2));
	}
}
