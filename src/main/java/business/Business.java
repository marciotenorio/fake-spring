package business;

import annotation.Get;
import annotation.Post;
import annotation.Put;
import annotation.RequestMapping;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RequestMapping(route = "/business")
public class Business {

    @Get(route = "/findAll")
    public JsonObject findAll(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("um campo", "value campo");
        jsonObject.addProperty("dois campo",  "value dois campo");
        return jsonObject;
    }

    @Post(route = "/myfirstroute")
    public JsonObject doSomething(JsonObject jsonObject){
        jsonObject.addProperty("hello", "world");
        return jsonObject;
    }

    @Put(route = "/postroute")
    public JsonObject hehe(JsonObject jsonObject){
        System.out.println("hehe");
        return jsonObject;
    }

    public void aposkd(){
        System.out.println("OI");
    }

    @Get(route = "/find")
    public JsonObject colocandoMusica(){
        JsonObject json = new JsonObject();
        json.addProperty("musica", "hehe boy");
        return json;
    }
}
