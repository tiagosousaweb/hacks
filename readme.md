## Excluir database:
https://www.postgresqltutorial.com/postgresql-drop-database/

SELECT
	pg_terminate_backend (pg_stat_activity.pid)
FROM
	pg_stat_activity
WHERE
	pg_stat_activity.datname = 'testdb1';
	

DROP DATABASE testdb1;


## Instalar PostgreSql 12 no Linux

'''
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'; wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -; sudo apt-get update -u; sudo apt-get -y install postgresql-12
'''

*Só copiar e colar no terminal*

### Start database server PostgreSql

> pg_ctlcluster 12 main start
