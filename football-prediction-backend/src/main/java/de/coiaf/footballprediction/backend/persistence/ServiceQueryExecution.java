package de.coiaf.footballprediction.backend.persistence;

import javax.persistence.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ServiceQueryExecution {

    private static final int DEFAULT_ENTRIES_PER_PAGE = 100;

    private final EntityManager entityManager;

    /**
     * Creates an instance of {@link ServiceQueryExecution}
     * @param entityManager the entity manager to be used
     * @throws NullPointerException if {@param entityManager} is null
     */
    public ServiceQueryExecution(EntityManager entityManager) {
        Objects.requireNonNull(entityManager);

        this.entityManager = entityManager;
    }

    /**
     * Updates or persists {@code entity} depending on the flag {@code update}.
     * @param entity the entity to be updated/persisted
     * @param update should the entity be updated (true) or persisted (false)
     * @param <T> the type of the entity
     * @return the updated or persisted entity
     * @throws NullPointerException if the instance is null
     * @throws EntityExistsException if the entity should be persisted but already
     *         exists.
     * @throws IllegalArgumentException if the instance is not an
     *         entity or if the instance is supposed to be updated but is a
     *         removed entity
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public <T> T saveOrUpdate(T entity, boolean update) {
        Objects.requireNonNull(entity);

        T result;

        if (update) {
            result = this.entityManager.merge(entity);
        }
        else {
            this.entityManager.persist(entity);
            result = entity;
        }

        return result;
    }

    /**
     *
     * @param id the id of the entity
     * @param entityClass the entity class
     * @param <T> the Type of the entity
     * @return the entity of class {@code entityClass} which corresponds to
     *         {@code id} or null if no such entity is present
     * @throws IllegalArgumentException if the first argument does
     *         not denote an entity type or the second argument is
     *         is not a valid type for that entity's primary key or
     *         is null
     */
    public <T> T loadEntity(Long id, Class<T> entityClass) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(entityClass);

        return this.entityManager.find(entityClass, id);
    }

    /**
     *
     * @param calculatorPages a function mapping the entries per page to the number of pages
     * @param paginator a function mapping a pair (current page number, entries per page) to a sublist of results
     * @param <T> the type of the elements contained by the result stream
     * @return a stream of all query results. If {@code calculatorPages} returns null or a value less than 1 an empty stream is returned.
     * @throws NullPointerException if {@code calculatorPages} or paginator is null
     */
    public <T> Stream<T> streamResults(Function<Integer, Integer> calculatorPages, BiFunction<Integer, Integer, List<T>> paginator) {
        return this.streamResults(calculatorPages, paginator, DEFAULT_ENTRIES_PER_PAGE);
    }

    /**
     *
     * @param calculatorPages a function mapping the entries per page to the number of pages
     * @param paginator a function mapping a pair (current page number, entries per page) to a sublist of results
     * @param entriesPerPage the number of entries queried per page
     * @param <T> the type of the elements contained by the result stream
     * @return a stream of all query results. If {@code calculatorPages} returns null or a value less than 1 an empty stream is returned.
     * @throws NullPointerException if {@code calculatorPages} or paginator is null
     * @throws IllegalArgumentException if {@code entriesPerPage} is less than 1
     */
    public <T> Stream<T> streamResults(Function<Integer, Integer> calculatorPages, BiFunction<Integer, Integer, List<T>> paginator, int entriesPerPage) {
        Objects.requireNonNull(calculatorPages);
        Objects.requireNonNull(paginator);
        this.validateEntriesPerPage(entriesPerPage);

        Integer pages = calculatorPages.apply(entriesPerPage);
        List<T> empty = Collections.emptyList();

        return pages == null || pages < 1 ? empty.stream()
            : IntStream.range(0, pages)
                .mapToObj(page -> paginator.apply(page, entriesPerPage))
                .filter(pageEntries -> pageEntries != null && !pageEntries.isEmpty())
                .flatMap(Collection::stream);
    }

    /**
     *
     * @param queryName the name referencing a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param entriesPerPage the number of entries queried per page
     * @return the number of pages for the query results
     * @throws NullPointerException if {@code queryName} is null
     * @throws IllegalArgumentException if {@code entriesPerPage} is less than 1
     */
    public int determineResultPagesByNamedQuery(String queryName, Consumer<TypedQuery<Number>> providerQueryParameters, int entriesPerPage) {
        int totalRows = this.loadSingleResultByNamedQuery(queryName, providerQueryParameters, Number.class, true)
                .intValue();

        return this.calculateResultPages(entriesPerPage, totalRows);
    }

    /**
     *
     * @param jpqlQuery a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param entriesPerPage the number of entries queried per page
     * @return the number of pages for the query results
     * @throws NullPointerException if {@code jpqlQuery} is null
     * @throws IllegalArgumentException if {@code entriesPerPage} is less than 1
     */
    public int determineResultPagesByQuery(String jpqlQuery, Consumer<TypedQuery<Number>> providerQueryParameters, int entriesPerPage) {
        int totalRows = this.loadSingleResultByQuery(jpqlQuery, providerQueryParameters, Number.class, true)
                .intValue();

        return this.calculateResultPages(entriesPerPage, totalRows);
    }

    private int calculateResultPages(int entriesPerPage, int totalRows) {
        this.validateEntriesPerPage(entriesPerPage);

        double ratio = totalRows * 1. / entriesPerPage;

        return (int) (ratio == (int) ratio ? ratio : 1 + Math.floor(ratio));
    }

    /**
     *
     * @param queryName the name referencing a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param page the index of the page to be queried. The first page is 0.
     * @param entriesPerPage the number of entries queried per page
     * @param <T> the type of the elements contained by the result list
     * @return a list of the elements {@code page * entriesPerPage} to {@code page * entriesPerPage + entriesPerPage - 1} found by the query which is
     * corresponding to {@code queryName}. If no element is found for that query within the given range an empty list is returned.
     * @throws NullPointerException if {@code queryName} or {@code resultClass} is null
     * @throws IllegalArgumentException if {@code page} is negative or {@code entriesPerPage} is less than 1
     */
    public <T> List<T> loadMultipleResultsByNamedQuery(String queryName, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass, int page, int entriesPerPage) {
        this.validateEntriesPerPage(entriesPerPage);

        TypedQuery<T> query = this.initializeNamedQuery(queryName, resultClass)
                .setFirstResult(page * entriesPerPage)
                .setMaxResults(entriesPerPage);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadMultipleResults(query);
    }

    /**
     *
     * @param queryName the name referencing a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param <T> the type of the elements contained by the result list
     * @return a list of all elements found by the query which is corresponding to {@code queryName}. If no element is found for that query
     * an empty list is returned.
     * @throws NullPointerException if {@code queryName} or {@code resultClass} is null
     */
    public <T> List<T> loadMultipleResultsByNamedQuery(String queryName, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass) {
        TypedQuery<T> query = this.initializeNamedQuery(queryName, resultClass);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadMultipleResults(query);
    }

    /**
     *
     * @param jpqlQuery a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param page the index of the page to be queried. The first page is 0.
     * @param entriesPerPage the number of entries queried per page
     * @param <T> the type of the elements contained by the result list
     * @return a list of the elements {@code page * entriesPerPage} to {@code page * entriesPerPage + entriesPerPage - 1} found by the query which is
     * orresponding to {@code queryName}. If no element is found for that query within the given range an empty list is returned.
     * @throws NullPointerException if {@code jpqlQuery} or {@code resultClass} is null
     * @throws IllegalArgumentException if {@code page} is negative or {@code entriesPerPage} is less than 1
     */
    public <T> List<T> loadMultipleResultsByQuery(String jpqlQuery, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass, int page, int entriesPerPage) {
        this.validateEntriesPerPage(entriesPerPage);

        TypedQuery<T> query = this.initializeQuery(jpqlQuery, resultClass)
                .setFirstResult(page * entriesPerPage)
                .setMaxResults(entriesPerPage);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadMultipleResults(query);
    }

    /**
     *
     * @param jpqlQuery a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param <T> the type of the elements contained by the result list
     * @return a list of all elements found by the query. If no element is found for that query an empty list is returned.
     * @throws NullPointerException if {@code jpqlQuery} or {@code resultClass} is null
     */
    public <T> List<T> loadMultipleResultsByQuery(String jpqlQuery, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass) {
        TypedQuery<T> query = this.initializeQuery(jpqlQuery, resultClass);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadMultipleResults(query);
    }

    /**
     *
     * @param queryName the name referencing a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param failOnNoResult if true a NoResultException is thrown if no result is found. Otherwise null is returned.
     * @param <T> <T> the type of the elements contained by the result list
     * @return the element found for the query. if no element is found either null is returned or a NoResultException is thrown depending on
     * {@code failOnNoResult}
     * @throws NullPointerException if {@code queryName} or {@code resultClass} is null
     * @throws NonUniqueResultException if more than one element is found by the query
     * @throws NoResultException if no element is found and {@code failOnNoResult} is true
     */
    public <T> T loadSingleResultByNamedQuery(String queryName, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass, boolean failOnNoResult) {
        TypedQuery<T> query = this.initializeNamedQuery(queryName, resultClass);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadSingleResult(query, failOnNoResult);
    }

    /**
     *
     * @param jpqlQuery a JPQL query
     * @param providerQueryParameters optional parameter to set query parameters
     * @param resultClass the class of the elements contained by the result list
     * @param failOnNoResult if true a NoResultException is thrown if no result is found. Otherwise null is returned.
     * @param <T> <T> the type of the elements contained by the result list
     * @return the element found for the query. if no element is found either null is returned or a NoResultException is thrown depending on
     * {@code failOnNoResult}
     * @throws NullPointerException if {@code jpqlQuery} or {@code resultClass} is null
     * @throws NonUniqueResultException if more than one element is found by the query
     * @throws NoResultException if no element is found and {@code failOnNoResult} is true
     */
    public <T> T loadSingleResultByQuery(String jpqlQuery, Consumer<TypedQuery<T>> providerQueryParameters, Class<T> resultClass, boolean failOnNoResult) {
        TypedQuery<T> query = this.initializeQuery(jpqlQuery, resultClass);

        this.initializeQueryParameters(query, providerQueryParameters);

        return this.loadSingleResult(query, failOnNoResult);
    }

    private <T> TypedQuery<T> initializeNamedQuery(String queryName, Class<T> resultClass) {
        Objects.requireNonNull(queryName);
        Objects.requireNonNull(resultClass);

        return this.entityManager.createNamedQuery(queryName, resultClass);
    }

    private <T> TypedQuery<T> initializeQuery(String query, Class<T> resultClass) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(resultClass);

        return this.entityManager.createQuery(query, resultClass);
    }

    private <T> void initializeQueryParameters(TypedQuery<T> query, Consumer<TypedQuery<T>> providerQueryParameters) {
        Objects.requireNonNull(query);

        if (providerQueryParameters != null) {
            providerQueryParameters.accept(query);
        }
    }

    private <T> List<T> loadMultipleResults(TypedQuery<T> query) {
        Objects.requireNonNull(query);

        List<T> result = new ArrayList<>();
        List<T> found = query.getResultList();

        if (found != null && !found.isEmpty()) {
            result.addAll(found);
        }

        return result;
    }

    private <T> T loadSingleResult(TypedQuery<T> query, boolean failOnNoResult) {
        Objects.requireNonNull(query);

        T result;

        try {
            result = query.getSingleResult();
        }
        catch (NoResultException e) {
            if (failOnNoResult) {
                throw e;
            }

            result = null;
        }

        return result;
    }

    private void validateEntriesPerPage(int entriesPerPage) {
        if (entriesPerPage < 1) {
            throw new IllegalArgumentException(("The parameter entriesPerPage must be greater than 0."));
        }
    }
}
