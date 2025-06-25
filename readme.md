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

## Instalar PostgreSQL 12 no Linux

```
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'; wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -; sudo apt-get update -u; sudo apt-get -y install postgresql-12
```
Para instalar o 17 basta mudar o 12 por 17

*Só copiar e colar no terminal*

## Start database server PostgreSQL
Criar o cluster:
```
sudo pg_createcluster 12 main --start
```
Iniciar o Postgresql:
```
sudo pg_ctlcluster 12 main start
```
ou
```
sudo systemctl start postgresql@12-main
```

## Alterar senha master PostgreSQL 12
```
sudo passwd postgres
```
*informe a nova senha*
```
su postgres
```
*logar com a senha alterada anteriormente*

## Alterar senha de conexão PostgreSQL

```
psql -c "ALTER USER postgres WITH PASSWORD 'nova_senha'" -d template1
```

## Limpar cache do Git para fazer funcionar o gitignore
```
git rm -r --cached . && git add . && git commit -m ".gitignore fix"
```
## Deixar o java rodando em segundo plano mesmo fechando o terminal do servidor e colocar o log na /tmp/logAplicacao.log
```
nohup java -jar app-0.0.1-SNAPSHOT.jar > /tmp/logAplicacao.log 2>&1 &
```
## Deixar o Python rodando em segundo plano mesmo fechando o terminal do servidor e colocar o log na /tmp/logAplicacao.log
```
nohup python3 /opt/script.py > /tmp/logAplicacao.log 2>&1 &
```
## Senha como argumento em comando no terminal do linux
```
#!/bin/bash

echo '33229822' | sudo -S apt update -y
sudo apt upgrade -y
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

## Como criar um certificado para um domínio usando certbot

Como criar o certificado automaticamente:
```
sudo apt-get update
sudo apt-get install certbot
sudo certbot certonly --standalone -d seu-dominio.com
```
Como criar o certificado manualmente:
```
sudo certbot certonly --manual --preferred-challenges http -d MEUSITE.COM.BR
```
Ou...
```
sudo certbot --nginx --email EMAIL --redirect --agree-tos -d MEUSITE.COM.BR
```

Como converter o cartificado em PKCS12
```
openssl pkcs12 -export -out /opt/certificado.p12 -inkey /etc/letsencrypt/live/MEUSITE.COM.BR/privkey.pem -in /etc/letsencrypt/live/MEUSITE.COM.BR/fullchain.pem
```

## Criar certificado autoassinado e transformá-lo em P12

Gere um certificado autoassinado:
```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /opt/certificado/server.key -out /opt/certificado/server.crt
```

Crie o arquivo PKCS12:
```
openssl pkcs12 -export -in /opt/certificado/server.crt -inkey /opt/certificado/server.key -out /opt/certificado/certificado.p12 -name server
```
...Coloque uma senha de exportação

Verifique o conteúdo do keystore:
Verifique o conteúdo do arquivo PKCS12 usando o comando keytool (ferramenta Java para gerenciar keystores):
```
keytool -list -keystore /opt/certificado/certificado.p12 -storetype PKCS12
```
Isso listará todas as entradas (certificados e chaves) no keystore. Verifique se o alias "server" está presente na lista.

## Configurar o KeyStore
Extrair o Certificado Público do KeyStore:
```
keytool -exportcert -alias server -keystore /opt/certificado/certificado.p12 -storetype PKCS12 -storepass fxiladmin -file /opt/certificado/server.crt
```
Criar um Novo TrustStore e Importar o Certificado:
```
keytool -importcert -alias server-cert -file /opt/certificado/server.crt -keystore /opt/certificado/truststore.jks -storepass fxiladmin -noprompt
```
Verificar o Conteúdo do TrustStore:
```
keytool -list -keystore /opt/certificado/truststore.jks -storepass fxiladmin -storetype JKS
```
Para adicionar em um projeto:
Coloque isso no application.properties:
```
server.ssl.enabled=true
server.ssl.key-store=/opt/server.p12
server.ssl.key-store-password=senha-do-keystore
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=server
```
Se for ejb:
```
System.setProperty("javax.net.ssl.trustStore", "/opt/certificado/certificado.p12");
System.setProperty("javax.net.ssl.trustStorePassword", "fxiladmin");
```
## Como criar um certificado pelo Certbot e transformar em .p12
Criar o certificado para o dominio
```
sudo certbot certonly --standalone -d example.com
```
Transformar em PKCS12
```
openssl pkcs12 -export -in /etc/letsencrypt/live/MEUSITE.COM.BR/cert.pem -inkey /etc/letsencrypt/live/MEUSITE.COM.BR/privkey.pem -out /opt/certificado.p12 -name "certificado" -CAfile /etc/letsencrypt/live/MEUSITE.COM.BR/chain.pem -caname certificado
```

## Extrair XSD em Classes JAVA:
```
xjc https://www2.correios.com.br/sistemas/encomendas/sigepweb/doc/SIGEPWEB_VALIDADOR_XML_V2.XSD
```
Caso queira criar um pacote específico:
```
xjc -p br.com.correios https://www2.correios.com.br/sistemas/encomendas/sigepweb/doc/SIGEPWEB_VALIDADOR_XML_V2.XSD
```
## Extrair WSDL em Classes JAVA:
```
wsimport -keep https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl
```
Caso queira criar um pacote específico:
```
wsimport -keep -p br.com.correios https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl
```
## Criar chave SSH git

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

## Exclusão com busca no Linux
```
find . -name "NOME_OU_EXTENSAO_DO_ARQUIVO" -type f -exec rm {} \;
```

## Localizar aquivos servidor:
```
locate ARQUIVO.pdf
```
ou
```
sudo find / -type f -iname "ARQUIVO.pdf"
```

## Listar arquivos maiores que 500MB no Terminal do Linux
```
sudo find / -type f -size +500M -exec ls -lh {} \;
```
## Ver config do TeamViwer pelo terminal
Ver o ID do teamviewer
```
sudo teamviewer --info
```
Alterar a senha do teamviewer
```
sudo teamviewer --passwd newPassword
```
## Redirecionar tráfego de uma porta para outra no UbuntuServer
Por exemplo, redirecionar o tráfego da porta 80 para a porta 5000 (faz sumir o dominio.com:5000 e fica só dominio.com)
```
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 5000
```
Salvar
```
sudo sh -c "iptables-save > /etc/iptables/rules.v4"
```
Listar os redirecionamentos e verificar se a regra foi aplicada
```
sudo iptables -t nat -L -n -v
```
Excluir uma regra usando o número da linha
```
sudo iptables -t nat -D PREROUTING 1
```
## Redirecionar tráfego e portas usando o NGINX
Entrar no arquivo de configuração
```
sudo nano /etc/nginx/sites-enabled/default
```
Configurações:
```
server {
    listen 443 ssl;
    server_name SITE.COM;
    client_max_body_size 500M; ## Tamanho dos uploads

    ssl_certificate /etc/letsencrypt/live/SITE.COM/cert.pem;
    ssl_certificate_key /etc/letsencrypt/live/SITE.COM/privkey.pem;
    ssl_trusted_certificate /etc/letsencrypt/live/SITE.COM/chain.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;
    ssl_ciphers "EECDH+AESGCM:EDH+AESGCM:AES256+EECDH:AES256+EDH";    

    ## Tamanho da requisição, feita pra aumentar o recebimento de arquivos grandes via api
    client_max_body_size 500M;

    location / {
        proxy_pass http://127.0.0.1:5000; ## Porta em que o Quarkus está rodando
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        ## Configurações de timeout
        proxy_read_timeout 1800s;
        proxy_connect_timeout 1800s;
        proxy_send_timeout 1800s;
        send_timeout 1800s;
    }
}

