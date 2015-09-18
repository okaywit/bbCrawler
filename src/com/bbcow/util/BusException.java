package com.bbcow.util;

public class BusException extends Throwable{

	private static final long serialVersionUID = 1L;

	public BusException(Throwable cause) {
		super(cause);
	}
	public BusException(String vUrl,Throwable cause) {
		super(vUrl,cause);
	}
	
}
