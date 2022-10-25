package basic;

import com.google.gson.JsonObject;
import identification.IdentificationRemoteObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker{

    private final IdentificationRemoteObject identificationRemoteObject;

    public Invoker() {
        this.identificationRemoteObject = new IdentificationRemoteObject();
    }

    public void mapAsRemoteObject(Object obj) {
        identificationRemoteObject.mapAsRemoteObject(obj);
    }

    public HTTPMessage invoke(HTTPMessage httpMessage) {
        JsonObject json = new JsonObject();
        try{
            Method method = identificationRemoteObject.getInvocationMethod(httpMessage.getUrl());

            Class<?> clazz = method.getDeclaringClass();
            Object instance = clazz.getDeclaredConstructor().newInstance();

            json = (JsonObject) method.invoke(instance);
        }catch (RuntimeException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e){
            e.printStackTrace();
        }

        System.out.println(json);
        return new HTTPMessage();
    }
}
