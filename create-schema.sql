use mysql;
create schema if not exists locations default character set utf8 collate utf8_hungarian_ci;

create user 'locations'@'%' identified by 'locations';
grant all on *.* to 'locations'@'%';

use locations;
