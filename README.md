# Conecta Alimento

Software desenvolvido para a AEP do curso de Engenharia de Software, 6o semestre, de 2026.

## Problema

Alimentos em boas condicoes podem ser desperdicados enquanto pessoas e instituicoes enfrentam dificuldades para obter alimentos. O Conecta Alimento serve para registrar e organizar doacoes de alimentos, permitindo acompanhar seus dados basicos, validade e status.

A proposta é servir como base para futuras evolucoes que conectem doadores a instituicoes ou pessoas que necessitam dos alimentos.

## ODS relacionado

O projeto esta alinhado principalmente ao **ODS 2 - Fome Zero e Agricultura Sustentavel**, pois contribui para organizar a disponibilidade de alimentos e facilitar o aproveitamento de doacoes.

## Escopo da primeira entrega

O software atualmente permite:

- cadastrar uma doacao;
- listar todas as doacoes;
- buscar uma doacao por ID;
- buscar doacoes por status;
- buscar doacoes por nome do doador;
- atualizar uma doacao existente;
- excluir uma doacao existente.

Os status disponiveis sao:

- `DISPONIVEL`;
- `COLETADO`;
- `CANCELADO`.

## Tecnologias

- Java 25;
- Spring Boot 4.0.8;
- Spring Data MongoDB;
- Lombok;
- Servidor de Banco de Dados NoSQL (MongoDB);
- Maven Wrapper;
- JUnit 5, Mockito e Spring Boot Test;
- JaCoCo para cobertura de testes.

## Arquitetura

A aplicacao utiliza uma organizacao em camadas:

```text
src/main/java/com/aep/
|-- ConectaAlimentoApplication.java
|-- entity/
|   `-- Doacao.java
|-- enums/
|   `-- Status.java
|-- repository/
|   `-- DoacaoRepository.java
|-- service/
|   `-- DoacaoService.java
`-- ui/
        `-- AppUi.java
```

- **UI:** apresenta o menu de console e coleta os dados do usuario.
- **Service:** concentra as operacoes e regras da aplicacao.
- **Repository:** acessa o MongoDB por meio do Spring Data.
- **Entity:** representa o documento persistido na colecao `doacoes`.

## Banco de dados NoSQL

O projeto utiliza o MongoDB com a seguinte estrutura:

```text
Banco: conecta_alimento
Colecao: doacoes
```

Exemplo de documento:

```json
{
    "_id": "af8274c9-2350-4394-aa25-8b2f71ead4da",
    "nome_doador": "Mercado Central",
    "descricao_alimento": "Arroz",
    "quantidade": 20,
    "unidade_de_medida": "kg",
    "data_de_validade": "2026-12-31",
    "status": "DISPONIVEL"
}
```

O ID e gerado como UUID no Java e persistido como texto no MongoDB para evitar incompatibilidades de representacao BSON.

## Pre-requisitos

- JDK 25;
- Servidor MongoDB (Deve ser configurado no .env);

## Configuracao do MongoDB

Crie um arquivo `.env` na raiz do projeto, no mesmo nivel do `pom.xml`, contendo a URI de conexão no Mongo:

```env
MONGODB_URI=mongodb+srv://usuario:senha@cluster.mongodb.net/
```

Nao coloque aspas em volta da URI.

O nome do banco e configurado em `src/main/resources/application.properties`:

```properties
spring.mongodb.database=conecta_alimento
```

## Execucao

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Depois que o Spring Boot iniciar, o menu sera exibido no terminal:

```text
=== Conecta Alimento ===
1 - Cadastrar doação
2 - Listar doações
3 - Buscar doação
4 - Atualizar doação
5 - Excluir doação
0 - Sair
```

## Testes automatizados

Para executar todos os testes:

```powershell
.\mvnw.cmd clean test
```

No Linux ou macOS:

```bash
./mvnw clean test
```

Os testes cobrem:

- regras do `DoacaoService`;
- geracao e preservacao do ID;
- busca, atualizacao e exclusao;
- buscas por status e doador;
- fluxos principais da UI de console;
- carregamento do contexto Spring.

## Cobertura de testes

O JaCoCo gera o relatorio em:

```text
target/site/jacoco/index.html
```

O comando reproduzivel para gerar a cobertura e:

```powershell
.\mvnw.cmd clean test
```

Na validacao desta versao, foram executados 16 testes, sem falhas, com cobertura de instrucoes de 95,6%, acima do minimo de 70% exigido.
