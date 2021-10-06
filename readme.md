## Config Notário:

## Pools de Conexões do JDBC

Nome do Pool: notarioPool

User: postgres

DatabaseName: notario

Password: fxiladmin

ServerName: localhost

PortNumber: 5432

## Recursos do JDBC

Nome JNDI: jdbc/notario

Nome do Pool: notarioPool


## Novo Realm

Nome: notarioRealm

Contexto do JAAS: jdbcRealm
  
JNDI: jdbc/notario
 
Tabela de Usuários: v_usuario_grupo
 
Coluna de Nomes de Usuários: username
 
Coluna de Senhas: senha
 
Tabela de Grupo: v_usuario_grupo
 
Coluna de Nomes de Grupos: grupo
 
Algoritmo de Síntese: MD5
 
Algoritmo de Criptografia de Senha: MD5
 
## Chave Akismet: 
f66391426add

## Excluir database:
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
 
 ## Configurar Oracle JDK no Ubuntu
 
 Steps.
1. Download tar ball and move to /usr/lib

2.Update your java laternatives using below scripts

sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/java/jdk1.7.0_65/bin/java" 1

sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/java/jdk1.7.0_65/bin/javac" 1

sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/java/jdk1.7.0_65/bin/javaws" 1

3. Update your ~/.bashrc file (sudo gedit ~/.bashrc)

##add these

export JAVA_HOME=/usr/lib/java/jdk1.8.0_77

export PATH="$PATH:$JAVA_HOME/bin"

4. Done!!!

Doing this the easy way.

sudo add-apt-repository ppa:webupd8team/java

sudo apt-get update

sudo apt-get install oracle-java8-installer





Win7 Professional Versão PTBR - X17-59247
