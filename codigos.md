Parar Glasfish
fuser -k 8080/tcp; fuser -k 4848/tcp;

Atalho Identar Sublime
{"keys": ["alt+shift+f"], "command": "reindent", "args": {"single_line": false}}

Chave Akismet: f66391426add

Excluir database:
https://www.postgresqltutorial.com/postgresql-drop-database/

SELECT
	pg_terminate_backend (pg_stat_activity.pid)
FROM
	pg_stat_activity
WHERE
	pg_stat_activity.datname = 'testdb1';
	
	
	
DROP DATABASE testdb1;

## PGADMIN3

Me sirvió para instalar pg 9.3:
http://www.codebind.com/linux-tutorials/install-postgresql-9-6-ubuntu-18-04-lts-linux/

Step 1: Add PostgreSQL Apt Repository

 sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/​ `lsb_release -cs`-pgdg main" @@ /etc/apt/sources.list.d/pgdg.list'

Muy importante: donde dice @@ cambiar por  doble signo mayor, Youtube no me deja escribirlo en esta descripción.

 wget -q https://www.postgresql.org/media/keys...​ -O - | sudo apt-key add -


Step 2: Install PostgreSQL

 sudo apt-get update

 sudo apt-get upgrade
 
 sudo apt-get install postgresql-9.3 pgadmin3



Step 3: Connecting to PostgreSQL

 sudo su - postgres
 psql

 To list the databases  type following command

 postgres


 How to change PostgreSQL user password? type following command to change PostgreSQL:
 ALTER USER postgres WITH PASSWORD 'test123';

 para salir, digita:     \q
