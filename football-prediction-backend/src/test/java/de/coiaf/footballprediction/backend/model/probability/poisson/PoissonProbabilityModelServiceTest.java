package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.sharedcontext.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PoissonProbabilityModelServiceTest {

    private static final ThresholdTotalGoals MOCK_PARAMETER_THRESHOLD = mock(ThresholdTotalGoals.class);
    private static final EstimatedGoals MOCK_PARAMETER_TOTAL_GOALS = mock(EstimatedGoals.class);
    private static final EstimatedScore MOCK_PARAMETER_SCORE = mock(EstimatedScore.class);
    private static final OddGroupTotalGoals MOCK_PARAMETER_ODDS_TOTAL_GOALS = mock(OddGroupTotalGoals.class);
    private static final OddGroupOutcome MOCK_PARAMETER_ODDS_OUTCOME = mock(OddGroupOutcome.class);
    private static final OddGroupTotalGoals MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS = mock(OddGroupTotalGoals.class);
    private static final OddGroupOutcome MOCK_RESULT_CALCULATION_ODDS_OUTCOME = mock(OddGroupOutcome.class);
    private static final OddGroupTotalGoals MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS = mock(OddGroupTotalGoals.class);
    private static final OddGroupTotalGoals MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS = mock(OddGroupTotalGoals.class);
    private static final EstimatedGoals MOCK_RESULT_LOADING_TOTAL_GOALS = mock(EstimatedGoals.class);

    private PoissonProbabilityModelService service;
    private PoissonTotalGoalsRepository totalGoalsRepoMock;
    private PoissonOddGroupCalculator calculatorMock;

    @BeforeClass
    public static void setUpClass() {
        when(MOCK_PARAMETER_SCORE.getTotalGoals()).thenReturn(MOCK_PARAMETER_TOTAL_GOALS);
    }

    @Before
    public void setUp() {
        this.totalGoalsRepoMock = FactoryPoissonTotalGoalsRepository.createMock();

        this.calculatorMock = mock(PoissonOddGroupCalculator.class);

        when(this.calculatorMock.createTotalGoalsOdds(any(EstimatedScore.class))).thenReturn(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS);
        when(this.calculatorMock.createTotalGoalsOdds(any(EstimatedScore.class), any(ThresholdTotalGoals.class))).thenReturn(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS);
        when(this.calculatorMock.createTotalGoalsOdds(any(EstimatedGoals.class))).thenReturn(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS);
        when(this.calculatorMock.createTotalGoalsOdds(any(EstimatedGoals.class), any(ThresholdTotalGoals.class))).thenReturn(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS);

        when(this.calculatorMock.createOutcomeOdds(any(EstimatedScore.class))).thenReturn(MOCK_RESULT_CALCULATION_ODDS_OUTCOME);

        this.service = FactoryPoissonProbabilityModelService.createInstance(this.calculatorMock, this.totalGoalsRepoMock);
    }

    @After
    public void tearDown() {
        this.totalGoalsRepoMock = null;
        this.calculatorMock = null;
        this.service = null;
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_nullScore() {
        this.service.determineTotalGoalsOdds((EstimatedScore) null);
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE)).thenReturn(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(0)).createTotalGoalsOdds(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(0)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_persistingSucceeds() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE)).thenReturn(null);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_persistingFails_noEntryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE)).thenReturn(null);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_persistingFails_entryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE)).thenReturn(null, MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedScore.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_nullScore_givenThreshold() {
        this.service.determineTotalGoalsOdds((EstimatedScore) null, MOCK_PARAMETER_THRESHOLD);
    }
    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_givenScore_nullThreshold() {
        this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE, null);
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreFoundInRepo_givenThreshold() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD)).thenReturn(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(0)).createTotalGoalsOdds(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(0)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_givenThreshold_persistingSucceeds() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD)).thenReturn(null);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_givenThreshold_persistingFails_noEntryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD)).thenReturn(null);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenScoreNotFoundInRepo_givenThreshold_persistingFails_entryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD)).thenReturn(null, MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_SCORE, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedScore.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_nullGoals() {
        this.service.determineTotalGoalsOdds((EstimatedGoals) null);
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS)).thenReturn(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(0)).createTotalGoalsOdds(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(0)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_persistingSucceeds() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS)).thenReturn(null);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_persistingFails_noEntryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS)).thenReturn(null);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_persistingFails_entryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS)).thenReturn(null, MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(OddGroupTotalGoals.class));
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_nullGoals_givenThreshold() {
        this.service.determineTotalGoalsOdds((EstimatedGoals) null, MOCK_PARAMETER_THRESHOLD);
    }
    @Test(expected = NullPointerException.class)
    public void determineTotalGoalsOdds_givenGoals_nullThreshold() {
        this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS, null);
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsFoundInRepo_givenThreshold() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING1_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(0)).createTotalGoalsOdds(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(0)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_givenThreshold_persistingSucceeds() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(null);

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).findOddGroup(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_givenThreshold_persistingFails_noEntryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(null);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_CALCULATION_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }
    @Test
    public void determineTotalGoalsOdds_givenGoalsNotFoundInRepo_givenThreshold_persistingFails_entryFoundInRepo() {
        OddGroupTotalGoals result;

        when(this.totalGoalsRepoMock.findOddGroup(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(null, MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS);
        doThrow(PersistenceException.class).when(this.totalGoalsRepoMock).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));

        result = this.service.determineTotalGoalsOdds(MOCK_PARAMETER_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING2_ODDS_TOTAL_GOALS, result);

        verify(this.calculatorMock, times(1)).createTotalGoalsOdds(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(2)).findOddGroup(any(EstimatedGoals.class), any(ThresholdTotalGoals.class));
        verify(this.totalGoalsRepoMock, times(1)).persist(any(EstimatedGoals.class), any(ThresholdTotalGoals.class), any(OddGroupTotalGoals.class));
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoals_nullGoals() {
        this.service.determineTotalGoals(null);
    }
    @Test
    public void determineTotalGoals_givenGoalsNotFoundInRepo() {
        EstimatedGoals result;

        when(this.totalGoalsRepoMock.findGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS)).thenReturn(null);

        result = this.service.determineTotalGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS);

        assertNull(result);
    }
    @Test
    public void determineTotalGoals_givenGoalsFoundInRepo() {
        EstimatedGoals result;

        when(this.totalGoalsRepoMock.findGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS)).thenReturn(MOCK_RESULT_LOADING_TOTAL_GOALS);

        result = this.service.determineTotalGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING_TOTAL_GOALS, result);
    }

    @Test(expected = NullPointerException.class)
    public void determineTotalGoals_nullGoals_givenThreshold() {
        this.service.determineTotalGoals(null, MOCK_PARAMETER_THRESHOLD);
    }
    @Test(expected = NullPointerException.class)
    public void determineTotalGoals_givenGoals_nullThreshold() {
        this.service.determineTotalGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS, null);
    }
    @Test
    public void determineTotalGoals_givenGoalsNotFoundInRepo_givenThreshold() {
        EstimatedGoals result;

        when(this.totalGoalsRepoMock.findGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(null);

        result = this.service.determineTotalGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNull(result);
    }
    @Test
    public void determineTotalGoals_givenGoalsFoundInRepo_givenThreshold() {
        EstimatedGoals result;

        when(this.totalGoalsRepoMock.findGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD)).thenReturn(MOCK_RESULT_LOADING_TOTAL_GOALS);

        result = this.service.determineTotalGoals(MOCK_PARAMETER_ODDS_TOTAL_GOALS, MOCK_PARAMETER_THRESHOLD);

        assertNotNull(result);
        assertSame(MOCK_RESULT_LOADING_TOTAL_GOALS, result);
    }
}