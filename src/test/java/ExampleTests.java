//import org.json.JSONObject;
//import org.testng.Assert;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import static io.restassured.RestAssured.*;
//
//
//
//public class ExampleTests {
//    String token = "Bearer 4033ed14c5f4b89764adc3db27243ad29cc18514173f62fa246c39cdb7356131";
//    String json = "application/json";
//
//
//
//    @BeforeTest
//    public void setUp(){
//        baseURI = "https://gorest.co.in/public/v1";
//    }
//
//    @Test
//    public void testListUsers(){
//        String resBody = given().header("Accept", json).header("Content-Type", json).header("Authorization", token).log().all().when().get("/users").then().assertThat().statusCode(200).log().all().extract().response().asString();
//    }
//
//    @Test
//    public void createUser(){
//
//        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//
//        String resBody =
//                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();
//
//        JSONObject jsonObj = new JSONObject(resBody);
//        JSONObject data = jsonObj.getJSONObject("data");
//
//        Assert.assertEquals("John Holt", data.getString("name"));
//        String testEmailStr = emailStr.replace("\"", "");
//        Assert.assertEquals(testEmailStr, data.getString("email"));
//    }
//
//    @Test
//    public void updateUser(){
//
//        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//
//        String resBody =
//                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();
//
//        JSONObject jsonObj = new JSONObject(resBody);
//        JSONObject data = jsonObj.getJSONObject("data");
//
//        int userID = data.getInt("id");
//
//        reqBody = reqBody = "{\"name\":\"Chase Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status" +
//                "\":\"active\"}";
//        resBody =
//                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().patch("/users/" + userID).then().assertThat().statusCode(200).log().all().extract().response().asString();
//        JSONObject updatedJson = new JSONObject(resBody);
//        data = updatedJson.getJSONObject("data");
//        Assert.assertEquals("Chase Holt", data.getString("name"));
//
//    }
//
//    @Test
//    public void deleteUser(){
//        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//
//        String resBody =
//                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();
//
//        JSONObject jsonObj = new JSONObject(resBody);
//        JSONObject data = jsonObj.getJSONObject("data");
//
//        int userID = data.getInt("id");
//
//        resBody = given().header("Accept", json).header("Content-Type", json).header("Authorization", token).log().all().when().delete("/users/" + userID).then().assertThat().statusCode(204).log().all().extract().response().asString();
//    }
//}
