package letterclassification.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bradhawk on 5/22/2016.
 */
@DatabaseTable(tableName = "alphabets")
public class Alphabet {

    public static final String LETTER = "letter";

    public static final String X_BOX = "x_box";
    public static final String Y_BOX = "y_box";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String ONPIX = "onpix";
    public static final String X_BAR = "x_bar";
    public static final String Y_BAR = "y_bar";
    public static final String X2BAR = "x2bar";
    public static final String Y2BAR = "y2bar";
    public static final String XYBAR = "xybar";
    public static final String X2YBAR = "x2ybar";
    public static final String XY2BAR = "xy2bar";
    public static final String X_EGE = "x_ege";
    public static final String XEGVY = "xegvy";
    public static final String Y_EGE = "y_ege";
    public static final String YEGVX = "yegvx";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = LETTER, canBeNull = false)
    private String letter;

    @DatabaseField(columnName = X_BOX, canBeNull = false)
    private long xBox;

    @DatabaseField(columnName = Y_BOX, canBeNull = false)
    private long yBox;

    @DatabaseField(columnName = WIDTH, canBeNull = false)
    private long width;

    @DatabaseField(columnName = HEIGHT, canBeNull = false)
    private long height;

    @DatabaseField(columnName = ONPIX, canBeNull = false)
    private long onPix;

    @DatabaseField(columnName = X_BAR, canBeNull = false)
    private long xBar;

    @DatabaseField(columnName = Y_BAR, canBeNull = false)
    private long yBar;

    @DatabaseField(columnName = X2BAR, canBeNull = false)
    private long x2Bar;

    @DatabaseField(columnName = Y2BAR, canBeNull = false)
    private long y2Bar;

    @DatabaseField(columnName = XYBAR, canBeNull = false)
    private long xyBar;

    @DatabaseField(columnName = X2YBAR, canBeNull = false)
    private long x2yBar;

    @DatabaseField(columnName = XY2BAR, canBeNull = false)
    private long xy2Bar;

    @DatabaseField(columnName = X_EGE, canBeNull = false)
    private long xEge;

    @DatabaseField(columnName = XEGVY, canBeNull = false)
    private long xEgvy;

    @DatabaseField(columnName = Y_EGE, canBeNull = false)
    private long yEge;

    @DatabaseField(columnName = YEGVX, canBeNull = false)
    private long yEgvx;

    public Alphabet() {
    }

    public Alphabet(String letter, long[] features) {
        this.letter = letter;
        setFeauresFromArray(features);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getId() {
        return id;
    }

    public long[] getFeaturesAsArray() {
        long[] features = {xBox, yBox, width, height, onPix, xBar, yBar, x2Bar, y2Bar, xyBar, x2yBar, xy2Bar, xEge,
                xEgvy, yEge, yEgvx};
        return features;
    }

    public void setFeauresFromArray(long[] features) {
        if (features.length == 16) {
            xBox = features[0];
            yBox = features[1];
            width = features[2];
            height = features[3];
            onPix = features[4];
            xBar = features[5];
            yBar = features[6];
            x2Bar = features[7];
            y2Bar = features[8];
            xyBar = features[9];
            x2yBar = features[10];
            xy2Bar = features[11];
            xEge = features[12];
            xEgvy = features[13];
            yEge = features[14];
            yEgvx = features[15];
        }
    }
}
