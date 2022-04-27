INSERT INTO public.state (state_code,name) VALUES
	 ('UP','Uttar Pradesh'),
	 ('DL','Delhi'),
	 ('MH','Maharashtra'),
	 ('KA','Karnataka'),
	 ('TN','Tamil Nadu'),
	 ('TS','Telangana'),
	 ('WB','West Bengal'),
	 ('RJ','Rajasthan');

INSERT INTO public.city (city_id,name,state) VALUES
	 (nextval('seq_city'),'Noida', 'UP'),
	 (nextval('seq_city'),'New Delhi', 'DL'),
	 (nextval('seq_city'),'Mumbai', 'MH'),
	 (nextval('seq_city'),'Kolkata', 'WB'),
	 (nextval('seq_city'),'Bengaluru', 'KA'),
	 (nextval('seq_city'),'Chennai', 'TN'),
	 (nextval('seq_city'),'Hyderabad', 'TS'),
	 (nextval('seq_city'),'Jaipur', 'RJ');

INSERT INTO public.pincode (pincode,city) VALUES
	 (201301,(select city_id from city where name='Noida')),
	 (201302,(select city_id from city where name='Noida')),
	 (201303,(select city_id from city where name='Noida')),
	 (201304,(select city_id from city where name='Noida')),
	 (110001,(select city_id from city where name='New Delhi')),
	 (110002,(select city_id from city where name='New Delhi')),
	 (110003,(select city_id from city where name='New Delhi')),
	 (110004,(select city_id from city where name='New Delhi')),
	 (110005,(select city_id from city where name='New Delhi')),
	 (110006,(select city_id from city where name='New Delhi')),
	 (400001,(select city_id from city where name='Mumbai')),
	 (400002,(select city_id from city where name='Mumbai')),
	 (300001,(select city_id from city where name='Kolkata')),
	 (300002,(select city_id from city where name='Kolkata')),
	 (510001,(select city_id from city where name='Bengaluru')),
	 (510002,(select city_id from city where name='Bengaluru')),
	 (601011,(select city_id from city where name='Chennai')),
	 (601012,(select city_id from city where name='Chennai')),
	 (701331,(select city_id from city where name='Hyderabad')),
	 (701332,(select city_id from city where name='Hyderabad')),
	 (154432,(select city_id from city where name='Jaipur')),
	 (154433,(select city_id from city where name='Jaipur'));

INSERT INTO public.branch (branch_code,branch_name,address_line,pincode) VALUES
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Noida HO','Sector-18', 201301),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Delhi HO','CP', 110001),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Mumbai HO','Bandra', 400001),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Kolkata HO','Somewhere', 300001),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Bangalore HO','MG Road', 510001),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Chennai HO','Civil lines',601011),
	 ('B'||lpad(cast (nextval('seq_branch_code') as varchar),4,'0'),'Hyderabad Main Branch','MG Road',701331);

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

INSERT INTO public.customer (contact_num,name,address_line,pincode) VALUES
    (9810101010,'Ajay','Sector-17',201301),
    (9810101011,'Vijay','Sector-18',201302),
    (9810101012,'Arvind','XYZ Street',110001),
    (9810101013,'Vinay','23 lane',110002),
    (9810101014,'Shivani','Shivaji park',110003),
    (9810101015,'Swati','A-2344, ABC Society',400001),
    (9810101016,'Priyanka','F-345, 10th Avenue',300001),
    (9810101017,'Shilpa','F-111, 14th Avenue',510002),
    (9810101018,'Anil','MG Road',601011),
    (9810101019,'Ravi','Mall Road',601012),
    (9810101020,'Dinesh','KD Road',701332);

INSERT INTO public.agent (contact_num,name,address_line,pincode,branch_code) VALUES
    (8810101010,'Sachin','Sector-17',201301,'B0001'),
    (8810101011,'Sourav','Sector-18',201302,'B0001'),
    (8810101012,'Rahul','XYZ Street',110001,'B0002'),
    (8810101013,'Laxman','23 lane',110002,'B0002'),
    (8810101014,'Virender','Shivaji park',110003,'B0002'),
    (8810101015,'Yuvraj','A-2344, ABC Society',400001,'B0003'),
    (8810101016,'Md Kaif','F-345, 10th Avenue',300001,'B0004'),
    (8810101017,'Zaheer','F-111, 14th Avenue',510002,'B0005'),
    (8810101018,'Harbhajan','MG Road',601011,'B0006'),
    (8810101019,'Mahi','Mall Road',601012,'B0006'),
    (8810101020,'Virat','KD Road',701332,'B0007');

INSERT INTO public.employee (employee_id, contact_num,name,branch_code) VALUES
    (nextval('seq_employee_id'),7810101010,'Surya','B0001'),
    (nextval('seq_employee_id'),7810101011,'Kartik','B0002'),
    (nextval('seq_employee_id'),7810101012,'Ramesh','B0003'),
    (nextval('seq_employee_id'),7810101013,'Gaurav','B0004'),
    (nextval('seq_employee_id'),7810101014,'Rishi','B0005'),
    (nextval('seq_employee_id'),7810101015,'Vipin','B0006'),
    (nextval('seq_employee_id'),7810101016,'Vimal','B0007');