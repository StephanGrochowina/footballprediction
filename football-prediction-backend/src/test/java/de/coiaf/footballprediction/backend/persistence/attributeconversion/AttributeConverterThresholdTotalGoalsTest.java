package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Deprecated
public class AttributeConverterThresholdTotalGoalsTest {

    private AttributeConverterThresholdTotalGoals attributeConverter = new AttributeConverterThresholdTotalGoals();

    @Test
    public void convertToDatabaseColumn_nullThreshold() {
        BigDecimal result = this.attributeConverter.convertToDatabaseColumn(null);

        assertNull(result);
    }
    @Test
    public void convertToDatabaseColumn_givenThreshold() {
        ThresholdTotalGoals threshold = ThresholdTotalGoals.getDefaultInstance();
        BigDecimal expected = threshold.toBigDecimal();
        BigDecimal result = this.attributeConverter.convertToDatabaseColumn(threshold);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void convertToEntityAttribute_nullThresholdValue() {
        ThresholdTotalGoals result = this.attributeConverter.convertToEntityAttribute(null);

        assertNull(result);
    }
    @Test
    public void convertToEntityAttribute_negativeThresholdValue() {
        BigDecimal thresholdValue = new BigDecimal("-0.5");
        ThresholdTotalGoals expected = ThresholdTotalGoals.getMinThreshold();
        ThresholdTotalGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertSame(expected, result);
    }
    @Test
    public void convertToEntityAttribute_zeroThresholdValue() {
        BigDecimal thresholdValue = BigDecimal.ZERO;
        ThresholdTotalGoals expected = ThresholdTotalGoals.getMinThreshold();
        ThresholdTotalGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertSame(expected, result);
    }
    @Test
    public void convertToEntityAttribute_positiveThresholdValue() {
        BigDecimal thresholdValue = new BigDecimal("3.5");
        ThresholdTotalGoals minThreshold = ThresholdTotalGoals.getMinThreshold();
        ThresholdTotalGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertTrue(minThreshold.compareTo(result) < 0);
    }
}