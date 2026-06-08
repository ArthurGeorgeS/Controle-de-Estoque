package br.com.sistema.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por encapsular a conexão com o banco de dados SQLite.
 * Utiliza o padrão Singleton para garantir uma única instância de conexão.
 */
public class ConexaoBD {

    private static final String URL = "jdbc:sqlite:sistema.db";
    private static Connection instancia;

    private ConexaoBD() {}

    /**
     * Retorna a instância única de conexão com o banco (Singleton).
     */
    public static Connection getConexao() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL);
                instancia.setAutoCommit(true);
                System.out.println("[BD] Conexão estabelecida com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("[BD] Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return instancia;
    }

    /**
     * Fecha a conexão com o banco de dados, se estiver aberta.
     */
    public static void fecharConexao() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                System.out.println("[BD] Conexão encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[BD] Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
