DROP TABLE IF EXISTS `football_prediction`.`matching_total_goals_p_threshold`;
CREATE TABLE  `football_prediction`.`matching_total_goals_p_threshold` (
  `matching_total_goals_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `threshold` decimal(4,1) NOT NULL COMMENT 'threshold for total match odds',
  `expected_total_goals` decimal(5,2) NOT NULL COMMENT 'mean of poisson distribution',
  `probability_less_than_threshold` decimal(14,13) NOT NULL,
  `probability_greater_than_threshold` decimal(14,13) NOT NULL,
  `odd_less_than_threshold` decimal(13,2) NOT NULL,
  `odd_greater_than_threshold` decimal(13,2) NOT NULL,
  PRIMARY KEY (`matching_total_goals_id`),
  UNIQUE KEY `Index_expected_goals` (`expected_total_goals`,`threshold`) USING BTREE,
  KEY `Index_odds_total_goals` (`odd_less_than_threshold`,`odd_greater_than_threshold`,`threshold`)
) ENGINE=InnoDB AUTO_INCREMENT=4023 DEFAULT CHARSET=utf8;