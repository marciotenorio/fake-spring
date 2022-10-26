package identification;

import annotation.*;
import basic.HTTPMessage;
import basic.HTTPVerbs;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IdentificationRemoteObject {

    private String rootClassPath;

    private final Map<String, Method> remoteObjectIdentification;

    public IdentificationRemoteObject() {
        this.remoteObjectIdentification = new HashMap<>();
    }

    public Method getInvocationMethod(HTTPMessage message){
        String path = message.getUrl() + "/" + message.getVerb().toLowerCase();
        return remoteObjectIdentification.get(path);
    }

    public boolean shouldMapMethod(String path){
        return !path.equals(rootClassPath);
    }

    public void mapAsRemoteObject(Object obj){
        Class<?> clazz = obj.getClass();

        rootClassPath = clazz.getAnnotation(RequestMapping.class).route();

        for(Method method : clazz.getMethods()){
            method.setAccessible(true);
            // ex: /{requestMappingRoute}/{methodRoute}/{httpVerb}
            StringBuilder path = new StringBuilder(rootClassPath);

            if(method.isAnnotationPresent(Get.class)){
                path.append(method.getAnnotation(Get.class).route());
                path.append("/");
                path.append(HTTPVerbs.GET);
            }

            if(method.isAnnotationPresent(Post.class)){
                path.append(method.getAnnotation(Post.class).route());
                path.append("/");
                path.append(HTTPVerbs.POST);
            }

            if(method.isAnnotationPresent(Delete.class)){
                path.append(method.getAnnotation(Delete.class).route());
                path.append("/");
                path.append(HTTPVerbs.DELETE);
            }

            if(method.isAnnotationPresent(Put.class)){
                path.append(method.getAnnotation(Put.class).route());
                path.append("/");
                path.append(HTTPVerbs.PUT);
            }

            if(shouldMapMethod(path.toString())){
                remoteObjectIdentification.put(path.toString(), method);
            }
        }
    }
}
