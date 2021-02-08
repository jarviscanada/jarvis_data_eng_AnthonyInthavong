# Introduction

The Jarvis Linux Cluster Administration (LCA) team manages a Linux cluster of nodes/servers which are running CentOS 7. These servers are internally connected through a switch and able to communicate through internal IPv4 addresses.

## Database Modelling
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


