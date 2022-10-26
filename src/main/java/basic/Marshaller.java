package basic;

import com.google.gson.JsonObject;

public interface Marshaller {

    public HTTPMessage deMarshaller(byte[] data);

    public byte[] marshaller(JsonObject data);
}
