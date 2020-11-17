package de.coiaf.footballprediction.testframework;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;

public abstract class AbstractJpaDbUnitSingleTestFileTestCase extends AbstractDbUnitAccessProvider {

    @BeforeClass
    public static void setUpDatabaseEnvironment() {
        setupDatabase();
    }

    @AfterClass
    public static void tearDownDatabaseEnvironment() throws Exception {
        shutdownDbUnit();
        closeDatabase();
    }

    @Before
    public void setUpDB() throws Exception {
        Class<?> referenceClass = this.getResourceReferenceClass();
        String resourcePath = this.getDataSetFilename();
        this.setEntityManager();
        EntityManager entityManager = this.getEntityManager();

        setupDbUnit(entityManager);
        try {
            beginTransaction(entityManager);
            initializeDatabase(referenceClass, resourcePath);
            commitTransaction(entityManager);
        }
        catch (Exception e) {
            rollbackTransaction(entityManager);
        }
    }

    @After
    public void tearDownEntityManager() {
        this.closeEntityManager();
    }

    protected abstract String getDataSetFilename();

    protected Class<?> getResourceReferenceClass() {
        return this.getClass();
//        return AbstractJpaDbUnitSingleTestFileTestCase.class;
    }
}
