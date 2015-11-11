package before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * For create test file
 */
public class CreateTextFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTextFile.class);
    private static final String nameFile = "C:/file.txt";

    public void createTextFile(String text) {
        try {
            LOGGER.info("Create file with test text");
            File file = new File(nameFile);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
            LOGGER.info(text);
            LOGGER.info("Create file with test text done");
        } catch (IOException e) {
            LOGGER.error("Create file with test text is fail", e, e.getMessage());
            e.printStackTrace();
        }
    }

    public String generateText() {
        Random random = new Random();
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            int ID = random.nextInt(1000) + 1;
            int amount = random.nextInt(10) + 1;
            output.append("\"").append(ID).append(";").append(amount).append("\";\n");
        }
        return output.toString();
    }

    public static void main(String[] args) {
        CreateTextFile createTextFile = new CreateTextFile();
        createTextFile.createTextFile(createTextFile.generateText());
    }
}
