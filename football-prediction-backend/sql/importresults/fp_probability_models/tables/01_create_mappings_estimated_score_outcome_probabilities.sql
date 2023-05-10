DROP TABLE IF EXISTS `fp_probability_models`.`mappings_estimated_score_outcome_probabilities`;
CREATE TABLE `fp_probability_models`.`mappings_estimated_score_outcome_probabilities`
(
    `id`                   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
    `mapping_id`           varchar(36)      NOT NULL COMMENT 'ddd entity identifier',
    `model_type`           varchar(50)      NOT NULL COMMENT 'type of the probability model',
    `goals_home`           decimal(5, 2)    NOT NULL COMMENT 'home goals',
    `goals_away`           decimal(5, 2)    NOT NULL COMMENT 'away goals',
    `goals_total`          decimal(5, 2)    NOT NULL COMMENT 'home + away goals',
    `probability_home_win` decimal(14, 13)  NOT NULL COMMENT 'home win probability',
    `probability_draw`     decimal(14, 13)  NOT NULL COMMENT 'draw probability',
    `probability_away_win` decimal(14, 13)  NOT NULL COMMENT 'away win probability',
    PRIMARY KEY (`id`),
    UNIQUE KEY `Idx_Probability_Model_Mapping_EstimatedScore_Identifier` (`mapping_id`) USING BTREE,
    UNIQUE KEY `Idx_Probability_Model_Mapping_EstimatedScore_Score` (`model_type`, `goals_home`, `goals_away`) USING BTREE,
    KEY `Idx_Probability_Model_Mapping_EstimatedScore_Total_Goals` (`model_type`, `goals_total`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
