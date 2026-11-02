# Documentação da API REST - Clientes

## Descrição

API REST desenvolvida em Java com Spring Boot para gerenciamento de clientes.

A API realiza operações CRUD (Create, Read, Update e Delete) e utiliza o banco de dados MariaDB para armazenamento persistente dos dados.

O acesso externo à API é realizado por meio do Nginx, configurado como proxy reverso na VM1.

## Endereço de acesso

```text
http://192.168.56.10/clientes
```

## Rotas disponíveis

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| POST | `/clientes` | Cadastrar um novo cliente |
| GET | `/clientes` | Listar todos os clientes |
| GET | `/clientes/{id}` | Buscar cliente por ID |
| PUT | `/clientes/{id}` | Atualizar dados de um cliente |
| DELETE | `/clientes/{id}` | Excluir cliente por ID |

## Estrutura dos dados

Os dados enviados e recebidos pela API são representados em formato JSON.

### Exemplo de cliente

```json
{
  "nome": "João Castro",
  "email": "joao@email.com",
  "telefone": "74888888888"
}
```

## Respostas HTTP

| Código | Significado |
|---|---|
| 200 | Requisição realizada com sucesso |
| 201 | Cliente cadastrado com sucesso |
| 204 | Cliente excluído com sucesso |
| 400 | Dados inválidos |
| 404 | Cliente não encontrado |
| 409 | E-mail já cadastrado |

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Data JPA
- MariaDB
- Maven
- Nginx
