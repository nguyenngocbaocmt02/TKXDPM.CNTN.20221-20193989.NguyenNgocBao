package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class implements send and received methods to server Date: 8/12/2021
 * 
 * @author anvd
 * @version 1.0
 */
public class API {
	/*
	 * Format date
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/*
	 * Log to console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Send GET Method
	 * 
	 * @param url:   url to server
	 * @param token: authenticate user
	 * @return String: response from server
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		// Setup connect to server
		HttpURLConnection connection = setupConnection(url, "GET", token);

		// Read returned data from server
		StringBuilder response = readResponse(connection);
		LOGGER.info("Respone Info: " + response.substring(0, response.length() - 1).toString());
		return response.substring(0, response.length() - 1).toString();
	}

	/**
	 * Send POST method to server
	 * 
	 * @param url:  url to server
	 * @param data: content of POST method
	 * @return String: response from server
	 * @throws IOException
	 */
	public static String post(String url, String data) throws IOException {
		allowMethods("PATCH");

		// Setup connection to server
		HttpURLConnection conn = setupConnection(url, "POST");

		// Send POST method to server
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();

		// Read returned data from server
		StringBuilder response = readResponse(conn);
		LOGGER.info("Respone Info: " + response.toString());
		return response.toString();
	}

	/**
	 * @param methods: other method such as PUT, PATCH, ...
	 * @deprecated Java <= 11
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * @param url:    url to server
	 * @param method: name of method
	 * @param token:  to authenticate user
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token)
			throws MalformedURLException, IOException, ProtocolException {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) line_api_url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		return connection;
	}

	/**
	 * @param url:    url to server
	 * @param method: name of method
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static HttpURLConnection setupConnection(String url, String method)
			throws MalformedURLException, IOException, ProtocolException {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) line_api_url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		return connection;
	}

	/**
	 * @param conn: connection to server
	 * @return response as StringBuilder
	 * @throws IOException
	 */
	private static StringBuilder readResponse(HttpURLConnection connection) throws IOException {
		BufferedReader in;
		String inputLine;
		if (connection.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		return response;
	}
}
