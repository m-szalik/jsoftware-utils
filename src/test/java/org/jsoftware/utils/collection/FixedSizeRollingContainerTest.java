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
}
