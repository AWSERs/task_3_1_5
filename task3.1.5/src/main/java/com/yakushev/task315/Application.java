package com.yakushev.task315;
import model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.StringJoiner;


@SpringBootApplication
public class Application {


	static String URL = "http://94.198.50.185:7081/api/users";
	static RestTemplate restTemplate = new RestTemplate();

	static StringBuilder resultString = new StringBuilder();

	public static void main(String[] args) {
		getUsersFromAPI();
	}

	// получения юзера и куки
	private static void getUsersFromAPI(){
		ResponseEntity<String> firstRequest = restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
		String result = firstRequest.getBody();
		System.out.println(result + " - FIRST RESPONSE");
		//получение COOKIE
		List<String> cookies = firstRequest.getHeaders().get("Set-Cookie");
		System.out.println(cookies + " - COOKIES");


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		assert cookies != null;
		headers.set("Cookie", String.join(";", cookies));


		User userJamesBrown = new User();
		userJamesBrown.setId(3L);
		userJamesBrown.setName("James");
		userJamesBrown.setLastName("Brown");
		userJamesBrown.setAge((byte) 21);


		getEntity(userJamesBrown, headers);

		createUser(getEntity(userJamesBrown, headers));

		ResponseEntity<String> secondRequest = restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
		System.out.println(secondRequest.getBody() + " - SECOND RESPONSE");

		User userThomasShelby = new User();
		userThomasShelby.setId(3L);
		userThomasShelby.setName("Thomas");
		userThomasShelby.setLastName("Shelby");
		userThomasShelby.setAge((byte) 21);

		updateUser(getEntity(userThomasShelby, headers));

		deleteUser(getEntity(userThomasShelby, headers));
		System.out.println(headers + " - COOKIES");
		System.out.println(resultString + " - RESULT STRING");


	}

	// entity user
	private static HttpEntity<User> getEntity(User user, HttpHeaders headers) {
		return new HttpEntity<>(user, headers);
	}


	//post create
	private static void createUser(HttpEntity<User> entity) {
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		System.out.println("statusCode - " + statusCode);
		System.out.println(response.getBody());
		resultString.append(response.getBody());
		HttpHeaders responseHeaders = response.getHeaders();
		System.out.println("responseHeaders : " + responseHeaders);
	}


	// put entity
	private static void updateUser(HttpEntity<User> entity) {
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		System.out.println("statusCode - " + statusCode);
		System.out.println(response.getBody());
		resultString.append(response.getBody());
		HttpHeaders responseHeaders = response.getHeaders();
		System.out.println(responseHeaders);
	}

	//delete
	private static void deleteUser(HttpEntity<User> entity) {
		ResponseEntity<String> response = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		System.out.println("statusCode - " + statusCode);
		System.out.println(response.getBody());
		resultString.append(response.getBody());
		HttpHeaders responseHeaders = response.getHeaders();
		System.out.println(responseHeaders);
	}

}
