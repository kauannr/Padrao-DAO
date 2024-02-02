package DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    // Fazer e pegar a conexao
    public static Connection getconnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    // Fechar conexao
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    // carregar as propriedade do Banco
    private static Properties loadProperties() {
        try (FileInputStream caminho = new FileInputStream(
                "C:/Users/kauan/Downloads/CURSO UDEMY/CURSO UDEMY/BancoDeDados-JDBC/jdbc1/properties.txt")) {
            Properties props = new Properties();
            props.load(caminho);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    // Fechamentos
    public static void closeStatemet(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