server {
    listen 80;
    server_name SITE.COM;
    client_max_body_size 500M; ## Tamanho dos uploads
    
    ## Tamanho da requisição, feita pra aumentar o recebimento de arquivos grandes via api
    client_max_body_size 500M;

    location / {
        proxy_pass http://127.0.0.1:5000; ## Porta em que o Quarkus está rodando
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        ## Configurações de timeout
        proxy_read_timeout 1800s;
        proxy_connect_timeout 1800s;
        proxy_send_timeout 1800s;
        send_timeout 1800s;
    }
}
```
Após isso basta reiniciar o Nginx:
```
sudo systemctl restart nginx
```

## Definir o fuso horário do Ubuntu server para São Paulo
```
sudo timedatectl set-timezone America/Sao_Paulo
```
## Converter arquivo .rtf em .html
Instalar o unrtf
```
sudo apt-get install unrtf
```
Executar
```
unrtf --html arquivo.rtf > arquivo.html
```
## Fazer backup database Postgresql:
Salvar o backup da base 'backup' na pasta /opt/backup_customizado.dump 
```
pg_dump -U postgres -h localhost -p 5432 -d backup -Fc > backup_customizado.dump
```
Se quiser apontar pra uma versão específica do pg_dump:
```
ls /usr/lib/postgresql/
```
Você verá algo como:
```
12  15  16
```
Aponte para a versão desejada:
```
/usr/lib/postgresql/16/bin/pg_dump -U postgres -h localhost -p 5432 -d nome_do_banco -Fc > /opt/backup_customizado.dump
```
Para usar o usuário root caso não seja possível autenticar com o usuário postgres:
```
sudo -u postgres /usr/lib/postgresql/16/bin/pg_restore -h localhost -p 5432 -d nome_do_banco /opt/backup_customizado.dump
```
Para restaurar:
```
sudo -u postgres /usr/lib/postgresql/16/bin/pg_restore -U postgres -d nome_do_banco -Fc /opt/backup_customizado.dump
```
## Fazer backup do Postgresql automaticamente (colocar em um arquivo .sh e chamar no crontab):
```
#!/bin/bash
## Cria um backup com data e hora
sudo PGPASSWORD="SENHA" pg_dump -U postgres -h localhost -p 5432 -d normas -Fc > /opt/PASTA_BACKUP/backup_$(date +%d-%m-%Y_%H-%M-%S).dump

