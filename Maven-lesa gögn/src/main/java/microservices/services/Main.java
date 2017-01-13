package microservices.services;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;



public class Main {
  OkHttpClient client = new OkHttpClient();

  String getWebData(int i) throws IOException {

    Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/movie/"+i+"?api_key=93a9fe8c0148b7c701f87801c1068f1e&append_to_response=casts,keywords")
        .build();

    try{
      Response response = client.newCall(request).execute();
      return response.body().string();
    }
    catch(IOException e){
    	return "heimskur";
    }
  }

  int latest()  throws IOException {

    Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/movie/latest?api_key=93a9fe8c0148b7c701f87801c1068f1e&language=en-US")
        .build();

    try{
      Response response = client.newCall(request).execute();
      int id = getId(response.body().string());
      System.out.println(id);
      return id;//response.body().string();
    }
    catch(IOException e){
      return 0;
    }
  }

  int getId(String s){
    String [] part = s.split(",");
    for(int n=0; n<part.length;n++){
      if(part[n].matches("\"id\".*")){
        System.out.println(part[n]);
        return Integer.parseInt(part[n].split(":")[1]);
      }
    }
    return 0;
  }

  String getInfo(JSONObject json){
    //try{
      if(json.get("title")!=null){
        return json.get("title").toString();  
      }
      else return null; 
   /* }
    catch(ParseException e){
      return "villa";
    }*/
  }


  public static void main(String[] args) throws Exception {
    Main example = new Main();
    FileWriter fileWriter=null;
    try{
      fileWriter = new FileWriter("movieinf.csv");
      int max= example.latest();
      for(int n=0; n<=max; n++){
        String response = example.getWebData(n);
        if(response.split(",")[0].matches(".*\"status_code\":25")){
          System.out.println("þyrfti að gera ehv hér");
          response=example.getWebData(n);
        }
        String s=null;
        if(!response.split(",")[0].matches(".*\"status_code\":34")){
          try{
            JSONObject json = (JSONObject)new JSONParser().parse(response);
            s = example.getInfo(json);
          }
          catch(ParseException e){
            s="villa";
          }
          fileWriter.append("\n");
          fileWriter.append(s);
        }
      }
    }
      catch (Exception e) {         
        System.out.println("Error in CsvFileWriter !!!");
        e.printStackTrace();
      }
      finally {
        try {
          fileWriter.flush();
          fileWriter.close();
        } catch (IOException e) {
          System.out.println("Error while flushing/closing fileWriter !!!");
          e.printStackTrace();
        }
      }
    }
}
