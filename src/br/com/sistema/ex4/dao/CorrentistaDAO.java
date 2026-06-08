package br.com.sistema.ex4.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex4.model.Correntista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorrentistaDAO {

    private Connection conn;

    public CorrentistaDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS correntistas (
                    id    INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome  TEXT NOT NULL,
                    cpf   TEXT NOT NULL UNIQUE,
                    email TEXT
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[CorrentistaDAO] " + e.getMessage()); }
    }

    public boolean inserir(Correntista c) {
        String sql = "INSERT INTO correntistas (nome, cpf, email) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.executeUpdate();
            System.out.println("[OK] Correntista cadastrado.");
            return true;
        } catch (SQLException e) {
            System.err.println("[CorrentistaDAO] Erro: " + e.getMessage());
            return false;
        }
    }

    public List<Correntista> listarTodos() {
        List<Correntista> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM correntistas ORDER BY nome")) {
            while (rs.next()) {
                Correntista c = new Correntista();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }
        } catch (SQLException e) { System.err.println("[CorrentistaDAO] " + e.getMessage()); }
        return lista;
    }

    public Correntista buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM correntistas WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Correntista c = new Correntista();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                return c;
            }
        } catch (SQLException e) { System.err.println("[CorrentistaDAO] " + e.getMessage()); }
        return null;
    }
}
