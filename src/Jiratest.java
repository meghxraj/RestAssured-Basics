import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.reusableMethods;

public class Jiratest {

	@Test
	public static void jiraScript() {
		// TODO Auto-generated method stub
		RestAssured.baseURI = "http://localhost:8080/";
		
		SessionFilter session = new SessionFilter(); // used to capture the session and use it throughout
		
		//Post request to login and get session ID
		given().log().all().header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"username\": \"megharaj\",\r\n"
				+ "    \"password\": \"IaminsanE11\"\r\n"
				+ "}")
		.filter(session) // keeps the session of the sent request
		.when().post("rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		
		//Post request to update the comment
		String inputComment = "Comment from Automation script for Rest Assured for test";
		String commentIDResponse = given().log().all().pathParam("issueid", 10104).header("Content-type","application/json")
		.body("{\r\n"
				+ "    \"body\": \""+inputComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session) // remembers the session from the previous login
		.when().post("rest/api/2/issue/{issueid}/comment")
		.then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js = reusableMethods.rawToJson(commentIDResponse);
		String commentID=js.get("id");
		
		//Add attachement to the issue
		given().log().all().pathParam("issueid", 10104).header("X-Atlassian-Token" ,"no-check")
		.header("Content-Type","multipart/form-data")  // for multi part head should have this as a value
		.multiPart("file", new File ("D:\\API Testing\\IMP.txt"))  	// Multi part is used to send files / new File object is created to attach the file from local
		.filter(session)
		.when().post("rest/api/2/issue/{issueid}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		
		//Get issue details
		String getIssueResponse = given().filter(session).log().all().pathParam("issueid", 10104) //.relaxedHTTPSValidation() - is for authentication for https sites
		.queryParam("fields", "comment") // to limit the response validation
		.when().get("rest/api/2/issue/{issueid}")
		.then().log().all().extract().asString();
		System.out.println(getIssueResponse);
		JsonPath jsComment = reusableMethods.rawToJson(getIssueResponse);
		int commentSize = jsComment.getInt("fields.comment.comments.size()");
		for (int i=0;i<commentSize;i++) {
			String getID= jsComment.get("fields.comment.comments["+i+"].id");
			if(commentID.equalsIgnoreCase(getID)) {
				String commentBody =jsComment.get("fields.comment.comments["+i+"].body").toString();
				Assert.assertEquals(inputComment, commentBody);
				System.out.println("Input comment - "+inputComment);
				System.out.println("Comment from GET response - "+commentBody);
				break;
			}
		}
	}

}
