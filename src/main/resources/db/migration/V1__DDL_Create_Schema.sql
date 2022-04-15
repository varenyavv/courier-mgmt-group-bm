CREATE TYPE status AS ENUM (
	'BOOKED',
	'IN_TRANSIT',
	'RECEIVED_AT_DEST_BRANCH',
	'OUT_FOR_DELIVERY',
	'DELIVERED',
	'UNDELIVERED');

CREATE TYPE transport_mode AS ENUM (
	'AIR',
	'RAILWAY',
	'ROAD');

CREATE TABLE branch (
	branch_code varchar(5) NOT NULL CHECK (branch_code ~ 'B[0-9]+'),
	branch_name varchar NULL,
	add_line varchar NULL,
	pincode numeric(6) NOT NULL,
	city varchar NOT NULL,
	state varchar(2) NOT NULL,
	CONSTRAINT branch_pk PRIMARY KEY (branch_code)
);

CREATE TABLE service_pincode (
	pincode numeric(6) NOT NULL,
	branch_code varchar(5) NULL,
	CONSTRAINT service_pincode_pk PRIMARY KEY (pincode),
	CONSTRAINT service_pincode_fk FOREIGN KEY (branch_code) REFERENCES branch(branch_code) ON DELETE CASCADE
);

CREATE TABLE distance (
	source_pincode numeric(6) NOT NULL,
	dest_pincode numeric(6) NOT NULL,
	distance_km numeric(5) NOT NULL,
	CONSTRAINT distance_pk PRIMARY KEY (source_pincode, dest_pincode),
	CONSTRAINT distance_fk1 FOREIGN KEY (source_pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE,
	CONSTRAINT distance_fk2 FOREIGN KEY (dest_pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE
);

CREATE TABLE route (
	source_pincode numeric(6) NOT NULL,
	dest_pincode numeric(6) NOT NULL,
	hop_counter numeric(1) NOT NULL,
	next_hop varchar(5) NULL,
	"transport_mode" transport_mode NULL,
	CONSTRAINT route_pk PRIMARY KEY (source_pincode, dest_pincode, hop_counter),
	CONSTRAINT route_fk1 FOREIGN KEY (next_hop) REFERENCES branch(branch_code) ON DELETE CASCADE
);

CREATE TABLE rate_card (
	distance_from_km numeric NOT NULL,
	distance_to_km numeric NOT NULL,
	base_rate numeric NULL,
	extra_weight_factor numeric NULL,
	CONSTRAINT rate_card_pk PRIMARY KEY (distance_from_km, distance_to_km)
);

CREATE TABLE customer (
	contact_num numeric(10) NOT NULL,
	"name" varchar NOT NULL,
	add_line varchar NOT NULL,
	pincode numeric(6) NOT NULL,
	city varchar NOT NULL,
	state varchar(2) NOT NULL,
	CONSTRAINT customer_pk PRIMARY KEY (contact_num),
	CONSTRAINT customer_fk FOREIGN KEY (pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE
);

CREATE TABLE agent (
	contact_num numeric(10) NOT NULL,
	"name" varchar NOT NULL,
	add_line varchar NOT NULL,
	pincode numeric(6) NOT NULL,
	city varchar NOT NULL,
	state varchar(2) NOT NULL,
	branch_code varchar(5) NOT NULL,
	CONSTRAINT agent_pk PRIMARY KEY (contact_num),
	CONSTRAINT agent_fk FOREIGN KEY (branch_code) REFERENCES branch(branch_code) ON DELETE CASCADE
);

CREATE SEQUENCE employee_id START 100001;

CREATE TABLE employee (
	employee_id numeric(6) NOT NULL,
	contact_num numeric(10) NOT NULL,
	"name" varchar NOT NULL,
	add_line varchar NOT NULL,
	pincode numeric(6) NOT NULL,
	city varchar NOT NULL,
	state varchar(2) NOT NULL,
	branch_code varchar(5) NOT NULL,
	CONSTRAINT employee_pk PRIMARY KEY (employee_id),
	CONSTRAINT employee_un UNIQUE (contact_num),
	CONSTRAINT employee_fk FOREIGN KEY (branch_code) REFERENCES branch(branch_code) ON DELETE CASCADE
);

CREATE SEQUENCE shipment_id START 100001;

CREATE TABLE shipment (
	shipment_id numeric NOT NULL,
	consignment_num varchar(10) NOT NULL CHECK (consignment_num ~ 'BM[0-9]+'),
	customer_id numeric(10) NOT NULL,
	weight_gm numeric NOT NULL,
	length_cm numeric NOT NULL,
	width_cm numeric NOT NULL,
	height_cm numeric NOT NULL,
	dest_add_line varchar NOT NULL,
	dest_pincode numeric(6) NOT NULL,
	dest_city varchar NOT NULL,
	dest_state varchar(2) NOT NULL,
	distance_km numeric NOT NULL,
	booking_amount numeric NOT NULL,
	"status" status NOT NULL,
	status_remarks varchar NULL,
	CONSTRAINT shipment_pk PRIMARY KEY (shipment_id),
	CONSTRAINT shipment_un UNIQUE (consignment_num),
	CONSTRAINT shipment_fk FOREIGN KEY (customer_id) REFERENCES customer(contact_num) ON DELETE CASCADE,
	CONSTRAINT shipment_fk_1 FOREIGN KEY (dest_pincode) REFERENCES service_pincode(pincode) ON DELETE CASCADE
);

CREATE TABLE shipment_tracker (
	shipment_tracker_id numeric NOT NULL,
	shipment_id numeric NOT NULL,
	current_branch varchar(5) NULL,
	next_branch varchar(5) NULL,
	"transport_mode" transport_mode NULL,
	"status" status NOT NULL,
	creation_datetime timestamp NOT NULL,
	employee_id numeric(6) NULL,
	agent_id numeric(10) NULL,
	CONSTRAINT shipment_tracker_pk PRIMARY KEY (shipment_tracker_id),
	CONSTRAINT shipment_tracker_fk FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id) ON DELETE CASCADE,
	CONSTRAINT shipment_tracker_fk_1 FOREIGN KEY (current_branch) REFERENCES branch(branch_code) ON DELETE CASCADE,
	CONSTRAINT shipment_tracker_fk_2 FOREIGN KEY (next_branch) REFERENCES branch(branch_code) ON DELETE CASCADE,
	CONSTRAINT shipment_tracker_fk_3 FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE,
	CONSTRAINT shipment_tracker_fk_4 FOREIGN KEY (agent_id) REFERENCES agent(contact_num) ON DELETE CASCADE
);