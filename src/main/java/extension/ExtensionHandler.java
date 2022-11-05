package extension;

import basic.HTTPMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExtensionHandler {

    private List<Object> extensions;

    public ExtensionHandler() {
        this.extensions = new ArrayList<>();
    }

    public void addExtension(Object extension){
        extensions.add(extension);
    }

    private void hook(String methodName, HTTPMessage httpMessage){
        for(Object extension : extensions){
            for(Method method : extension.getClass().getMethods()){
                if(method.getName().equals(methodName)){
                    try{
                        if(httpMessage.getBody() != null){
                            method.invoke(extension.getClass().getConstructor().newInstance(),
                                httpMessage.getBody().toString());
                        }
                    }catch (NoSuchMethodException
                            | InstantiationException
                            | IllegalAccessException |
                            InvocationTargetException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //Inspired in Vue Hooks
    public void beforeRemoteInvoke(HTTPMessage httpMessage){
        hook("beforeRemoteInvoke", httpMessage);
    }

    public void afterRemoteInvoke(HTTPMessage httpMessage){
        hook("afterRemoteInvoke", httpMessage);
    }

}
