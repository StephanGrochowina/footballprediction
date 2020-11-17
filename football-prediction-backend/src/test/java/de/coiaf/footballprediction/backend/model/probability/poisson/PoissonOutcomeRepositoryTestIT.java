package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.persistence.FactoryServiceQueryExecution;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;
import de.coiaf.footballprediction.testframework.AbstractJpaDbUnitSingleTestFileTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class PoissonOutcomeRepositoryTestIT extends AbstractJpaDbUnitSingleTestFileTestCase {

    private PoissonOutcomeRepository repository = null;

    @Before
    public void setUpRepository() {
        EntityManager entityManager = this.getEntityManager();
        ServiceQueryExecution queryExecutor = FactoryServiceQueryExecution.createInstance(entityManager);

        this.repository = null;
    }

    @After
    public void tearDownRepository() throws Exception {
        this.rollbackTransaction();
        this.repository = null;
    }

    @Test
    public void findOddGroup() {
    }

    @Test
    public void persist() {
    }

    @Test
    public void findScore() {
    }

    @Test
    public void findScore1() {
    }

    @Override
    protected String getDataSetFilename() {
        return Settings.TEST_RESOURCES_PATH + "/PoissonOutcomeRepository.xml";
    }
}