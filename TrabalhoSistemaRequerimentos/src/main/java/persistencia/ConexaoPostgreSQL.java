package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgreSQL {

    private String host = "localhost";
    private String port = "5432";
    private String banco = "sistema_requerimento";
    private String usuario = "postgres";
    private String senha = "123";

    public Connection getConexao() throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + banco;

        return DriverManager.getConnection(url, usuario, senha);
    }

}
