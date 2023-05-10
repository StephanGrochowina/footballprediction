-- Check if different names are used for the same team
select
    team_name,
    count(*) as Seasons
from fp_match_results.teams
group by team_name
order by team_name;
