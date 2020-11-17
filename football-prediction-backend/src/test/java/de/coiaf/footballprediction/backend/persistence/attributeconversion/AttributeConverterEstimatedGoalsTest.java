package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedGoals;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AttributeConverterEstimatedGoalsTest {

    private static final BigDecimal GIVEN_STATISTICAL_GOALS_VALUE = new BigDecimal("1.23");
    private static final EstimatedGoals GIVEN_STATISTICAL_GOALS = EstimatedGoals.valueOf(GIVEN_STATISTICAL_GOALS_VALUE);

    private AttributeConverterEstimatedGoals attributeConverter = new AttributeConverterEstimatedGoals();

    @Test
    public void convertToDatabaseColumn_nullGoals() {
        BigDecimal result = this.attributeConverter.convertToDatabaseColumn(null);

        assertNull(result);
    }
    @Test
    public void convertToDatabaseColumn_givenThreshold() {
        EstimatedGoals goals = GIVEN_STATISTICAL_GOALS;
        BigDecimal expected = GIVEN_STATISTICAL_GOALS_VALUE;
        BigDecimal result = this.attributeConverter.convertToDatabaseColumn(goals);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void convertToEntityAttribute_nullThresholdValue() {
        EstimatedGoals result = this.attributeConverter.convertToEntityAttribute(null);

        assertNull(result);
    }
    @Test
    public void convertToEntityAttribute_negativeThresholdValue() {
        BigDecimal thresholdValue = new BigDecimal("-0.5");
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;
        EstimatedGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void convertToEntityAttribute_zeroThresholdValue() {
        BigDecimal thresholdValue = BigDecimal.ZERO;
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;
        EstimatedGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void convertToEntityAttribute_positiveThresholdValue() {
        BigDecimal thresholdValue = GIVEN_STATISTICAL_GOALS_VALUE;
        EstimatedGoals expected = GIVEN_STATISTICAL_GOALS;
        EstimatedGoals minThreshold = EstimatedGoals.MIN_ESTIMATED_GOALS;
        EstimatedGoals result = this.attributeConverter.convertToEntityAttribute(thresholdValue);

        assertNotNull(result);
        assertTrue(minThreshold.compareTo(result) < 0);
        assertEquals(expected, result);
    }
}