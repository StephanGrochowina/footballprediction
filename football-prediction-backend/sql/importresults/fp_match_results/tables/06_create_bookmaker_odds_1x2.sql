-- odds on outcomes for a particular match
DROP TABLE IF EXISTS `fp_match_results`.`bookmaker_odds_1x2`;
CREATE TABLE `fp_match_results`.`bookmaker_odds_1x2`
(
    `odd_id`                          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `match_result_id`                 bigint unsigned NOT NULL COMMENT 'reference to match_results.match_result_id',
    `bookmaker_name`                  varchar(100)     NOT NULL,
    `odds_home_win`                   decimal(13, 2)   NOT NULL COMMENT 'odds for home win',
    `odds_draw`                       decimal(13, 2)   NOT NULL COMMENT 'odds for draw',
    `odds_away_win`                   decimal(13, 2)   NOT NULL COMMENT 'odds for away win',
    `probability_implied_home_win`    decimal(14, 13)  NOT NULL COMMENT 'probability of home win derived from odd',
    `probability_implied_draw`        decimal(14, 13)  NOT NULL COMMENT 'probability of a draw derived from odd',
    `probability_implied_away_win`    decimal(14, 13)  NOT NULL COMMENT 'probability of an away win derived from odd',
    `margin`                          decimal(4, 3)    NOT NULL COMMENT 'bookmaker''s margin',
    `probability_normalized_home_win` decimal(14, 13)  NOT NULL COMMENT 'probability of home win after normalization',
    `probability_normalized_draw`     decimal(14, 13)  NOT NULL COMMENT 'probability of away win after normalization',
    `probability_normalized_away_win` decimal(14, 13)  NOT NULL COMMENT 'probability of draw after normalization',
    `version`                         bigint unsigned NOT NULL,
    `odd_normalized_home_win`         decimal(13, 2)   NOT NULL COMMENT 'odd of home win after normalization',
    `odd_normalized_draw`             decimal(13, 2)   NOT NULL COMMENT 'odd of draw after normalization',
    `odd_normalized_away_win`         decimal(13, 2)   NOT NULL COMMENT 'odd of away win after normalization',
    PRIMARY KEY (`odd_id`),
    KEY `FK_bookmaker_odds_1x2_match_results` (`match_result_id`),
    KEY `Index_normalized_bookmaker_odds_1x2` (`odd_normalized_home_win`, `odd_normalized_draw`,
                                               `odd_normalized_away_win`),
    CONSTRAINT `FK_bookmaker_odds_1x2_match_results` FOREIGN KEY (`match_result_id`) REFERENCES `match_results` (`match_result_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
