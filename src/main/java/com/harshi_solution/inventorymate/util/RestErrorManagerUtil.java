package com.harshi_solution.inventorymate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.harshi_solution.inventorymate.exception.CoreErrorHandler;
import com.harshi_solution.inventorymate.external.model.response.BaseExtResponse;

@Component
public class RestErrorManagerUtil {
	
    private static final Logger log = LoggerFactory.getLogger(RestErrorManagerUtil.class);


	public void sendErrorIfExist(Object request, BaseExtResponse response, boolean... customizeTheError) {
		if (response == null) {
			CustomError error = CustomError.getNewError(123,
					"<p> We're sorry. ,<br>Unable to process your request right now. <br>Please try again later</p>");
			if (!customizeTheError[0]) {
				CoreErrorHandler.handleExternalServiceError(error);
			}
		} else {

			int errorCode = response.getStatusCode();
			String errorMessage = response.getMessage();
			if ((errorCode != 200)) {
				log.debug("Error code from Service: {}", errorCode);
				CustomError error = CustomError.getNewError(errorCode, errorMessage);
				if (customizeTheError.length == 0 || !customizeTheError[0]) {
					CoreErrorHandler.handleExternalServiceError(error);
				}
			}
		}
	}

}
