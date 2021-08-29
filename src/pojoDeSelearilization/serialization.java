package pojoDeSelearilization;

import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoSerialization.AddPlace;
import pojoSerialization.location;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Test
public class serialization {
	public static void serial() throws IOException{
		
		AddPlace sr = new AddPlace();
		sr.setAccuracy(50);
		sr.setAddress("mysuru");
		sr.setLanguage("kannada");
		sr.setName("Majggie");
		sr.setPhone_number("9876543210");
		sr.setWebsite("www.google.com");
		List<String> ls= new ArrayList<String>();
		ls.add("type1");
		ls.add("type 2");
		sr.setTypes(ls);
		location loc = new location();
		loc.setLat(38.383494);
		loc.setLng(33.427362);
		sr.setLocation(loc);
		
		RequestSpecification req = new RequestSpecBuilder() // return type of the request builder is request specification
				.setContentType(ContentType.JSON)
				.setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123")
				.build();
		
		ResponseSpecification res = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON)
				.build();
		
		//RestAssured.baseURI ="https://rahulshettyacademy.com";
		
		
		RequestSpecification request = given().log().all().spec(req)
		.body(sr); // now the request is created separately and assigned to RequestSpecification 
		
		
		String response =request.when().post("maps/api/place/add/json")
		.then().spec(res).extract().response().asString();
		
		System.out.println(response);
	
	}
	
	

}
