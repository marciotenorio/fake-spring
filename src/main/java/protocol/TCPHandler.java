package protocol;

import basic.HTTPMessage;
import basic.Invoker;
import basic.TCPMarshaller;

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
        byte[] data = new byte[4096];

        try {
            inputStream = requesterSocket.getInputStream();
            inputStream.read(data);

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        } finally {
//            close(null, inputStream, null);
        }
        return data;
    }

    private void send(HTTPMessage message){
        DataOutputStream dataOutputStream = null;
        StringBuilder sendMessage = new StringBuilder();
        try{

            dataOutputStream = new DataOutputStream(requesterSocket.getOutputStream());

            sendMessage.append("HTTP/1.1").append(" ");
            sendMessage.append(message.getStatusCode()).append(" ").append(System.lineSeparator());
            sendMessage.append(System.lineSeparator());
            sendMessage.append(System.lineSeparator());

            byte[] bodySerialized = tcpMarshaller.marshaller(message.getBody());

            dataOutputStream.writeBytes(sendMessage.toString());
            dataOutputStream.writeBytes(new String(bodySerialized, StandardCharsets.UTF_8));

            dataOutputStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
//            close(dataOutputStream, null, null);
        }
    }

    public void close(DataOutputStream dataOutputStream,
                      InputStream inputStream,
                      Socket requesterSocket) {
        try{
            if(inputStream != null){
                inputStream.close();
            }
            if(dataOutputStream != null){
                dataOutputStream.close();
            }
            if(requesterSocket != null){
                requesterSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