## Remove backups mais antigos que 7 dias
find /opt/PASTA_BACKUP/ -name "*.dump" -type f -mtime +7 -exec rm {} \;
```
## Listas os 10 maiores PDF's em número de páginas
```
find . -type f -name "*.pdf" -exec sh -c 'pdfinfo "$1" | grep "^Pages:" | awk "{print \$2, \"$1\"}"' _ {} \; | sort -nr | head -n 10
```
## Listar os 10 maiores PDF's em tamanho
```
find . -type f -name "*.pdf" -exec du -h {} + | sort -hr | head -n 10
```
## Listar PDF's que possuem mais de 10 páginas
```
find /caminho/para/pasta/ -type f -iname "*.pdf" -print0 | while IFS= read -r -d '' pdf; do
    ## Extrai o número de páginas do PDF usando pdfinfo
    pages=$(pdfinfo "$pdf" | grep "^Pages:" | awk '{print $2}')
    
    ## Verifica se o número de páginas é maior que 10
    if [ "$pages" -gt 10 ]; then
        ## Exibe o caminho completo do arquivo PDF que atende ao critério
        echo "$pdf"
    fi
done
```
## (Postgresql) Listar todas as colunas do tipo varchar que possui tamanho 255
```
SELECT table_name, column_name, character_maximum_length
FROM information_schema.columns
WHERE character_maximum_length = 255
AND table_schema NOT IN ('information_schema', 'pg_catalog');

