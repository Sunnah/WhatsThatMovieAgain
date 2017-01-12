package microservices.service;

import java.io.File;
import org.codehaus.jackson.map.ObjectMapper;
import java.net.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


public class ReadDb {

  public static void main(String[] args)
  {
    
      HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/8?language=en-US&api_key=93a9fe8c0148b7c701f87801c1068f1e")
      .body("{}")
      .asString();

      ObjectMapper mapper=new ObjectMapper();
      JsonNode jsonNode=mapper.readValue(response, JsonNode.class);
      System.out.println(jsonNode);
  }

}