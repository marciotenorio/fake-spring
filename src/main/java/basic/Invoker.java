package basic;

import basic.protocol.tcp.TCPMarshaller;
import com.google.gson.JsonObject;
import identification.IdentificationRemoteObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker{

    private TCPMarshaller tcpMarshaller;

    private final IdentificationRemoteObject identificationRemoteObject;

    public Invoker() {
        tcpMarshaller = new TCPMarshaller();
        identificationRemoteObject = new IdentificationRemoteObject();
    }

    public void mapAsRemoteObject(Object obj) {
        identificationRemoteObject.mapAsRemoteObject(obj);
    }

    public HTTPMessage invoke(HTTPMessage httpMessage) {
        JsonObject json;
        HTTPMessage response = new HTTPMessage();

        if(httpMessage.getVerb() == null){
            response.setStatusCode(HTTPStatus.NOT_FOUND);
            return response;
        }

        try{
            Method method = identificationRemoteObject.getInvocationMethod(httpMessage);

            if(method != null) {

                Class<?> clazz = method.getDeclaringClass();
                Object instance = clazz.getDeclaredConstructor().newInstance();

                if(httpMessage.getBody() == null){
                    json = (JsonObject) method.invoke(instance);
                }
                else{
                    json = (JsonObject) method.invoke(instance, httpMessage.getBody());
                }

                response = new HTTPMessage();
                response.setBody(json);
                response.setStatusCode(HTTPStatus.OK);
                return response;
            }
        }catch (RuntimeException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | InstantiationException e){
            //Put stacktrace as response body
            e.printStackTrace();
        }

        response.setStatusCode(HTTPStatus.NOT_FOUND);
        return response;
    }
}
