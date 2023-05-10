-- odds on under/over bets
DROP TABLE IF EXISTS `fp_match_results`.`bookmaker_odds_total_goals`;
CREATE TABLE `fp_match_results`.`bookmaker_odds_total_goals`
(
    `odd_id`                                       bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
    `match_result_id`                              bigint unsigned NOT NULL COMMENT 'reference to match_results.match_result_id',
    `odds_goals_below_threshold`                   decimal(13, 2)   NOT NULL COMMENT 'odds that less than threshold goals are scored',
    `odds_goals_above_threshold`                   decimal(13, 2)   NOT NULL COMMENT 'odds that more than threshold goals are scored',
    `probability_implied_goals_below_threshold`    decimal(14, 13)  NOT NULL COMMENT 'probability that less than threshold goals are scored derived from odds',
    `probability_implied_goals_above_threshold`    decimal(14, 13)  NOT NULL COMMENT 'probability that more than threshold goals are scored derived from odds',
    `margin`                                       decimal(4, 3)    NOT NULL COMMENT 'margin for bookmaker',
    `threshold`                                    decimal(4, 1)    NOT NULL COMMENT 'total goals threshold',
    `probability_normalized_goals_below_threshold` decimal(14, 13)  NOT NULL COMMENT 'probability that less than threshold goals are scored after normalization',
    `probability_normalized_goals_above_threshold` decimal(14, 13)  NOT NULL COMMENT 'probability that more than threshold goals are scored after normalization',
    `version`                                      bigint unsigned NOT NULL,
    `bookmaker_name`                               varchar(100)     NOT NULL COMMENT 'name of the bookmaker',
    `odd_normalized_goals_below_threshold`         decimal(13, 2)   NOT NULL COMMENT 'odd for less goals than threshold',
    `odd_normalized_goals_above_threshold`         decimal(13, 2)   NOT NULL COMMENT 'odd for less goals than threshold',
    PRIMARY KEY (`odd_id`),
    KEY `FK_bm_odds_total_goals_match_result` (`match_result_id`),
    KEY `Index_normalized_bm_odds_total_goals` (`odd_normalized_goals_below_threshold`,
                                                `odd_normalized_goals_above_threshold`),
    CONSTRAINT `FK_bm_odds_total_goals_match_result` FOREIGN KEY (`match_result_id`) REFERENCES `match_results` (`match_result_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
