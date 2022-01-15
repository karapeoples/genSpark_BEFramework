package tests;

import models.CommentData;
import models.Data;
import models.PostData;
import models.ToDoData;
import org.json.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class GoRestTests {
    String token = "Bearer 4033ed14c5f4b89764adc3db27243ad29cc18514173f62fa246c39cdb7356131";
    String json = "application/json";



    @BeforeTest
    public void setUp() {
        baseURI = "https://gorest.co.in/public/v1";
        requestSpecification = given().header("Accept", json).header("Content-Type", json).header("Authorization",
                token).log().all();
    }
//This Test Doesn't work due to token as part of requestSpecification
//    @Test
//    public void noToken(){
//
//        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
////        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");
//              String resBody =
//                      given().header("Authorization", "").body(reqBody).log().all().when().post(
//                        "/users").then().assertThat().statusCode(401).log().all().extract().response().asString();
//
//                JSONObject jsonObj = new JSONObject(resBody);
//                JSONObject data = jsonObj.getJSONObject("data");
//                Assert.assertEquals("Authentication failed", data.getString("message"));
//
//    }

    @Test
    public void createUserNoBody(){

        String resBody =
                requestSpecification.when().post("/users").then().assertThat().statusCode(422).log().all().extract().response().asString();

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

        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        Assert.assertEquals(reqBody.getName(), data.getString("name"));
        Assert.assertEquals(reqBody.getEmail(), data.getString("email"));
        Assert.assertEquals(reqBody.getGender(), data.getString("gender"));
        Assert.assertEquals(reqBody.getStatus(), data.getString("status"));

    }

    @Test
    public void getUserNotExisting(){
        String resBody=
       requestSpecification.when().get("/users/" + 123).then().assertThat().statusCode(404).log().all().extract().response().asString();
    }

    @Test
    public void getUserByID(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");
        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");


        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        resBody =
               requestSpecification.when().get("/users/"+ userID).then().assertThat().statusCode(200).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals(userID, data.getInt("id"));
        Assert.assertEquals(reqBody.getName(), data.getString("name"));
        Assert.assertEquals(reqBody.getEmail(), data.getString("email"));
        Assert.assertEquals(reqBody.getGender(), data.getString("gender"));
        Assert.assertEquals(reqBody.getStatus(), data.getString("status"));

    }

    @Test
    public void updateUserNotExisting(){
        String emailStr = "\"holt"+ System.currentTimeMillis()+"@faker.com\"";

//        String reqBody = "{\"name\":\"Chase Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status" +
//                "\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "Chase Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

            String resBody=
                    requestSpecification.body
                (reqBody).when().put("/users/" + 123).then().assertThat().statusCode(404).log().all()
                .extract().response().asString();
            JSONObject jsonObj = new JSONObject(resBody);
            JSONObject data = jsonObj.getJSONObject("data");

            Assert.assertEquals("Resource not found", data.getString("message"));

    }

    @Test
    public void updateUserExists(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender("male");
        reqBody.setEmail(emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");


//      reqBody ="{\"name\":\"Chasie Holt\", \"gender\":\"female\",\"status\":\"active\"}";
        reqBody = new Data();
        reqBody.setName("Chasie Holt");
        reqBody.setEmail(emailStr);
        reqBody.setGender("female");
        reqBody.setStatus("active");

        resBody =
               requestSpecification.body(reqBody).when().put("/users/" + userID).then().assertThat().statusCode(200).log().all().extract().response().asString();
        JSONObject updatedJson = new JSONObject(resBody);
        data = updatedJson.getJSONObject("data");
        Assert.assertEquals(reqBody.getName(), data.getString("name"));
        Assert.assertEquals(reqBody.getEmail(), data.getString("email"));
        Assert.assertEquals(reqBody.getGender(), data.getString("gender"));
        Assert.assertEquals(reqBody.getStatus(), data.getString("status"));

    }

    @Test
    public void deleteUserNonExisting(){
       String resBody =
               requestSpecification.when().delete("/users/" + 123).then().assertThat().statusCode(404).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        Assert.assertEquals("Resource not found", data.getString("message"));
    }

    @Test
    public void deleteUser(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");


        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

        resBody =
                requestSpecification.when().delete("/users/" + userID).then().assertThat().statusCode(204).log().all().extract().response().asString();
    }

    @Test
    public void createAPost(){

        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

//        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";
////        reqBody = new HashMap<>();
////        reqBody.put("title", "Testing Post Title");
////        reqBody.put("body", "Here is a little body");

        PostData postBody = new PostData();
        postBody.setTitle("Testing Post Title");
        postBody.setBody("Here is a little body");


        resBody =
                requestSpecification.body(postBody).when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

            jsonObj = new JSONObject(resBody);
            data = jsonObj.getJSONObject("data");

            Assert.assertEquals(postBody.getTitle(), data.getString("title"));
            Assert.assertEquals(postBody.getBody(), data.getString("body"));
    }

    @Test
    public void getAUsersPosts(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

//        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("title", "Testing Post Title");
//        reqBody.put("body", "Here is a little body");

        PostData postBody = new PostData();
        postBody.setTitle("Testing Post Title");
        postBody.setBody("Here is a little body");

        resBody =
                requestSpecification.body(postBody).when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        resBody=
               requestSpecification.when().get("/users/"+ userID +"/posts").then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

    @Test
    public void createComment(){

        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

//        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("title", "Testing Post Title");
//        reqBody.put("body", "Here is a little body");
        PostData postBody = new PostData();
        postBody.setTitle("Testing Post Title");
        postBody.setBody("Here is a little body");

        resBody =
                requestSpecification.body(postBody).when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        int postID = data.getInt("id");

//        reqBody = "{\"name\": \"Sara Rose\", \"email\": \"sara@rose.com\", \"body\": \"Here is a comment body\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("name", "Sara Rose");
//        reqBody.put("email", "sara@rose.com");
//        reqBody.put("body", "Here is a comment body");
        CommentData commentBody = new CommentData();
        commentBody.setName("Sara Rose");
        commentBody.setEmail("sara@rose.com");
        commentBody.setBody("Here is a comment body");

        resBody =
                requestSpecification.body(commentBody).when().post("/posts/" + postID+ "/comments").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals(commentBody.getName(), data.getString("name"));
        Assert.assertEquals(commentBody.getEmail(), data.getString("email"));
        Assert.assertEquals(commentBody.getBody(), data.getString("body"));

    }

    @Test
    public void getPostsComments(){

        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

//        reqBody = "{\"title\": \"Testing Post Title\", \"body\":\"Here is a little body\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("title", "Testing Post Title");
//        reqBody.put("body", "Here is a little body");
        PostData postBody = new PostData();
        postBody.setTitle("Testing Post Title");
        postBody.setBody("Here is a little body");

        resBody =
                requestSpecification.body(postBody).when().post("/users/"+ userID +"/posts").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        int postID = data.getInt("id");

//        reqBody = "{\"name\": \"Sara Rose\", \"email\": \"sara@rose.com\", \"body\": \"Here is a comment body\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("name", "Sara Rose");
//        reqBody.put("email", "sara@rose.com");
//        reqBody.put("body", "Here is a comment body");

        CommentData commentBody = new CommentData();
        commentBody.setName("Sara Rose");
        commentBody.setEmail("sara@rose.com");
        commentBody.setBody("here is a comment body");

        resBody =
                requestSpecification.body(commentBody).when().post("/posts/" + postID+ "/comments").then().assertThat().statusCode(201).log().all().extract().response().asString();


        resBody =
                requestSpecification.when().get("/posts/" + postID+ "/comments").then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

    @Test
    public void createToDo(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");

//        reqBody = "{\"title\": \"Testing ToDo Title\", \"status\":\"pending\"}";
//        reqBody = new HashMap<>();
//        reqBody.put("title", "Testing ToDo Title");
//        reqBody.put("status", "pending");

        ToDoData toDoBody = new ToDoData();
        toDoBody.setTitle("Testing ToDo Title");
        toDoBody.setStatus("pending");

        resBody =
                requestSpecification.body(toDoBody).when().post("/users/"+ userID +"/todos").then().assertThat().statusCode(201).log().all().extract().response().asString();

        jsonObj = new JSONObject(resBody);
        data = jsonObj.getJSONObject("data");

        Assert.assertEquals(toDoBody.getTitle(), data.getString("title"));
        Assert.assertEquals(toDoBody.getStatus(), data.getString("status"));
    }


    @Test
    public void getTodo(){
        String emailStr = "holt"+ System.currentTimeMillis()+"@faker.com";
//        String reqBody = "{\"name\":\"John Holt\", \"gender\":\"male\", \"email\":"+emailStr+",\"status\":\"active\"}";
//        HashMap<String, Object> reqBody = new HashMap<>();
//        reqBody.put("name", "John Holt");
//        reqBody.put("gender", "male");
//        reqBody.put("email", emailStr);
//        reqBody.put("status", "active");

        Data reqBody = new Data();
        reqBody.setName("John Holt");
        reqBody.setGender( "male");
        reqBody.setEmail( emailStr);
        reqBody.setStatus("active");

        String resBody =
                requestSpecification.body(reqBody).when().post("/users").then().assertThat().statusCode(201).log().all().extract().response().asString();

        JSONObject jsonObj = new JSONObject(resBody);
        JSONObject data = jsonObj.getJSONObject("data");

        int userID = data.getInt("id");


//        reqBody = new HashMap<>();
//        reqBody.put("title", "Testing ToDo Title");
//        reqBody.put("status", "pending");

        ToDoData toDoBody = new ToDoData();
        toDoBody.setTitle("Testing ToDo Title");
        toDoBody.setStatus("pending");
        resBody =
                requestSpecification.body(toDoBody).when().post("/users/"+ userID +"/todos").then().assertThat().statusCode(201).log().all().extract().response().asString();

       jsonObj = new JSONObject(resBody);
       data = jsonObj.getJSONObject("data");

       int todoID = data.getInt("id");
        resBody =
                requestSpecification.when().get("/todos/" + todoID).then().assertThat().statusCode(200).log().all().extract().response().asString();
    }

}
