package letterclassification.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by bradhawk on 6/1/2016.
 */
public class ClassifyTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ClassifyTable classifyTable = (ClassifyTable) table.getModel();
        if(classifyTable.getClassifies().get(row).getClassifyResult().equalsIgnoreCase(classifyTable.getClassifies().get(row).getAlphabet().getLetter())) {
            label.setBackground(Color.GREEN);
        } else {
            label.setBackground(Color.RED);
        }
        return label;
    }
}
