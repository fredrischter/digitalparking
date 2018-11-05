package com.digitalparking.resource;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AssetResourceIntegrationTest {

	private static final String CREATE_ASSET_RESOURCE = "/pms/v1/assets/";

	private static final String START_SESSION_RESOURCE = "/pms/v1/assets/1/sessions";

	private static final String END_SESSION_RESOURCE = "/pms/v1/assets/1/vehicle/X111/session";

	@Test
	public void createAssetTest() {
		when()
			.post(CREATE_ASSET_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void startSessionTest() {
		when()
			.post(START_SESSION_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void endSessionTest() {
		given()
			.body("{ \"status\": \"stopped\" }") 
		.when()
			.post(END_SESSION_RESOURCE)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	
}
