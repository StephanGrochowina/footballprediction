-- FIFA countries
DROP TABLE IF EXISTS `football_prediction`.`fifa_countries`;
CREATE TABLE  `football_prediction`.`fifa_countries` (
  `country_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `country_name` varchar(200) NOT NULL COMMENT 'FIFA country name',
  `country_code` char(3) NOT NULL COMMENT 'FIFA country code',
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- divisions of a FIFA country
DROP TABLE IF EXISTS `football_prediction`.`divisions`;
CREATE TABLE  `football_prediction`.`divisions` (
  `division_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `country_id` int(10) unsigned NOT NULL COMMENT 'FK to fifa_countries.country_id',
  `division_name` varchar(200) NOT NULL COMMENT 'name of the division',
  `division_level` tinyint(3) NOT NULL,
  PRIMARY KEY (`division_id`),
  UNIQUE KEY `Index_unique_country_level` (`country_id`,`division_level`),
  CONSTRAINT `FK_divisions_fifa_countries` FOREIGN KEY (`country_id`) REFERENCES `fifa_countries` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- seasons of a division
DROP TABLE IF EXISTS `football_prediction`.`seasons`;
CREATE TABLE  `football_prediction`.`seasons` (
  `season_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `division_id` int(10) unsigned NOT NULL COMMENT 'reference to divisions.division_id',
  `description` varchar(200) NOT NULL COMMENT 'description of the season',
  `version` int(10) unsigned NOT NULL,
  PRIMARY KEY (`season_id`),
  KEY `FK_seasons_division` (`division_id`),
  CONSTRAINT `FK_seasons_division` FOREIGN KEY (`division_id`) REFERENCES `divisions` (`division_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- teams playing in one season of a division
DROP TABLE IF EXISTS `football_prediction`.`teams`;
CREATE TABLE  `football_prediction`.`teams` (
  `team_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `season_id` int(10) unsigned NOT NULL COMMENT 'reference to seasons.season_id',
  `team_name` varchar(200) NOT NULL COMMENT 'team name',
  `version` int(10) unsigned NOT NULL,
  PRIMARY KEY (`team_id`),
  KEY `FK_teams_seasons` (`season_id`),
  CONSTRAINT `FK_teams_seasons` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`season_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- results of a particular season
DROP TABLE IF EXISTS `football_prediction`.`match_results`;
CREATE TABLE  `football_prediction`.`match_results` (
  `match_result_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `season_id` int(10) unsigned NOT NULL COMMENT 'reference to seasons.season_id',
  `match_day` date NOT NULL COMMENT 'day of the match',
  `home_team_id` int(10) unsigned NOT NULL COMMENT 'reference to teams.team_id',
  `away_team_id` int(10) unsigned NOT NULL COMMENT 'reference to teams.team_id',
  `full_time_goals_home_team` tinyint(4) NOT NULL,
  `full_time_goals_away_team` tinyint(4) NOT NULL,
  `full_time_outcome` varchar(50) NOT NULL,
  `half_time_goals_home_team` tinyint(4) DEFAULT NULL,
  `half_time_goals_away_team` tinyint(4) DEFAULT NULL,
  `half_time_outcome` varchar(50) DEFAULT NULL,
  `version` int(10) unsigned NOT NULL,
  `home_team_shots` int(11) DEFAULT NULL COMMENT 'shots by home team',
  `away_team_shots` int(11) DEFAULT NULL COMMENT 'shots by away team',
  `home_team_shots_on_target` int(11) DEFAULT NULL COMMENT 'shots on target by home team',
  `away_team_shots_on_target` int(11) DEFAULT NULL COMMENT 'shots on target by away team',
  `home_team_corners` int(11) DEFAULT NULL COMMENT 'corners for home team',
  `away_team_corners` int(11) DEFAULT NULL COMMENT 'corners for away team',
  `home_team_fouls_committed` int(11) DEFAULT NULL COMMENT 'fouls committed by home team',
  `away_team_fouls_committed` int(11) DEFAULT NULL COMMENT 'fouls committed by away team',
  `home_team_free_kicks_conceded` int(11) DEFAULT NULL COMMENT 'free kicks conceded by home team',
  `away_team_free_kicks_conceded` int(11) DEFAULT NULL COMMENT 'free kicks conceded by away team',
  `home_team_offsides` int(11) DEFAULT NULL COMMENT 'offsides by home team',
  `away_team_offsides` int(11) DEFAULT NULL COMMENT 'offsides by away team',
  `home_team_yellow_cards` int(11) DEFAULT NULL COMMENT 'yellow cards by home team',
  `away_team_yellow_cards` int(11) DEFAULT NULL COMMENT 'yellow cards by away team',
  `home_team_red_cards` int(11) DEFAULT NULL COMMENT 'red cards by home team',
  `away_team_red_cards` int(11) DEFAULT NULL COMMENT 'red cards by away team',
  PRIMARY KEY (`match_result_id`),
  KEY `FK_match_results_season` (`season_id`),
  KEY `FK_match_results_home_team` (`home_team_id`),
  KEY `FK_match_results_away_team` (`away_team_id`),
  CONSTRAINT `FK_match_results_away_team` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `FK_match_results_home_team` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `FK_match_results_season` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`season_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- odds on outcomes for a particular match
DROP TABLE IF EXISTS `football_prediction`.`bookmaker_odds_1x2`;
CREATE TABLE  `football_prediction`.`bookmaker_odds_1x2` (
  `odd_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `match_result_id` int(10) unsigned NOT NULL COMMENT 'reference to match_results.match_result_id',
  `bookmaker_name` varchar(100) NOT NULL,
  `odds_home_win` decimal(13,2) NOT NULL COMMENT 'odds for home win',
  `odds_draw` decimal(13,2) NOT NULL COMMENT 'odds for draw',
  `odds_away_win` decimal(13,2) NOT NULL COMMENT 'odds for away win',
  `probability_implied_home_win` decimal(14,13) NOT NULL COMMENT 'probability of home win derived from odd',
  `probability_implied_draw` decimal(14,13) NOT NULL COMMENT 'probability of a draw derived from odd',
  `probability_implied_away_win` decimal(14,13) NOT NULL COMMENT 'probability of an away win derived from odd',
  `margin` decimal(4,3) NOT NULL COMMENT 'bookmaker''s margin',
  `probability_normalized_home_win` decimal(14,13) NOT NULL COMMENT 'probability of home win after normalization',
  `probability_normalized_draw` decimal(14,13) NOT NULL COMMENT 'probability of away win after normalization',
  `probability_normalized_away_win` decimal(14,13) NOT NULL COMMENT 'probability of draw after normalization',
  `version` int(10) unsigned NOT NULL,
  `odd_normalized_home_win` decimal(13,2) NOT NULL COMMENT 'odd of home win after normalization',
  `odd_normalized_draw` decimal(13,2) NOT NULL COMMENT 'odd of draw after normalization',
  `odd_normalized_away_win` decimal(13,2) NOT NULL COMMENT 'odd of away win after normalization',
  PRIMARY KEY (`odd_id`),
  KEY `FK_bookmaker_odds_1x2_match_results` (`match_result_id`),
  KEY `Index_normalized_bookmaker_odds_1x2` (`odd_normalized_home_win`,`odd_normalized_draw`,`odd_normalized_away_win`),
  CONSTRAINT `FK_bookmaker_odds_1x2_match_results` FOREIGN KEY (`match_result_id`) REFERENCES `match_results` (`match_result_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- odds on under/over bets
DROP TABLE IF EXISTS `football_prediction`.`bookmaker_odds_total_goals`;
CREATE TABLE  `football_prediction`.`bookmaker_odds_total_goals` (
  `odd_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `match_result_id` int(10) unsigned NOT NULL COMMENT 'refernece to match_results.match_result_id',
  `odds_goals_below_threshold` decimal(13,2) NOT NULL COMMENT 'odds that less than threshold goals are scored',
  `odds_goals_above_threshold` decimal(13,2) NOT NULL COMMENT 'odds that more than threshold goals are scored',
  `probability_implied_goals_below_threshold` decimal(14,13) NOT NULL COMMENT 'probability that less than threshold goals are scored derived from odds',
  `probability_implied_goals_above_threshold` decimal(14,13) NOT NULL COMMENT 'probability that more than threshold goals are scored derived from odds',
  `margin` decimal(4,3) NOT NULL COMMENT 'margin for bookmaker',
  `threshold` decimal(4,1) NOT NULL COMMENT 'total goals threshold',
  `probability_normalized_goals_below_threshold` decimal(14,13) NOT NULL COMMENT 'probability that less than threshold goals are scored after normalization',
  `probability_normalized_goals_above_threshold` decimal(14,13) NOT NULL COMMENT 'probability that more than threshold goals are scored after normalization',
  `version` int(10) unsigned NOT NULL,
  `bookmaker_name` varchar(100) NOT NULL COMMENT 'name of the bookmaker',
  `odd_normalized_goals_below_threshold` decimal(13,2) NOT NULL COMMENT 'odd for less goals than threshold',
  `odd_normalized_goals_above_threshold` decimal(13,2) NOT NULL COMMENT 'odd for less goals than threshold',
  PRIMARY KEY (`odd_id`),
  KEY `FK_bm_odds_total_goals_match_result` (`match_result_id`),
  KEY `Index_normalized_bm_odds_total_goals` (`odd_normalized_goals_below_threshold`,`odd_normalized_goals_above_threshold`),
  CONSTRAINT `FK_bm_odds_total_goals_match_result` FOREIGN KEY (`match_result_id`) REFERENCES `match_results` (`match_result_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
