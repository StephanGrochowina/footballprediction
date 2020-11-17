select
  team_name,
  count(*) as Seasons
from teams
group by team_name
order by team_name;
