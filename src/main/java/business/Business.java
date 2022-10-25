package business;

import annotation.Get;
import annotation.Put;
import annotation.RequestMapping;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RequestMapping(route = "/business")
public class Business {

    @Get(route = "/myfirstroute")
    public JsonObject doSomething(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hello", "world");
        return jsonObject;
    }

    @Put(route = "/postroute")
    public void hehe(){
        System.out.println("hehe");
    }

    public void aposkd(){
        System.out.println("OI");
    }
}
