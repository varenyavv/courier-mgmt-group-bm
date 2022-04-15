INSERT INTO public.branch (branch_code,branch_name,add_line,pincode,city,state) VALUES
	 ('B0001','Noida HO','Sector-18', 201301, 'Noida', 'UP'),
	 ('B0002','Delhi HO','CP', 110001,'New Delhi', 'DL'),
	 ('B0003','Mumbai HO','Bandra', 400001, 'Mumbai', 'MH'),
	 ('B0004','Kolkata HO','Somewhere', 300001,'Kolkata', 'WB'),
	 ('B0005','Bangalore HO','MG Road', 510001, 'Bengaluru','KN'),
	 ('B0006','Chennai HO','Civil lines',601011, 'Chennai', 'TN'),
	 ('B0007','Hyderabad Main Branch','MG Road',701331,'Hyderabad','TS');

INSERT INTO public.rate_card (distance_from_km,distance_to_km,base_rate,extra_weight_factor) VALUES
	 (0,50,50,10),
	 (51,100,100,10),
	 (101,500,150,10),
	 (501,10000,200,20);

INSERT INTO public.service_pincode (pincode,branch_code) VALUES
	 (201301,'B0001'),
	 (201302,'B0001'),
	 (201303,'B0001'),
	 (201304,'B0001'),
	 (110001,'B0002'),
	 (110002,'B0002'),
	 (110003,'B0002'),
	 (110004,'B0002'),
	 (110005,'B0002'),
	 (110006,'B0002'),
	 (400001,'B0003'),
	 (400002,'B0003'),
	 (300001,'B0004'),
	 (300002,'B0004'),
	 (510001,'B0005'),
	 (510002,'B0005'),
	 (601011,'B0006'),
	 (601012,'B0006'),
	 (701331,'B0007'),
	 (701332,'B0007');

INSERT INTO public.customer (contact_num,name,add_line,pincode,city,state) VALUES
    (9810101010,'Ajay','Sector-17',201301,'Noida','UP'),
    (9810101011,'Vijay','Sector-18',201302,'Noida','UP'),
    (9810101012,'Arvind','XYZ Street',110001,'New Delhi','DL'),
    (9810101013,'Vinay','23 lane',110002,'New Delhi','DL'),
    (9810101014,'Shivani','Shivaji park',110003,'New Delhi','DL'),
    (9810101015,'Swati','A-2344, ABC Society',400001,'Mumbai','MH'),
    (9810101016,'Priyanka','F-345, 10th Avenue',300001,'Kolkata','WB'),
    (9810101017,'Shilpa','F-111, 14th Avenue',510002,'Bengaluru','KN'),
    (9810101018,'Anil','MG Road',601011,'Chennai','TN'),
    (9810101019,'Ravi','Mall Road',601012,'Chennai','TN'),
    (9810101020,'Dinesh','KD Road',701332,'Hyderabad','TS');

INSERT INTO public.agent (contact_num,name,add_line,pincode,city,state,branch_code) VALUES
    (8810101010,'Sachin','Sector-17',201301,'Noida','UP','B0001'),
    (8810101011,'Sourav','Sector-18',201302,'Noida','UP','B0001'),
    (8810101012,'Rahul','XYZ Street',110001,'New Delhi','DL','B0002'),
    (8810101013,'Laxman','23 lane',110002,'New Delhi','DL','B0002'),
    (8810101014,'Virender','Shivaji park',110003,'New Delhi','DL','B0002'),
    (8810101015,'Yuvraj','A-2344, ABC Society',400001,'Mumbai','MH','B0003'),
    (8810101016,'Md Kaif','F-345, 10th Avenue',300001,'Kolkata','WB','B0004'),
    (8810101017,'Zaheer','F-111, 14th Avenue',510002,'Bengaluru','KN','B0005'),
    (8810101018,'Harbhajan','MG Road',601011,'Chennai','TN','B0006'),
    (8810101019,'Mahi','Mall Road',601012,'Chennai','TN','B0006'),
    (8810101020,'Virat','KD Road',701332,'Hyderabad','TS','B0007');

INSERT INTO public.employee (employee_id, contact_num,name,add_line,pincode,city,state,branch_code) VALUES
    (nextval('employee_id'),7810101010,'Surya','Sector-17',201301,'Noida','UP','B0001'),
    (nextval('employee_id'),7810101011,'Kartik','23 lane',110002,'New Delhi','DL','B0002'),
    (nextval('employee_id'),7810101012,'Ramesh','A-2344, ABC Society',400001,'Mumbai','MH','B0003'),
    (nextval('employee_id'),7810101013,'Gaurav','F-345, 10th Avenue',300001,'Kolkata','WB','B0004'),
    (nextval('employee_id'),7810101014,'Rishi','F-111, 14th Avenue',510002,'Bengaluru','KN','B0005'),
    (nextval('employee_id'),7810101015,'Vipin','Mall Road',601012,'Chennai','TN','B0006'),
    (nextval('employee_id'),7810101016,'Vimal','KD Road',701332,'Hyderabad','TS','B0007');