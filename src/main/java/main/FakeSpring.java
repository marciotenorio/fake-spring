package main;

import basic.ServerRequestHandler;
import business.Business;
import business.FakeLogger;
import extension.ExtensionHandler;

import java.util.logging.Logger;

public class FakeSpring {

    private ServerRequestHandler serverRequestHandler;

    private ExtensionHandler extensionHandler;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public FakeSpring() {
        this.serverRequestHandler = new ServerRequestHandler();
    }

    public void mapAsRemoteObject(Object obj){
        serverRequestHandler.mapAsRemoteObject(obj);
    }

    public void addExtension(Object object){
        serverRequestHandler.addExtension(object);
    }

    public void listen(int port){
        logger.info("Listen in " + port  + "...");
        serverRequestHandler.listen(port);
    }

    public static void main(String[] args) {

        FakeSpring fakeSpring = new FakeSpring();
        Business business = new Business();
        FakeLogger fakeLogger = new FakeLogger();

        fakeSpring.mapAsRemoteObject(business);
        fakeSpring.addExtension(fakeLogger);

        fakeSpring.listen(Integer.parseInt(args[0]));

    }
}
