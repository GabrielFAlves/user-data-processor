# 📊 User Data Processor

Sistema de processamento e gerenciamento de dados de usuários com suporte a múltiplos formatos (CSV, JSON, XML).

## 🚀 Como Executar

### Com Docker (Recomendado)

```bash
docker-compose up --build
```

Acesse: http://localhost:8080

## 🧪 Testando a API

### Upload de arquivos
```bash
# CSV
curl -X POST http://localhost:8080/api/users/upload \
  -F "file=@examples/users.csv" \
  -F "fileType=CSV"

# JSON
curl -X POST http://localhost:8080/api/users/upload \
  -F "file=@examples/users.json" \
  -F "fileType=JSON"

# XML
curl -X POST http://localhost:8080/api/users/upload \
  -F "file=@examples/users.xml" \
  -F "fileType=XML"
```

### Consultar dados
```bash
# JSON (padrão)
curl http://localhost:8080/api/users

# CSV
curl http://localhost:8080/api/users?format=csv

# XML
curl http://localhost:8080/api/users?format=xml
```

### Buscar por ID
```bash
curl http://localhost:8080/api/users/1
```

### Buscar por origem
```bash
curl http://localhost:8080/api/users/source/CSV
```

### Deletar usuário
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 📋 Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/users/upload` | Upload de arquivo (CSV/JSON/XML) |
| GET | `/api/users` | Listar todos (suporta ?format=json/csv/xml) |
| GET | `/api/users/{id}` | Buscar por ID |
| GET | `/api/users/source/{source}` | Buscar por origem (CSV/JSON/XML) |
| DELETE | `/api/users/{id}` | Deletar usuário |
| GET | `/actuator/health` | Health check |

## 📄 Formatos de Arquivo

### CSV
```csv
name,email
John Doe,john.doe@example.com
```

### JSON
```json
[
  {
    "name": "John Doe",
    "email": "john.doe@example.com"
  }
]
```

### XML
```xml
<users>
  <user>
    <n>John Doe</n>
    <email>john.doe@example.com</email>
  </user>
</users>
```

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.5.7
- MySQL 8.0
- Docker
- Maven

## 📚 Documentação

Swagger: `docs/swagger.yaml`

Visualizar: https://editor.swagger.io/

## 🗄️ Banco de Dados

```bash
# Acessar MySQL (Docker)
docker exec -it userdb-mysql mysql -u root -proot userdb

# Consultar dados
SELECT * FROM users;
```
