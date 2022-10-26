package basic;

import protocol.TCPServerHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class ServerRequestHandler {

    //This guy can be an interface to work um generic protocols
    //Now i don't receive them, but using interfaces i can receive.
    private TCPServerHandler tcpServerHandler;

    private Invoker invoker;

    private TCPMarshaller TCPMarshaller;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerRequestHandler() {
        this.invoker = new Invoker();
        this.TCPMarshaller = new TCPMarshaller();
    }

    public void listen(int port) {
        try{
            tcpServerHandler = new TCPServerHandler(port, invoker, TCPMarshaller);
        }catch (IOException e){
            logger.warning("Error to create new network handler.");
            e.printStackTrace();
        }
    }

    public void mapAsRemoteObject(Object object){
        invoker.mapAsRemoteObject(object);
    }

}
