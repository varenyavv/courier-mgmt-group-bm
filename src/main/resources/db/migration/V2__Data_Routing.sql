INSERT INTO public.branch (branch_code,branch_name,branch_address) VALUES
	 ('B0001','Noida HO','Sector-18, Noida, UP'),
	 ('B0002','Central Noida','Sector-79, Noida, UP'),
	 ('B0003','Gurgaon','Sector-27, Gurgaon, HR'),
	 ('B0004','Delhi HO','CP, New Delhi, DL'),
	 ('B0005','Rohini Delhi','Rohini, Sector-5, DL'),
	 ('B0006','Dwarka Delhi','Dwarka, Sector-2, DL'),
	 ('B0007','South Delhi','Saket, New Delhi, DL');

INSERT INTO public.rate_card (distance_from_km,distance_to_km,base_rate,express_mode_factor,extra_weight_factor_per_kg) VALUES
	 (0,50,50,1.2,10),
	 (51,100,100,1.5,10),
	 (101,500,150,1.5,10),
	 (501,10000,200,2,20);

INSERT INTO public.service_pincode (pincode,branch_code) VALUES
	 (201301,'B0001'),
	 (201302,'B0001'),
	 (201303,'B0001'),
	 (201307,'B0002'),
	 (201310,'B0002'),
	 (122001,'B0003'),
	 (110006,'B0004'),
	 (122002,'B0003'),
	 (110001,'B0004');