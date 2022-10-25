package basic;

import utils.StringUtils;

import java.net.Socket;
import java.util.logging.Logger;

public class Marshaller {

    public HTTPMessage deMarshaller(String message) {
        HTTPMessage httpMessage = new HTTPMessage();

        httpMessage.setVerb(StringUtils.getVerb(message));
        httpMessage.setUrl(StringUtils.getPath(message));

        return httpMessage;
    }
}
