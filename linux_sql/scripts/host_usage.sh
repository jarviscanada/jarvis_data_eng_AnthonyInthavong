#!/bin/bash

# assign CLI arguments to variables
psql_host="$1"
psql_port="$2"
db_name="$3"
psql_user="$4"
psql_password="$5"

# export password for psql instance (environment variable)
# https://www.postgresql.org/docs/9.1/libpq-envars.html
export PGPASSWORD="$psql_password"

#save mentotal to a variable
mentotal_out=`cat /proc/meminfo`

#free disk space in root directory
df_out=`df -BM /`

# disk statistics
vm_disk_out=`vmstat -d`

vmstat_out=`vmstat -t`

#note: `xargs` is a trick to remove leading and trailing white spaces

#Linux resource usage data
#data columns
#timestamp,host_id,memory_free,cpu_idle,cpu_kernel,disk_io,disk_available
hostname=$(hostname -f) #fully qualified hostname

timestamp=$(date -u '+%Y-%m-%d %H:%M:%S')								  #UTC time zone
# use subquery to collect id based on `unique` hostname from host_info table
host_id=$(psql -h localhost -p 5432 -U postgres -d host_agent -c "SELECT id FROM host_info WHERE hostname='$hostname'" | sed -n "3p" | xargs)                   														#host id from `hosts` table
memory_free=$(echo "$mentotal_out" | egrep "^MemFree:" | awk '{print $2}' | xargs) 	#in MB
cpu_idle=$(echo "$vmstat_out" | sed -n 3p | awk '{print  $15}' | xargs) 			#in percentage
cpu_kernel=$(echo "$vmstat_out" | sed -n 3p | awk '{print  $14}' | xargs) 			#in percentage
disk_io=$(vmstat -D | sed -n 1p | awk '{print $1}' | xargs) 						#number of disk I/O
disk_available=$(echo "$df_out" | sed -n 2p | awk '{print $4}' | sed 's/\(\d*\).$/\1/' | xargs) #in MB. root directory avaliable disk

# construct the INSERT statement for host_usage
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
  VALUES ('$timestamp', '$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

# execute the INSERT statement through psql CLI tool
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_stmt"
