CREATE TABLE branch (
	branch_code varchar(5) NOT NULL,
	branch_name varchar NULL,
	branch_address varchar NULL,
	CONSTRAINT branch_pk PRIMARY KEY (branch_code)
);

CREATE TABLE rate_card (
	distance_from_km numeric NOT NULL,
	distance_to_km numeric NOT NULL,
	base_rate numeric NULL,
	express_mode_factor numeric NULL,
	extra_weight_factor_per_kg numeric NULL,
	CONSTRAINT rate_card_pk PRIMARY KEY (distance_from_km, distance_to_km)
);

CREATE TABLE service_pincode (
	pincode numeric(6) NOT NULL,
	branch_code varchar(5) NULL,
	CONSTRAINT service_pincode_pk PRIMARY KEY (pincode),
	CONSTRAINT service_pincode_fk FOREIGN KEY (branch_code) REFERENCES branch(branch_code) ON DELETE CASCADE
);