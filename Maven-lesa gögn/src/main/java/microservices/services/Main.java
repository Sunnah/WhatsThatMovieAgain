package microservices.services;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;



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

  String getTitle(JSONObject json){
      if(json.get("title")!=null){
        return json.get("title").toString();  
      }
      else return "null"; 
  }

  String getLanguage(JSONObject json){
    if(json.get("original_language")!=null){
      return json.get("original_language").toString();
    }
    else return "null";
  }

  String getRelease(JSONObject json){
    if(json.get("release_date")!=null){
      return json.get("release_date").toString();
    }
    else return "null";
  }

  JSONArray getKeywords(JSONObject json){
    JSONArray keyWords = new JSONArray();
    if(json.get("keywords")!=null){
      JSONObject jo = (JSONObject) json.get("keywords");
      JSONArray ja = (JSONArray) jo.get("keywords");
      
      for(int i = 0; i<ja.size(); i++){
        JSONObject jo1 = (JSONObject) ja.get(i);
        //System.out.println(jo1.get("name").toString());
        keyWords.add(jo1.get("name").toString());

      }
      //JSONObject jo1 = (JSONObject) ja.get(0);
      //System.out.println("Hér er talan"+keyWords.size());
      
    }
    return keyWords;
    //}
  }
  //dat = myObj.keywordsORG[0].keywords[1].id;

  JSONArray getGenres(JSONObject json){
    JSONArray genreArray = new JSONArray();
    if(json.get("genres")!=null){
      JSONArray ja = (JSONArray) json.get("genres");
      for(int i = 0; i < ja.size(); i++){
        JSONObject joTemp = (JSONObject) ja.get(i);
        genreArray.add(joTemp.get("name").toString());
      }
      
    }
    return genreArray;
  }

  JSONArray getActors(JSONObject json){
    JSONArray actorArray = new JSONArray();
    if(json.get("casts")!=null){
      JSONObject jo = (JSONObject) json.get("casts");
      JSONArray ja = (JSONArray) jo.get("cast");
      
      for(int i = 0; i < 5 && i<ja.size(); i++){
        JSONObject joTemp = (JSONObject) ja.get(i);
        actorArray.add(joTemp.get("name").toString());
      }
      
    }
    return actorArray;
  }

  String getProdComp(JSONObject json){
    if(json.get("production_companies")!=null){
      JSONArray ja = (JSONArray) json.get("production_companies");
      if(ja.size()>0){
        JSONObject prodcompObject = (JSONObject) ja.get(0);
        String prodcompString = prodcompObject.get("name").toString();
        /*
        for(int i = 0; i < ja.size(); i++){
          JSONObject joTemp = (JSONObject) ja.get(i);
          prodcompArray.add(joTemp.get("name").toString());
        }*/
        return prodcompString;
      }
    }
    return "null";
  }

  int getLongest(String theme) throws Exception{
    Main example = new Main();
    int max=example.latest();
    int longest=0;
    for(int n=0;n<max;n++){
      String response = example.getWebData(n);
      try{
        JSONObject json = (JSONObject)new JSONParser().parse(response);
        if(json.get(theme)!=null){
          //String t = json.get(theme).toString();
          JSONArray ja = new JSONArray();
          ja = getKeywords(json);
          int number = ja.size();
          //int tala=t.split(",").length/2;
          if(number>longest){
            longest=number;
            System.out.println("her er talan number"+" "+number);
          }
        }
      }
      catch(ParseException e){}
      /*if(n%5000==0){
        System.out.println(n);
      }*/
    }
    return longest;
  }


  public static void main(String[] args) throws Exception {
    
    Main example = new Main();  
    FileWriter fileWriter=null;
    try{
      fileWriter = new FileWriter("movieinf.csv");
      fileWriter.append("Title;Original_language;Release_date;Production_companies");
      for(int i = 1; i<11; i++){
        fileWriter.append(";Genre"+i);
      }
      for(int i = 1; i<86; i++){
        fileWriter.append(";Keywords"+i);
      }
      for(int i = 1; i<6; i++){
        fileWriter.append(";Actor"+i);
      }
      int max= example.latest();
      for(int n=0; n<=100; n++){
       /* if(n%40==0){
          TimeUnit.SECONDS.sleep(1);
        }*/

        String response = example.getWebData(n);
        while(response.split(",")[0].matches(".*\"status_code\":25")){
          //TimeUnit.SECONDS.sleep(1);
          //System.out.println("þyrfti að gera ehv hér");
          response=example.getWebData(n);
        }
        if(!response.split(",")[0].matches(".*\"status_code\":34")){
          try{
            JSONObject json = (JSONObject)new JSONParser().parse(response);
            fileWriter.append("\n");
            fileWriter.append(example.getTitle(json));
            fileWriter.append(";");
            fileWriter.append(example.getLanguage(json));
            fileWriter.append(";");
            fileWriter.append(example.getRelease(json));
            fileWriter.append(";");
            fileWriter.append(example.getProdComp(json));
            JSONArray genres = example.getGenres(json);
            for(int i = 0; i<10; i++){
              fileWriter.append(";");
              if(i<genres.size()){
                fileWriter.append(genres.get(i).toString());
              }
            }
           JSONArray keyWords = example.getKeywords(json);
            for(int i = 0; i<85; i++){
              fileWriter.append(";");
              if(i<keyWords.size()){
                fileWriter.append(keyWords.get(i).toString());
              }
            }
            JSONArray actors = example.getActors(json);
            for(int i = 0; i<5; i++){
              fileWriter.append(";");
              if(i<actors.size()){
                fileWriter.append(actors.get(i).toString());
              }
            }
            
          }
          catch(ParseException e){
            
          }
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
