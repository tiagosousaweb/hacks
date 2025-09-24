# Instalar o docker e rodar um Hello World
```
sudo apt-get update -y
sudo apt-get install -y ca-certificates curl
sudo install -y -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update -y
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo docker run hello-world
```

# Recriar Imagem Docker
```
sudo docker build -t NOME_IMAGEM .
```

# Recriar sem cache
```
sudo docker build --no-cache -t NOME_IMAGEM .
```
# Fazer commit da imagem para uma nova imagem (para casos de alterações dentro de um container)
```
docker commit NOME NOVO_NOME
```

# Exportar imagem .tar
```
sudo docker save -o arquivo.tar NOME_IMAGEM
```

# Restaurar a imagem
```
sudo docker load -i arquivo.tar
```

# Rodar a imagem
# Sem passar nome do banco
```
sudo docker run -d -p 7000:7000 --add-host=host.docker.internal:host-gateway --name NOME NOME
```

# Passando nome do banco
```
sudo docker run -d -p 7000:7000 --add-host=host.docker.internal:host-gateway -e "banco=NOME_BANCO" --restart always --name NOME NOME
```

# LOG ao vivo do container startando
```
sudo docker logs -f NOME
```

# Rodar e monitorar log
```
sudo docker run -it --rm -p 7000:7000 --add-host=host.docker.internal:host-gateway NOME
```

# Navegar pelos diretórios de um container que já está rodando
```
sudo docker exec -it NOME /bin/bash
```
ou
```
sudo docker exec -it NOME /bin/sh
```

# Navegar pelos diretórios de uma imagem (vai criar um container mas vai apagar depois que sair)
```
docker run -it --rm NOME /bin/bash
```
ou
```
docker run -it --rm NOME /bin/sh
```

# Apagar todas as imagens e todos os containers
```
sudo docker rm -f $(sudo docker ps -aq) ; sudo docker rmi -f $(sudo docker images -aq)
```
# Apagar tudo de Docker
```
sudo systemctl stop docker && sudo systemctl disable docker && sudo systemctl stop containerd && sudo systemctl disable containerd && sudo apt remove --purge docker-* -y && sudo apt autoremove -y && sudo docker rm $(docker ps -a -q) && sudo docker rmi $(docker images -a -q) && sudo docker volume rm $(docker volume ls -q) && sudo docker network prune && sudo docker system prune -a --volumes && sudo rm -rf /var/lib/docker* /etc/docker* /var/run/docker* /opt/docker* ~/.docker* /var/lib/containerd* /usr/bin/docker* /usr/local/bin/docker* && sudo groupdel docker && sudo groupdel dockerroot && sudo ps aux | grep docker | awk '{print $2}' | xargs sudo kill -9 && sudo find / -name "*docker*" -exec sudo rm -rf {} + && docker swarm leave --force && docker swarm leave --force && sudo rm -rf /var/lib/docker/swarm && sudo rm -rf ~/.docker/swarm && sudo lsof -i :2377 -t | xargs sudo kill -9 2>/dev/null || true && sudo lsof -i :7946 -t | xargs sudo kill -9 2>/dev/null || true && sudo lsof -i :4789 -t | xargs sudo kill -9 2>/dev/null || true && sudo iptables -F DOCKER && sudo iptables -F DOCKER-ISOLATION-STAGE-1 && sudo iptables -F DOCKER-ISOLATION-STAGE-2 && sudo iptables -F DOCKER-USER && sudo iptables -t nat -F DOCKER && sudo iptables -t nat -F POSTROUTING && sudo journalctl --vacuum-time=1s --vacuum-size=10M -u docker && sudo ps aux | grep docker && sudo find / -name "*docker*" && sudo iptables -L -v && sudo iptables -t nat -L -v && getent group docker && getent group dockerroot
```
