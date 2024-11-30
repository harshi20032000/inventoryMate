package com.harshi_solution.inventorymate.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component("serviceProperties")
@Data
public class ServiceProperties {
	private boolean isMock;
	
	private String protocol="https";
	
	private String port ="8083";
	
	private String host = "10.18.168.222";
	
	private String freeApiLoginPath = "api.freeapi.app/api/v1/users/login";
	
	public boolean isMock() {
		return isMock;
	}

	public void setMock(boolean isMock) {
		this.isMock = isMock;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFreeApiLoginPath() {
		return freeApiLoginPath;
	}

	public void setFreeApiLoginPath(String freeApiRegisterPath) {
		this.freeApiLoginPath = freeApiRegisterPath;
	}

	/**
	 * Constructs and returns a URL string based on the provided port and path.
	 * 
	 * @param port The port number to be used in the URL. If null or empty, the port is omitted from the URL.
	 * @param path The path to be appended to the base URL.
	 * @return A constructed URL string in the format of "protocol://host[:port]path".
	 *
	 * The method checks if the provided port is null or empty:
	 * - If the port is null or empty, the URL is constructed without a port.
	 * - If the port is provided, the URL includes the port.
	 * 
	 * Example usage:
	 * - For a non-empty port: "http://example.com:8080/myPath"
	 * - For an empty port: "http://example.com/myPath"
	 * 
	 * @see CommonUtil#isNullOrEmpty(String) for the port validation.
	 */
	public String getUrl(String port,String path) {
		String url ="";
		if(CommonUtil.isNullOrEmpty(port)) {
			url = this.getProtocol()+"://"+this.getHost()+path;
		}
		else {
			url = this.getProtocol()+"://"+this.getHost()+":"+port+path;			
		}
		return url;
	}
	
	/**
	 * Constructs and returns a URL string based on the provided port, path, and additional path variables.
	 * 
	 * @param port The port number to be used in the URL. If null or empty, the port is omitted from the URL.
	 * @param path The initial path to be appended to the base URL.
	 * @param pathVariables A list of additional path variables to be appended to the URL.
	 * @return A constructed URL string in the format of "protocol://host[:port]path/pathVariable1/pathVariable2/...".
	 *
	 * The method performs the following steps:
	 * 1. It first constructs a base URL by calling the `getUrl(String port, String path)` method, which handles
	 *    the inclusion of the port if provided.
	 * 2. It then iterates over the `pathVariables` list, appending each path variable to the base URL.
	 *    The path variables are concatenated directly to the URL.
	 * 
	 * Example usage:
	 * - If `port` is provided and `pathVariables` contains ["var1", "var2"], the resulting URL might look like:
	 *   "http://example.com:8080/myPath/var1/var2"
	 * - If `port` is empty and `pathVariables` contains ["var1", "var2"], the resulting URL might look like:
	 *   "http://example.com/myPath/var1/var2"
	 * 
	 * @see #getUrl(String, String) for constructing the initial base URL.
	 */
	public String getUrl(String port, String path, List<String> pathVariables) {
		String url=getUrl(port,path);
		for(final String pathVariable: pathVariables) {
			url = String.format("%s%s", url, pathVariable);
		}
		return url;
	}
	
	
	
	/**
	 * Constructs and returns a URL string based on the provided port, path, and query parameters.
	 *
	 * @param port The port number to be used in the URL. If null or empty, the port is omitted from the URL.
	 * @param path The initial path to be appended to the base URL.
	 * @param paramKeyValue A map containing query parameters as key-value pairs, which will be appended to the URL.
	 * @return A constructed URL string in the format of "protocol://host[:port]path?key1=value1&key2=value2...".
	 *
	 * The method performs the following steps:
	 * 1. Constructs a base URL by calling the `getUrl(String port, String path)` method, which handles the inclusion of the port if provided.
	 * 2. Appends a '?' character to the base URL to indicate the start of query parameters.
	 * 3. Iterates over the `paramKeyValue` map, appending each key-value pair as a query parameter to the URL.
	 *    Each key-value pair is formatted as "key=value", and pairs are separated by an '&' character.
	 * 4. Removes the trailing '&' character from the constructed URL to ensure proper formatting.
	 *
	 * Example usage:
	 * - If `port` is "8080", `path` is "/search", and `paramKeyValue` contains {"query":"books", "sort":"asc"},
	 *   the resulting URL might look like:
	 *   "http://example.com:8080/search?query=books&sort=asc".
	 * - If `paramKeyValue` is empty, the method will return the base URL without any query parameters.
	 *
	 * @see #getUrl(String, String) for constructing the initial base URL.
	 */
	public String getUrl(String port, String path, Map<String, String> paramKeyValue) {
		StringBuilder paramUrl = new StringBuilder();
		paramUrl.append(getUrl(port, path)).append('?');
		paramKeyValue.forEach((key, value) -> paramUrl.append(key).append('=').append(value).append('&'));
		return paramUrl.substring(0, (paramUrl.length()-1));
	}
	
	
}
