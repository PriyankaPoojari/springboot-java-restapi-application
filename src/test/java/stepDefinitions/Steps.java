package stepDefinitions;



import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

import com.test.studentapp.HttpRequest;
import com.test.studentapp.StringUtil;

import io.cucumber.core.internal.gherkin.ast.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.internal.http.ContentTypeExtractor;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class Steps {
	
HttpHeaders headers;
//ResponseEntity<String> response;
Response response;
public static int id;
String responseMessage ;
String uri = "http://localhost:8080";
static RequestSpecification httpRequest;

@Given("^I set Base url$")
public void i_set_Base_url() {
	RestAssured.baseURI = uri;
 }


@When("^I Set request HEADER$")
public void i_Set_request_HEADER() {
	httpRequest = RestAssured.given();
	httpRequest.contentType("application/json; utf-8");
	httpRequest.accept("application/json");
}


@Given("^I Set student service api endpoint$")
public void i_Set_POST_student_service_api_endpoint() {
	//uri = "http://localhost:8080/student";
    System.out.println("Add URL :"+uri);
}

@Given("I Set student service api endpoint {string}")
public void i_Set_student_service_api_endpoint(String endPoint) {
	//httpRequest = (RequestSpecification) httpRequest.request(endPoint);
	System.out.println("Add URL :"+uri);
}



@When("Send a POST HTTP request {string}")
public void send_a_POST_HTTP_request(String requestBody) {
	id = (int) (Math.random() * (999999 - 111111)) + 111111;
	System.out.println("id=="+id);
	requestBody = requestBody.replaceFirst("@id", String.valueOf(id));
		
	this.response = httpRequest.body(requestBody).post();
}

@Given("Send a POST HTTP request to {string} and {string}")
public void send_a_POST_HTTP_request_to_and(String endpoint, String requestBody) {
	id = (int) (Math.random() * (999999 - 111111)) + 111111;
	System.out.println("id=="+id);
	requestBody = requestBody.replaceFirst("@id", String.valueOf(id));
		
	this.response = httpRequest.body(requestBody).post(endpoint);
}

private String getStudentIdFromResponse(String responseBody) {
	List<String> val = StringUtil.getMatches(responseBody, "(ID=.*)");
	return (val.size()> 0)?val.get(0):"";
}

@When("Send a PUT HTTP request to endpoint {string}")
public void send_a_PUT_HTTP_request_to_endpoint(String endpoint,List<String> body) {
	if(body!=null) {
		String requestBody = body.get(0);
		requestBody = requestBody.replaceFirst("@id", String.valueOf(id));
		System.out.println("requestBody PUT="+requestBody +", endpoint="+endpoint);
		this.response = httpRequest.body(requestBody).put(endpoint);	
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}


@When("Send a DELETE HTTP request to endpoint {string}")
public void send_a_DELETE_HTTP_request(String endpoint) {
	String requestBody = "{\"id\":"+id+ "}";
	this.response = httpRequest.body(requestBody).delete(endpoint);	
	Assert.assertTrue(response.getStatusCode() == 200);
	//Assert.assertEquals(is(HttpStatus.OK),response.getStatusCode());
}
	

@When("Send a GET HTTP request to endpoint {string}")
public void send_a_GET_HTTP_request(String endpoint) {
	this.response = httpRequest.body("").get(endpoint);	
	Assert.assertTrue(response.getStatusCode() == 200);
}

@When("Send a GET HTTP request by ID to endpoint {string}")
public void send_a_GET_HTTP_request_by_ID(String endpoint) {
	//String uri = "http://localhost:8080/fetchStudents?Id="+ id;
	
	this.response = httpRequest.body("").get(endpoint+id);	
	Assert.assertTrue(response.getStatusCode() == 200);
}


@Then("^I receive valid Response$")
public void i_receive_valid_Response() {
    String responseBody = response.getBody().asString();
    System.out.println("responseBody --->" + responseBody);
    // Get ID from the Response object
    String studentId =  getStudentIdFromResponse(responseBody);
    System.out.println("studentId is :" + studentId);
    // Check if the added studentId is present in the response body.
    
    Assert.assertTrue("Valid Response check:", responseBody.contains(studentId));
     
    // Check if the status code is 200
    Assert.assertTrue(response.getStatusCode() == 200);
    System.out.println("Action done successfully. StudentId:"+studentId);    
}


@Then("^I receive invalid Response$")
public void i_receive_invalid_Response() {
	String responseBody = response.getBody().asString();
    System.out.println("responseBody --->" + responseBody);
    Assert.assertTrue("Invalid Response check:", responseBody.contains("Error"));
    
}


@When("Send a GET HTTP request by Class {string} to endpoint {string}")
public void send_a_GET_HTTP_request_by_Class_to_endpoint(String className, String endpoint) {
	this.response = httpRequest.body("").get(endpoint+className);	
	Assert.assertTrue(response.getStatusCode() == 200);
}



  
}
