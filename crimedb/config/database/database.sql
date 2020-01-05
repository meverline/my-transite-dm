-- Table: crm_address

-- DROP TABLE crm_address;

CREATE TABLE crm_address
(
  address_id bigint NOT NULL,
  accuracy integer,
  city character varying(255),
  location character varying(255),
  coordinate geometry(Point,4326),
  service character varying(255),
  state character varying(255),
  zipcode character varying(255),
  CONSTRAINT crm_address_pkey PRIMARY KEY (address_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crm_address OWNER TO postgres;

-- Table: crm_crime

-- DROP TABLE crm_crime;

CREATE TABLE crm_crime
(
  crime_id bigint NOT NULL,
  urc_id bigint NOT NULL,
  bussiness character varying(255) NOT NULL,
  crimecatagory character varying(255) NOT NULL,
  county character varying(255) NOT NULL,
  crime_number character varying(255) NOT NULL,
  description character varying(255),
  file character varying(255) NOT NULL,
  rank_ double precision NOT NULL,
  start_date timestamp without time zone NOT NULL,
  time_ordinal double precision NOT NULL,
  address_id bigint NOT NULL,
  CONSTRAINT crm_crime_pkey PRIMARY KEY (crime_id),
  CONSTRAINT fk4rkab6p9cqj77uha7m1d51j92 FOREIGN KEY (address_id)
      REFERENCES crm_address (address_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkt9m2avbruek0ofvli036no9d3 FOREIGN KEY (urc_id)
      REFERENCES crm_urccatagories (urc_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crm_crime OWNER TO postgres;

-- Table: crm_urccatagories

-- DROP TABLE crm_urccatagories;

CREATE TABLE crm_urccatagories
(
  urc_id bigint NOT NULL,
  catagorie character varying(255),
  crimegroup character varying(255),
  CONSTRAINT crm_urccatagories_pkey PRIMARY KEY (urc_id),
  CONSTRAINT uk_d0wmflcn9hw89rp9u8rr25xjf UNIQUE (catagorie),
  CONSTRAINT uk_n43f64hr0l628ey37sjus960n UNIQUE (crimegroup)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crm_urccatagories OWNER TO postgres;
