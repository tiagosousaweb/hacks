## Excluir database:
https://www.postgresqltutorial.com/postgresql-drop-database/

```
SELECT
	pg_terminate_backend (pg_stat_activity.pid)
FROM
	pg_stat_activity
WHERE
	pg_stat_activity.datname = 'testdb1';
	

DROP DATABASE testdb1;
```

## Instalar PostgreSql 12 no Linux

```
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'; wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -; sudo apt-get update -u; sudo apt-get -y install postgresql-12
```

*Só copiar e colar no terminal*

## Start database server PostgreSql

```
pg_ctlcluster 12 main start
```
ou
```
sudo systemctl start postgresql@12-main
```

## Alterar senha master PostgreSql 12
*Logar como sudo no postgres*
```
sudo passwd postgres
```
*informe a nova senha*
```
su postgres
```
*logar com a senha alterada anteriormente*

## Alterar senha de conexão PostgreSql 12

```
psql -c "ALTER USER postgres WITH PASSWORD 'nova_senha'" -d template1
```

## Limpar cache do Git para fazer funcionar o gitignore
```
git rm -r --cached . && git add . && git commit -m ".gitignore fix"
```
## Deixar o java rodando em segundo plano mesmo fechando o terminal do servidor
```
nohup java -jar app-0.0.1-SNAPSHOT.jar &
```
## Senha como argumento em comando no terminal do linux
```
#!/bin/bash

echo 'SUA_SENHA' | sudo -S COMANDO_AQUI
```
*A parte #!/bin/bash é somente para quando o comando for colocado dentro de um arquivo .sh*

## Atualizar driver's Linux
```
sudo pkcon update && apt upgrade

sudo pkcon install linux-headers-$(uname -r) build-essential

sudo apt install broadcom-sta-dkms
```

## Remover programa instalado via arquivo .DEB no Linux
Salvar os nomes de todos os programas no arquivo lista.txt na pasta /opt/
```
sudo dpkg -l > /opt/lista.txt
```
Procurar pelo nome do programa e depois executar:
```
sudo dkpg -P nome-do-programa
```
