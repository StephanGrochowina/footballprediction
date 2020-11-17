package de.coiaf.footballprediction.testframework;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import java.io.*;
import java.util.Objects;

class ReaderXmlDatabase {

    IDataSet createReplacedDataSet(Class<?> referenceClass, String resourcePath) throws DataSetException {
        IDataSet originalDataSet = this.loadDataSet(referenceClass, resourcePath);
        return this.createReplacedDataSet(originalDataSet);
    }

    IDataSet createReplacedDataSet(IDataSet originalDataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(originalDataSet);
        replacementDataSet.addReplacementObject("[NULL]", null);

        return replacementDataSet;
    }

    IDataSet loadDataSet(Class<?> referenceClass, String resourcePath) throws DataSetException {
        InputStream inputStream = createInputStream(referenceClass, resourcePath);

        return this.readDataSet(inputStream);
    }

    IDataSet loadDataSet(Class<?> referenceClass, String resourcePath, CreatorDataset creatorDataSet) throws IOException, DataSetException {
        Objects.requireNonNull(creatorDataSet);

        InputStream inputStream = createInputStream(referenceClass, resourcePath);
        Reader reader = new InputStreamReader(inputStream);
        final FlatXmlDataSet dataSet = creatorDataSet.create(reader);
        final ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);

        replacementDataSet.addReplacementObject("[NULL]", null);

        return replacementDataSet;
    }

    private InputStream createInputStream(Class<?> referenceClass, String resourcePath) {
        Objects.requireNonNull(referenceClass);
        Objects.requireNonNull(resourcePath);

        InputStream inputStream = referenceClass.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("Unknown resource " + resourcePath + " for class " + referenceClass.getName());
        }

        return inputStream;
    }

    private IDataSet readDataSet(InputStream inputStream) throws DataSetException {
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
        flatXmlDataSetBuilder.setColumnSensing(true);

        return flatXmlDataSetBuilder.build(reader);
    }

    String toString(IDataSet dataSet) throws DataSetException, IOException {
        try (StringWriter writer = new StringWriter()) {
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, writer);
            } else {
                writer.write("null");
            }
            return writer.toString();
        }
    }

    @FunctionalInterface
    interface CreatorDataset {
        FlatXmlDataSet create(Reader reader) throws IOException, DataSetException;
    }
}
