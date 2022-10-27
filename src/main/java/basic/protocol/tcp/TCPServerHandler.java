package basic.protocol.tcp;

import basic.Invoker;
import extension.ExtensionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerHandler {

    private ServerSocket serverSocket;

    private ExecutorService executorService;


    public TCPServerHandler(int port, Invoker invoker, TCPMarshaller tcpMarshaller,
                            ExtensionHandler extensionHandler) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(10);

        this.service(invoker, tcpMarshaller, extensionHandler);
    }

    public void service(Invoker invoker, TCPMarshaller tcpMarshaller, ExtensionHandler extensionHandler){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                executorService.execute(new TCPHandler(socket, invoker, tcpMarshaller, extensionHandler));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
