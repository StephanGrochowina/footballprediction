DROP VIEW IF EXISTS `fp_match_results`.`view_aggregated_bookmaker_odds_total_goals_per_threshold`;
CREATE VIEW `fp_match_results`.`view_aggregated_bookmaker_odds_total_goals_per_threshold` AS
select `odds`.`match_result_id`                                                          AS `aggregated_odds_match_id`,
       `odds`.`threshold`                                                                AS `aggregated_odds_threshold`,
       (sum(`odds`.`probability_normalized_goals_below_threshold`) / count(0))           AS `aggregated_odds_probability_goals_below_threshold`,
       (sum(`odds`.`probability_normalized_goals_above_threshold`) / count(0))           AS `aggregated_odds_probability_goals_above_threshold`,
       round((count(0) / sum(`odds`.`probability_normalized_goals_below_threshold`)), 2) AS `aggregated_odds_odd_goals_below_threshold`,
       round((count(0) / sum(`odds`.`probability_normalized_goals_above_threshold`)), 2) AS `aggregated_odds_odd_goals_above_threshold`
from `fp_match_results`.`bookmaker_odds_total_goals` `odds`
group by
         `odds`.`match_result_id`,
         `odds`.`threshold`;