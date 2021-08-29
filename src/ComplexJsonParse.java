import org.testng.Assert;

import files.payLoad;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js= new JsonPath(payLoad.coursePrice());
		
		//print number of courses in API
		int courseCount = js.getInt("courses.size()");
		System.out.println("Number of courses ="+courseCount);
		
		//print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount ="+purchaseAmount);
		
		//print the name of first course
		String courseName = js.get("courses[0].title");
		System.out.println("First Course Name ="+courseName);
		
		//Print All course titles and their respective Prices
		for (int i=0;i<courseCount;i++) {
			String arrCourse =js.get("courses["+i+"].title");
			int arrPrice = js.getInt("courses["+i+"].price");
			System.out.println("Name of "+(i+1)+" course is -"+arrCourse);
			System.out.println("Price of "+arrCourse+" course is -"+arrPrice);
		}
		
		//Print no of copies sold by RPA Course
		for (int i=0;i<courseCount;i++) {
			
			if (js.get("courses["+i+"].title").toString().equalsIgnoreCase("RPA")) {
				System.out.println("No of copies sold for RPA course is -"+js.get("courses["+i+"].copies").toString());
				break;
			}
		}
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		int totalPrice = 0;
		for (int i=0;i<courseCount;i++) {
			 
			totalPrice= totalPrice + (js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies"));
		}
			if(totalPrice==purchaseAmount) {
				Assert.assertEquals(totalPrice,purchaseAmount);
				System.out.println("Purchase Amount - "+purchaseAmount+ " is equal to totalPrice - "+totalPrice);
				
			}
				else
				System.out.println("Purchase Amount - "+purchaseAmount+ " is not equal to totalPrice- "+totalPrice);	
		
			}
		
		
		
	}


