-- Odds: (2.60, 3.50, 3.03)
SELECT
  *,
  sqrt(power(odd_home_win - 2.60, 2) + power(odd_draw - 3.50, 2) + power(odd_away_win - 3.03, 2)) as proximity
FROM football_prediction.matching_goals_home_away m
where
  odd_home_win between 2.58 and 2.62
  and odd_draw between 3.48 and 3.52
  and odd_away_win between 3.01 and 3.05
order by
  sqrt(power(odd_home_win - 2.60, 2) + power(odd_draw - 3.50, 2) + power(odd_away_win - 3.03, 2))
limit 1
;

-- Determine estimated score by odd group and threshold
SELECT
  round(min(goals_home), 2) as minimumHomeGoals,
  avg(goals_home) as averageUnroundedHomeGoals,
  round(avg(goals_home), 2) as averageHomeGoals,
  round(max(goals_home), 2) as maximumHomeGoals,
  round(min(goals_away), 2) as minimumAwayGoals,
  avg(goals_away) as averageUnroundedAwayGoals,
  round(avg(goals_away), 2) as averageAwayGoals,
  round(max(goals_away), 2) as maximumAwayGoals,
  round(avg(goals_home), 2) + round(avg(goals_away), 2) as averageTotalGoals,
  count(*) as size,
  round(sqrt(power(odd_home_win - 2.60, 2) + power(odd_draw - 3.50, 2) + power(odd_away_win - 3.03, 2)), 4) as proximity
FROM football_prediction.matching_goals_home_away m
where
  odd_home_win between 2.58 and 2.62
  and odd_draw between 3.48 and 3.52
  and odd_away_win between 3.01 and 3.05
group by
  sqrt(power(odd_home_win - 2.60, 2) + power(odd_draw - 3.50, 2) + power(odd_away_win - 3.03, 2))
order by
  sqrt(power(odd_home_win - 2.60, 2) + power(odd_draw - 3.50, 2) + power(odd_away_win - 3.03, 2))
limit 1
;
