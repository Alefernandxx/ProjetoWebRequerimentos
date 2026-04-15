package apresentacao;

import io.javalin.Javalin;
import negocio.Aluno;
import negocio.Requerimento;
import persistencia.RequerimentoDAO;
import persistencia.AlunoDAO;
import persistencia.TipoRequerimentoDAO;
import java.sql.SQLException; // Importante para tratar erros de banco
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Inicia o servidor na porta 7000
        Javalin app = Javalin.create().start(7000);

        // 2. Instancia os DAOs
        RequerimentoDAO requerimentoDAO = new RequerimentoDAO();
        AlunoDAO alunoDAO = new AlunoDAO();
        TipoRequerimentoDAO tipoDAO = new TipoRequerimentoDAO();

        // Rota Inicial
        app.get("/", ctx -> ctx.result("Servidor de Requerimento na porta 7000"));

        // Rota: Listar todos os requerimentos
        app.get("/requerimentos", ctx -> {
            try {
                ctx.json(requerimentoDAO.listarTodos());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao acessar o banco: " + e.getMessage());
            }
        });

        // Rota: Listar todos os tipos de requerimentos disponíveis
        app.get("/tipos", ctx -> {
            try {
                ctx.json(tipoDAO.listarTodos());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao buscar tipos: " + e.getMessage());
            }
        });

        // Rota: Buscar aluno por matrícula
        app.get("/aluno/{matricula}", ctx -> {
            String matricula = ctx.pathParam("matricula");
            try {
                ctx.json(alunoDAO.buscarPorMatricula(matricula));
            } catch (SQLException e) {
                ctx.status(500).result("Erro na busca: " + e.getMessage());
            }
        });

        app.post("/abrir", ctx -> {
            String matricula = ctx.queryParam("matricula");
            int tipoId = Integer.parseInt(ctx.queryParam("tipo"));
            String observacao = ctx.queryParam("obs");

            try {
                requerimentoDAO.abrirRequerimento(matricula, tipoId, observacao);
                ctx.result("Requerimento aberto com sucesso!");
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao salvar: " + e.getMessage());
            }
        });

        app.get("/alunos", ctx -> {
            try {

                List<Aluno> listaAlunos = alunoDAO.listarTodos();

                ctx.contentType("text/html; charset=utf-8");

                String html = "<h1>Listagem de Alunos</h1>";
                html += "<table border='1' style='border-collapse: collapse; width: 100%;'>";
                html += "<tr>" +
                        "<th>Matrícula</th>" +
                        "<th>Nome</th>" +
                        "<th>Curso</th>" +
                        "<th>Status</th>" +
                        "</tr>";

                for (Aluno a : listaAlunos) {
                    html += "<tr>";
                    html += "<td>" + a.getMatricula() + "</td>";
                    html += "<td>" + a.getUsuario().getNome() + "</td>";
                    html += "<td>" + a.getCurso().getNome() + "</td>";
                    html += "<td>Ativo</td>";
                    html += "</tr>";
                }

                html += "</table>";

                // Define o tipo de resposta como HTML para o navegador renderizar a tabela
                ctx.html(html);

            } catch (SQLException e) {
                ctx.status(500).result("Erro ao listar alunos: " + e.getMessage());
            }
        });

        // Rota: Requerimentos de um aluno específico (Página 2 do trabalho)
        app.get("/aluno/{matricula}/requerimentos", ctx -> {

            String matricula = ctx.pathParam("matricula");

            try {

                List<Requerimento> lista = requerimentoDAO.listarPorAluno(matricula);

                ctx.contentType("text/html; charset=utf-8");

                String html = "<h1>Requerimentos do Aluno: " + matricula + "</h1>";
                html += "<table border='1' style='border-collapse: collapse; width: 100%; text-align: left;'>";
                html += "<tr>" +
                        "<th>ID</th>" +
                        "<th>Tipo</th>" +
                        "<th>Data de Abertura</th>" +
                        "<th>Status</th>" +
                        "</tr>";

                if (lista.isEmpty()) {
                    html += "<tr><td colspan='4'>Nenhum requerimento encontrado para este aluno.</td></tr>";
                } else {
                    for (Requerimento r : lista) {
                        html += "<tr>";
                        html += "<td>" + r.getId() + "</td>";
                        html += "<td>" + r.getTipo().getDescricao() + "</td>";
                        html += "<td>" + r.getDataHoraAbertura() + "</td>";
                        html += "<td>" + r.getStatus() + "</td>";
                        html += "</tr>";
                    }
                }

                html += "</table>";
                html += "<br><a href='/alunos'>Voltar para listagem</a>";

                ctx.html(html);

            } catch (SQLException e) {
                ctx.status(500).result("Erro ao buscar requerimentos: " + e.getMessage());
            }
        });

        System.out.println("Servidor rodando em: http://localhost:7000");
    }
}