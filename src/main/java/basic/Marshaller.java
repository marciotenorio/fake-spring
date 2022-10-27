package basic;

import com.google.gson.JsonObject;

public interface Marshaller {

    HTTPMessage deMarshaller(byte[] data);

    byte[] marshaller(JsonObject data);
}
