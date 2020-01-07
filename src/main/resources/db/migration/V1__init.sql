create table locations(id int auto_increment primary key, name varchar(255), lat double, lon double, interesting_at datetime);

create table location_tags(id int auto_increment primary key, location_id int, tags varchar(255), foreign key (location_id) references locations(id));

insert into locations(id, name, lat, lon, interesting_at) values (1, 'Budapest', 47.497912, 19.040235, '2019-09-17 05:00:00');
insert into location_tags(id, location_id, tags) values (1, 1, 'capital');
insert into location_tags(id, location_id, tags) values (2, 1, 'favourite');

insert into locations(id, name, lat, lon) values (2, 'Debrecen', 47.5316049, 21.6273124);
insert into locations(id, name, lat, lon) values (3, 'Miskolc', 48.1034775, 20.7784384);
insert into locations(id, name, lat, lon) values (4, 'Veszprém', 47.1028087, 17.9093019);
insert into locations(id, name, lat, lon) values (5, 'Győr', 47.6874569, 17.6503974);

