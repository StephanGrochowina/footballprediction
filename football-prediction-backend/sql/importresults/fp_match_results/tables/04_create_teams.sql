-- teams playing in one season of a division
DROP TABLE IF EXISTS `fp_match_results`.`teams`;
CREATE TABLE `fp_match_results`.`teams`
(
    `team_id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `season_id` bigint unsigned NOT NULL COMMENT 'reference to seasons.season_id',
    `team_name` varchar(200)     NOT NULL COMMENT 'team name',
    `version`   bigint unsigned NOT NULL,
    PRIMARY KEY (`team_id`),
    KEY `FK_teams_seasons` (`season_id`),
    CONSTRAINT `FK_teams_seasons` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`season_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
