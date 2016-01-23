package org.jsoftware.utils.io;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MimeTypeResolverTest {
	private MimeTypeResolver resolver;
	
	@Before
	public void init() {
		resolver = new MimeTypeResolver("default");
	}
	
	@Test
	public void testFilename() {
		String mimeType = resolver.getContenType("/abc.js");
		Assert.assertEquals("application/javascript", mimeType);
	}

	@Test
	public void testExtenssion1() {
		String mimeType = resolver.getContenType(".jS");
		Assert.assertEquals("application/javascript", mimeType);
	}
	
	@Test
	public void testExtenssion2() {
		String mimeType = resolver.getContenType("JS");
		Assert.assertEquals("application/javascript", mimeType);
	}
	
	@Test
	public void testExtenssionDefault() {
		String mimeType = resolver.getContenType("UHSD*&SDGWG");
		Assert.assertEquals("default", mimeType);
	}
	
}
