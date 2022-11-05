package basic;

import basic.protocol.tcp.TCPMarshaller;
import basic.protocol.tcp.TCPServerHandler;
import extension.ExtensionHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class ServerRequestHandler {

    //This guy can be an interface to work um generic protocols
    private TCPServerHandler tcpServerHandler;

    private Invoker invoker;

    private TCPMarshaller tcpMarshaller;

    private ExtensionHandler extensionHandler;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerRequestHandler() {
        this.invoker = new Invoker();
        this.tcpMarshaller = new TCPMarshaller();
        this.extensionHandler = new ExtensionHandler();
    }

    public void listen(int port) {
        try{
            tcpServerHandler = new TCPServerHandler(port, invoker, tcpMarshaller, extensionHandler);
        }catch (IOException e){
            logger.warning("Error to create new network handler.");
            e.printStackTrace();
        }
    }

    public void mapAsRemoteObject(Object object){
        invoker.mapAsRemoteObject(object);
    }

    public void addExtension(Object object){
        extensionHandler.addExtension(object);
    }

}
