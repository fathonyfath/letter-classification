package letterclassification.form;

import letterclassification.classification.KNNClassifier;
import letterclassification.model.Alphabet;
import letterclassification.model.Classify;
import letterclassification.model.ClassifyTable;
import letterclassification.model.ClassifyTableCellRenderer;
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
 * Created by bradhawk on 6/1/2016.
 */
public class TestDataForm {
    private static JFrame frame;

    private JButton btnBrowse;
    private JButton btnClassify;
    private JTextField txtFileLocation;
    private JTextArea txtStatus;
    private JProgressBar prgLoading;
    private JTable tblClassifiedData;
    private JPanel mainForm;
    private JLabel lblDataCount;
    private JLabel lblDataSuccess;
    private JLabel lblDataFailed;
    private JLabel lblPercentage;

    private JFileChooser fileChooser;
    private DataReader dataReader;

    private MainForm parentForm;

    private List<Classify> classifies;

    private List<Alphabet> parentData;

    public static JFrame getInstance(MainForm parentForm, List<Alphabet> parentData) {
        if (frame == null) {
            frame = new JFrame("Classify Data");
            frame.setContentPane(new TestDataForm(parentForm, parentData).mainForm);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setMinimumSize(new Dimension(600, 300));
            frame.pack();
        }
        return frame;
    }

    public TestDataForm(MainForm parentForm, List<Alphabet> parentData) {
        fileChooser = new JFileChooser();

        this.parentForm = parentForm;
        this.parentData = parentData;

        this.classifies = new ArrayList<>();

        ClassifyTable classifyTable = new ClassifyTable();
        tblClassifiedData.setModel(classifyTable);

        tblClassifiedData.getColumnModel().getColumn(0).setCellRenderer(new ClassifyTableCellRenderer());
        tblClassifiedData.getColumnModel().getColumn(1).setCellRenderer(new ClassifyTableCellRenderer());

        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(mainForm);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtFileLocation.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        btnClassify.addActionListener(new ActionListener() {
            @Override
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

    private void updateTable() {
        ((ClassifyTable) tblClassifiedData.getModel()).setData(classifies);
    }

    private void disableUI() {
        txtFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnClassify.setEnabled(false);
    }

    private void enableUI() {
        txtFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnClassify.setEnabled(true);
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
                    txtStatus.append(String.format("%d data(s) is being classified", dataReader.getDataProcessed()));
                    new ClassifyWorker(parentData, returnList).execute();
                } else {
                    txtStatus.append("File format does not match with configured settings.\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClassifyWorker extends SwingWorker<Void, Integer> {

        List<Alphabet> dataTable;
        List<Alphabet> dataTraining;
        List<Classify> classifyResult;
        int allData;
        int currentData;
        List<Integer> chunkData;
        KNNClassifier knnClassifier;

        public ClassifyWorker(List<Alphabet> dataTable, List<Alphabet> dataTraining) {
            this.dataTable = dataTable;
            this.dataTraining = dataTraining;
            this.classifyResult = new ArrayList<>();
            this.allData = dataTraining.size();
            this.currentData = 0;
            this.chunkData = new ArrayList<>();
            this.chunkData.add(0);
            prgLoading.setMaximum(this.allData);
            prgLoading.setMinimum(0);
            knnClassifier = new KNNClassifier(dataTable, 1);
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Alphabet training : dataTraining) {
                double[] features = new double[training.getFeaturesAsArray().length];
                for (int i = 0; i < features.length; i++) {
                    features[i] = training.getFeaturesAsArray()[i];
                }
                String classification = knnClassifier.classify(features);
                this.classifyResult.add(new Classify(training, classification));
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
            txtStatus.append(String.format("%d data(s) classified successfully.\n", this.allData));
            classifies = classifyResult;

            int dataCount = classifies.size();
            int dataSuccess = 0;
            int dataFailed = 0;
            for(Classify classify : classifies) {
                if(classify.getClassifyResult().equalsIgnoreCase(classify.getAlphabet().getLetter())) {
                    dataSuccess++;
                } else {
                    dataFailed++;
                }
            }
            float percentage = ((float) dataSuccess) / ((float) dataCount) * 100f;

            lblDataCount.setText(String.valueOf(dataCount));
            lblDataSuccess.setText(String.valueOf(dataSuccess));
            lblDataFailed.setText(String.valueOf(dataFailed));
            lblPercentage.setText(String.format("%.2f", percentage));
            updateTable();
        }
    }
}
