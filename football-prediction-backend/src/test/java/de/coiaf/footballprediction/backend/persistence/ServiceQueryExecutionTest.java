package de.coiaf.footballprediction.backend.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ServiceQueryExecutionTest {

    private static final int ENTRIES_PER_PAGE_VALID = 10;
    private static final int ENTRIES_PER_PAGE_ZERO = 0;
    private static final int ENTRIES_PER_PAGE_NEGATIVE = -10;
    private static final int CALCULATED_PAGES_POSITIVE = 10;
    private static final Function<Integer, Integer> CALCULATOR_PAGES_RETURNING_NULL = integer -> null;
    private static final Function<Integer, Integer> CALCULATOR_PAGES_RETURNING_ZERO = integer -> 0;
    private static final Function<Integer, Integer> CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE = integer -> CALCULATED_PAGES_POSITIVE;
    private static final Function<Integer, Integer> CALCULATOR_PAGES_RETURNING_NEGATIVE_VALUE = integer -> -CALCULATED_PAGES_POSITIVE;
    private static final List<Integer> PAGINATOR_RESULT = IntStream.range(0, ENTRIES_PER_PAGE_VALID)
            .boxed()
            .collect(Collectors.toList());
    private static final BiFunction<Integer, Integer, List<Integer>> PAGINATOR_RETURNING_NULL = (pages, entriesPerPage) -> null;
    private static final BiFunction<Integer, Integer, List<Integer>> PAGINATOR_RETURNING_EMPTY_LIST = (pages, entriesPerPage) -> Collections.emptyList();
    private static final BiFunction<Integer, Integer, List<Integer>> PAGINATOR_RETURNING_FILLED_LIST = (pages, entriesPerPage) -> PAGINATOR_RESULT;


    private EntityManager entityMangerMock;
    private ServiceQueryExecution queryExecutor;

    @Before
    public void setUp() {
        this.entityMangerMock = mock(EntityManager.class);

        this.queryExecutor = FactoryServiceQueryExecution.createInstance(this.entityMangerMock);
    }

    @After
    public void tearDown() {
        this.entityMangerMock = null;
        this.queryExecutor = null;
    }

    @Test(expected = NullPointerException.class)
    public void streamResults_nullCalculatorPages_givenPaginator_validEntriesPerPage() {
        this.queryExecutor.streamResults(null, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_VALID);
    }
    @Test
    public void streamResults_givenCalculatorPagesReturningNull_givenPaginator_validEntriesPerPage() {
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_NULL, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(0, result);
    }
    @Test
    public void streamResults_givenCalculatorPagesReturningZero_givenPaginator_validEntriesPerPage() {
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_ZERO, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(0, result);
    }
    @Test
    public void streamResults_givenCalculatorPagesReturningNegativeValue_givenPaginator_validEntriesPerPage() {
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_NEGATIVE_VALUE, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(0, result);
    }
    @Test(expected = NullPointerException.class)
    public void streamResults_givenCalculatorPages_nullPaginator_validEntriesPerPage() {
        this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, null, ENTRIES_PER_PAGE_VALID);
    }
    @Test
    public void streamResults_givenCalculatorPages_givenPaginatorReturningNull_validEntriesPerPage() {
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, PAGINATOR_RETURNING_NULL, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(0, result);
    }
    @Test
    public void streamResults_givenCalculatorPages_givenPaginatorReturningEmtyList_validEntriesPerPage() {
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, PAGINATOR_RETURNING_EMPTY_LIST, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(0, result);
    }
    @Test(expected = IllegalArgumentException.class)
    public void streamResults_givenCalculatorPages_givenPaginator_negativeEntriesPerPage() {
        this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void streamResults_givenCalculatorPages_givenPaginator_zeroEntriesPerPage() {
        this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_ZERO);
    }
    @Test
    public void streamResults_givenCalculatorPages_givenPaginator_positiveEntriesPerPage() {
        long expected = ENTRIES_PER_PAGE_VALID * CALCULATED_PAGES_POSITIVE;
        long result = this.queryExecutor.streamResults(CALCULATOR_PAGES_RETURNING_POSTIVE_VALUE, PAGINATOR_RETURNING_FILLED_LIST, ENTRIES_PER_PAGE_VALID)
                .count();

        assertEquals(expected, result);
    }

}