```
## Executar alias (atalhos) no ubuntu
Colocar os comandos no arquivo ~/.profile
```
nano ~/.profile
```
Se ele não existir colocar no ~/.bash_profile
```
~/.bash_profile
```
## Extrair informações certificado pfx
```
openssl pkcs12 -in /opt/certificado.pfx -nokeys -clcerts -legacy -passin pass:12345 | openssl x509 -noout -subject -issuer -pubkey -serial -enddate
```
## Instalar versão mais recente do BR Office
Remover o Office antigo (opcional)
```
sudo apt remove --purge libreoffice*
```
Instalar
```
sudo add-apt-repository ppa:libreoffice/ppa; sudo apt update; sudo apt install libreoffice
```
## Colocar a saída de um comando para o log:
```
COMANDO AQUI >> /tmp/logScript.log 2>&1
```
Exemplo: pyhton3 /opt/script.py >> /tmp/logScript.log 2>&1
## Saber tamanho da pasta no Linux
```
du -sh /opt/
```
## Contar número de páginas pdf usando iText
```
PdfDocument pdfPaginas = new PdfDocument(new PdfReader(new File(caminhoCompleto)));
pdfPaginas.getNumberOfPages()
```
## Remover cache do Maven
```
rm -rf ~/.m2
```
## Buscar arquivo no Ubuntu
```
sudo find / -type f -iname "Arquivo.pdf"
```
## Instalar o Caddy para certificados - Ubuntu server:
```
sudo apt install -y debian-keyring debian-archive-keyring apt-transport-https curl
curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | sudo gpg --dearmor -o /usr/share/keyrings/caddy-stable-archive-keyring.gpg
curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | sudo tee /etc/apt/sources.list.d/caddy-stable.list
sudo apt update
sudo apt install caddy
```

## Configurar o Caddy
```
sudo nano /etc/caddy/Caddyfile
```
Adicione a seguinte linha>
```
meudominio.com {
    reverse_proxy 127.0.0.1:8080
}
```

## Colocar página específica como index no Caddy
```
meudominio.com {
    route / {
        uri replace / /indexPersonalizada.xhtml
        reverse_proxy 127.0.0.1:8080
    }
}
```
Depois rode:
```
sudo caddy validate --config /etc/caddy/Caddyfile
sudo systemctl restart caddy
```
## Apagar tudo de Docker
```
sudo systemctl stop docker && sudo systemctl disable docker && sudo systemctl stop containerd && sudo systemctl disable containerd && sudo apt remove --purge docker-* -y && sudo apt autoremove -y && sudo docker rm $(docker ps -a -q) && sudo docker rmi $(docker images -a -q) && sudo docker volume rm $(docker volume ls -q) && sudo docker network prune && sudo docker system prune -a --volumes && sudo rm -rf /var/lib/docker* /etc/docker* /var/run/docker* /opt/docker* ~/.docker* /var/lib/containerd* /usr/bin/docker* /usr/local/bin/docker* && sudo groupdel docker && sudo groupdel dockerroot && sudo ps aux | grep docker | awk '{print $2}' | xargs sudo kill -9 && sudo find / -name "*docker*" -exec sudo rm -rf {} + && docker swarm leave --force && docker swarm leave --force && sudo rm -rf /var/lib/docker/swarm && sudo rm -rf ~/.docker/swarm && sudo lsof -i :2377 -t | xargs sudo kill -9 2>/dev/null || true && sudo lsof -i :7946 -t | xargs sudo kill -9 2>/dev/null || true && sudo lsof -i :4789 -t | xargs sudo kill -9 2>/dev/null || true && sudo iptables -F DOCKER && sudo iptables -F DOCKER-ISOLATION-STAGE-1 && sudo iptables -F DOCKER-ISOLATION-STAGE-2 && sudo iptables -F DOCKER-USER && sudo iptables -t nat -F DOCKER && sudo iptables -t nat -F POSTROUTING && sudo journalctl --vacuum-time=1s --vacuum-size=10M -u docker && sudo ps aux | grep docker && sudo find / -name "*docker*" && sudo iptables -L -v && sudo iptables -t nat -L -v && getent group docker && getent group dockerroot
```
## Instalar Java 21
```
sudo apt  install curl; curl -s "https://get.sdkman.io" | bash; sdk install java 21.0.6-zulu
```
## Instalar Apache + PHP
```
sudo apt update && sudo apt install apache2 php libapache2-mod-php php-mysql php-curl php-gd php-mbstring php-xml php-xmlrpc php-soap php-intl php-zip
```
## Rodar projeto Quarkus passando o path javahome
```
JAVA_HOME=/opt/21.0.6-zulu quarkus run &
```
## Listar dispositivos USB
```
lsblk
```

## Formatar dispositivo

umount /dev/sda1 - Desmontar dispositivo

mkfs.ext4 /dev/sda1 – formata em EXT4

mkfs.vfat /dev/sda1 – para FAT32 (pendrives)

mkfs.ntfs /dev/sda1 – para NTFS

Apagar tudo - sudo dd if=/dev/zero of=/dev/sda1 bs=1M status=progress

## Forçar formatação
sudo mkfs.ntfs -f /dev/sda2
