package letterclassification.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bradhawk on 5/22/2016.
 */

@DatabaseTable(tableName = "tests")
public class TestModel {

    public static final String field1 = "nama";
    public static final String field2 = "alamat";
    public static final String field3 = "ipk";

    @DatabaseField(id = true, columnName = field1)
    private String nama;

    @DatabaseField(columnName = field2)
    private String alamat;

    @DatabaseField(columnName = field3)
    private double IPK;

    public TestModel() {

    }

    public TestModel(String nama, String alamat, double IPK) {
        this.nama = nama;
        this.alamat = alamat;
        this.IPK = IPK;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public double getIPK() {
        return IPK;
    }

    public void setIPK(double IPK) {
        this.IPK = IPK;
    }
}
