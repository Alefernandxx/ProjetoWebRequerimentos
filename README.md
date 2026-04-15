# 🎓 Sistema de Requerimentos Acadêmicos Web

Este projeto é uma aplicação web completa desenvolvida para a gestão de protocolos e requerimentos estudantis. A aplicação conecta uma interface web dinâmica a um banco de dados relacional, seguindo padrões rigorosos de arquitetura de software.

## 🛠️ Tecnologias e Ferramentas
* **Back-end:** Java 17
* **Servidor Web:** [Javalin](https://javalin.io/) (Porta 7000)
* **Banco de Dados:** PostgreSQL
* **Persistência:** JDBC (Java Database Connectivity)
* **Gerenciamento:** Maven
* **Estilização:** CSS3 integrado às rotas

## 🏗️ Arquitetura e Padrões
O projeto foi estruturado em camadas para garantir a separação de responsabilidades e facilidade de manutenção:

* **Padrão DAO (Data Access Object):** Todo o acesso ao banco de dados é centralizado em classes DAO. Não há execução de SQL diretamente nas rotas da aplicação.
* **Modelagem POJO:** As entidades (`Aluno`, `Requerimento`, `Curso`, etc.) são representadas por objetos Java puros no pacote `negocio`.
* **Arquitetura Multicamadas:** Separação clara entre Apresentação (Javalin), Negócio (Entidades) e Persistência (JDBC/DAO).

## 🚀 Funcionalidades
A aplicação entrega as seguintes funcionalidades conforme os requisitos acadêmicos:

1.  **Listagem Geral de Alunos:** Página principal que recupera todos os alunos do PostgreSQL.
2.  **Consulta de Histórico:** Visualização de todos os requerimentos vinculados a uma matrícula específica.
3.  **Abertura de Protocolos:** Formulário para criação de novos requerimentos, utilizando valores *default* do banco para data e status.
4.  **Detalhamento de Requerimento:** Visualização completa de um protocolo, incluindo observações e status atualizado.

## 💻 Como Rodar a Aplicação
1. Clone o repositório:
   ```bash
   git clone [https://github.com/Alefernandxx/ProjetoWebRequerimentos.git](https://github.com/Alefernandxx/ProjetoWebRequerimentos.git)
