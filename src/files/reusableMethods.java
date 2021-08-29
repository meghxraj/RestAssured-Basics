package files;

import io.restassured.path.json.JsonPath;

public class reusableMethods {

	//parsing to json with string as input
	public static JsonPath rawToJson(String response) {
		JsonPath js= new JsonPath(response);
		return js;
	}

}
