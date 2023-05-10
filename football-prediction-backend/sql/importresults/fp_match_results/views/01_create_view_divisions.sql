DROP VIEW IF EXISTS `fp_match_results`.`view_divisions`;
CREATE VIEW `fp_match_results`.`view_divisions` AS
select `d`.`division_id`    AS `division_id`,
       `c`.`country_code`   AS `country_code`,
       `c`.`country_name`   AS `country_name`,
       `d`.`division_name`  AS `division_name`,
       `d`.`division_level` AS `division_level`
from `fp_match_results`.`fifa_countries` `c`
inner join `fp_match_results`.`divisions` `d` on
    `d`.`country_id` = `c`.`country_id`;
