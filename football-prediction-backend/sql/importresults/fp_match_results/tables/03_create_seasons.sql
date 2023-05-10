-- seasons of a division
DROP TABLE IF EXISTS `fp_match_results`.`seasons`;
CREATE TABLE `fp_match_results`.`seasons`
(
    `season_id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `division_id` bigint unsigned NOT NULL COMMENT 'reference to divisions.division_id',
    `description` varchar(200)     NOT NULL COMMENT 'description of the season',
    `version`     bigint unsigned NOT NULL,
    PRIMARY KEY (`season_id`),
    KEY `FK_seasons_division` (`division_id`),
    CONSTRAINT `FK_seasons_division` FOREIGN KEY (`division_id`) REFERENCES `divisions` (`division_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
