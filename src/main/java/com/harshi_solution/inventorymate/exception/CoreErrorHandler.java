package com.harshi_solution.inventorymate.exception;

import com.harshi_solution.inventorymate.util.CustomError;

public class CoreErrorHandler {

	private CoreErrorHandler() {
		throw new IllegalStateException("Not allowed");
	}
	
	public static void handleExternalServiceError(CustomError error) {
		throw new ExternalServiceException(error);
	}
}
