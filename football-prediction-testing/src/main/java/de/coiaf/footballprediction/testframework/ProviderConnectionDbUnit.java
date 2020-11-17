package de.coiaf.footballprediction.testframework;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.operation.DatabaseOperation;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

class ProviderConnectionDbUnit {

    private final ReaderXmlDatabase readerXmlDatabase = new ReaderXmlDatabase();
    private final HandlerEntityManager handlerEntityManager = new HandlerEntityManager();
    private final EntityManager entityManager;
    private final HsqldbConnection dbUnitConnection;

    ProviderConnectionDbUnit(EntityManager entityManager) throws DatabaseUnitException {
        Objects.requireNonNull(entityManager);

        Connection connection;

        this.handlerEntityManager.beginTransaction(entityManager);
        connection = this.handlerEntityManager.createConnection(entityManager);
        this.entityManager = entityManager;
        this.dbUnitConnection = new HsqldbConnection(connection, null);
    }

    void close() throws SQLException {
        if (this.entityManager.getTransaction().isActive()) {
            this.handlerEntityManager.rollbackTransaction(this.entityManager);
        }
        if (this.dbUnitConnection != null) {
            this.dbUnitConnection.close();
        }
    }

    void initializeDatabase(Class<?> referenceClass, String resourcePath) throws DatabaseUnitException, SQLException {
        this.initializeDatabase(this.createDataSetFromXml(referenceClass, resourcePath));
    }

    void initializeDatabase(IDataSet dataSet) throws DatabaseUnitException, SQLException {
        Objects.requireNonNull(dataSet);

        DatabaseOperation.CLEAN_INSERT.execute(this.dbUnitConnection, dataSet);
    }

    private IDataSet createDataSetFromXml(Class<?> referenceClass, String resourcePath) throws DatabaseUnitException {
        return readerXmlDatabase.loadDataSet(referenceClass, resourcePath);
    }

    IDataSet createActualDataset() throws Exception {
        return this.dbUnitConnection.createDataSet();
    }

    IDataSet createStrippedDataset(IDataSet expectedDataSet) throws Exception {
        String[] tableNames = expectedDataSet.getTableNames();
        return this.dbUnitConnection.createDataSet(tableNames);
    }

    void disableForeignKeyIntegrity() throws SQLException {
        this.fireSqlStatement("SET DATABASE REFERENTIAL INTEGRITY FALSE;");
    }

    void enableForeignKeyIntegrity() throws SQLException {
        this.fireSqlStatement("SET DATABASE REFERENTIAL INTEGRITY TRUE;");
    }

    private void fireSqlStatement(String sql) throws SQLException {
        Connection conn = this.dbUnitConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.execute();
    }
}
