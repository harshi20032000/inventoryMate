package com.harshi_solution.inventorymate.external.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ErrorDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stackTrace;
	private String exceptionType;
	private String errorCode;
	private String errorMessage;
	

}
