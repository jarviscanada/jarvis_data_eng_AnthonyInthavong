# Introduction
TODO
The Jarvis Linux Cluster Administration (LCA) team manages a Linux cluster of nodes/servers which are running CentOS 7. These servers are internally connected through a switch and able to communicate through internal IPv4 addresses.



# Quick Start
TODO


# Implementation
TODO


## Architecture
TODO


## Scripts

### File Structure
```
+-- scripts
|   +-- psql_docker.sh
|   +-- host_info.sh
|   +-- host_usage.sh
+-- sql
|   +-- ddl.sql
|   +-- queries.sql
+-- README.md
```
### Usage
1. `./scripts/psql_docker.sh start|stop|create db_username db_password`
Start or stop a existing container with `psql image` called `jrvs_psql` associated with `db_username`, or create a new docker container called `jrvs_psql` with `db_username` and `db_password`.

2. `./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`
Collect hardware specification data and upload into db_name within `psql image` in docker container specified by
	- `psql_host`: postgreSQL host name
	- `psql_port`: postgreSQL port (default is 5432)
	- `db_name`: database name
	- `psql_user`: postgreSQL username
	- `psql_password`: postgreSQL password

Note: script is only run once to collect hardware data.

3. `./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`
Collect Linux usage data and upload into `db_name` within `psql image` in docker container specified by 
	- `psql_host`: postgreSQL host name
	- `psql_port`: postgreSQL port (default is 5432)
	- `db_name`: database name
	- `psql_user`: postgreSQL username
	- `psql_password`: postgreSQL password

Script is run by Linux command `crontab` to collect Linux usage data every minute

```
### Example usage
#edit crontab jobs 
crontab -e 

#add this to crontab subsituting <path>
* * * * * bash <path>/host_usage.sh psql_host psql_port db_name psql_user psql_password
 
#list crontab jobs to verify process is running
crontab -l
```

4. `./sql/ddl.sql`
Generate `host_info` and `host_usage` tables to store hardware specification data and Linux usage data respectively. Note: script is run once to generate SQL tables.
```bash
#execute a sql file using psql command
psql -h HOST_NAME -p 5432 -U USER_NAME -d DB_NAME -f FILE_NAME.sql
```
5. `./sql/queries.sql`
SQL script containing multiple queries to answer relevant business questions. Currently has 3 distinct queries implemented to answer 3 different business questions.

a. Group host by hardware info sorted by total memory usage
cpu_number|host_id|total_mem 
|-------|--------------|-------------|
1|1|2048 
1|5|1568 
1|9|1024 
2|4|4088
2|6|1024


b. Average memory usage in percentage over 5 minute intervals
|host_id| host_name| timestamp|avg_used_mem_percentage |
|-------|--------------|-------------|-------------|
|1|node1.jrvs.ca|2019-01-01 00:00:00|97|
|1|node1.jrvs.ca|2019-01-01 00:05:00|90|
|1|node1.jrvs.ca|2019-01-01 00:10:00|65|

c. Detect host failure if sever failed (less than 3 rows of data are collected within 5 minutes)
host_id|timestamp|num_data_points 
|-------|--------------|-------------|
2|2019-01-01 00:10:00|2 
This indicates the server corresponding to `host_id = 2` has failed
```bash
#execute a sql file using psql command
psql -h HOST_NAME -p 5432 -U USER_NAME -d DB_NAME -f FILE_NAME.sql
```


## Database Modeling
- host_info: Stores Hardware Specific data

|Column                |Type             | Description                     |Example                       |
|--------------------|---------------|-------------------------------|-----------------------------|
|id                  |`serial`         |`primary key` for the host info table | 1
|hostname            |`unique varchar` |`fully qualified name` for each host system |jrvs-remote-desktop-centos7.us-east1-c.c.prefab-mapper-303519.internal |
|cpu_number          |`integer`        |number of cpu(s) on system  |2 |
|cpu_architecture    |`varchar`        |architecture of the cpu     |x86_64 |
|cpu_model           |`varchar`        |model of the cpu            |AMD EPYC 7B12 |
|cpu_mhz             |`real`           |speed of cpu in `mhz`               |2249.998046875
|L2_cache            |`integer`        |storage of L2 cache in `kb`         |512 |
|total_mem           |`integer`        |total memory on host system in `kb` |7492120 |
|timestamp           |`timestamp`      |recorded time in `UTC`              |Feb 5, 2021, 9:17:57 PM |

- host_usage: Stores Linux Resource Data Usage

|Column         |Type        |Description                  		|Example                      |
|---------------|------------|------------------------------------------|-----------------------------|
|timestamp      |`timestamp` |recorded time in `UTC`             	|Feb 5, 2021, 9:17:57 PM      |
|host_id        |`serial`    |`foreign key` for the host info table 	|1 			      |
|memory_free    |`integer`   |free memory in `mb` 			|3811920                      |
|cpu_idle       |`integer`   |idle cpu usage in `percentage` 		|96                           |
|cpu_kernel     |`integer`   |kernel cpu usage in `percentage` 		|0                            |
|disk_io        |`integer`   |number of disk I/O 			|1                 	      |
|disk_available |`integer`   |root directory available memory 		|25905 			      |


# Test
TODO


# Improvements
