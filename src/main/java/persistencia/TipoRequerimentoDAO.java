package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import negocio.TipoRequerimento;

public class TipoRequerimentoDAO {

    public List<TipoRequerimento> listarTodos() throws SQLException {
        List<TipoRequerimento> tipos = new ArrayList<>();
        String sql = "SELECT id, descricao FROM tipo_requerimento ORDER BY descricao";

        try (Connection conn = new ConexaoPostgreSQL().getConexao();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoRequerimento tipo = new TipoRequerimento();
                tipo.setId(rs.getInt("id"));
                tipo.setDescricao(rs.getString("descricao"));
                tipos.add(tipo);
            }
        }
        return tipos;
    }

}
