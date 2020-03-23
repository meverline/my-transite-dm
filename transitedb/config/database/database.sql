-- Table: tran_agency

-- DROP TABLE tran_agency;

CREATE TABLE tran_agency
(
  angency_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  fareurl character varying(255),
  id character varying(255),
  lang character varying(255),
  mbr geometry,
  name character varying(255) NOT NULL,
  phone character varying(255),
  timezone character varying(255) NOT NULL,
  url character varying(255) NOT NULL,
  version character varying(255),
  CONSTRAINT tran_agency_pkey PRIMARY KEY (agency_uuid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_agency OWNER TO postgres;

-- Table: tran_calendar_date

-- DROP TABLE tran_calendar_date;

CREATE TABLE tran_calendar_date
(
  calendar_date_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  date timestamp without time zone,
  excceptoin_type character varying(255),
  id character varying(255),
  version character varying(255),
  calendar_date_uuid bigint,
  CONSTRAINT tran_calendar_date_pkey PRIMARY KEY (calendar_date_uuid),
  CONSTRAINT fk_calendar_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_calendar_date OWNER TO postgres;

-- Table: tran_route

-- DROP TABLE tran_route;

CREATE TABLE tran_route
(
  route_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  color character varying(255),
  description text,
  id character varying(255) NOT NULL,
  long_name character varying(255) NOT NULL,
  short_name character varying(255) NOT NULL,
  text_color character varying(255),
  type integer NOT NULL,
  url character varying(255),
  version character varying(255),
  route_uuid bigint NOT NULL,
  sort_order integer,
  CONSTRAINT tran_route_pkey PRIMARY KEY (route_uuid),
  CONSTRAINT fk_route_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_route OWNER TO postgres;

-- Table: tran_route_geometry

-- DROP TABLE tran_route_geometry;

CREATE TABLE tran_route_geometry
(
  routegeometry_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  id character varying(255) NOT NULL,
  shape geometry,
  version character varying(255),
  route_geometry_uuid bigint,
  CONSTRAINT tran_route_geometry_pkey PRIMARY KEY (route_geometry_uuid),
  CONSTRAINT fk_route_geometry_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_route_geometry OWNER TO postgres;

-- Table: tran_service_date

-- DROP TABLE tran_service_date;

CREATE TABLE tran_service_date
(
  servicedate_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  end_date timestamp without time zone,
  id character varying(255) NOT NULL,
  service_type character varying(255),
  service_days integer,
  start_date timestamp without time zone,
  version character varying(255),
  service_date_uuid bigint,
  CONSTRAINT tran_service_date_pkey PRIMARY KEY (service_date_uuid),
  CONSTRAINT fk_service_date_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_service_date OWNER TO postgres;

-- Table: tran_stop

-- DROP TABLE tran_stop;

CREATE TABLE tran_stop
(
  tran_stop_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  code character varying(255),
  description character varying(255),
  id character varying(255) NOT NULL,
  location geometry,
  type character varying(255),
  name character varying(255) NOT NULL,
  parent_station integer,
  url character varying(255),
  version character varying(255),
  wheelchairboarding character varying(255),
  zone character varying(255),
  stop_uuid bigint NOT NULL,
  CONSTRAINT tran_stop_pkey PRIMARY KEY (stop_uuid),
  CONSTRAINT fk_stop_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_stop OWNER TO postgres;

-- Table: tran_trip

-- DROP TABLE tran_trip;

CREATE TABLE tran_trip
(
  trip_type character varying(31) NOT NULL,
  agency_uuid bigint NOT NULL,
  direction character varying(255),
  head_sign character varying(255),
  id character varying(255) NOT NULL,
  name character varying(255),
  version character varying(255),
  service_date_uuid bigint NOT NULL,
  route_geometry_uuid bigint NOT NULL,
  route_uuid bigint NOT NULL,
  trip_uuid bigint,
  CONSTRAINT tran_trip_pkey PRIMARY KEY (trip_uuid),
  CONSTRAINT fk_trip_route_geometry_fkey FOREIGN KEY (route_geometry_uuid)
      REFERENCES tran_route_geometry (route_geometry_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_trip_service_date_fkey FOREIGN KEY (service_date_uuid)
      REFERENCES tran_service_date (service_date_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_trip_agency_fkey FOREIGN KEY (agency_uuid)
      REFERENCES tran_agency (agency_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_trip_route_fkey FOREIGN KEY (route_uuid)
      REFERENCES tran_route (route_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_trip
  OWNER TO postgres;

-- Table: hmt_spatialgrid

-- DROP TABLE hmt_spatialgrid;

CREATE TABLE hmt_spatialgrid
(
  grid_uuid bigint NOT NULL,
  heatmapname character varying(255),
  numrows integer,
  numcolumns integer,
  tilesize integer,
  gridspacingmeters double precision,
  crosscovariance double precision,
  upperleft geometry,
  lowerright geometry,
  CONSTRAINT hmt_spatialgrid_pkey PRIMARY KEY (grid_uuid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE hmt_spatialgrid OWNER TO postgres;

-- Table: hmt_tilefragment

-- DROP TABLE hmt_tilefragment;

CREATE TABLE hmt_tilefragment
(
  fragment_uuid bigint NOT NULL,
  heatmapuuid bigint,
  heatmapname character varying(255),
  tileindex bigint,
  mbr geometry,
  CONSTRAINT hmt_tilefragment_pkey PRIMARY KEY (fragment_uuid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE hmt_tilefragment OWNER TO postgres;

-- Table: tran_routetrip

-- DROP TABLE tran_routetrip;

CREATE TABLE tran_routetrip
(
  routetrip_uuid bigint NOT NULL,
  routeTrip_type character varying(31) NOT NULL,
  route_uuid bigint NOT NULL,
  trip_uuid bigint NOT NULL,
  trip_ndx integer,
  CONSTRAINT tran_routetrip_pkey PRIMARY KEY (routetrip_uuid),
  CONSTRAINT fk_route_uuid FOREIGN KEY (route_uuid)
      REFERENCES tran_route (route_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_trip_uuid FOREIGN KEY (trip_uuid)
      REFERENCES tran_trip (trip_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tran_routetrip OWNER TO postgres;


 
 