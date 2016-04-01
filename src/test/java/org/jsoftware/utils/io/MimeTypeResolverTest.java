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
		String mimeType = resolver.getContentType("/abc.js");
		Assert.assertEquals("application/javascript", mimeType);
	}

	@Test
	public void testExtension1() {
		String mimeType = resolver.getContentType(".jS");
		Assert.assertEquals("application/javascript", mimeType);
	}
	
	@Test
	public void testExtension2() {
		String mimeType = resolver.getContentType("JS");
		Assert.assertEquals("application/javascript", mimeType);
	}
	
	@Test
	public void testExtensionDefault() {
		String mimeType = resolver.getContentType("UHSD*&SDGWG");
		Assert.assertEquals("default", mimeType);
	}

	@Test
	public void testDefaultOctetStream() throws Exception {
		MimeTypeResolver resolver = MimeTypeResolver.newInstanceOctetStream();
		String ct = resolver.getContentType("whatever");
		Assert.assertEquals("application/octet-stream", ct);
	}

	@Test
	public void testNoDefault() throws Exception {
		MimeTypeResolver resolver = MimeTypeResolver.newInstance();
		String ct = resolver.getContentType("abc.oSt");
		Assert.assertNull(ct);
	}

	@Test
	public void testRegisterCustomType() throws Exception {
		resolver.registerExtension("OST", "application/ost");
		String ct = resolver.getContentType("abc.oSt");
		Assert.assertEquals("application/ost", ct);
	}
}
