DROP TABLE IF EXISTS `fp_model_poisson`.`matching_goals_home_away`;
CREATE TABLE `fp_model_poisson`.`matching_goals_home_away`
(
    `matching_home_away_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
    `goals_home`            decimal(5, 2)    NOT NULL COMMENT 'home goals',
    `goals_away`            decimal(5, 2)    NOT NULL COMMENT 'away goals',
    `total_goals`           decimal(5, 2)    NOT NULL COMMENT 'home + away goals',
    `probability_draw`      decimal(14, 13)  NOT NULL COMMENT 'draw probability',
    `probability_home_win`  decimal(14, 13)  NOT NULL COMMENT 'home win probability',
    `probability_away_win`  decimal(14, 13)  NOT NULL COMMENT 'away win probability',
    `odd_home_win`          decimal(13, 2)   NOT NULL COMMENT 'home win odd',
    `odd_draw`              decimal(13, 2)   NOT NULL COMMENT 'draw odd',
    `odd_away_win`          decimal(13, 2)   NOT NULL COMMENT 'away win draw',
    PRIMARY KEY (`matching_home_away_id`),
    UNIQUE KEY `Index_Score` (`goals_home`, `goals_away`) USING BTREE,
    KEY `Index_total_goals` (`total_goals`),
    KEY `Index_odds_1x2` (`odd_home_win`, `odd_draw`, `odd_away_win`)
) ENGINE = InnoDB AUTO_INCREMENT = 4002001 DEFAULT CHARSET = utf8;
