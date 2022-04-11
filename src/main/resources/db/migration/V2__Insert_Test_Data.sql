INSERT INTO public.branch (branch_code,branch_name,add_line,pincode,city,state) VALUES
	 ('B0001','Noida HO','Sector-18', 201301, 'Noida', 'UP'),
	 ('B0002','Central Noida','Sector-79', 201307, 'Noida', 'UP'),
	 ('B0003','Gurgaon','Sector-27', 122001, 'Gurgaon', 'HR'),
	 ('B0004','Delhi HO','CP', 110001,'New Delhi', 'DL'),
	 ('B0005','Rohini Delhi','Rohini, Sector-5', 110025, 'New Delhi','DL'),
	 ('B0006','Dwarka Delhi','Dwarka, Sector-2',110055, 'New Delhi', 'DL'),
	 ('B0007','South Delhi','Saket',110096,'New Delhi','DL');

INSERT INTO public.rate_card (distance_from_km,distance_to_km,base_rate,extra_weight_factor) VALUES
	 (0,50,50,10),
	 (51,100,100,10),
	 (101,500,150,10),
	 (501,10000,200,20);

INSERT INTO public.service_pincode (pincode,branch_code) VALUES
	 (201301,'B0001'),
	 (201302,'B0001'),
	 (201303,'B0001'),
	 (201307,'B0002'),
	 (201310,'B0002'),
	 (122001,'B0003'),
	 (110006,'B0004'),
	 (122002,'B0003'),
	 (110001,'B0004'),
	 (110025,'B0005'),
	 (110055,'B0006'),
	 (110096,'B0007');