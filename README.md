# todo-list-api

Projeto para demonstração de habilidades com Java BackEnd

###### Status
[![Build Status](https://travis-ci.com/carlos-olr/todo-list-api.svg?branch=main)](https://travis-ci.com/carlos-olr/todo-list-api)


## Descrição

Nesse projeto é criado uma aplicação para gerenciar tarefas, sendo que as tarefas sempre pertencem ao usuário logado.

O desenvolvimento foi feito com Java 11 + SpringBoot e a execução do sistema é feito de através de scripts encontrado no próprio repositório.


**OBS**: O projeto foi criado e testado em Ubuntu, talvez seja necessária alguma customização para plataforma Windows. 

### Pré-requisitos (Sistema)
- Java 11 ([openJdk11](https://openjdk.java.net/projects/jdk/11/))
- Maven 3.2.5+ ([maven](https://maven.apache.org/install.html))
- Docker 20.10.3+ ([docker](https://docs.docker.com/))
- Git 2.17.1+ ([git](https://git-scm.com/downloads))


### Pré-requisitos (Imagem)
- Java 11 ([docker-openJdk11](https://hub.docker.com/_/openjdk))
- Postgresql ([docker-postgres](https://hub.docker.com/_/postgres))


### Execução
O comando a seguir utiliza três etapas para inicialização do sistema:
```
reset && mvn clean install && ./build.sh && docker-compose up
```

1. Build utilizando maven com testes
2. Execução do script "build.sh" (detalhado abaixo)
3. Execução do docker-compose com base nas configurações da no arquivo "docker-compose.yml"

**build.sh**
```
#!/bin/bash
set -e

docker build -t todo-list-app .
```

### Aplicação Rodando
A url http://localhost:8080/swagger-ui/#/ pode ser utilizada para uma documentação técnica do formato dos JSONs, métodos utilizados e opções existentes

## Exemplos de Uso

#### PUT /user
Criação de usuário
```sh
curl --location --request PUT 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login":"carlosoliveira",
    "password": "teste"
}'
```

#### POST /auth
Geração de token de autenticação
```sh
curl --location --request POST 'localhost:8080/auth' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login":    "carlosoliveira",
    "password": "teste"
}'
```
Retorno
```json
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJsb2dpblwiOlwiY2FybG9zb2xpdmVpcmFcIn0iLCJleHAiOjE2MjM2MDk1OTd9.yki9ot33Uv9tKcyhO4OCfCgHWw9R0Y_35DsQhV8zoXM9MUJfuCx76CZpZtcwKg2pOGEQg-0JOlZ0jrP-J3Ou_Q
```

#### PUT /todo ou POST /todo/{id}
Criação e atualização de tarefas, Para se atualizar é necessário informar o ID da tarefa, só é possível alterar tarefas que perteçam ao usuário do token
```sh
curl --location --request PUT 'localhost:8080/todo' \
--header 'Authorization: Bearer TOKEN_GERADO_NO_AUTH' \
--header 'Content-Type: application/json' \
--data-raw '{
    "resumo": "resumo da tarefa",
    "descricao" : "descrição da tarefa"
}'
```
Retorno
```json
{
    "id": 42,
    "usuario": {
        "login": "carlosoliveira"
    },
    "dataInclusao": "2021-06-13T18:37:38",
    "resumo": "resumo da tarefa",
    "descricao": "descrição da tarefa",
    "dataAlteracao": "2021-06-13T18:37:38",
    "status": "PENDING"
}
```

#### GET /TODO
Listagem de tarefas por usuário, pode conter o filtro de "status"
```sh
curl --location --request GET 'localhost:8080/todo?status=COMPLETED' \
--header 'Authorization: Bearer TOKEN_GERADO_NO_AUTH'
```
```sh
curl --location --request GET 'localhost:8080/todo' \
--header 'Authorization: Bearer TOKEN_GERADO_NO_AUTH'
```
Retorno
```json
[
    {
        "id": 41,
        "usuario": {
            "login": "carlosoliveira"
        },
        "dataInclusao": "2021-06-13T18:36:03",
        "resumo": "t3",
        "descricao": "d3",
        "dataAlteracao": "2021-06-13T18:36:03",
        "status": "PENDING"
    },
    {
        "id": 42,
        "usuario": {
            "login": "carlosoliveira"
        },
        "dataInclusao": "2021-06-13T18:37:38",
        "resumo": "resumo da tarefa",
        "descricao": "descrição da tarefa",
        "dataAlteracao": "2021-06-13T18:37:38",
        "status": "PENDING"
    }
]
```

#### DELETE /todo ou /todo/{id}
Api para deletar tarefas existente que perteçam ao usuário o token informado
```sh
curl --location --request DELETE 'localhost:8080/todo' \
--header 'Authorization: Bearer TOKEN_GERADO_NO_AUTH' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 42
}'
```

### Usuário Super
A aplciaçaõ cria por padrão um usuário com "poderes" de listar todas as tarefas de todos os usuários
```json
{
    "login":    "adminSuper",
    "password": "passwordSuper"
}
```