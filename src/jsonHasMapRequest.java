import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.Test;

import excelUtility.DataDriven;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import files.reusableMethods;
import static io.restassured.RestAssured.*;

public class jsonHasMapRequest {

	@Test
	public void datadrivenRequest() throws IOException {
		DataDriven dd = new DataDriven();
		ArrayList data = dd.getData("AddBook","RestAPI");
		HashMap<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("name", data.get(1));
		jsonMap.put("isbn", data.get(2));
		jsonMap.put("aisle",data.get(3));
		jsonMap.put("author", data.get(4));
		//use nested hash map to have construct a nested map
		//jsonMap.put("key",anotherMap);
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String response =given().log().all().header("Content-Type","application/json")
		.body(jsonMap)
		.when().post("/Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = reusableMethods.rawToJson(response);
		String bookID =js.get("ID").toString();
		System.out.println("Book ID is "+bookID);
		
		HashMap<String,Object> delR = new HashMap<String,Object>();
		delR.put("ID", bookID);
		given().log().all()
		.body(delR)
		.when().delete("/Library/DeleteBook.php")
		.then().assertThat().statusCode(200);
		
		
	}
	
	
}