import org.testng.annotations.Test;

import files.reusableMethods;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojoDeSelearilization.api;
import pojoDeSelearilization.getCourses;

import static io.restassured.RestAssured.*;

import java.util.List;

public class oAuthTest {
	
	@Test
	public void testOAuth() {
		/*
		//get the code for authorix=zation via selenium script
		 System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
		 WebDriver driver = new ChromeDriver();
		 driver.get("url");
		
		//url =https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("maggie.puthran");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(keys.ENTER);
		thread.sleep(4000);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(keys.ENTER);
		thread.sleep(4000);
		String codeUrl = driver.getCurrentUrl();
		
		*/
		String codeUrl ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWjN4dszhEWTIbHMZCkvSGwWdPjS_ZK96nlXbpFxUqgKz_MNZNpVqDXZNjT7mgGdZg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";
		String partialCode=codeUrl.split("code=")[1];
		String accessCode = partialCode.split("&scope")[0];
		
		System.out.println(accessCode);
		//access token request
		String accesTokenResponse = given().urlEncodingEnabled(false) // to disable the encoding of special characters from the response
		.queryParams("code", accessCode)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js= reusableMethods.rawToJson(accesTokenResponse);
		String accessToken = js.getString("access_token");
		
		
		//actual request - getting the response to deserialization using pojo classes
		getCourses actResponse =given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON) // can be removed if content-type header is application/json
			.when()
			.get("https://rahulshettyacademy.com/getCourse.php").as(getCourses.class);
			
		System.out.println(actResponse.getLinkedIn());
		System.out.println(actResponse.getInstructor());
		
		List<api> apiCourses = actResponse.getCourses().getApi();
		
		for (int i=0;i<apiCourses.size();i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("soapui webservices testing")) {
					System.out.println("Cost of "+apiCourses.get(i).getCourseTitle()+" = "+apiCourses.get(i).getPrice());
					break;
			}
		}
		//System.out.println( actResponse);
	}

}
