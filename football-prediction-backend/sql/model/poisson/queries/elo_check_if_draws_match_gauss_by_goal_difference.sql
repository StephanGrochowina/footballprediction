-- Check if distribution of draws matches normal distribution depending on goal difference -> true
SELECT goals_home - goals_away                                                                  as GoalsDifference,
       avg(probability_draw)                                                                    as ProbabilityDrawAvg,
       sum(probability_draw) /
       (select sum(g.probability_draw) from `fp_model_poisson`.`matching_goals_home_away` g)    as ProbabilityDrawDristibution
FROM `fp_model_poisson`.`matching_goals_home_away` m
group by
    goals_home - goals_away
order by
    goals_home - goals_away
;
