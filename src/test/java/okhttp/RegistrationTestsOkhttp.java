package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkhttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;

        AuthRequestDTO auth = AuthRequestDTO.builder()

                .username("margo"+i+"@gmail.com")
                .password("Mmar123456$")

                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()

                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)

                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void registrationFailWrongEmail() throws IOException {
        int i = new Random().nextInt(1000)+1000;

        AuthRequestDTO auth = AuthRequestDTO.builder()

                .username("margo"+i+"gmail.com")
                .password("Mmar123456$")

                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()

                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)

                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
    }

    @Test
    public void registrationFailWrongPassword() throws IOException {
        int i = new Random().nextInt(1000)+1000;

        AuthRequestDTO auth = AuthRequestDTO.builder()

                .username("margo"+i+"@gmail.com")
                .password("Mmar1")

                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()

                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)

                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
    }

    @Test
    public void registrationFailDuplicateUser() throws IOException {

        AuthRequestDTO auth = AuthRequestDTO.builder()

                .username("margo@gmail.com")
                .password("Mmar123456$")

                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()

                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)

                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 409);
    }
}
