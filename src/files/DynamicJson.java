package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	@Test (dataProvider="testData") //runs test without void main of java with data from DataProvider Annotation
	public void addBook(String isbn, String asile) {
		
		//add book 
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String postResponse=given().header("Content-Type","application/json").
		body(payLoad.addBookLibrary(isbn,asile)).
		when().post("Library/Addbook.php").
		then().assertThat().statusCode(200).
		extract().response().asString();

		JsonPath js =reusableMethods.rawToJson(postResponse);
		String bookID = js.get("ID");
		System.out.println(bookID);
		
		// delete book
		given().header("Content-Type","application/json")
		.body(payLoad.deleteBookLibrary(bookID))
		.when().delete("Library/DeleteBook.php")
		.then().assertThat().statusCode(200);
		
	
	}

	@DataProvider(name="testData") //to input data to the test 
	public Object[][] getData() {
		return new Object [][] {{"Megh","123"}, {"kulli","124"},{"ganu","456"}};
	}
	
}
