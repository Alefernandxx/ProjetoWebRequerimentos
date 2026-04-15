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

    private static final String CSS = "<style>" +
            "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; color: #333; margin: 40px; line-height: 1.6; }"
            +
            "h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }" +
            "table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 5px rgba(0,0,0,0.1); border-radius: 8px; overflow: hidden; margin-top: 20px; }"
            +
            "th { background-color: #3498db; color: white; padding: 15px; text-align: left; }" +
            "td { padding: 12px 15px; border-bottom: 1px solid #ddd; }" +
            "tr:hover { background-color: #f1f1f1; }" +
            "form { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); max-width: 500px; }"
            +
            "label { font-weight: bold; display: block; margin-top: 15px; }" +
            "input, select, textarea { width: 100%; padding: 12px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px; }"
            +
            "button { background-color: #27ae60; color: white; border: none; padding: 12px 20px; border-radius: 4px; cursor: pointer; font-size: 16px; margin-top: 20px; transition: background 0.3s; width: 100%; }"
            +
            "button:hover { background-color: #219150; }" +
            ".detalhes-box { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); border-left: 5px solid #3498db; }"
            +
            ".obs-box { background: #f9f9f9; padding: 15px; border-left: 3px solid #777; margin-top: 10px; font-style: italic; }"
            +
            "a { color: #3498db; text-decoration: none; font-weight: bold; }" +
            "a:hover { text-decoration: underline; }" +
            ".status { font-weight: bold; color: #e67e22; }" +
            "</style>";

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);

        RequerimentoDAO requerimentoDAO = new RequerimentoDAO();
        AlunoDAO alunoDAO = new AlunoDAO();
        TipoRequerimentoDAO tipoDAO = new TipoRequerimentoDAO();

        
        app.get("/", ctx -> {
            ctx.contentType("text/html; charset=utf-8");
            ctx.html(CSS + "<h1>Sistema de Requerimentos Acadêmicos</h1>" +
                    "<p>Bem-vindo ao portal de solicitações.</p>" +
                    "<ul>" +
                    "<li><a href='/alunos'>Ver Listagem de Alunos</a></li>" +
                    "<li><a href='/novo-requerimento'>Abrir Novo Requerimento</a></li>" +
                    "</ul>");
        });

        app.get("/alunos", ctx -> {
            try {
                List<Aluno> listaAlunos = alunoDAO.listarTodos();
                ctx.contentType("text/html; charset=utf-8");

                StringBuilder html = new StringBuilder(CSS + "<h1>Listagem de Alunos</h1>");
                html.append("<table><tr><th>Matrícula</th><th>Nome</th><th>Curso</th><th>Status</th></tr>");

                for (Aluno a : listaAlunos) {
                    html.append("<tr>");
                    html.append("<td><a href='/aluno/").append(a.getMatricula()).append("/requerimentos'>")
                            .append(a.getMatricula()).append("</a></td>");
                    html.append("<td>").append(a.getUsuario().getNome()).append("</td>");
                    html.append("<td>").append(a.getCurso().getNome()).append("</td>");
                    html.append("<td><span class='status'>Ativo</span></td>");
                    html.append("</tr>");
                }
                html.append("</table><br><a href='/'>Voltar ao Início</a>");
                ctx.html(html.toString());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao listar alunos: " + e.getMessage());
            }
        });

        app.get("/aluno/{matricula}/requerimentos", ctx -> {
            String matricula = ctx.pathParam("matricula");
            try {
                List<Requerimento> lista = requerimentoDAO.listarPorAluno(matricula);
                ctx.contentType("text/html; charset=utf-8");

                StringBuilder html = new StringBuilder(CSS + "<h1>Requerimentos do Aluno: " + matricula + "</h1>");
                html.append(
                        "<table><tr><th>ID</th><th>Tipo</th><th>Data de Abertura</th><th>Status</th><th>Ações</th></tr>");

                if (lista.isEmpty()) {
                    html.append("<tr><td colspan='5'>Nenhum requerimento encontrado.</td></tr>");
                } else {
                    for (Requerimento r : lista) {
                        html.append("<tr>");
                        html.append("<td>").append(r.getId()).append("</td>");
                        html.append("<td>").append(r.getTipo().getDescricao()).append("</td>");
                        html.append("<td>").append(r.getDataHoraAbertura()).append("</td>");
                        html.append("<td><span class='status'>").append(r.getStatus()).append("</span></td>");
                        html.append("<td><a href='/requerimento/").append(r.getId()).append("'>Ver Detalhes</a></td>");
                        html.append("</tr>");
                    }
                }
                html.append("</table><br><a href='/alunos'>Voltar para Alunos</a>");
                ctx.html(html.toString());
            } catch (SQLException e) {
                ctx.status(500).result("Erro ao buscar requerimentos: " + e.getMessage());
            }
        });

        app.get("/novo-requerimento", ctx -> {
            try {
                List<TipoRequerimento> tipos = tipoDAO.listarTodos();
                ctx.contentType("text/html; charset=utf-8");

                StringBuilder html = new StringBuilder(CSS + "<h1>Abrir Novo Requerimento</h1>");
                html.append("<form action='/novo-requerimento' method='post'>");
                html.append("<label>Matrícula do Aluno:</label>");
                html.append("<input type='text' name='matricula' required placeholder='Ex: 2026000003'>");

                html.append("<label>Tipo de Requerimento:</label>");
                html.append("<select name='tipo_id'>");
                for (TipoRequerimento t : tipos) {
                    html.append("<option value='").append(t.getId()).append("'>").append(t.getDescricao())
                            .append("</option>");
                }
                html.append("</select>");

                html.append("<label>Observação:</label>");
                html.append(
                        "<textarea name='observacao' rows='4' placeholder='Descreva sua solicitação...'></textarea>");

                html.append("<button type='submit'>Enviar Requerimento</button>");
                html.append("</form><br><a href='/alunos'>Cancelar</a>");

                ctx.html(html.toString());
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
                ctx.status(500).result("Erro ao salvar: " + e.getMessage());
            }
        });

        app.get("/requerimento/{id}", ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Requerimento r = requerimentoDAO.buscarPorId(id);
                ctx.contentType("text/html; charset=utf-8");

                if (r == null) {
                    ctx.status(404).html(CSS + "<h1>Requerimento não encontrado</h1><a href='/alunos'>Voltar</a>");
                    return;
                }

                String html = CSS + "<h1>Detalhes do Requerimento #" + r.getId() + "</h1>";
                html += "<div class='detalhes-box'>";
                html += "<strong>Aluno:</strong> " + r.getAluno().getUsuario().getNome() + " ("
                        + r.getAluno().getMatricula() + ")<br>";
                html += "<strong>Tipo:</strong> " + r.getTipo().getDescricao() + "<br>";
                html += "<strong>Data de Abertura:</strong> " + r.getDataHoraAbertura() + "<br>";
                html += "<strong>Status:</strong> <span class='status'>" + r.getStatus() + "</span><br>";
                html += "<strong>Observação:</strong><div class='obs-box'>"
                        + (r.getObservacao() != null ? r.getObservacao() : "Sem observações.") + "</div>";
                html += "</div>";
                html += "<br><a href='/aluno/" + r.getAluno().getMatricula()
                        + "/requerimentos'>Voltar para a lista</a>";

                ctx.html(html);
            } catch (SQLException e) {
                ctx.status(500).result("Erro: " + e.getMessage());
            } catch (NumberFormatException e) {
                ctx.status(400).result("ID inválido.");
            }
        });

        System.out.println("Servidor rodando em: http://localhost:7000");
    }
}