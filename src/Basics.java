import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.testng.Assert;

import files.payLoad;
import files.reusableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {
		
		/*validate Add place API
		 - given - all input details (query parms, headers, body)
		 - when - submit/hit the API request (resources, http methods-PUT,POST,GET,DELETE)
		 - then - validate the response
		*/
		
		RestAssured.baseURI ="https://rahulshettyacademy.com"; 	//setting the base URI for test
		
		//POST Request - Add place
		
		String Response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Path.of("D:\\API Testing\\addPlaceRequest.json"))))
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		
		System.out.println(Response);
		
		JsonPath jsObj = reusableMethods.rawToJson(Response);
		String placeId =jsObj.getString("place_id"); // give the path as parent.chlid relation 
		System.out.println("Place ID is : "+placeId);
		
		//PUT Request - Update Place
		String address= "70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+address+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("/maps/api/place/update/json")
		.then().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
			
		
		//GET request - Get the place
		
		String getResponse= given().log().all().queryParams("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath jsObjGET = reusableMethods.rawToJson(getResponse);
		String actAddress= jsObjGET.getString("address");
		
		System.out.println(actAddress);
		Assert.assertEquals(address, actAddress);
	}

}
