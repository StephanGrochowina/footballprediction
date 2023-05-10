-- add schema
CREATE DATABASE `fp_probability_models`;

-- add roles
CREATE ROLE 'role_prob_models_show_views', 'role_prob_models_read', 'role_prob_models_write';
GRANT SELECT ON fp_probability_models.* TO 'role_prob_models_read';
GRANT INSERT, UPDATE, DELETE ON fp_probability_models.* TO 'role_prob_models_write';
GRANT SHOW VIEW ON fp_probability_models.* TO 'role_prob_models_show_views';

--  grant roles to users
GRANT 'role_prob_models_read' TO 'probmodels'@'localhost';
GRANT 'role_prob_models_write' TO 'probmodels'@'localhost';
GRANT 'role_prob_models_show_views' TO 'probmodels'@'localhost';
