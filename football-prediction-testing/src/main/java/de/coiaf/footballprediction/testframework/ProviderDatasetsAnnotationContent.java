package de.coiaf.footballprediction.testframework;

import java.lang.reflect.Method;
import java.util.function.Function;

class ProviderDatasetsAnnotationContent {

    static final String DEFAULT_SETUP_DATASET = "/empty.xml";

    String determineSetupDataSetPath(Class<?> testClass, Method testMethod) {
        return this.determineAnnotationContent(testClass, testMethod, dataSetAnnotation -> {
            String setupDataSet = dataSetAnnotation.setUpDataSet();

            if (!DEFAULT_SETUP_DATASET.equals(setupDataSet)) {
                return setupDataSet;
            }

            return this.createResourcePathFromPackageName(testClass) + setupDataSet;
        });
    }

    String determineAssertDataSetPath(Class<?> testClass, Method testMethod) {
        return this.determineAnnotationContent(testClass, testMethod, DataSets::assertDataSet);
    }

    Class<?> determineReferenceClass(Class<?> testClass, Method testMethod) {
        return this.determineAnnotationContent(testClass, testMethod, DataSets::referenceClass);
    }

    private <T> T determineAnnotationContent(Class<?> testClass, Method testMethod, Function<DataSets, T> supplierContent) {
        DataSets dataSetsAnnotation = this.determineAnnotation(testClass, testMethod);

        if (dataSetsAnnotation == null) {
            return null;
        }

        return supplierContent == null ? null : supplierContent.apply(dataSetsAnnotation);
    }

    private DataSets determineAnnotation(Class<?> testClass, Method testMethod) {
        DataSets dataSetsAnnotation = testMethod == null ? null : testMethod.getAnnotation(DataSets.class);

        if (dataSetsAnnotation == null) {
            dataSetsAnnotation = testClass == null ? null : testClass.getAnnotation(DataSets.class);
        }

        return dataSetsAnnotation;
    }

    private String createResourcePathFromPackageName(Class<?> testClass) {
        Class<?> referenceClass = testClass == null ? AbstractJpaDbUnitELTemplateTestCase.class : testClass;
        String packageName = referenceClass.getPackage().getName();

        return packageName.replaceAll("\\.", "/");
    }
}
