INSERT INTO Customer (email, password) VALUES ('andrew@test.com', 123);
INSERT INTO Customer (email, password) VALUES ('mary@test.com', 321);
INSERT INTO Customer (email, password) VALUES ('nathew@test.com', 223);
INSERT INTO Customer (email, password) VALUES ('zoe@test.com', 331);

INSERT INTO Vehicle (plate, customer_id) values ('A111', (select id from customer where email like 'andrew@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('A222', (select id from customer where email like 'andrew@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('A333', (select id from customer where email like 'andrew@test.com'));

INSERT INTO Vehicle (plate, customer_id) values ('M111', (select id from customer where email like 'mary@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('M222', (select id from customer where email like 'mary@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('M333', (select id from customer where email like 'mary@test.com'));

INSERT INTO Vehicle (plate, customer_id) values ('N111', (select id from customer where email like 'nathew@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('N222', (select id from customer where email like 'nathew@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('N333', (select id from customer where email like 'nathew@test.com'));

INSERT INTO Vehicle (plate, customer_id) values ('Z111', (select id from customer where email like 'zoe@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('Z222', (select id from customer where email like 'zoe@test.com'));
INSERT INTO Vehicle (plate, customer_id) values ('Z333', (select id from customer where email like 'zoe@test.com'));

INSERT INTO Transaction (value, customer_id) values (10, (select id from customer where email like 'andrew@test.com'));
INSERT INTO Transaction (value, customer_id) values (20, (select id from customer where email like 'mary@test.com'));
INSERT INTO Transaction (value, customer_id) values (30, (select id from customer where email like 'nathew@test.com'));
INSERT INTO Transaction (value, customer_id) values (40, (select id from customer where email like 'zoe@test.com'));

INSERT INTO Parking_Lot (address, active) values ('Mad Ave 123', true);
INSERT INTO Parking_Lot (address, active) values ('Wall St 34', true);
INSERT INTO Parking_Lot (address, active) values ('Strange Neighborhood 999', false);
INSERT INTO Parking_Lot (address, active) values ('Java Ave 23', true);

INSERT INTO Parking_Session (amount_charged, start, end, customer_id, vehicle_id) values (5, '2018-01-01 10:00:00', '2018-01-01 15:00:00', (select id from customer where email like 'zoe@test.com'), (select id from vehicle where plate='Z333'));



