# Order of operations
These commands are for linux/Mac, changes will need to made if you are running this in Microsoft Windows.

## Prerequisites
Docker is installed
psql client is installed

## Actions

### Running PostgreSQL
1. Pull Docker Image
`docker pull postgres`

2. Build data directory
`mkdir -p ~/srv/postgres`

3. Run docker image
`docker run --rm --name lil-postgres -e POSTGRES_PASSWORD=password -d -v $HOME/srv/postgres:/var/lib/postgresql/data -p 5432:5432 postgres`

### Stopping PostgreSQL
`docker stop lil-postgres`

### Logging into Database
* `psql -h localhost -U postgres -d hplussport`

### Creating starter data
1. `psql -h localhost -U postgres -f database.sql`
2. `psql -h localhost -U postgres -d hplussport -f customer.sql`
3. `psql -h localhost -U postgres -d hplussport -f product.sql`
4. `psql -h localhost -U postgres -d hplussport -f salesperson.sql`
5. `psql -h localhost -U postgres -d hplussport -f orders.sql`
