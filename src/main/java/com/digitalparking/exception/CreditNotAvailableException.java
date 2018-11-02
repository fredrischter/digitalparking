package com.digitalparking.exception;

public class CreditNotAvailableException extends Exception {

	private static final long serialVersionUID = -6961896002093344100L;

	public CreditNotAvailableException(String msg) {
		super(msg);
	}
}
