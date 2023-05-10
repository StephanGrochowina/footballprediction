-- Odds: (1.92, 2.08)
SELECT matching_total_goals_id,
       threshold,
       expected_total_goals,
       probability_less_than_threshold,
       probability_greater_than_threshold,
       odd_less_than_threshold,
       odd_greater_than_threshold,
       sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2)) as proximity
FROM `fp_model_poisson`.`matching_total_goals_p_threshold` m
where
    odd_less_than_threshold between 1.87 and 1.97
    and odd_greater_than_threshold between 2.03 and 2.13
order by
    sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2))
limit 1
;

-- Determine estimated total goals by odd group and threshold
SELECT
    round(min(expected_total_goals), 2) 	                                                             as minimum,
    avg(expected_total_goals)				                                                             as averageUnrounded,
    round(avg(expected_total_goals), 2)		                                                             as average,
    round(max(expected_total_goals), 2)		                                                             as maximum,
    count(*)								                                                             as size,
    round(avg(probability_less_than_threshold), 13)	                                                     as probability_less_than_threshold,
    round(avg(probability_greater_than_threshold), 13)	                                                 as probability_greater_than_threshold,
    round(avg(probability_less_than_threshold), 13) + round(avg(probability_greater_than_threshold), 13) as checksum,
    proximity								                                                             as proximity,
    round(exp(proximity * count(*) * -2.564664719) , 13)                                                 as confidentiality
FROM (
         SELECT
             expected_total_goals,
             probability_less_than_threshold,
             probability_greater_than_threshold,
             round(sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2)), 4) as proximity
         FROM `fp_model_poisson`.`matching_total_goals_p_threshold` m
         WHERE
                 threshold = 2.5
         -- and odd_less_than_threshold between 1.87 and 1.97
         -- and odd_greater_than_threshold between 2.03 and 2.13
     ) AS PreSelectedTotalGoals
GROUP BY
    proximity
-- having size > 1
ORDER BY
    proximity
LIMIT 1
;
