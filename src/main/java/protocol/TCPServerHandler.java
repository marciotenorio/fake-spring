package protocol;

import basic.Invoker;
import basic.TCPMarshaller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerHandler {

    private ServerSocket serverSocket;

    private ExecutorService executorService;

    private final int POOL_SIZE = 10;

    public TCPServerHandler(int port, Invoker invoker, TCPMarshaller TCPMarshaller) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);

        this.service(invoker, TCPMarshaller);
    }

    public void service(Invoker invoker, TCPMarshaller TCPMarshaller){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                executorService.execute(new TCPHandler(socket, invoker, TCPMarshaller));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
