DROP TABLE IF EXISTS `fp_match_results`.`fifa_countries`;
CREATE TABLE  `fp_match_results`.`fifa_countries` (
    `country_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `country_name` varchar(200) NOT NULL COMMENT 'FIFA country name',
    `country_code` char(3) NOT NULL COMMENT 'FIFA country code',
    PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
