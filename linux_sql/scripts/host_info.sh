#!/bin/bash

# collects hardware specifications and inserts data into psql instance

# assign CLI arguments to variables
psql_host="$1"
psql_port="$2"
db_name="$3"
psql_user="$4"
psql_password="$5"

# check if valid arguments
if [ "$#" -ne 5 ]; then
  echo "host_info: invalid arguments: require 5 arguments" >&2
  echo "usage: ./host_info.sh psql_host psql_port db_name psql_user psql_password" >&2
  exit 1
fi

# export password for psql instance (environment variable)
# https://www.postgresql.org/docs/9.1/libpq-envars.html
export PGPASSWORD="$psql_password"

# parse host hardware specifications

#save number of CPU to a variable
lscpu_out=`lscpu`

#save mentotal to a variable
mentotal_out=`cat /proc/meminfo`

#note: `xargs` is a trick to remove leading and trailing white spaces

#Hardware
#data columns
#id,hostname,cpu_number,cpu_architecture,cpu_model,cpu_mhz,L2_cache,total_mem,timestamp
#id=1      #psql db auto-increment
hostname=$(hostname -f) #fully qualified hostname
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | sed 's/Model name://' | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | sed 's/CPU MHz://' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | sed 's/L2 cache://' | sed 's/\(\d*\).$/\1/' | xargs) 	# in kB
total_mem=$(echo "$mentotal_out" | egrep "^MemTotal:" | awk '{print $2}' | awk '{print int($1/1000)}' | xargs) 							# in kB
timestamp=$(date -u '+%Y-%m-%d %H:%M:%S')

# construct the INSERT statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
  VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp');"

# execute the INSERT statement through psql CLI tool
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_stmt"
