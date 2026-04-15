# 🎓 Sistema de Requerimentos Acadêmicos Web

Este projeto é uma aplicação web completa desenvolvida para a gestão de protocolos e requerimentos estudantis. A aplicação conecta uma interface web dinâmica a um banco de dados relacional PostgreSQL, seguindo padrões rigorosos de arquitetura de software para garantir escalabilidade e organização.

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java 17
* **Servidor Web:** [Javalin](https://javalin.io/) (Rodando na porta 7000)
* **Banco de Dados:** PostgreSQL
* **Persistência:** JDBC (Java Database Connectivity)
* **Gerenciamento de Dependências:** Maven
* **Interface:** HTML5 e CSS3 (Injetados via rotas)

---

## 🏗️ Arquitetura e Padrões

O projeto foi estruturado em camadas para garantir a separação de responsabilidades e facilidade de manutenção:

* **Padrão DAO (Data Access Object):** Todo o acesso ao banco de dados é centralizado em classes DAO (como `AlunoDAO` e `RequerimentoDAO`). Não há execução de SQL diretamente nas rotas da aplicação, cumprindo as regras de arquitetura segura.
* **Modelagem POJO:** As entidades (`Aluno`, `Requerimento`, `Curso`, `Usuario`, `TipoRequerimento` e `Anexo`) são representadas por objetos Java puros no pacote `negocio`.
* **Arquitetura Multicamadas:** Divisão clara entre **Apresentação** (Main/Javalin), **Negócio** (Classes de entidade) e **Persistência** (JDBC/Conexão).

---

## 🚀 Funcionalidades

A aplicação entrega as seguintes funcionalidades conforme os requisitos acadêmicos:

1.  **Listagem Geral de Alunos:** Página que recupera e exibe todos os alunos matriculados.
2.  **Consulta de Histórico:** Visualização de todos os requerimentos vinculados a uma matrícula específica através de parâmetros de rota.
3.  **Abertura de Protocolos:** Formulário interativo para criação de novos requerimentos, respeitando os valores *default* do banco para data de abertura e status inicial.
4.  **Detalhamento de Requerimento:** Visualização individual de um protocolo, exibindo observações e o status de processamento.

---

## 💻 Como Rodar a Aplicação

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Alefernandxx/ProjetoWebRequerimentos.git](https://github.com/Alefernandxx/ProjetoWebRequerimentos.git)
    ```

2.  **Configuração do Banco:**
    Edite as credenciais de acesso (host, porta, usuário e senha) no arquivo:
    `src/main/java/persistencia/ConexaoPostgreSQL.java`

3.  **Execução:**
    Utilize o Maven para compilar e iniciar o servidor:
    ```bash
    mvn compile exec:java -Dexec.mainClass="apresentacao.Main"
    ```

4.  **Acesso:**
    Abra o seu navegador em: [http://localhost:7000](http://localhost:7000)

---

**Desenvolvido por:** [Alessandro Fernandes da Silva Filho](https://github.com/Alefernandxx)
