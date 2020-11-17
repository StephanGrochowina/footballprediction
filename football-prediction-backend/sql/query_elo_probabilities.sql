-- Check if Pwin(elo) and Ploss(elo) correlate to poisson -> not clearly 1:1
SELECT
min(goals_home) as HomeGoalsMin,
avg(goals_home) as HomeGoalsAvg,
max(goals_home) as HomeGoalsMax,
std(goals_home) as HomeGoalsStandardDev,
min(goals_away) as AwayGoalsMin,
avg(goals_away) as AwayGoalsAvg,
max(goals_away) as AwayGoalsMax,
std(goals_away) as AwayGoalsStandardDev,
count(*) as Size,
probability_home_win + 0.5 * probability_draw as ProbabilityEloHomeWin,
probability_away_win + 0.5 * probability_draw as ProbabilityEloAwayWin,
max(goals_home) - min(goals_home) as HomeGoalsRange,
max(goals_away) - min(goals_away) as AwayGoalsRange
FROM matching_goals_home_away m
where goals_home between 0.1 and 8 and goals_away between 0.1 and 8
group by
probability_home_win + 0.5 * probability_draw,
probability_away_win + 0.5 * probability_draw
having
count(*) > 1
order by
max(goals_home) - min(goals_home) + max(goals_away) - min(goals_away)
;

-- Check if distribution of draws matches normal distribution debending on goal difference -> true
SELECT
goals_home - goals_away as GoalsDifference,
avg(probability_draw) as ProbabilityDrawAvg,
sum(probability_draw)/(select sum(g.probability_draw) from matching_goals_home_away g) as ProbabilityDrawDristibution
FROM matching_goals_home_away m
group by
goals_home - goals_away
order by
goals_home - goals_away
;

