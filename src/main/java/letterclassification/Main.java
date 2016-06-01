package letterclassification;

import letterclassification.classification.KNNClassifier;
import letterclassification.db.Database;
import letterclassification.form.MainForm;
import letterclassification.model.Alphabet;
import letterclassification.util.DataReader;
import letterclassification.util.InvalidFileFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        Database.initialize();
        MainForm.getInstance().setVisible(true);
    }
}
