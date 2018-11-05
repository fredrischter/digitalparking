package com.digitalparking.resource;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AssetResourceIntegrationTest {
	
	@LocalServerPort
	private int port;
	
	private static final String CREATE_ASSET_RESOURCE = "/pms/v1/assets";

	private static final String START_SESSION_RESOURCE = "/pms/v1/assets/1/sessions";

	private static final String END_SESSION_RESOURCE = "/pms/v1/assets/1/vehicle/A111/session";
	
	@Before
	public void setUp() throws Exception {
	    RestAssured.port = port;
	}
	
	@Test
	public void createAssetTest() {
		given()
			.body("{\"address\":\"Example St.\",\"active\":false}")
			.contentType(ContentType.JSON)
		.when()
			.post(CREATE_ASSET_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void startSessionTest() {
		given()
			.body("{\"licensePlateNumber\":\"A111\"}") 
			.contentType(ContentType.JSON)
		.when()
			.post(START_SESSION_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void endSessionTest() {
		startSessionTest();
		
		given()
			.body("{ \"status\": \"stopped\" }") 
			.contentType(ContentType.JSON)
		.when()
			.post(END_SESSION_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	
}
