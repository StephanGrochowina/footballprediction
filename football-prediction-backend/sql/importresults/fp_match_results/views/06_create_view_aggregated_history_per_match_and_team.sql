DROP VIEW IF EXISTS `fp_match_results`.`view_aggregated_history_per_match_and_team`;
CREATE VIEW `fp_match_results`.`view_aggregated_history_per_match_and_team` AS
(select `vhmt`.`match_history_match_id`                                                        AS `aggregated_history_match_id`,
        `vhmt`.`match_history_team_id`                                                         AS `aggregated_history_team_id`,
        `vhmt`.`match_history_location`                                                        AS `aggregated_history_location`,
        count(0)                                                                               AS `aggregated_history_matches_played`,
        avg(`vhmt`.`match_history_full_time_goals_scored`)                                     AS `aggregated_history_avg_full_time_goals_scored`,
        std(`vhmt`.`match_history_full_time_goals_scored`)                                     AS `aggregated_history_stddev_full_time_goals_scored`,
        avg(`vhmt`.`match_history_full_time_goals_conceded`)                                   AS `aggregated_history_avg_full_time_goals_conceded`,
        std(`vhmt`.`match_history_full_time_goals_conceded`)                                   AS `aggregated_history_stddev_full_time_goals_conceded`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'WIN') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_win`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'DRAW') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_draw`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'LOSS') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_loss`,
        avg(`vhmt`.`match_history_half_time_goals_scored`)                                     AS `aggregated_history_avg_half_time_goals_scored`,
        std(`vhmt`.`match_history_half_time_goals_scored`)                                     AS `aggregated_history_stddev_half_time_goals_scored`,
        avg(`vhmt`.`match_history_half_time_goals_conceded`)                                   AS `aggregated_history_avg_half_time_goals_conceded`,
        std(`vhmt`.`match_history_half_time_goals_conceded`)                                   AS `aggregated_history_stddev_half_time_goals_conceded`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'WIN') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_win`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'DRAW') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_draw`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'LOSS') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_loss`
 from `fp_match_results`.`view_history_per_match_and_team` `vhmt`
 group by
          `vhmt`.`match_history_match_id`,
          `vhmt`.`match_history_team_id`,
          `vhmt`.`match_history_location`
)
union all
(select `vhmt`.`match_history_match_id`                                                        AS `aggregated_history_match_id`,
        `vhmt`.`match_history_team_id`                                                         AS `aggregated_history_team_id`,
        NULL                                                                                   AS `aggregated_history_location`,
        count(0)                                                                               AS `aggregated_history_matches_played`,
        avg(`vhmt`.`match_history_full_time_goals_scored`)                                     AS `aggregated_history_avg_full_time_goals_scored`,
        std(`vhmt`.`match_history_full_time_goals_scored`)                                     AS `aggregated_history_stddev_full_time_goals_scored`,
        avg(`vhmt`.`match_history_full_time_goals_conceded`)                                   AS `aggregated_history_avg_full_time_goals_conceded`,
        std(`vhmt`.`match_history_full_time_goals_conceded`)                                   AS `aggregated_history_stddev_full_time_goals_conceded`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'WIN') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_win`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'DRAW') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_draw`,
        sum((case
                 when (`vhmt`.`match_history_full_time_outcome` = 'LOSS') then 1
                 else 0 end))                                                                  AS `aggregated_history_full_time_loss`,
        avg(`vhmt`.`match_history_half_time_goals_scored`)                                     AS `aggregated_history_avg_half_time_goals_scored`,
        std(`vhmt`.`match_history_half_time_goals_scored`)                                     AS `aggregated_history_stddev_half_time_goals_scored`,
        avg(`vhmt`.`match_history_half_time_goals_conceded`)                                   AS `aggregated_history_avg_half_time_goals_conceded`,
        std(`vhmt`.`match_history_half_time_goals_conceded`)                                   AS `aggregated_history_stddev_half_time_goals_conceded`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'WIN') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_win`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'DRAW') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_draw`,
        sum((case
                 when (`vhmt`.`match_history_half_time_outcome` = 'LOSS') then 1
                 else 0 end))                                                                  AS `aggregated_history_half_time_loss`
 from `fp_match_results`.`view_history_per_match_and_team` `vhmt`
 group by
          `vhmt`.`match_history_match_id`,
          `vhmt`.`match_history_team_id`
);