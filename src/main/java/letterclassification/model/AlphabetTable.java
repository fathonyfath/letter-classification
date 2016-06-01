package letterclassification.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class AlphabetTable extends AbstractTableModel {

    private String[] columnName = {"Id", "Letter", "X Box", "Y Box", "Width", "Height",
    "On Pix", "X Bar", "Y Bar", "X2 Bar", "Y2 Bar", "XY Bar", "X2Y Bar", "XY2 Bar",
    "X Ege", "X Egv Y", "Y Ege", "Y Egv X"};
    private List<Alphabet> alphabetList = new ArrayList<>();

    public void setData(List<Alphabet> data) {
        this.alphabetList = data;
    }

    public int getRowCount() {
        return alphabetList.size();
    }

    public int getColumnCount() {
        return columnName.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return alphabetList.get(rowIndex).getId();
        } else if(columnIndex == 1) {
            return alphabetList.get(rowIndex).getLetter();
        } else {
            int columnData = columnIndex - 2;
            return alphabetList.get(rowIndex).getFeaturesAsArray()[columnData];
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
}
