DROP VIEW IF EXISTS `fp_match_results`.`view_history_match_results`;
CREATE VIEW `fp_match_results`.`view_history_match_results` AS
(select `m`.`home_team_id`              AS `history_team_id`,
        `m`.`match_day`                 AS `history_match_day`,
        'HOME'                          AS `history_location`,
        `m`.`full_time_goals_home_team` AS `history_full_time_goals_scored`,
        `m`.`full_time_goals_away_team` AS `history_full_time_goals_conceded`,
        (case
             when (`m`.`full_time_goals_home_team` > `m`.`full_time_goals_away_team`) then 'WIN'
             when (`m`.`full_time_goals_home_team` < `m`.`full_time_goals_away_team`) then 'LOSS'
             else 'DRAW' end)           AS `history_full_time_outcome`,
        `m`.`half_time_goals_home_team` AS `history_half_time_goals_scored`,
        `m`.`half_time_goals_away_team` AS `history_half_time_goals_conceded`,
        (case
             when (`m`.`half_time_goals_home_team` > `m`.`half_time_goals_away_team`) then 'WIN'
             when (`m`.`half_time_goals_home_team` < `m`.`half_time_goals_away_team`) then 'LOSS'
             else 'DRAW' end)           AS `history_half_time_outcome`
 from `fp_match_results`.`match_results` `m`)
union all
(select `m`.`away_team_id`              AS `history_team_id`,
        `m`.`match_day`                 AS `history_match_day`,
        'AWAY'                          AS `history_location`,
        `m`.`full_time_goals_away_team` AS `history_full_time_goals_scored`,
        `m`.`full_time_goals_home_team` AS `history_full_time_goals_conceded`,
        (case
             when (`m`.`full_time_goals_away_team` > `m`.`full_time_goals_home_team`) then 'WIN'
             when (`m`.`full_time_goals_away_team` < `m`.`full_time_goals_home_team`) then 'LOSS'
             else 'DRAW' end)           AS `history_full_time_outcome`,
        `m`.`half_time_goals_away_team` AS `history_half_time_goals_scored`,
        `m`.`half_time_goals_home_team` AS `history_half_time_goals_conceded`,
        (case
             when (`m`.`half_time_goals_away_team` > `m`.`half_time_goals_home_team`) then 'WIN'
             when (`m`.`half_time_goals_away_team` < `m`.`half_time_goals_home_team`) then 'LOSS'
             else 'DRAW' end)           AS `history_half_time_outcome`
 from `fp_match_results`.`match_results` `m`);