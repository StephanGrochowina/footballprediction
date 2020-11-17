package de.coiaf.footballprediction.testframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSets {

    String setUpDataSet() default ProviderDatasetsAnnotationContent.DEFAULT_SETUP_DATASET;

    String assertDataSet() default "";

    Class<?> referenceClass() default AbstractJpaDbUnitELTemplateTestCase.class;
}
