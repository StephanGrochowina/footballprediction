-- Odds: (1.92, 2.08)
SELECT
  *,
  sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2)) as proximity
FROM football_prediction.matching_total_goals_p_threshold m
where
  odd_less_than_threshold between 1.87 and 1.97
  and odd_greater_than_threshold between 2.03 and 2.13
order by
  sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2))
limit 1;
;

-- Determine estimated total goals by odd group and threshold
SELECT
  round(min(expected_total_goals), 2) as minimum,
  avg(expected_total_goals) as averageUnrounded,
  round(avg(expected_total_goals), 2) as average,
  round(max(expected_total_goals), 2) as maximum,
  count(*) as size,
  round(sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2)), 4) as proximity
FROM football_prediction.matching_total_goals_p_threshold m
where
  threshold = 2.5
--  and odd_less_than_threshold between 1.87 and 1.97
--  and odd_greater_than_threshold between 2.03 and 2.13
group by
  sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2))
-- having size > 1
order by
  sqrt(power(odd_less_than_threshold - 1.92, 2) + power(odd_greater_than_threshold - 2.08, 2))
-- limit 1
;
