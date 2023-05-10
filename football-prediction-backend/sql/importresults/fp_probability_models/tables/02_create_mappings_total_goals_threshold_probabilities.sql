DROP TABLE IF EXISTS `fp_probability_models`.`mappings_total_goals_threshold_probabilities`;
CREATE TABLE `fp_probability_models`.`mappings_total_goals_threshold_probabilities`
(
    `id`                                 bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
    `mapping_id`                         varchar(36)      NOT NULL COMMENT 'ddd entity identifier',
    `model_type`                         varchar(50)      NOT NULL COMMENT 'type of the probability model',
    `expected_total_goals`               decimal(5, 2)    NOT NULL COMMENT 'expected total goals',
    `threshold`                          decimal(4, 1)    NOT NULL COMMENT 'threshold for below/above probabilities',
    `probability_less_than_threshold`    decimal(14, 13)  NOT NULL,
    `probability_greater_than_threshold` decimal(14, 13)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `Idx_Probability_Model_Mapping_TotalGoals_Identifier` (`mapping_id`) USING BTREE,
    UNIQUE KEY `Idx_Probability_Model_Mapping_TotalGoals_Total_Goals` (`model_type`, `expected_total_goals`, `threshold`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;