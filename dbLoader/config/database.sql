-- Table: omd_locations

-- DROP TABLE omd_locations;

CREATE TABLE omd_locations
(
    location_uuid bigint NOT NULL,
    id bigint NOT NULL,
    pid bigint NOT NULL,
    title character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    lat float4 NOT NULL,
    lon float4 NOT NULL
)
    WITH (
        OIDS=FALSE
    );
ALTER TABLE omd_locations OWNER TO postgres;