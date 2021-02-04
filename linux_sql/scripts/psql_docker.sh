#!/bin/bash

# if docker_deamon is not running start docker_deamon
sudo systemctl status docker || systemctl start docker


if [[ "$1" == "create" ]]; then

  # if `db_username` or `db_password` is not passed through CLI arguments
  if [ "$#" -ne 3 ]; then
    # print error and usage
    echo "psql_docker: \`db_username\` or \`db_password\` is not passed through the CLI arguments" >&2
    echo "psql_docker: create: create a new docker container \`jrvs-psql\` with db_username and db_password" >&2
    echo "usage: ./psql_docker.sh create [db_username] [db_password]" >&2
    echo "psql_docker: start: start the \`jrvs-psql\` container" >&2
    echo "usage: ./psql_docker.sh start" >&2
    echo "psql_docker: stop: stop the \`jrvs-psql\` container" >&2
    echo "usage: ./psql_docker.sh stop" >&2
    exit 1
  fi


  # set up arguments
  db_username=$2
  db_password=$3
  # save line count into variable - should be 2
  res=`docker container ls -a -f name=jrvs-psql | wc -l`
  # print error message if `jrvs-psql` container already created
  if [ "$res" -eq 2 ]; then
    echo "psql_docker: \`jrvs-sql\` container already created" >&2
    echo "psql_docker: create: create a new docker container \`jrvs-psql\` with db_username and db_password" >&2
    echo "usage: ./psql_docker.sh create [db_username] [db_password]" >&2
    echo "psql_docker: start: start the \`jrvs-psql\` container" >&2
    echo "usage: ./psql_docker.sh start" >&2
    echo "psql_docker: stop: stop the \`jrvs-psql\` container" >&2
    echo "usage: ./psql_docker.sh stop" >&2
    exit 1
  fi

  # provision a psql instance using docker
  # pull postgres image from docker
  docker pull postgres

  #create `pgdate` volume
  docker volume create pgdata

  #create a psql container
  docker run --name jrvs-psql -e POSTGRES_PASSWORD="$db_password" \
    -e POSTGRES_USER="$db_username" -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres

  # exit with previous pipelne/command state
  exit $?
fi


# save line count into variable - should be 2
res=`docker container ls -a -f name=jrvs-psql | wc -l`
# if `jrvs-psql` container is not created
if [ "$res" -ne 2 ]; then
  #print error and usage
  echo "psql_docker: \`jrvs-psql\` container is not created" >&2
  echo "psql_docker: create: create a new docker container \`jrvs-psql\` with db_username and db_password" >&2
  echo "usage: ./psql_docker.sh create [db_username] [db_password]" >&2
  echo "psql_docker: start: start the \`jrvs-psql\` container" >&2
  echo "usage: ./psql_docker.sh start" >&2
  echo "psql_docker: stop: stop the \`jrvs-psql\` container" >&2
  echo "usage: ./psql_docker.sh stop" >&2

  exit 1
fi

# start the stopped docker container
if [[ "$1" == "start" ]]; then
  # start container
  docker container start jrvs-psql
  exit $?
fi

# stop the running docker container
if [[ "$1" == "stop" ]]; then
  # stop container
  docker container stop jrvs-psql
  exit $?
fi

# if $1 is invalid
# print error message and usage
echo "psql_docker: invalid first argument" >&2
echo "psql_docker: create: create a new docker container \`jrvs-psql\` with db_username and db_password" >&2
echo "usage: ./psql_docker.sh create [db_username] [db_password]" >&2
echo "psql_docker: start: start the \`jrvs-psql\` container" >&2
echo "usage: ./psql_docker.sh start">&2
echo "psql_docker: stop: stop the \`jrvs-psql\` container" >&2
echo "usage: ./psql_docker.sh stop">&2
exit 1
