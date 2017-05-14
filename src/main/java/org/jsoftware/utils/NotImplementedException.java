package org.jsoftware.utils;

/**
 * Operation is not yet implemented.
 * @author m-szalik
 */
public class NotImplementedException extends RuntimeException {
	static final String DEFAULT_MESSAGE = "Not implemented";
	private static final long serialVersionUID = 2281311197226328461L;

	public NotImplementedException() {
		super(DEFAULT_MESSAGE);
	}

	public NotImplementedException(String message) {
		super(message == null ? DEFAULT_MESSAGE : message);
	}
}
