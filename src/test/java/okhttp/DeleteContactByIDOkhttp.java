package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.DeleteByIdResponseDTO;
import dto.MessageResponseDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static okhttp.LoginTestsOkhttp.JSON;

public class DeleteContactByIDOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NTE4MTkxNzYsImlhdCI6MTc1MTIxOTE3Nn0.Ae5hLaWG44f_7DuCHwjT_CZef-a8QIO4y--IxCd1g2U";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    String id;


    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        //get id from "message"; "Contact was added! ID: 3215488 kjskffgfdg-2757577"

        ContactDTO contact = ContactDTO.builder()
                .name("John")
                .lastName("Smith")
                .email("smith@gmail.com")
                .phone("88005553535")
                .address("Chicago, IL, USA")
                .description("Nice guy!")
                .build();


        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);

        Request request = new Request.Builder()

                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        String message = gson.fromJson(response.body().string(), MessageResponseDTO.class).getMessage();

        String[] parts = message.split(" ");
        id = parts[parts.length - 1];



    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        DeleteByIdResponseDTO dto = gson.fromJson(response.body().string(), DeleteByIdResponseDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(),"Contact was deleted!");
    }

    @Test
    public void deleteContactByIdFailWrongID() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id.substring(2))
                .delete()
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);
        DeleteByIdResponseDTO dto = gson.fromJson(response.body().string(), DeleteByIdResponseDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(),"Contact with id: " +id.substring(2)+ " not found in your contacts!");
    }


}


//2bf48366-4cf7-4f37-9674-750fb091c401
//a@a
//==============================
//        3573a61f-8b3f-4d38-a109-92d3bb2201ca
//b2b@b
//==============================
