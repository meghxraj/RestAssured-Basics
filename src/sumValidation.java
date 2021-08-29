import org.testng.Assert;
import org.testng.annotations.*;
import files.payLoad;
import io.restassured.path.json.JsonPath;

public class sumValidation {

	@Test
	public void sumOfAllCourses() {
		
		JsonPath js= new JsonPath(payLoad.coursePrice());
		int totalPrice = 0;
		int purchaseAmount=js.getInt("dashboard.purchaseAmount");
		
		for (int i=0;i<js.getInt("courses.size()");i++) 
			totalPrice= totalPrice + (js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies"));
		
			Assert.assertEquals(totalPrice,purchaseAmount);
			
		
	}
		
}

