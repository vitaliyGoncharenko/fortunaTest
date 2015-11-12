package actors;

import akka.actor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Before start program please create file. To create the file, use class CreateTextFile
 */
public class ClientActor extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientActor.class);
    private static ActorSystem system;
    private ActorRef firstWorker;
    private static final String nameFile = "C:/file.txt";
    private static final String newNameFile = "C:/newFile.txt";

    @Override
    public void preStart() throws Exception {
        LOGGER.info("Start Client action");
        firstWorker = context().system().actorOf(Props.create(FirstWorkerActor.class));
        readAndSend();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            LOGGER.info("Rezult: \n"+message.toString());
            write(message.toString());
            LOGGER.info("Exit program");
            getContext().system().shutdown();
        }
    }

    @Override
    public void postStop() throws Exception {
        LOGGER.info("Client stop");
    }

    private void readAndSend(){
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(nameFile));

            while ((sCurrentLine = br.readLine()) != null) {
                firstWorker.tell(sCurrentLine, self());
            }

        } catch (IOException e) {
            LOGGER.error("Read from " + nameFile + " is fail",e ,e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                LOGGER.error("Read from " + nameFile + " is fail",ex ,ex.getMessage());
            }
            LOGGER.info("Reading from a " + nameFile + " is complete");
            firstWorker.tell("Reading from a file is complete", self());
        }
    }

    private void write(String message){
        try {
            File file = new File(newNameFile);
            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(message.toString());
            bw.close();
            LOGGER.info("Write to "+newNameFile+" is complete");
        } catch (IOException e) {
            LOGGER.error("Write to file is fail");
        }
    }

    public static void main(String[] args) {
        system = ActorSystem.create("TestSystem");
        system.actorOf(Props.create(ClientActor.class));
    }
}
