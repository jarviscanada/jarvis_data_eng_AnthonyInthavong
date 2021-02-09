-- Connect to database
\c host_agent

-- Create tables host_info and host_usage
CREATE TABLE IF NOT EXISTS PUBLIC.host_info
(
	id               	SERIAL NOT NULL,
	hostname         	VARCHAR(100) NOT NULL,
	cpu_number			INTEGER NOT NULL,
	cpu_architecture	VARCHAR(100) NOT NULL,
	cpu_model			VARCHAR(100) NOT NULL,
	cpu_mhz				REAL NOT NULL,
	L2_cache			INTEGER NOT NULL,
	total_mem			INTEGER NOT NULL,
	"timestamp"			TIMESTAMP NOT NULL,

	-- primary key constraint
	PRIMARY KEY (id),
	-- unique hostname constraint
	UNIQUE (hostname)
);

CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
(
	"timestamp"    	TIMESTAMP NOT NULL,
	host_id        	SERIAL NOT NULL,
	memory_free		INTEGER NOT NULL,
	cpu_idle		INTEGER NOT NULL,
	cpu_kernel		INTEGER NOT NULL,
	disk_io			INTEGER NOT NULL,
	disk_available	INTEGER NOT NULL,

	-- add foreign key constraint
	FOREIGN KEY (host_id)
		REFERENCES PUBLIC.host_info(id)
);
