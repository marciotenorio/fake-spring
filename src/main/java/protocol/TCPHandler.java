package protocol;

import basic.HTTPMessage;
import basic.Invoker;
import basic.Marshaller;
import utils.StringUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TCPHandler implements Runnable{
    private Socket requesterSocket;

    private final Invoker invoker;

    private final Marshaller marshaller;


    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public TCPHandler(Socket socket, Invoker invoker, Marshaller marshaller) {
        this.invoker = invoker;
        this.marshaller = marshaller;
        this.requesterSocket = socket;
    }

    public void run() {
        HTTPMessage httpMessage = receive();

        HTTPMessage httpResponse = invoker.invoke(httpMessage);
    }

    private HTTPMessage receive() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            inputStream = requesterSocket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            byte[] buffer = new byte[4096];
            inputStream.read(buffer);

            String httpMessage = new String(buffer, StandardCharsets.UTF_8);
            String[] httpMessageSplitted = httpMessage.split(System.lineSeparator());

            marshaller.deMarshaller(httpMessageSplitted);

            //verificar pelo verbo para parsear o body ou não

//            String inputLine;
//            StringBuilder startLineAndHttpHeaders = new StringBuilder();
//
//            /**
//             * Get verb, path, HTTP version and headers separated by OS line separator
//             * */
//            while((inputLine = bufferedReader.readLine()) != null && !inputLine.equals("")){
//                startLineAndHttpHeaders.append(inputLine).append(System.lineSeparator());
//            }

//            int sizeWithoutBody = startLineAndHttpHeaders.toString().getBytes().length;
//            String[] messageTokenized = startLineAndHttpHeaders.toString().split(StringUtils.HTTP_MESSAGE_DELIMITER);

            /**
             * Get body, skipping start line and headers using message byte size
             * */
//            int encodedStringLength = List.of(buffer).indexOf(0);
//            String httpMessage = new String(buffer, sizeWithoutBody, encodedStringLength,StandardCharsets.UTF_8);
//            System.out.println("meu payload:->" + httpMessage + "\n###FIMDOCONTEUDO");

//            for(String a : messageTokenized)
//                System.out.println(a);

//            body.append(bufferedReader.readLine());

//            System.out.println("A seguir o body:\n" + body.toString());

//            System.out.println("BODY ABAIXO!");
//            System.out.println(body.toString());

//            inputLine = null;
//            while((inputLine = bufferedReader.readLine()) != null){
//                body.append(inputLine);
//            }
//            System.out.println(body.toString());

//            httpMessage.setVerb(messageTokenized[0]);
//            httpMessage.setUrl(messageTokenized[1]);
            //set do get body
//            httpMessage.setBody(getBody());

//            PrintWriter printWriter = new PrintWriter(requesterSocket.getOutputStream(), true);

//            printWriter.write("Hello Client Number:"+"\n"+inputLine+"\r\n");
//            print.flush();

//            logger.info(inputLine);
            //think to marshaller return anyMessage format
//            HTTPMessage httpMessage = marshaller.deMarshaller(inputLine);
//            invoker.invoke(httpMessage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(bufferedReader, null);
        }
        return new HTTPMessage();
    }

    public void close(BufferedReader bufferedReader, PrintWriter printWriter ) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(printWriter != null){
                printWriter.close();
            }
            if(requesterSocket != null){
                requesterSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
