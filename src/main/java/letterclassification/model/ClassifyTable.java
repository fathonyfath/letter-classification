package letterclassification.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradhawk on 6/1/2016.
 */
public class ClassifyTable extends AbstractTableModel {

    private String[] columnName = {"Alphabet di Data", "Alphabet hasil klasifikasi"};
    private List<Classify> classifies = new ArrayList<>();

    public void setData(List<Classify> data) {
        this.classifies = data;
    }

    @Override
    public int getRowCount() {
        return classifies.size();
    }

    @Override
    public int getColumnCount() {
        return columnName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return classifies.get(rowIndex).getAlphabet().getLetter();
        } else {
            return classifies.get(rowIndex).getClassifyResult();
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnName[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    public List<Classify> getClassifies() {
        return classifies;
    }
}
