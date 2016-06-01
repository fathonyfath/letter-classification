package letterclassification.form;

import letterclassification.db.Database;
import letterclassification.model.Alphabet;
import letterclassification.model.AlphabetTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class MainForm {
    private static JFrame frame;

    private JPanel mainForm;
    private JButton btnInsertData;
    private JButton btnTestData;
    private JTable tblTrainedData;
    private JLabel lblStatus;

    private AlphabetTable alphabetTable;
    private List<Alphabet> alphabetList;

    public static JFrame getInstance() {
        if (frame == null) {
            frame = new JFrame("MainForm");
            frame.setContentPane(new MainForm().mainForm);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(600, 400));
            frame.pack();
        }
        return frame;
    }

    public MainForm() {
        alphabetTable = new AlphabetTable();
        tblTrainedData.setModel(alphabetTable);
        updateTable();

        btnInsertData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InsertDataForm.getInstance(MainForm.this).setVisible(true);
            }
        });
        btnTestData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestDataForm.getInstance(MainForm.this, alphabetList).setVisible(true);
            }
        });
    }

    public void updateTable() {
        try {
            alphabetList = Database.AlphabetDao().queryForAll();
            ((AlphabetTable) tblTrainedData.getModel()).setData(alphabetList);
            String status = String.format("Total Data = %d row(s).", alphabetList.size());
            lblStatus.setText(status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
