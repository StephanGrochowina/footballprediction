-- add schema
CREATE DATABASE `fp_match_results`;

-- add roles
CREATE ROLE 'role_match_results_show_views', 'role_match_results_read', 'role_match_results_write';
GRANT SELECT ON fp_match_results.* TO 'role_match_results_read';
GRANT INSERT, UPDATE, DELETE ON fp_match_results.* TO 'role_match_results_write';
GRANT SHOW VIEW ON fp_match_results.* TO 'role_match_results_show_views';

--  grant roles to users
GRANT 'role_match_results_read' TO 'predictor'@'localhost';
GRANT 'role_match_results_write' TO 'predictor'@'localhost';
GRANT 'role_match_results_show_views' TO 'predictor'@'localhost';

GRANT 'role_match_results_read' TO 'resultimporter'@'localhost';
GRANT 'role_match_results_write' TO 'resultimporter'@'localhost';
GRANT 'role_match_results_show_views' TO 'resultimporter'@'localhost';

