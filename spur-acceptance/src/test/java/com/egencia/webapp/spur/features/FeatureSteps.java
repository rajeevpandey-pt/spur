package com.egencia.webapp.spur.features;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.egencia.webapp.spur.Application;

import cucumber.api.java.en.Given;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest(randomPort=true)
public class FeatureSteps {
	
	private ResponseEntity<String> response;
	
	private RestTemplate restTemplate = new TestRestTemplate();

	@Value("${local.server.port}")
	private int port;
	
	private String baseUrl() {
		return format("http://localhost:%d/spur", port).toLowerCase();
	}
	
	/*
	 * TODO Create some Cucumber steps here
	 */
	@Given("The app is up")
	public void itsUp() {
		response = restTemplate.getForEntity(baseUrl(), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.FOUND));
	}
}