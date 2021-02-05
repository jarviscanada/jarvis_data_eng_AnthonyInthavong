#!/bin/bash

# collects hardware specifications and inserts data into psql instance

# assign CLI arguments to variables
psql_host="$1"
psql_port="$2"
db_name="$3"
psql_user="$4"
psql_password="$5"

# parse host hardware specifications

#save number of CPU to a variable
lscpu_out=`lscpu`

#save mentotal to a variable
mentotal_out=`cat /proc/meminfo`

#free disk space in root directory
df_out=`df -BM /`

# disk statistics
vm_disk_out=`vmstat -d`

vmstat_out=`vmstat -t`

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
total_mem=$(echo "$mentotal_out" | egrep "^MemTotal:" | awk '{print $2}' | xargs) 							# in kB
timestamp=$(date -u '+%Y-%m-%d %H:%M:%S')

# construct the INSERT statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
  VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp');"

# execute the INSERT statement through psql CLI tool
export PGPASSWORD="$psql_password"
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_stmt"
