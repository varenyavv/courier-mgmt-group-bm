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
	extra_weight_factor_per_kg numeric NULL,
	CONSTRAINT rate_card_pk PRIMARY KEY (distance_from_km, distance_to_km)
);

CREATE TABLE service_pincode (
	pincode numeric(6) NOT NULL,
	branch_code varchar(5) NULL,
	CONSTRAINT service_pincode_pk PRIMARY KEY (pincode),
	CONSTRAINT service_pincode_fk FOREIGN KEY (branch_code) REFERENCES branch(branch_code) ON DELETE CASCADE
);

CREATE TABLE distance (
	source_pincode numeric(6) NOT NULL,
	destination_pincode numeric(6) NOT NULL,
	distance_in_km numeric(5) NOT NULL,
	CONSTRAINT distance_pk PRIMARY KEY (source_pincode,destination_pincode),
	CONSTRAINT distance_fk1 FOREIGN KEY (source_pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE,
	CONSTRAINT distance_fk2 FOREIGN KEY (destination_pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE
);

CREATE TYPE transportation_mode AS ENUM ('AIR', 'RAILWAY', 'ROAD');

CREATE TABLE route (
	source_pincode numeric(6) NOT NULL,
	destination_pincode numeric(6) NOT NULL,
	hop_counter numeric(1) NOT NULL,
	next_hop varchar(5) NULL,
	transportation_mode transportation_mode NULL,
	CONSTRAINT route_pk PRIMARY KEY (source_pincode,destination_pincode,hop_counter),
	CONSTRAINT route_fk1 FOREIGN KEY (next_hop) REFERENCES branch(branch_code) ON DELETE CASCADE
);