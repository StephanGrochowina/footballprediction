-- seasonal stats, original query from mysql 5
-- TODO prüfen, ob season_stats.sql dieselben Ergebnisse liefert
select
    s.division_id,
    s.season_id,
    s.description as season_name,
    min(mr.match_day) as season_begin,
    max(mr.match_day) as season_end,
    year(min(mr.match_day)) as season_begin_year,
    year(max(mr.match_day)) as season_end_year,
    avg(mr.full_time_goals_home_team - mr.full_time_goals_away_team) as season_home_advantage,
    count(*) as matches,
    ifnull(sum((select 1 from fp_match_results.bookmaker_odds_1x2 o where o.match_result_id = mr.match_result_id group by o.match_result_id)), 0) as matches_with_odds_outcome,
    ifnull(sum((select 1 from fp_match_results.bookmaker_odds_total_goals o where o.match_result_id = mr.match_result_id group by o.match_result_id)), 0) as matches_with_odds_total_goals
from fp_match_results.seasons s
inner join fp_match_results.match_results mr on mr.season_id = s.season_id
group by division_id, season_id
order by division_id, season_begin
;