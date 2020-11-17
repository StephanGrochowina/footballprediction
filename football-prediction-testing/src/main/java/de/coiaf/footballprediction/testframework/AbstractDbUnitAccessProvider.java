package de.coiaf.footballprediction.testframework;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

abstract class AbstractDbUnitAccessProvider extends AbstractEntityManagerAccessProvider {

    private static ProviderConnectionDbUnit providerConnectionDbUnit;
    static final ReaderXmlDatabase READER_XML_DATABASE = new ReaderXmlDatabase();

    public static void setupDbUnit() throws Exception {
        setupDbUnit(createEntityManager());
    }
    static void setupDbUnit(EntityManager entityManager) throws Exception {
        Objects.requireNonNull(entityManager);

        providerConnectionDbUnit = new ProviderConnectionDbUnit(entityManager);
    }

    static void shutdownDbUnit() throws Exception {
        providerConnectionDbUnit.close();
    }

    static void initializeDatabase(Class<?> referenceClass, String resourcePath) throws Exception {
        providerConnectionDbUnit.initializeDatabase(referenceClass, resourcePath);
    }

    static void initializeDatabase(IDataSet dataSet) throws Exception {
        providerConnectionDbUnit.initializeDatabase(dataSet);
    }

    protected static void disableForeignKeyIntegrity() throws SQLException {
        providerConnectionDbUnit.disableForeignKeyIntegrity();
    }

    protected static void enableForeignKeyIntegrity() throws SQLException {
        providerConnectionDbUnit.enableForeignKeyIntegrity();
    }

    protected static IDataSet loadDataSet(Class<?> referenceClass, String resourcePath) throws Exception {
        return READER_XML_DATABASE.loadDataSet(referenceClass, resourcePath);
    }

    protected static IDataSet createReplacedDataSet(Class<?> referenceClass, String resourcePath) throws Exception {
        return READER_XML_DATABASE.createReplacedDataSet(referenceClass, resourcePath);
    }

    protected static IDataSet createReplacedDataSet(IDataSet originalDataSet) {
        return READER_XML_DATABASE.createReplacedDataSet(originalDataSet);
    }

    protected static IDataSet createActualDataset() throws Exception {
        return providerConnectionDbUnit.createActualDataset();
    }

    static IDataSet getStrippedDataset(IDataSet expectedDataSet) throws Exception {
        return providerConnectionDbUnit.createStrippedDataset(expectedDataSet);
    }

    protected static String toString(IDataSet dataSet) throws IOException, DataSetException {
        return READER_XML_DATABASE.toString(dataSet);
    }
}
