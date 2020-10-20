create table location(id int auto_increment primary key, name varchar(255), lat double, lon double, interesting_at datetime);

create table tag(id int auto_increment primary key, location_id int, name varchar(255), foreign key (location_id) references location(id));

insert into location(id, name, lat, lon, interesting_at) values (1, 'Budapest', 47.497912, 19.040235, '2019-09-17 05:00:00');
insert into tag(id, location_id, name) values (1, 1, 'capital');
insert into tag(id, location_id, name) values (2, 1, 'favourite');

insert into location(id, name, lat, lon) values (2, 'Debrecen', 47.5316049, 21.6273124);
insert into location(id, name, lat, lon) values (3, 'Miskolc', 48.1034775, 20.7784384);
insert into location(id, name, lat, lon) values (4, 'Veszprém', 47.1028087, 17.9093019);
insert into location(id, name, lat, lon) values (5, 'Győr', 47.6874569, 17.6503974);

