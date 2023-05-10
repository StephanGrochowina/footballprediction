DROP VIEW IF EXISTS `fp_match_results`.`view_history_per_match_and_team`;
CREATE VIEW `fp_match_results`.`view_history_per_match_and_team` AS
(select `m`.`match_result_id`                    AS `match_history_match_id`,
        `vhm`.`history_team_id`                  AS `match_history_team_id`,
        `vhm`.`history_match_day`                AS `match_history_match_day`,
        `vhm`.`history_location`                 AS `match_history_location`,
        `vhm`.`history_full_time_goals_scored`   AS `match_history_full_time_goals_scored`,
        `vhm`.`history_full_time_goals_conceded` AS `match_history_full_time_goals_conceded`,
        `vhm`.`history_full_time_outcome`        AS `match_history_full_time_outcome`,
        `vhm`.`history_half_time_goals_scored`   AS `match_history_half_time_goals_scored`,
        `vhm`.`history_half_time_goals_conceded` AS `match_history_half_time_goals_conceded`,
        `vhm`.`history_half_time_outcome`        AS `match_history_half_time_outcome`
 from `fp_match_results`.`match_results` `m`
 inner join `fp_match_results`.`view_history_match_results` `vhm` on `m`.`home_team_id` = `vhm`.`history_team_id`
 where `m`.`match_day` > `vhm`.`history_match_day`)
union all
(select `m`.`match_result_id`                    AS `match_history_match_id`,
        `vhm`.`history_team_id`                  AS `match_history_team_id`,
        `vhm`.`history_match_day`                AS `match_history_match_day`,
        `vhm`.`history_location`                 AS `match_history_location`,
        `vhm`.`history_full_time_goals_scored`   AS `match_history_full_time_goals_scored`,
        `vhm`.`history_full_time_goals_conceded` AS `match_history_full_time_goals_conceded`,
        `vhm`.`history_full_time_outcome`        AS `match_history_full_time_outcome`,
        `vhm`.`history_half_time_goals_scored`   AS `match_history_half_time_goals_scored`,
        `vhm`.`history_half_time_goals_conceded` AS `match_history_half_time_goals_conceded`,
        `vhm`.`history_half_time_outcome`        AS `match_history_half_time_outcome`
 from `fp_match_results`.`match_results` `m`
 inner join `fp_match_results`.`view_history_match_results` `vhm` on `m`.`away_team_id` = `vhm`.`history_team_id`
 where `m`.`match_day` > `vhm`.`history_match_day`);