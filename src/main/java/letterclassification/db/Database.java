package letterclassification.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import letterclassification.model.Alphabet;
import letterclassification.model.TestModel;

import java.sql.SQLException;

/**
 * Created by bradhawk on 5/22/2016.
 */
public class Database {
    private static final String connectionString = "jdbc:sqlite:database.db";
    private static ConnectionSource databaseConnection;

    private static Dao<TestModel, String> TestModelDao;
    private static Dao<Alphabet, Integer> AlphabetDao;

    public static void initialize() {
        if (databaseConnection == null) {
            try {
                databaseConnection = new JdbcConnectionSource(connectionString);

                TestModelDao = DaoManager.createDao(databaseConnection, TestModel.class);
                TableUtils.createTableIfNotExists(databaseConnection, TestModel.class);

                AlphabetDao = DaoManager.createDao(databaseConnection, Alphabet.class);
                TableUtils.createTableIfNotExists(databaseConnection, Alphabet.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static Dao<TestModel, String> TestModelDao() {
        return TestModelDao;
    }

    public static Dao<Alphabet, Integer> AlphabetDao() {
        return AlphabetDao;
    }
}
