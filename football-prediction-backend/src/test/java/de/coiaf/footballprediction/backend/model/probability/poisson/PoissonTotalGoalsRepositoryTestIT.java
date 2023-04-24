package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupTotalGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import de.coiaf.footballprediction.backend.persistence.FactoryServiceQueryExecution;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;
import de.coiaf.footballprediction.testframework.AbstractJpaDbUnitSingleTestFileTestCase;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PoissonTotalGoalsRepositoryTestIT extends AbstractJpaDbUnitSingleTestFileTestCase {

    private static final ThresholdTotalGoals THRESHOLD_KNOWN = ThresholdTotalGoals.getDefaultInstance();
    private static final ThresholdTotalGoals THRESHOLD_UNKNOWN = ThresholdTotalGoals.valueOf(3.5);
    private static final EstimatedGoals GOALS_KNOWN = EstimatedGoals.valueOf(1);
    private static final EstimatedGoals GOALS_UNKNOWN = EstimatedGoals.valueOf(10);
    private static final EstimatedGoals GOALS_UNKNOWN_FOR_PERSISTING = EstimatedGoals.valueOf(5);
    private static final EstimatedGoals GOALS_SINGLE_ODD_GROUP_QUERY_RESULT = EstimatedGoals.valueOf(new BigDecimal("2.59"));
    private static final EstimatedGoals GOALS_MULTIPLE_ODD_GROUP_RESULTS = EstimatedGoals.valueOf(new BigDecimal("2.67"));
    private static final Probability PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_BELOW_THRESHOLD = Probability.valueOf(new BigDecimal("0.1246520194831"));
    private static final Probability PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD = Probability.valueOf(new BigDecimal("0.8753479805169"));
    private static final DecimalOdd ODD_GOALS_UNKNOWN_FOR_PERSISTING_BELOW_THRESHOLD = DecimalOdd.from(PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_BELOW_THRESHOLD);
    private static final DecimalOdd ODD_GOALS_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD = DecimalOdd.from(PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD);
    private static final OddGroupTotalGoals ODD_GROUP_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD = new OddGroupTotalGoals(ODD_GOALS_UNKNOWN_FOR_PERSISTING_BELOW_THRESHOLD, ODD_GOALS_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD);
    private static final Probability PROBABILITY_GOALS_KNOWN_BELOW_THRESHOLD = Probability.valueOf(new BigDecimal("0.9196986029286"));
    private static final Probability PROBABILITY_GOALS_KNOWN_ABOVE_THRESHOLD = Probability.valueOf(new BigDecimal("0.0803013970714"));
    private static final DecimalOdd ODD_GOALS_KNOWN_BELOW_THRESHOLD = DecimalOdd.from(PROBABILITY_GOALS_KNOWN_BELOW_THRESHOLD);
    private static final DecimalOdd ODD_GOALS_KNOWN_ABOVE_THRESHOLD = DecimalOdd.from(PROBABILITY_GOALS_KNOWN_ABOVE_THRESHOLD);
    private static final OddGroupTotalGoals ODD_GROUP_GOALS_KNOWN = new OddGroupTotalGoals(ODD_GOALS_KNOWN_BELOW_THRESHOLD, ODD_GOALS_KNOWN_ABOVE_THRESHOLD);
    private static final Probability PROBABILITY_SINGLE_ODD_GROUP_QUERY_RESULT_BELOW_THRESHOLD = Probability.valueOf(new BigDecimal("0.5209429093545"));
    private static final Probability PROBABILITY_SINGLE_ODD_GROUP_QUERY_RESULT_ABOVE_THRESHOLD = Probability.valueOf(new BigDecimal("0.4790570906455"));
    private static final Probability PROBABILITY_MULTIPLE_ODD_GROUP_QUERY_RESULTS = Probability.UNCERTAIN;
    private static final DecimalOdd ODD_SINGLE_ODD_GROUP_QUERY_RESULT_BELOW_THRESHOLD = DecimalOdd.from(PROBABILITY_SINGLE_ODD_GROUP_QUERY_RESULT_BELOW_THRESHOLD);
    private static final DecimalOdd ODD_SINGLE_ODD_GROUP_QUERY_RESULT_ABOVE_THRESHOLD = DecimalOdd.from(PROBABILITY_SINGLE_ODD_GROUP_QUERY_RESULT_ABOVE_THRESHOLD);
    private static final DecimalOdd ODD_MULTIPLE_ODD_GROUP_QUERY_RESULTS = DecimalOdd.from(PROBABILITY_MULTIPLE_ODD_GROUP_QUERY_RESULTS);
    private static final OddGroupTotalGoals ODD_GROUP_SINGLE_ODD_GROUP_QUERY_RESULT = new OddGroupTotalGoals(ODD_SINGLE_ODD_GROUP_QUERY_RESULT_BELOW_THRESHOLD, ODD_SINGLE_ODD_GROUP_QUERY_RESULT_ABOVE_THRESHOLD);
    private static final OddGroupTotalGoals ODD_GROUP_MULTIPLE_ODD_GROUP_QUERY_RESULTS = new OddGroupTotalGoals(ODD_MULTIPLE_ODD_GROUP_QUERY_RESULTS, ODD_MULTIPLE_ODD_GROUP_QUERY_RESULTS);

    private PoissonTotalGoalsRepository repository = null;

    @Before
    public void setUpRepository() {
        EntityManager entityManager = this.getEntityManager();
        ServiceQueryExecution queryExecutor = FactoryServiceQueryExecution.createInstance(entityManager);

        this.repository = FactoryPoissonTotalGoalsRepository.createSpy(queryExecutor);
    }

    @After
    public void tearDownRepository() {
        this.rollbackTransaction();
        this.repository = null;
    }

    @Test(expected = NullPointerException.class)
    public void findOddGroup_nullTotalGoals() {
        this.repository.findOddGroup((EstimatedGoals) null);
    }
    @Test
    public void findOddGroup_unknownTotalGoals() {
        OddGroupTotalGoals odds = this.repository.findOddGroup(GOALS_UNKNOWN);

        assertNull(odds);
        verify(this.repository, times(1)).findOddGroup(GOALS_UNKNOWN, THRESHOLD_KNOWN);
    }
    @Test
    public void findOddGroup_knownTotalGoals() {
        OddGroupTotalGoals odds = this.repository.findOddGroup(GOALS_KNOWN);

        assertNotNull(odds);
        assertEquals(PROBABILITY_GOALS_KNOWN_BELOW_THRESHOLD, odds.getOddBelowThreshold().getImpliedProbability());
        assertEquals(PROBABILITY_GOALS_KNOWN_ABOVE_THRESHOLD, odds.getOddAboveThreshold().getImpliedProbability());
        verify(this.repository, times(1)).findOddGroup(GOALS_KNOWN, THRESHOLD_KNOWN);
    }

    @Test(expected = NullPointerException.class)
    public void findOddGroup_knownTotalGoals_nullThreshold() {
        this.repository.findOddGroup(GOALS_KNOWN, null);
    }
    @Test
    public void findOddGroup_knownTotalGoals_unknownThreshold() {
        OddGroupTotalGoals odds = this.repository.findOddGroup(GOALS_UNKNOWN, THRESHOLD_UNKNOWN);

        assertNull(odds);
    }
    @Test(expected = NullPointerException.class)
    public void findOddGroup_nullTotalGoals_knownThreshold() {
        this.repository.findOddGroup((EstimatedGoals) null, THRESHOLD_KNOWN);
    }
    @Test
    public void findOddGroup_unknownTotalGoals_knownThreshold() {
        OddGroupTotalGoals odds = this.repository.findOddGroup(GOALS_UNKNOWN, THRESHOLD_KNOWN);

        assertNull(odds);
    }
    @Test
    public void findOddGroup_knownTotalGoals_knownThreshold() {
        OddGroupTotalGoals odds = this.repository.findOddGroup(GOALS_KNOWN, THRESHOLD_KNOWN);

        assertNotNull(odds);
        assertEquals(PROBABILITY_GOALS_KNOWN_BELOW_THRESHOLD, odds.getOddBelowThreshold().getImpliedProbability());
        assertEquals(PROBABILITY_GOALS_KNOWN_ABOVE_THRESHOLD, odds.getOddAboveThreshold().getImpliedProbability());
    }

    @Test(expected = NullPointerException.class)
    public void persist_nullExpectedGoals_knownThreshold_unknownOddGroup() {
        this.beginTransaction();
        this.repository.persist(null, THRESHOLD_KNOWN, ODD_GROUP_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD);
        this.commitTransaction();
    }
    @Test(expected = NullPointerException.class)
    public void persist_unknownExpectedGoals_nullThreshold_unknownOddGroup() {
        this.beginTransaction();
        this.repository.persist(GOALS_UNKNOWN_FOR_PERSISTING, null, ODD_GROUP_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD);
        this.commitTransaction();
    }
    @Test(expected = NullPointerException.class)
    public void persist_unknownExpectedGoals_knownThreshold_nullOddGroup() {
        this.beginTransaction();
        this.repository.persist(GOALS_UNKNOWN_FOR_PERSISTING, THRESHOLD_KNOWN, null);
        this.commitTransaction();
    }
    @Test(expected = PersistenceException.class)
    public void persist_knownExpectedGoals_knownThreshold_knownOddGroup() {
        this.beginTransaction();
        this.repository.persist(GOALS_KNOWN, THRESHOLD_KNOWN, ODD_GROUP_GOALS_KNOWN);
        this.commitTransaction();
    }
    @Test
    public void persist_unknownExpectedGoals_knownThreshold_unknownOddGroup() {
        OddGroupTotalGoals odds;

        this.beginTransaction();
        this.repository.persist(GOALS_UNKNOWN_FOR_PERSISTING, THRESHOLD_KNOWN, ODD_GROUP_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD);
        this.commitTransaction();

        odds = this.repository.findOddGroup(GOALS_UNKNOWN_FOR_PERSISTING, THRESHOLD_KNOWN);

        assertNotNull(odds);
        assertEquals(PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_BELOW_THRESHOLD, odds.getOddBelowThreshold().getImpliedProbability());
        assertEquals(PROBABILITY_GOALS_UNKNOWN_FOR_PERSISTING_ABOVE_THRESHOLD, odds.getOddAboveThreshold().getImpliedProbability());
    }

    @Test(expected = NullPointerException.class)
    public void findGoals_nullOddGroup_knownThreshold() {
        this.repository.findGoals(null, THRESHOLD_KNOWN);
    }
    @Test(expected = NullPointerException.class)
    public void findGoals_knownOddGroup_nullThreshold() {
        this.repository.findGoals(ODD_GROUP_SINGLE_ODD_GROUP_QUERY_RESULT, null);
    }
    @Test
    public void findGoals_knownOddGroup_unknownThreshold() {
        EstimatedGoals result = this.repository.findGoals(ODD_GROUP_SINGLE_ODD_GROUP_QUERY_RESULT, THRESHOLD_UNKNOWN);

        assertNull(result);
    }
    @Test
    public void findGoals_knownSingleOddGroup_knownThreshold() {
        EstimatedGoals result = this.repository.findGoals(ODD_GROUP_SINGLE_ODD_GROUP_QUERY_RESULT, THRESHOLD_KNOWN);

        assertNotNull(result);
        assertEquals(GOALS_SINGLE_ODD_GROUP_QUERY_RESULT, result);
    }
    @Test
    public void findGoals_knownMultipleOddGroup_knownThreshold() {
        EstimatedGoals result = this.repository.findGoals(ODD_GROUP_MULTIPLE_ODD_GROUP_QUERY_RESULTS, THRESHOLD_KNOWN);

        assertNotNull(result);
        assertEquals(GOALS_MULTIPLE_ODD_GROUP_RESULTS, result);
    }

    @Override
    protected String getDataSetFilename() {
        return Settings.TEST_RESOURCES_PATH + "/PoissonTotalGoalsRepository.xml";
    }
}