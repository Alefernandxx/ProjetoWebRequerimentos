package apresentacao;

import io.javalin.Javalin;
import negocio.Aluno;
import negocio.Requerimento;
import negocio.TipoRequerimento;
import persistencia.RequerimentoDAO;
import persistencia.AlunoDAO;
import persistencia.TipoRequerimentoDAO;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);

        RequerimentoDAO requerimentoDAO = new RequerimentoDAO();
        AlunoDAO alunoDAO = new AlunoDAO();
        TipoRequerimentoDAO tipoDAO = new TipoRequerimentoDAO();

        app.get("/", ctx -> ctx.result("Servidor de Requerimento na porta 7000"));

        app.get("/requerimentos", ctx -> {
            try {
                ctx.json(requerimentoDAO.listarTodos());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao acessar o banco: " + e.getMessage());
            }
        });

        app.get("/tipos", ctx -> {
            try {
                ctx.json(tipoDAO.listarTodos());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao buscar tipos: " + e.getMessage());
            }
        });

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
                    html += "<td><a href='/aluno/" + a.getMatricula() + "/requerimentos'>" + a.getMatricula()
                            + "</a></td>";
                    html += "<td>" + a.getUsuario().getNome() + "</td>";
                    html += "<td>" + a.getCurso().getNome() + "</td>";
                    html += "<td>Ativo</td>";
                    html += "</tr>";
                }

                html += "</table>";

                ctx.html(html);

            } catch (SQLException e) {
                ctx.status(500).result("Erro ao listar alunos: " + e.getMessage());
            }
        });

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

        app.get("/novo-requerimento", ctx -> {
            try {

                List<TipoRequerimento> tipos = tipoDAO.listarTodos();

                ctx.contentType("text/html; charset=utf-8");

                String html = "<h1>Abrir Novo Requerimento</h1>";
                html += "<form action='/novo-requerimento' method='post' style='display: flex; flex-direction: column; width: 300px; gap: 10px;'>";

                html += "<label>Matrícula do Aluno:</label>";
                html += "<input type='text' name='matricula' required placeholder='Ex: 2026000003'>";

                html += "<label>Tipo de Requerimento:</label>";
                html += "<select name='tipo_id'>";
                for (TipoRequerimento t : tipos) {
                    html += "<option value='" + t.getId() + "'>" + t.getDescricao() + "</option>";
                }
                html += "</select>";

                html += "<label>Observação:</label>";
                html += "<textarea name='observacao' rows='4'></textarea>";

                html += "<button type='submit' style='padding: 10px; cursor: pointer;'>Enviar Requerimento</button>";
                html += "</form>";
                html += "<br><a href='/alunos'>Cancelar e voltar</a>";

                ctx.html(html);
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao carregar tipos: " + e.getMessage());
            }
        });

        app.post("/novo-requerimento", ctx -> {

            String matricula = ctx.formParam("matricula");
            int tipoId = Integer.parseInt(ctx.formParam("tipo_id"));
            String observacao = ctx.formParam("observacao");

            try {

                requerimentoDAO.abrirRequerimento(matricula, tipoId, observacao);

                ctx.redirect("/aluno/" + matricula + "/requerimentos");

            } catch (SQLException e) {
                ctx.status(500).result("Erro ao salvar requerimento: " + e.getMessage());
            }
        });

        app.get("/requerimento/{id}", ctx -> {

            int id = Integer.parseInt(ctx.pathParam("id"));

            try {

                Requerimento r = requerimentoDAO.buscarPorId(id);

                ctx.contentType("text/html; charset=utf-8");

                if (r == null) {
                    ctx.status(404).html("<h1>Erro: Requerimento #" + id + " não encontrado.</h1>");
                    return;
                }

                String html = "<h1>Detalhes do Requerimento #" + r.getId() + "</h1>";
                html += "<div style='border: 1px solid #ccc; padding: 20px; border-radius: 8px; line-height: 1.6;'>";
                html += "<strong>Aluno:</strong> " + r.getAluno().getUsuario().getNome() + " ("
                        + r.getAluno().getMatricula() + ")<br>";
                html += "<strong>Tipo:</strong> " + r.getTipo().getDescricao() + "<br>";
                html += "<strong>Data de Abertura:</strong> " + r.getDataHoraAbertura() + "<br>";
                html += "<strong>Status:</strong> <span style='color: blue; font-weight: bold;'>" + r.getStatus()
                        + "</span><br>";
                html += "<strong>Observação:</strong><br><p style='background: #f9f9f9; padding: 10px; border-left: 3px solid #777;'>"
                        + r.getObservacao() + "</p>";
                html += "</div>";

                html += "<br><a href='/aluno/" + r.getAluno().getMatricula()
                        + "/requerimentos'>Voltar para lista do aluno</a>";

                ctx.html(html);

            } catch (SQLException e) {
                ctx.status(500).result("Erro ao consultar requerimento: " + e.getMessage());
            } catch (NumberFormatException e) {
                ctx.status(400).result("ID inválido fornecido na URL.");
            }
        });

        System.out.println("Servidor rodando em: http://localhost:7000");
    }
}