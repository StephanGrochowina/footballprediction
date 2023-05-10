DROP VIEW IF EXISTS `fp_model_poisson`.`view_probability_goal_matching`;
CREATE VIEW `fp_model_poisson`.`view_probability_goal_matching` AS
select `m1x2`.`matching_home_away_id`             AS `matching_id`,
       `m1x2`.`goals_home`                        AS `matching_goals_home`,
       `m1x2`.`goals_away`                        AS `matching_goals_away`,
       `m1x2`.`total_goals`                       AS `matching_goals_total`,
       `mtg`.`threshold`                          AS `matching_threshold_goals`,
       `mtg`.`probability_less_than_threshold`    AS `matching_probability_less_goals_than_threshold`,
       `mtg`.`probability_greater_than_threshold` AS `matching_probability_more_goals_than_threshold`,
       `mtg`.`odd_less_than_threshold`            AS `matching_odd_less_goals_than_threshold`,
       `mtg`.`odd_greater_than_threshold`         AS `matching_odd_more_goals_than_threshold`,
       `m1x2`.`probability_draw`                  AS `matching_probability_draw`,
       `m1x2`.`probability_home_win`              AS `matching_probability_home_win`,
       `m1x2`.`probability_away_win`              AS `matching_probability_away_win`,
       `m1x2`.`odd_draw`                          AS `matching_odd_draw`,
       `m1x2`.`odd_home_win`                      AS `matching_odd_home_win`,
       `m1x2`.`odd_away_win`                      AS `matching_odd_away_win`
from `fp_model_poisson`.`matching_total_goals_p_threshold` `mtg`
inner join `fp_model_poisson`.`matching_goals_home_away` `m1x2` on
    `m1x2`.`total_goals` = `mtg`.`expected_total_goals`;
