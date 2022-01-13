import org.json.*;
import org.testng.Assert;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;

public class GoRestTests {
    String token = "Bearer 4033ed14c5f4b89764adc3db27243ad29cc18514173f62fa246c39cdb7356131";
    String json = "application/json";



    @BeforeTest
    public void setUp(){
        baseURI = "https://gorest.co.in/public/v1";
    }

    @Test
    public void noToken(){

        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

              String resBody =
                      given().header("Accept", json).header("Content-Type", json).body(reqBody).log().all().when().post(
                        "/users").then().assertThat().statusCode(401).log().all().extract().response().asString();

                JSONObject jsonObj = new JSONObject(resBody);
                JSONObject data = jsonObj.getJSONObject("data");
                Assert.assertEquals("Authentication failed", data.getString("message"));

    }

    @Test
    public void createUserNoBody(){

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).log().all().when().post("/users").then().assertThat().statusCode(422).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONArray data = jsonObj.getJSONArray("data");

        JSONObject one = (JSONObject) data.get(0);
        JSONObject two = (JSONObject) data.get(1);
        JSONObject three = (JSONObject) data.get(2);
        JSONObject four = (JSONObject) data.get(3);
        Assert.assertEquals("email", one.getString("field"));
        Assert.assertEquals("can't be blank", one.getString("message"));
        Assert.assertEquals("name", two.getString("field"));
        Assert.assertEquals("can't be blank", two.getString("message"));
        Assert.assertEquals("gender", three.getString("field"));
        Assert.assertEquals("can't be blank", three.getString("message"));
        Assert.assertEquals("status", four.getString("field"));
        Assert.assertEquals("can't be blank", four.getString("message"));
    }

    @Test
    public void createUser(){

        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        Assert.assertEquals("John Holt", data.getString("name"));
        String testEmailStr = emailStr.replace("\"", "");
        Assert.assertEquals(testEmailStr, data.getString("email"));
        Assert.assertEquals("male", data.getString("gender"));
        Assert.assertEquals("active", data.getString("status"));

    }

    @Test
    public void getUserNotExisting(){
        String resBody=
       when().get("/users/" + 123).then().assertThat().statusCode(404).log().all().extract().response().asString();
    }

    @Test
    public void getUserByID(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        resBody =
                when().get("/users/"+ userID).then().assertThat().statusCode(200).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals(userID, data.getInt("id"));
        Assert.assertEquals("John Holt", data.getString("name"));
        String testEmailStr = emailStr.replace("\"", "");
        Assert.assertEquals(testEmailStr, data.getString("email"));
        Assert.assertEquals("male", data.getString("gender"));
        Assert.assertEquals("active", data.getString("status"));

    }

    @Test
    public void updateUserNotExisting(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";

        String reqBody = "{\"name\":\"Chase Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status" +
                "\":\"active\"}";

            String resBody=
                    given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body
                (reqBody).log().all().when().put("/users/" + 123).then().assertThat().statusCode(404).log().all()
                .extract().response().asString();
            JSONObject jsonObj = new JSONObject(resBody);
            JSONObject data = jsonObj.getJSONObject("data");

            Assert.assertEquals("Resource not found", data.getString("message"));

    }

    @Test
    public void updateUserExists(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");


        reqBody = reqBody = "{\"name\":\"Chasie Holt\", \"gender\":\"female\",\"status\":\"active\"}";
        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().patch("/users/" + userID).then().assertThat().statusCode(200).log().all().extract().response().asString();
        JSONObject updatedJson = new JSONObject(resBody);
        data = updatedJson.getJSONObject("data");
        Assert.assertEquals("Chasie Holt", data.getString("name"));
        String testEmailStr = emailStr.replace("\"", "");
        Assert.assertEquals(testEmailStr, data.getString("email"));
        Assert.assertEquals("female", data.getString("gender"));
        Assert.assertEquals("active", data.getString("status"));

    }

    @Test
    public void deleteUserNonExisting(){
       String resBody =
               given().header("Accept", json).header("Content-Type", json).header("Authorization", token).log().all().when().delete("/users/" + 123).then().assertThat().statusCode(404).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        Assert.assertEquals("Resource not found", data.getString("message"));
    }

    @Test
    public void deleteUser(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        resBody = given().header("Accept", json).header("Content-Type", json).header("Authorization", token).log().all().when().delete("/users/" + userID).then().assertThat().statusCode(204).log().all().extract().response().asString();
    }

    @Test
    public void createAPost(){

        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

            jsonObj = new JSONObject(resBody);
            data = jsonObj.getJSONObject("data");

            Assert.assertEquals("Testing Post Title", data.getString("title"));
            Assert.assertEquals("Here is a little body", data.getString("body"));
    }

    @Test
    public void getAUsersPosts(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        resBody=
                when().get("/users/"+ userID +"/posts").then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

    @Test
    public void createComment(){

        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        int postID = data.getInt("id");

        reqBody = "{\"name\": \"Sara Rose\", \"email\": \"sara@rose.com\", \"body\": \"Here is a comment body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/posts/" + postID+ "/comments").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals("Sara Rose", data.getString("name"));
        Assert.assertEquals("sara@rose.com", data.getString("email"));
        Assert.assertEquals("Here is a comment body", data.getString("body"));

    }

    @Test
    public void getPostsComments(){

        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        int postID = data.getInt("id");

        reqBody = "{\"name\": \"Sara Rose\", \"email\": \"sara@rose.com\", \"body\": \"Here is a comment body\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/posts/" + postID+ "/comments").then().assertThat().statusCode(201).log().all().extract().response().asString();


        resBody =
                when().get("/posts/" + postID+ "/comments").then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

    @Test
    public void createToDo(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing ToDo Title\", \"status\":\"pending\"}";

        resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users/"+ userID +"/todos").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals("Testing ToDo Title", data.getString("title"));
        Assert.assertEquals("pending", data.getString("status"));
    }

    @Test
    public void getTodo(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";
        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";

        String resBody =
                given().header("Accept", json).header("Content-Type", json).header("Authorization", token).body(reqBody).log().all().when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        reqBody = "{\"title\": \"Testing ToDo Title\", \"status\":\"pending\"}";

        resBody =
                when().get("/users/"+ userID +"/todos").then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

}
