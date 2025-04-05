DROP USER IF EXISTS 'jobportal'@'%';
-- Creating user in localhost by password "jobportal"
CREATE USER 'jobportal'@'localhost' IDENTIFIED BY 'jobportal';

GRANT ALL PRIVILEGES ON * . * TO 'jobportal'@'localhost';