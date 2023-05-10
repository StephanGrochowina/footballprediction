-- add schema
CREATE DATABASE `fp_model_poisson`;

-- add roles
CREATE ROLE 'role_model_poisson_show_views', 'role_model_poisson_read', 'role_model_poisson_write';
GRANT SELECT ON fp_model_poisson.* TO 'role_model_poisson_read';
GRANT INSERT, UPDATE, DELETE ON fp_model_poisson.* TO 'role_model_poisson_write';
GRANT SHOW VIEW ON fp_model_poisson.* TO 'role_model_poisson_show_views';

--  grant roles to users
GRANT 'role_model_poisson_read' TO 'predictor'@'localhost';
-- GRANT 'role_model_poisson_write' TO 'predictor'@'localhost';
-- GRANT 'role_model_poisson_show_views' TO 'predictor'@'localhost';
