package basic.protocol.tcp;

import basic.HTTPMessage;
import basic.Invoker;
import utils.HTTPUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TCPHandler implements Runnable{
    private Socket requesterSocket;

    private final Invoker invoker;

    private final TCPMarshaller tcpMarshaller;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public TCPHandler(Socket socket, Invoker invoker, TCPMarshaller tcpMarshaller) {
        this.invoker = invoker;
        this.tcpMarshaller = tcpMarshaller;
        this.requesterSocket = socket;
    }

    public void run() {
        byte[] message = receive();
        HTTPMessage response = invoker.invoke(message);

        if(response != null){
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
            dataOutputStream.writeBytes(allMessage.toString());

//            //Headers
//            allMessage.append(serverName).append(" ");
////            dataOutputStream.writeBytes(serverName);
//
//            if(message.getBody() != null){
//                allMessage.append(HTTPUtils.HEADER_APPLICATION_JSON).append(" ");
//
//                byte[] bodySerialized = tcpMarshaller.marshaller(message.getBody());
//                payload.append(new String(bodySerialized, StandardCharsets.UTF_8));
//
//                allMessage.append(HTTPUtils.HEADER_CONTENT_LENGTH)
//                        .append(payload.toString().getBytes().length);
//                allMessage.append(System.lineSeparator());
//
//                dataOutputStream.writeBytes(allMessage.toString());
//                dataOutputStream.writeBytes(new String(bodySerialized));
//            }
//            else{
//                dataOutputStream.writeBytes(System.lineSeparator());
//            }

            dataOutputStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(dataOutputStream != null){
                    dataOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
