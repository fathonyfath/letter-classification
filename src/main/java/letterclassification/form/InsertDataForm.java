package letterclassification.form;

import letterclassification.db.Database;
import letterclassification.model.Alphabet;
import letterclassification.util.DataReader;
import letterclassification.util.InvalidFileFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class InsertDataForm {
    private static JFrame frame;

    private JPanel mainForm;
    private JTextField txtFileLocation;
    private JButton btnBrowse;
    private JTextArea txtStatus;
    private JButton btnTrain;
    private JProgressBar prgLoading;

    private JFileChooser fileChooser;
    private DataReader dataReader;

    private MainForm parentForm;

    public static JFrame getInstance(MainForm parentForm) {
        if (frame == null) {
            frame = new JFrame("Train New Data");
            frame.setContentPane(new InsertDataForm(parentForm).mainForm);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setMinimumSize(new Dimension(600, 300));
            frame.pack();
        }
        return frame;
    }

    public InsertDataForm(MainForm parentForm) {
        fileChooser = new JFileChooser();

        this.parentForm = parentForm;

        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(mainForm);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtFileLocation.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        btnTrain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processReadFile(txtFileLocation.getText());
            }
        });
    }

    private void processReadFile(String fileLocation) {
        txtStatus.setText("");
        txtStatus.append(String.format("Reading %s ...\n", fileLocation));
        try {
            disableUI();
            dataReader = new DataReader(fileLocation);
            txtStatus.append("Processing file...\n");
            new DataReaderWorker().execute();

        } catch (FileNotFoundException e) {
            txtStatus.append(String.format("Error: %s file not found.\n", fileLocation));
        }
    }

    private void disableUI() {
        txtFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnTrain.setEnabled(false);
    }

    private void enableUI() {
        txtFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnTrain.setEnabled(true);
    }

    private class DataReaderWorker extends SwingWorker<List<Alphabet>, Void> {

        protected List<Alphabet> doInBackground() throws Exception {
            try {
                return dataReader.processFile();
            } catch (InvalidFileFormatException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            super.done();
            try {
                List<Alphabet> returnList = get();
                if (returnList != null) {
                    txtStatus.append(String.format("%d line(s) of data found.\n", dataReader.getDataProcessed()));
                    new DataProcessWorker(returnList).execute();
                    txtStatus.append(String.format("Saving %d data(s) to database...\n", dataReader.getDataProcessed()));
                } else {
                    txtStatus.append("File format does not match with configured settings.\n");
                    enableUI();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class DataProcessWorker extends SwingWorker<Void, Integer> {

        List<Alphabet> alphabetList;
        int allData;
        int currentData;
        List<Integer> chunkData;

        public DataProcessWorker(List<Alphabet> alphabetList) {
            this.alphabetList = alphabetList;
            this.allData = alphabetList.size();
            this.currentData = 0;
            this.chunkData = new ArrayList<>();
            this.chunkData.add(0);
            prgLoading.setMaximum(this.allData);
            prgLoading.setMinimum(0);
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Alphabet alphabet : alphabetList) {
                Database.AlphabetDao().createIfNotExists(alphabet);
                this.currentData++;
                this.chunkData.set(0, this.currentData);
                this.process(this.chunkData);
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            super.process(chunks);
            int currentData = chunks.get(0);
            prgLoading.setValue(currentData);
        }

        @Override
        protected void done() {
            super.done();
            enableUI();
            txtStatus.append(String.format("%d data(s) saved successfully.\n", this.allData));
            parentForm.updateTable();
        }
    }
}
