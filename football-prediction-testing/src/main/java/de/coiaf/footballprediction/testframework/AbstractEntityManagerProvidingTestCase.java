package de.coiaf.footballprediction.testframework;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractEntityManagerProvidingTestCase extends AbstractEntityManagerAccessProvider {

    @BeforeClass
    public static void setUpDatabaseEnvironment() {
        setupDatabase();
    }

    @AfterClass
    public static void tearDownDatabaseEnvironment() throws Exception {
        closeDatabase();
    }

    @Before
    public void setUpEntityManager() {
        this.setEntityManager();
    }

    @After
    public void tearDownEntityManager() {
        this.closeEntityManager();
    }
}
