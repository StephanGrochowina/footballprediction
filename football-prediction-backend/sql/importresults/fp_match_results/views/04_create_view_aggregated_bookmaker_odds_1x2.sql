DROP VIEW IF EXISTS `fp_match_results`.`view_aggregated_bookmaker_odds_1x2`;
CREATE VIEW `fp_match_results`.`view_aggregated_bookmaker_odds_1x2` AS
select `odds`.`match_result_id`                                             AS `aggregated_odds_match_id`,
       (sum(`odds`.`probability_normalized_home_win`) / count(0))           AS `aggregated_odds_probability_home_win`,
       (sum(`odds`.`probability_normalized_draw`) / count(0))               AS `aggregated_odds_probability_draw`,
       (sum(`odds`.`probability_normalized_away_win`) / count(0))           AS `aggregated_odds_probability_away_win`,
       round((count(0) / sum(`odds`.`probability_normalized_home_win`)), 2) AS `aggregated_odds_odd_home_win`,
       round((count(0) / sum(`odds`.`probability_normalized_draw`)), 2)     AS `aggregated_odds_odd_draw`,
       round((count(0) / sum(`odds`.`probability_normalized_away_win`)), 2) AS `aggregated_odds_odd_away_win`
from `fp_match_results`.`bookmaker_odds_1x2` `odds`
group by `odds`.`match_result_id`;
