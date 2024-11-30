package com.harshi_solution.inventorymate.exception;

import com.harshi_solution.inventorymate.util.CustomError;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ExternalServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int errorCode;
	private final String errorMessage;
	
	protected ExternalServiceException (CustomError error) {
		super(error.getMessage());
		this.errorCode=error.getCode();
		this.errorMessage=error.getMessage();
	}
}
