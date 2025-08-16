package restassured;

import dto.ContactDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NTE4MTkxNzYsImlhdCI6MTc1MTIxOTE3Nn0.Ae5hLaWG44f_7DuCHwjT_CZef-a8QIO4y--IxCd1g2U";
    String id;

    @BeforeMethod
    public void  preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";

        int i = new Random().nextInt(100)+100;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Pirogova")
                .email("pirogova"+i+"@gmail.com")
                .phone("88005553434"+i)
                .address("Tel Aviv")
                .description("BFF")
                .build();
        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        String[] all = message.split(": ");
        id = all[1];
    }

    @Test
    public void deleteContactByIdSuccess(){
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was deleted!"));
    }

    @Test
    public  void deleteContactByIdWrongToken(){
        given()

                .header("Authorization", "hghsfvu")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401);
    }

}
