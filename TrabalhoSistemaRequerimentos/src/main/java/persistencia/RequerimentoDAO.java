package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import negocio.Aluno;
import negocio.Curso;
import negocio.Requerimento;
import negocio.TipoRequerimento;
import negocio.Usuario;

public class RequerimentoDAO {

    private Requerimento mapearRequerimento(ResultSet rs) throws SQLException {

        Usuario u = new Usuario();
        u.setId(rs.getInt("usuario_id"));
        u.setNome(rs.getString("nome_usuario"));

        Curso c = new Curso();
        c.setId(rs.getInt("curso_id"));
        c.setNome(rs.getString("nome_curso"));

        Aluno a = new Aluno();
        a.setMatricula(rs.getString("matricula"));
        a.setUsuario(u);
        a.setCurso(c);

        TipoRequerimento tr = new TipoRequerimento();
        tr.setId(rs.getInt("tipo_id"));
        tr.setDescricao(rs.getString("descricao_tipo"));

        Requerimento r = new Requerimento();
        r.setId(rs.getInt("id"));
        r.setAluno(a);
        r.setTipo(tr);
        r.setDataHoraAbertura(rs.getTimestamp("data_hora_abertura"));
        r.setDataHoraEncerramento(rs.getTimestamp("data_hora_encerramento"));
        r.setObservacao(rs.getString("observacao"));
        r.setStatus(rs.getString("status"));

        return r;

    }

    private final String SQL_BASE = "SELECT r.*, a.matricula, u.id as usuario_id, u.nome as nome_usuario, " +
            "c.id as curso_id, c.nome as nome_curso, tr.id as tipo_id, tr.descricao as descricao_tipo " +
            "FROM requerimento r " +
            "JOIN aluno a ON r.aluno_matricula = a.matricula " +
            "JOIN usuario u ON a.usuario_id = u.id " +
            "JOIN curso c ON a.curso_id = c.id " +
            "JOIN tipo_requerimento tr ON r.tipo_requerimento_id = tr.id ";

    public List<Requerimento> listarTodos() throws SQLException {
        List<Requerimento> lista = new ArrayList<>();
        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(SQL_BASE);
                ResultSet rs = ps.executeQuery()) { // ResultSet rs = ps.executeQuery() faz a consulta e armazena o resultado em rs
            while (rs.next()) {
                lista.add(mapearRequerimento(rs));
            }
        }
        return lista;
    }

    public Requerimento buscarPorId(int id) throws SQLException {
        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                
                PreparedStatement ps = conn.prepareStatement(SQL_BASE + " WHERE r.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearRequerimento(rs);
                }
            }
        }
        return null; // Retorna null se não encontrar o requerimento com o ID especificado
    }

    public List<Requerimento> listarPorAluno(String matricula) throws SQLException {
        List<Requerimento> lista = new ArrayList<>();
        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(SQL_BASE + " WHERE r.aluno_matricula = ?")) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRequerimento(rs));
                }
            }
        }
        return lista;
    }

    public void abrirRequerimento(String matricula, int tipoId, String observacao) throws SQLException {
        String sql = "INSERT INTO requerimento(aluno_matricula, tipo_requerimento_id, observacao) VALUES (?, ?, ?)";

        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ps.setInt(2, tipoId);
            ps.setString(3, observacao);

            ps.executeUpdate();
        }

    }

}
