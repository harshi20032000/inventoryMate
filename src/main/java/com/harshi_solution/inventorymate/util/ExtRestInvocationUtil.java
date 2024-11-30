package com.harshi_solution.inventorymate.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.harshi_solution.inventorymate.external.model.response.BaseExtResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExtRestInvocationUtil {

	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	private static final String COMMONLOG_MESSAGE = "Object value of Rest Template in use : {} ";
	private static final String TOKEN = "token";
	
    private static final Logger log = LoggerFactory.getLogger(ExtRestInvocationUtil.class);


	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestErrorManagerUtil serviceErrorHandler;

	public <T extends BaseExtResponse> T getData(final String url, Class<T> clazz, boolean... errorCustomization) {
		return getData(url, clazz, null, errorCustomization);
	}

	private <T extends BaseExtResponse> T getData(final String url, Class<T> clazz, Map<String, String> headers,
			boolean... errorCustomization) {

		HttpHeaders requestHeaders = new HttpHeaders();
		setHeaders(requestHeaders);
		if (headers != null) {
			Set<Entry<String, String>> headerKVPair = headers.entrySet();
			for (Entry<String, String> entry : headerKVPair) {
				requestHeaders.add(entry.getKey(), entry.getValue());
			}
		}
		HttpEntity<T> entity = new HttpEntity<>(requestHeaders);
		log.info(COMMONLOG_MESSAGE, restTemplate);
		ResponseEntity<T> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, clazz);
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException e) {
			throw e;
		}
		T response = responseEntity.getBody();
		serviceErrorHandler.sendErrorIfExist(entity, response, errorCustomization);
		return response;
	}

	public <U extends BaseExtResponse, T> U postData(String url, T data, Class<U> clazz,
			boolean... errorCustomization) {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		setHeaders(requestHeaders);

		U returnValue = postData(url, data, clazz, requestHeaders);
		serviceErrorHandler.sendErrorIfExist(data, returnValue, errorCustomization);
		return returnValue;
	}

	private <T, U> U postData(String url, T data, Class<U> clazz, HttpHeaders requestHeaders) {
		HttpEntity<T> requestEntity = new HttpEntity<>(data, requestHeaders);
		log.info(COMMONLOG_MESSAGE, restTemplate);
		ResponseEntity<U> responseEntity = null;
		try {
			responseEntity = restTemplate.postForEntity(url, requestEntity, clazz);
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException e) {
			throw e;
		} catch (RestClientException e1) {
			throw e1;
		}
		return responseEntity.getBody();
	}

	public <U extends BaseExtResponse, T> Mono<Void> postDataInAsynch(String url, T data, Class<U> clazz) {
		WebClient client = WebClient.builder().baseUrl(url)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)//
				.defaultHeader("correlationHeader", MDC.get(Constants.CORELATION_REQUEST_HEADERS)).build();

		return client.post().uri(url).contentType(MediaType.APPLICATION_JSON)//
				.body(BodyInserters.fromValue(data)).retrieve().bodyToMono(clazz).then();
	}

	public <U extends BaseExtResponse, T> Mono<U> postInAsynch(String url, T data, Class<U> clazz) {
		WebClient client = WebClient.builder().baseUrl(url)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)//
				.defaultHeader("correlationHeader", MDC.get(Constants.CORELATION_REQUEST_HEADERS)).build();

		return client.post().uri(url).contentType(MediaType.APPLICATION_JSON)//
				.body(BodyInserters.fromValue(data)).retrieve().bodyToMono(clazz);
	}

	public <U extends BaseExtResponse, T> U postForMultipart(String url, T data, String token, Class<U> clazz,
			boolean... errorCustomization) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", CONTENT_TYPE_APPLICATION_JSON);
		headers.set(TOKEN, token);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		ResponseEntity<U> responseEntity = null;
		HttpEntity<T> requestEntity = new HttpEntity<T>(data, headers);
		try {
			responseEntity = restTemplate.postForEntity(url, requestEntity, clazz);
		} catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException e) {
			throw e;
		} catch (RestClientException e1) {
			throw e1;
		}
		return responseEntity.getBody();
	}

	private void setHeaders(HttpHeaders requestHeaders) {
		requestHeaders.set(Constants.SERVICE_CORELATION_REQUEST_HEADERS,
				CommonUtil.isNullOrEmpty(MDC.get(Constants.CORELATION_REQUEST_HEADERS)) ? "1234567890"
						: MDC.get(Constants.CORELATION_REQUEST_HEADERS));

	}
}
