package basic.protocol.tcp;

import basic.HTTPMessage;
import basic.Invoker;
import extension.ExtensionHandler;
import utils.HTTPUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TCPHandler implements Runnable{
    private Socket requesterSocket;

    private final Invoker invoker;

    private final TCPMarshaller tcpMarshaller;

    private final ExtensionHandler extensionHandler;

    public TCPHandler(Socket socket, Invoker invoker, TCPMarshaller tcpMarshaller, ExtensionHandler extensionHandler) {
        this.invoker = invoker;
        this.tcpMarshaller = tcpMarshaller;
        this.requesterSocket = socket;
        this.extensionHandler = extensionHandler;
    }

    public void run() {
        byte[] message = receive();

        if(new String(message).trim().length() != 0){
            HTTPMessage httpMessage = tcpMarshaller.deMarshaller(message);

            extensionHandler.beforeRemoteInvoke(httpMessage);

            HTTPMessage response = invoker.invoke(httpMessage);

            extensionHandler.afterRemoteInvoke(response);

            send(response);
        }
    }

    private byte[] receive() {
        InputStream inputStream = null;
        byte[] data = new byte[102400];

        try {
            inputStream = requesterSocket.getInputStream();
            inputStream.read(data);

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void send(HTTPMessage message){
        DataOutputStream dataOutputStream = null;
        StringBuilder allMessage = new StringBuilder();
        StringBuilder payload = new StringBuilder();
        String serverName = "Server: ViradaoServer";

        try{
            dataOutputStream = new DataOutputStream(requesterSocket.getOutputStream());

            //First-line
            allMessage.append("HTTP/1.1").append(" ")
                .append(message.getStatusCode()).append(" ")
                .append(System.lineSeparator());

            //Headers
            allMessage.append(serverName).append(" ");

            if(message.getBody() != null){
                allMessage.append(HTTPUtils.HEADER_APPLICATION_JSON).append(" ");

                byte[] bodySerialized = tcpMarshaller.marshaller(message.getBody());
                payload.append(new String(bodySerialized, StandardCharsets.UTF_8));

                allMessage.append(HTTPUtils.HEADER_CONTENT_LENGTH).append(": ")
                        .append(payload.toString().getBytes().length);
                allMessage.append(System.lineSeparator());
                allMessage.append(System.lineSeparator());

                String body = new String(bodySerialized, StandardCharsets.UTF_8);

                dataOutputStream.writeBytes(allMessage.toString());
                dataOutputStream.writeBytes(body);

            }
            else{
                dataOutputStream.writeBytes(System.lineSeparator());
            }
            dataOutputStream.flush();
            dataOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
