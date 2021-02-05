#!/bin/bash

# if docker_deamon is not running start docker_deamon
sudo systemctl status docker || systemctl start docker

# set up arguements
command=$1
db_username=$2
db_password=$3

if [[ "$command" == "create" ]]; then
  # if `db_username` or `db_password` is not passed through CLI arguments
  if [ "$#" -ne 3 ]; then
    # print error and usage
    echo "psql_docker: \`db_username\` or \`db_password\` is not passed through the CLI arguments" >&2
    exit 1
  fi

  # save line count into variable - if container already created should be 2
  res=`docker container ls -a -f name=jrvs-sql | wc -l`
  # if `jrvs-psql` container already created
  if [ "$res" -eq 2 ]; then
    # print error and usage
    echo "psql_docker: \`jrvs-sql\` container already created" >&2
    exit 1
  fi
  #create `pgdate` volume
  docker volume create pgdata

  #create a psql container
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres

  # exit with previous pipelne/command state
  exit $?
fi

# save line count into variable - should be 2
res=`docker container ls -a -f name=jrvs-psql | wc -l`
# if `jrvs-psql` container is not created
if [ "$res" -ne 2 ]; then
  #print error and usage
  echo "psql_docker: \`jrvs-psql\` container is not created" >&2
  exit 1
fi

# if $1 = "start"
if [[ "$command" == "start" ]]; then
  # start container
  docker container run jrvs-psql
  exit $?
fi

# if $1 = "stop"
if [[ "$command" == "stop" ]]; then
  # stop container
  docker container stop jrvs-psql
  exit $?
fi

# if first argument is invalid
# print error message and usage
echo "psql_docker: invalid first arguement" >&2
exit 1
