package com.harshi_solution.inventorymate.util;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomError {

	private int code;
	private String message;
	
	public static CustomError getNewError(int errorKey, String message) {
		return new CustomError(errorKey, Objects.requireNonNullElse(message, "An error occurred for some unknown reason"));
	}

	public CustomError(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
