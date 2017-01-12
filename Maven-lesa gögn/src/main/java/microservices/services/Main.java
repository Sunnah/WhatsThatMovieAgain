package microservices.services;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
  OkHttpClient client = new OkHttpClient();

  String run(String url) throws IOException {

    Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/discover/movie?page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key=93a9fe8c0148b7c701f87801c1068f1e")
        .build();

    try{
      Response response = client.newCall(request).execute();
      return response.body().string();
    }
    catch(IOException e){
    	return "heimskur";
    }
  }

  public static void main(String[] args) throws IOException {
    Main example = new Main();
    String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
    System.out.println(response);
  }
}

/*
import org.codehaus.jackson.map.ObjectMapper;
import java.net.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.JsonNode;
import static java.net.http.HttpRequest.*;
*/
/*
import okhttp3.OkHttpClient;
//import javax.ws.rs.core.MediaType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
*/
/*
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Main {

  public static void main(String[] args)
  {
  /*  
      //HttpResponse<JsonNode> response = Unirest.get("https://api.themoviedb.org/3/movie/8?language=en-US&api_key=93a9fe8c0148b7c701f87801c1068f1e").body("{}").asJson();
      HttpRequest request = Unirest.get("https://api.themoviedb.org/3/movie/8?language=en-US&api_key=93a9fe8c0148b7c701f87801c1068f1e");

  
      //ObjectMapper mapper=new ObjectMapper();
      //JsonNode jsonNode=mapper.readValue(response, JsonNode.class);
      System.out.println(request);
*//*
      OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/octet-stream");
		RequestBody body = RequestBody.create(mediaType, "{}");
		Request request = new Request.Builder()
		  .url("https://api.themoviedb.org/3/discover/movie?page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key=93a9fe8c0148b7c701f87801c1068f1e")
		  .get()
		  .build();

		Response response = client.newCall(request).execute();
		PostExample example = new PostExample();
     	String response = example.post("http://www.roundsapp.com/post", json);
    	System.out.println(response);
  }

}

public class PostExample {
  public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  OkHttpClient client = new OkHttpClient();

  String post(String url, String json) throws IOException {
    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }


}
*/