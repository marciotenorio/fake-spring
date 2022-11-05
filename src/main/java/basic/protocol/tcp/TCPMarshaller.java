package basic.protocol.tcp;

import basic.HTTPMessage;
import basic.HTTPVerbs;
import basic.Marshaller;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.HTTPUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class TCPMarshaller implements Marshaller {
    @Override
    public HTTPMessage deMarshaller(byte[] data) {
        String parseData = new String(data, StandardCharsets.UTF_8).trim();
        if(parseData.length() == 0){
            return new HTTPMessage();
        }

        HTTPMessage httpMessage = new HTTPMessage();
        String[] splitData = parseData.split(System.lineSeparator());
        String[] startLine = splitData[0].split(" ");
        httpMessage.setVerb(startLine[0]);
        httpMessage.setUrl(startLine[1]);

        //Get the index of last header
        int lastHeader = -1;
        for(int i=0; i < splitData.length; i++){
            if(splitData[i].contains(HTTPUtils.LAST_HEADER)){
                lastHeader = i;
            }
        }

        String[] onlyHeaders = Arrays.copyOfRange(splitData, 1, lastHeader);
        Map<String, String> headers = Arrays.stream(onlyHeaders)
                .map(line -> line.split(":"))
                .filter(headerValue -> headerValue.length == 2)
                .collect(Collectors.toMap(element -> element[0], element -> element[1]));
        httpMessage.setHeaders(headers);

        //get without firstline and if have body, no get body
        StringBuilder body = new StringBuilder();
        for(int i=lastHeader+1; i<splitData.length; i++){
            body.append(splitData[i]);
        }

        JsonObject jsonObject = JsonParser.parseString(body.toString()).getAsJsonObject();

        if(!jsonObject.isJsonObject())
            return new HTTPMessage();

        httpMessage.setBody(jsonObject);

        return httpMessage;
    }

    @Override
    public byte[] marshaller(JsonObject data) {
        return data == null ?
                null : data.toString().getBytes(StandardCharsets.UTF_8);
    }
}
