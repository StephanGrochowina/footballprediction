package de.coiaf.footballprediction.testframework;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(AbstractJpaDbUnitELTemplateTestCase.DataSetsTemplateRunner.class)
public abstract class AbstractJpaDbUnitELTemplateTestCase extends AbstractDbUnitAccessProvider {
    private static ELContextImpl context;

    public static IDataSet loadReplacedDataSet(Class<?> referenceClass, String resourcePath) throws Exception {
        IDataSet loadedDataSet = READER_XML_DATABASE.loadDataSet(referenceClass, resourcePath, ELAwareFlatXmlDataSet::new);
        return READER_XML_DATABASE.createReplacedDataSet(loadedDataSet);
    }

    protected static long getClassId(Class<?> clazz) {
        return ELFunctionMapperImpl.getId(clazz);
    }

    protected static void setClassId(String className, long id) {
        ELFunctionMapperImpl.setId(className, id);
    }

    @Before
    public void setUpEntityManager() {
        this.setEntityManager();
    }

    @After
    public void tearDownEntityManager() {
        this.closeEntityManager();
    }

    public static class DataSetsTemplateRunner extends BlockJUnit4ClassRunner {
        private static final ProviderDatasetsAnnotationContent PROVIDER_DATASETS_ANNOTATION_CONTENT = new ProviderDatasetsAnnotationContent();

        public DataSetsTemplateRunner(Class<?> clazz) throws InitializationError {
            super(clazz);
        }

        @Override
        protected void runChild(FrameworkMethod method, RunNotifier notifier) {
            EntityManager entityManager = null;
            Method calledMethod;

            context = new ELContextImpl();
            ELFunctionMapperImpl.resetIds();

            try {
                setupDatabase();
                entityManager = createEntityManager();
                calledMethod = method == null ? null : method.getMethod();

                setupDbUnit(entityManager);
                this.setupDataSet(calledMethod);
                commitTransaction(entityManager);
                super.runChild(method, notifier);
                this.assertDataSet(calledMethod);
            } catch (Exception e) {
                e.printStackTrace();
                rollbackTransaction(entityManager);
            }
            finally {
                try {
                    closeEntityManager(entityManager);
                    shutdownDbUnit();
                    closeDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void setupDataSet(Method method) {
            String dataSetName = PROVIDER_DATASETS_ANNOTATION_CONTENT.determineSetupDataSetPath(this.getTestClass().getJavaClass(), method);
            if (!dataSetName.equals("")) {

                try {
                    IDataSet dataSet = this.loadDataSet(method, dataSetName);
                    initializeDatabase(dataSet);
                } catch (Exception e) {
                    throw new RuntimeException("exception inserting dataset " + dataSetName, e);
                }
            }
        }

        private void assertDataSet(Method method) {
            String dataSetName = PROVIDER_DATASETS_ANNOTATION_CONTENT.determineAssertDataSetPath(this.getTestClass().getJavaClass(), method);
            if (!dataSetName.equals("")) {
                try {
                    IDataSet expectedDataSet = this.loadDataSet(method, dataSetName);
                    IDataSet actualDataSet = READER_XML_DATABASE.createReplacedDataSet(getStrippedDataset(expectedDataSet));

                    String expectedAsString = expectedDataSet == null ? null : expectedDataSet.toString().trim();
                    String actualAsString = actualDataSet == null ? null : actualDataSet.toString().trim();

                    assertEquals(expectedAsString, actualAsString);
                } catch (Exception e) {
                    throw new RuntimeException("exception asserting dataset " + dataSetName, e);
                }
            }
        }

        private IDataSet loadDataSet(Method method, String dataSetName) throws Exception {
            Objects.requireNonNull(dataSetName);

            Class<?> referenceClass = PROVIDER_DATASETS_ANNOTATION_CONTENT.determineReferenceClass(this.getTestClass().getJavaClass(), method);

            if (referenceClass == null) {
                throw new IllegalArgumentException("No DataSets annotation with valid referenceClass found.");
            }

            return loadReplacedDataSet(referenceClass, dataSetName);
        }

    }

    @SuppressWarnings("deprecation")
    private static class ELAwareFlatXmlDataSet extends FlatXmlDataSet {

        ELAwareFlatXmlDataSet(Reader reader) throws DataSetException, IOException {
            super(reader);
        }

        @Override
        public void row(Object[] values) throws DataSetException {
            if (context != null) {
                ExpressionFactory factory = context.getFactory();
                int i = 0;

                for (Object value : values) {
                    String stringValue = "" + value;
                    Object newValue;

                    if (stringValue.startsWith("${") && stringValue.endsWith("}")) {
                        ValueExpression converted = factory.createValueExpression(context, stringValue, Object.class);
                        newValue = converted.getValue(context);
                    } else {
                        newValue = value;
                    }
                    values[i++] = newValue;
                }
            } else {
                throw new IllegalStateException("No context on thread");
            }
            super.row(values);
        }
    }
}
