package letterclassification.util;

import letterclassification.model.Alphabet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class DataReader {

    private int dataProcessed;
    private List<Alphabet> alphabetList;
    private BufferedReader bufferedReader;
    private FileReader fileReader;

    public DataReader(String fileLocation) throws FileNotFoundException {
        System.out.println("File Location = " + fileLocation);
        fileReader = new FileReader(fileLocation);
    }

    public List<Alphabet> processFile() throws IOException, InvalidFileFormatException {
        String currentLine;
        bufferedReader = new BufferedReader(fileReader);
        alphabetList = new ArrayList<>();
        dataProcessed = 0;
        while((currentLine = bufferedReader.readLine()) != null) {
            String[] split  = currentLine.split(",");
            if(split.length != 17) {
                throw new InvalidFileFormatException();
            }
            String letter = split[0];
            long[] data = {Long.parseLong(split[1]), Long.parseLong(split[2]), Long.parseLong(split[3]),
                    Long.parseLong(split[4]), Long.parseLong(split[5]), Long.parseLong(split[6]),
                    Long.parseLong(split[7]), Long.parseLong(split[8]), Long.parseLong(split[9]),
                    Long.parseLong(split[10]), Long.parseLong(split[11]), Long.parseLong(split[12]),
                    Long.parseLong(split[13]), Long.parseLong(split[14]), Long.parseLong(split[15]),
                    Long.parseLong(split[16])};
            dataProcessed++;
            alphabetList.add(new Alphabet(letter, data));
        }

        if(bufferedReader != null) {
            bufferedReader = null;
        }

        return alphabetList;
    }

    public int getDataProcessed() {
        return dataProcessed;
    }
}
