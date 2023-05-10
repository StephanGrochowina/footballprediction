DROP VIEW IF EXISTS `fp_match_results`.`view_match_results_with_aggregated_odds`;
CREATE VIEW `fp_match_results`.`view_match_results_with_aggregated_odds` AS
select `m`.`match_result_id`                                     AS `match_result_id`,
       `m`.`season_id`                                           AS `season_id`,
       `m`.`match_day`                                           AS `match_day`,
       `m`.`home_team_id`                                        AS `home_team_id`,
       `m`.`away_team_id`                                        AS `away_team_id`,
       `m`.`full_time_goals_home_team`                           AS `full_time_goals_home_team`,
       `m`.`full_time_goals_away_team`                           AS `full_time_goals_away_team`,
       `m`.`full_time_outcome`                                   AS `full_time_outcome`,
       `m`.`half_time_goals_home_team`                           AS `half_time_goals_home_team`,
       `m`.`half_time_goals_away_team`                           AS `half_time_goals_away_team`,
       `m`.`half_time_outcome`                                   AS `half_time_outcome`,
       `otg`.`aggregated_odds_threshold`                         AS `total_goals_threshold`,
       `otg`.`aggregated_odds_probability_goals_below_threshold` AS `total_goals_probability_goals_below_threshold`,
       `otg`.`aggregated_odds_probability_goals_above_threshold` AS `total_goals_probability_goals_above_threshold`,
       `otg`.`aggregated_odds_odd_goals_below_threshold`         AS `total_goals_odd_goals_below_threshold`,
       `otg`.`aggregated_odds_odd_goals_above_threshold`         AS `total_goals_odd_goals_above_threshold`,
       `oo`.`aggregated_odds_probability_home_win`               AS `outcome_probability_home_win`,
       `oo`.`aggregated_odds_probability_draw`                   AS `outcome_probability_draw`,
       `oo`.`aggregated_odds_probability_away_win`               AS `outcome_probability_away_win`,
       `oo`.`aggregated_odds_odd_home_win`                       AS `outcome_odd_home_win`,
       `oo`.`aggregated_odds_odd_draw`                           AS `outcome_odd_draw`,
       `oo`.`aggregated_odds_odd_away_win`                       AS `outcome_odd_away_win`
from `fp_match_results`.`match_results` `m`
inner join `fp_match_results`.`view_aggregated_bookmaker_odds_total_goals_per_threshold` `otg` on
    `otg`.`aggregated_odds_match_id` = `m`.`match_result_id`
inner join `fp_match_results`.`view_aggregated_bookmaker_odds_1x2` `oo` on
    `oo`.`aggregated_odds_match_id` = `m`.`match_result_id`;
