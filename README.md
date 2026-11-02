# Infraestrutura de Redes com Proxy Reverso e API REST

## Sobre o projeto

Este projeto tem como objetivo implementar uma arquitetura de rede utilizando duas máquinas virtuais, com o Nginx atuando como proxy reverso para encaminhar requisições HTTP a uma API REST desenvolvida em Java com Spring Boot.

A API realiza operações CRUD (Create, Read, Update e Delete) de clientes, com persistência dos dados em um banco de dados relacional MariaDB.

## Integrante

- Stela Rocha

## Arquitetura do projeto

```text
Cliente (Windows)
       |
       | HTTP - Porta 80
       v
VM1 - Banco-Servidor
Nginx (Proxy Reverso)
IP: 192.168.56.10
       |
       | HTTP - Porta 8080
       v
VM2 - Banco-Cliente
API REST (Spring Boot)
IP: 192.168.56.11
       |
       | Porta 3306
       v
Banco de Dados MariaDB
```

## Ambiente utilizado

| Componente | Configuração |
|---|---|
| Sistema operacional | Debian 13 |
| Virtualização | Oracle VirtualBox |
| Tipo de rede | Host-Only |
| VM1 | Banco-Servidor |
| IP da VM1 | `192.168.56.10` |
| Serviço da VM1 | Nginx |
| VM2 | Banco-Cliente |
| IP da VM2 | `192.168.56.11` |
| Serviços da VM2 | API REST e MariaDB |

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Maven
- MariaDB
- Nginx
- Debian 13
- Oracle VirtualBox

## Estrutura do projeto

```text
infraestrutura-proxy-reverso/
├── api/
│   ├── .mvn/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/projeto/api_clientes/
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   ├── service/
│   │   │   │   └── ApiClientesApplication.java
│   │   │   └── resources/
│   │   ├── test/
│   │   ├── pom.xml
│   │   ├── mvnw
│   │   └── mvnw.cmd
├── cliente/
│   └── MenuClientes.java
├── database/
│   └── script.sql
├── docs/
│   └── rotas-api.md
├── nginx/
│   └── api
├── .gitignore
└── README.md
```

## Banco de dados

O banco de dados utilizado é o MariaDB, instalado na VM2 (`192.168.56.11`).

O script de criação do banco de dados e da tabela está disponível em:

`database/script.sql`

O banco se chama `api_clientes` e a tabela utilizada para armazenar os dados é `clientes`.

Para executar o script SQL na VM2, estando no diretório onde o arquivo se encontra:

```bash
sudo mariadb < script.sql
```

## Executando a API REST

A API Spring Boot está hospedada na VM2, no endereço `192.168.56.11`, utilizando a porta `8080`.

Para iniciar a aplicação na VM2:

```bash
export DB_PASSWORD='SUA_SENHA_DO_BANCO'
java -jar api-clientes-0.0.1-SNAPSHOT.jar
```

A variável `DB_PASSWORD` deve conter a senha configurada para o usuário do banco de dados.

## Configuração do Nginx

O arquivo de configuração do proxy reverso está disponível em:

`nginx/api`

Na VM1 (`192.168.56.10`), copie o arquivo para o diretório de configurações do Nginx:

```bash
sudo cp api /etc/nginx/sites-available/api
```

Ative o novo virtual host:

```bash
sudo ln -s /etc/nginx/sites-available/api /etc/nginx/sites-enabled/api
```

Desative o site padrão:

```bash
sudo rm /etc/nginx/sites-enabled/default
```

Verifique a configuração e recarregue o serviço:

```bash
sudo nginx -t
sudo systemctl reload nginx
sudo systemctl status nginx
```

## Acessando a API

As requisições são realizadas pelo endereço da VM1, utilizando o Nginx como proxy reverso.

**URL base:** `http://192.168.56.10/clientes`

O Nginx encaminha as requisições para a API Spring Boot na VM2.

## Rotas da API

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| POST | `/clientes` | Cadastrar cliente |
| GET | `/clientes` | Listar todos os clientes |
| GET | `/clientes/{id}` | Buscar cliente por ID |
| PUT | `/clientes/{id}` | Atualizar cliente |
| DELETE | `/clientes/{id}` | Excluir cliente |

A documentação detalhada das rotas, exemplos de requisições e respostas está disponível em `docs/rotas-api.md`.

## Cliente Java

O projeto também possui um cliente Java de terminal para interagir com a API por meio de um menu de opções.

Para executar:

```bash
cd cliente
javac MenuClientes.java
java MenuClientes
```

O menu permite:

- Cadastrar clientes
- Listar clientes
- Buscar clientes por ID
- Atualizar clientes
- Excluir clientes

## Testes realizados

- Comunicação entre as duas máquinas virtuais.
- Acesso à API por meio do Nginx.
- Encaminhamento de requisições HTTP pelo proxy reverso.
- Operações CRUD de clientes.
- Persistência dos dados no MariaDB.
- Testes dos endpoints da API.
- Verificação do funcionamento do Nginx e do virtual host configurado.
