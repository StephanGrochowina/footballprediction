-- divisions of a FIFA country
DROP TABLE IF EXISTS `fp_match_results`.`divisions`;
CREATE TABLE  `fp_match_results`.`divisions` (
    `division_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `country_id` bigint unsigned NOT NULL COMMENT 'FK to fifa_countries.country_id',
    `division_name` varchar(200) NOT NULL COMMENT 'name of the division',
    `division_level` tinyint NOT NULL,
    PRIMARY KEY (`division_id`),
    UNIQUE KEY `Index_unique_country_level` (`country_id`,`division_level`),
    CONSTRAINT `FK_divisions_fifa_countries` FOREIGN KEY (`country_id`) REFERENCES `fifa_countries` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
