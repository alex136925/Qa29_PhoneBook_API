package restassured;

import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class LoginTestsRA {
    String endpoint = "user/login/usernamepassword";

    @BeforeMethod
    public void  preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void loginSuccessTest() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("margo@gmail.com")
                .password("Mmar123456$")
                .build();

        AuthResponseDTO responseDTO = given()
                .body(auth)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDTO.class);
        System.out.println(responseDTO.getToken());
    }

    @Test
    public  void loginWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("margogmail.com")
                .password("Mmar123456$")
                .build();
        ErrorDTO errorDTO = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(), "Login or Password incorrect");
    }

    @Test
    public  void loginWrongEmailFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("margogmail.com")
                .password("Mmar123456$")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }

    @Test
    public  void loginUnregistered(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("margarita@gmail.com")
                .password("Mmar123456$")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }



}
