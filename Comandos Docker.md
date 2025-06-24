# Instalar o docker e rodar um Hello World
sudo apt-get update -y
sudo apt-get install -y ca-certificates curl
sudo install -y -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo \
  "deb \[arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc\] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update -y
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo docker run hello-world

# Exportar imagem .tar
sudo docker save -o arquivo.tar NOME

# Restaurar a imagem
sudo docker load -i arquivo.tar

# Recriar Imagem Docker
sudo docker build -t NOME .

# Recriar sem cache
sudo docker build --no-cache -t NOME .

# Rodar a imagem
# Sem passar nome do banco
sudo docker run -d -p 7000:7000 --add-host=host.docker.internal:host-gateway --name NOME NOME

# Passando nome do banco
sudo docker run -d -p 7000:7000 --add-host=host.docker.internal:host-gateway -e "banco=notario" --restart always --name api-notario NOME

# LOG ao vivo do container startando
sudo docker logs -f api-notario

# Rodar e monitorar log
sudo docker run -it --rm -p 7000:7000 --add-host=host.docker.internal:host-gateway api-notario

# Navegar pelos diretórios de um container que já está rodando
sudo docker exec -it NOME /bin/bash
ou
sudo docker exec -it NOME /bin/sh

# Navegar pelos diretórios de uma imagem (vai criar um container mas vai apagar depois que sair)
docker run -it --rm NOME /bin/bash
ou
docker run -it --rm NOME /bin/sh

# Apagar todas as imagens e todos os containers
sudo docker rm -f $(sudo docker ps -aq) && sudo docker rmi -f $(sudo docker images -aq)
