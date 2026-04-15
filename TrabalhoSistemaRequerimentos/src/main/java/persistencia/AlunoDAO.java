package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import negocio.Aluno;
import negocio.Curso;
import negocio.Usuario;

public class AlunoDAO {

    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setNome(rs.getString("nome_usuario"));
        usuario.setEmail(rs.getString("email"));
        usuario.setCpf(rs.getString("cpf"));

        Curso curso = new Curso();
        curso.setId(rs.getInt("curso_id"));
        curso.setNome(rs.getString("nome_curso"));

        Aluno aluno = new Aluno();
        aluno.setMatricula(rs.getString("matricula"));
        aluno.setUsuario(usuario);
        aluno.setCurso(curso);

        return aluno;
    }

    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.matricula, u.id AS usuario_id, u.nome AS nome_usuario, u.email, u.cpf, c.id AS curso_id, c.nome AS nome_curso "
                +
                "FROM aluno a " +
                "JOIN usuario u ON a.usuario_id = u.id " +
                "JOIN curso c ON a.curso_id = c.id";

        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Aluno aluno = mapearAluno(rs);
                alunos.add(aluno);
            }
        }

        return alunos;
    }

    public Aluno buscarPorMatricula(String matricula) throws SQLException {
        String sql = "SELECT a.matricula, u.id AS usuario_id, u.nome AS nome_usuario, u.email, u.cpf, c.id AS curso_id, c.nome AS nome_curso "
                +
                "FROM aluno a " +
                "JOIN usuario u ON a.usuario_id = u.id " +
                "JOIN curso c ON a.curso_id = c.id " +
                "WHERE a.matricula = ?";

        try (Connection conn = new ConexaoPostgreSQL().getConexao(); // Usa try-with-resources para garantir o fechamento do PreparedStatement e ResultSet
                PreparedStatement ps = conn.prepareStatement(sql)) { // Prepara a consulta SQL 

            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAluno(rs);
                }
            }
        }

        return null; // Retorna null se não encontrar o aluno
    }

    public List<Aluno> listarPorCurso(int cursoId) throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.matricula, u.id AS usuario_id, u.nome AS nome_usuario, u.email, u.cpf, c.id AS curso_id, c.nome AS nome_curso "
                +
                "FROM aluno a " +
                "JOIN usuario u ON a.usuario_id = u.id " +
                "JOIN curso c ON a.curso_id = c.id " +
                "WHERE c.id = ?";

        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cursoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = mapearAluno(rs);
                    alunos.add(aluno);
                }
            }
        }

        return alunos;
    }

}
