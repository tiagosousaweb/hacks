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
sudo pg_ctlcluster 12 main start
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
sudo dpkg-query -l > /opt/programas.txt
```
Procurar pelo nome do programa e depois executar:
```
sudo dkpg -P nome-do-programa
```

## Para instalar e reiniciar o MySQL no Ubuntu, você pode seguir estas etapas:

sudo apt-get update
sudo apt-get install mysql-server

Configuração do MySQL:

sudo systemctl status mysql

## Reiniciar o serviço do MySQL:

sudo systemctl restart mysql


## Definir uma senha para o usuário root do MySQL
sudo mysql

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'nova_senha';

FLUSH PRIVILEGES;


## Caso dê erro ao iniciar o MySQL
Verifique se há algum processo relacionado ao MySQL em execução usando o seguinte comando:
sudo lsof -i :3306
sudo reboot

Após reiniciar o sistema, tente iniciar o serviço MySQL usando o comando 'sudo systemctl start mysql' novamente.


## Para restaurar a base digite:
mysql -u nome_de_usuario -p nome_do_banco_de_dados < /path/to/backup.sql

Caso não dê certo, executar os comandos abaixo:
mysql -u nome_do_usuario -p

CREATE DATABASE nome_da_base;

USE nome_da_base;

SOURCE /caminho/para/backup_geral.sql;

## Como criar um certificado para um domónio usando certbot

Crie o certificado manualmente:
```
sudo certbot certonly --manual --preferred-challenges http -d MEUSITE.COM.BR
```
Ou...
```
sudo certbot --nginx --email EMAIL --redirect --agree-tos -d MEUSITE.COM.BR
```

Converta o certificado em chave .p12
```
openssl pkcs12 -export -out /opt/certificado.p12 -inkey /etc/letsencrypt/live/MEUSITE.COM.BR/privkey.pem -in /etc/letsencrypt/live/MEUSITE.COM.BR/fullchain.pem
```

## Colocar certificado autoassinado no Spring Boot

Gere um certificado autoassinado:
```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /opt/server.key -out /opt/server.crt
```

Crie o arquivo PKCS12 que irá para o application.properties:
```
openssl pkcs12 -export -in /opt/server.crt -inkey /opt/server.key -out /opt/server.p12 -name server
```

Verifique o conteúdo do keystore:
Verifique o conteúdo do arquivo PKCS12 usando o comando keytool (ferramenta Java para gerenciar keystores):
```
keytool -list -keystore /opt/server.p12 -storetype PKCS12
```
Isso listará todas as entradas (certificados e chaves) no keystore. Verifique se o alias "server" está presente na lista.


...Coloque uma senha de exportação

Coloque isso no application.properties:
```
server.ssl.enabled=true
server.ssl.key-store=/opt/server.p12
server.ssl.key-store-password=senha-do-keystore
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=server
```

# Extrair XSD em Classes JAVA:
```
xjc https://www2.correios.com.br/sistemas/encomendas/sigepweb/doc/SIGEPWEB_VALIDADOR_XML_V2.XSD
```
Caso queira criar um pacote específico:
```
xjc -p br.com.correios https://www2.correios.com.br/sistemas/encomendas/sigepweb/doc/SIGEPWEB_VALIDADOR_XML_V2.XSD
```
# Extrair WSDL em Classes JAVA:
```
wsimport -keep https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl
```
Caso queira criar um pacote específico:
```
wsimport -keep -p br.com.correios https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl
```
# Criar chave SSH git

Criar a pasta (se não existir)
```
cd /opt; mkdir chave-git
```
Gerar a chave
```
ssh-keygen -t ed25519 -b 4096 -C EMAIL -f /opt/chave-git/NOME_CHAVE
```
Inicie o agente ssh
```
eval "$(ssh-agent -s)"
```
depois execute...
```
ssh-add /opt/chave-git/NOME_CHAVE
```
Abra o arquivo config dentro de ~/.ssh. Se o arquivo nao existir, crie-o:

```
nano ~/.ssh/config
```
Depois cole esse conteúdo (caso for Bitbucket):
```
Host bitbucket.org
  AddKeysToAgent yes
  IdentityFile /opt/chave-git/NOME_CHAVE
PubkeyAcceptedKeyTypes +ssh-rsa
```
Depois cole esse conteúdo (caso for Github):
```
Host github.com
  AddKeysToAgent yes
  IdentityFile /opt/chave-git/NOME_CHAVE
PubkeyAcceptedKeyTypes +ssh-rsa
```
Faça um teste:
BitBucket: 
```
ssh -T git@bitbucket.org
```
Github: 
```
ssh -T git@github.com
```

# Exclusão com busca no Linux
```
find . -name "NOME_OU_EXTENSAO_DO_ARQUIVO" -type f -exec rm {} \;
```

# Localizar aquivos servidor:
```
locate ARQUIVO.pdf
```
ou
```
sudo find / -type f -iname "ARQUIVO.pdf"
```

# Listar arquivos maiores que 500MB no Terminal do Linux
```
sudo find / -type f -size +500M -exec ls -lh {} \;
```
# Ver config do TeamViwer pelo terminal
Ver o ID do teamviewer
```
sudo teamviewer --info
```
Alterar a senha do teamviewer
```
sudo teamviewer --passwd newPassword
```
