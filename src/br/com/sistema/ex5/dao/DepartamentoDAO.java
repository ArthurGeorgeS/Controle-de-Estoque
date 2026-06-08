package br.com.sistema.ex5.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex5.model.Departamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {

    private Connection conn;

    public DepartamentoDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS departamentos (
                    id   INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[DepartamentoDAO] " + e.getMessage()); }
    }

    public boolean inserir(Departamento d) {
        String sql = "INSERT INTO departamentos (nome) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNome());
            ps.executeUpdate();
            System.out.println("[OK] Departamento cadastrado.");
            return true;
        } catch (SQLException e) { System.err.println("[DepartamentoDAO] " + e.getMessage()); return false; }
    }

    public List<Departamento> listarTodos() {
        List<Departamento> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departamentos ORDER BY nome")) {
            while (rs.next()) {
                Departamento d = new Departamento();
                d.setId(rs.getInt("id"));
                d.setNome(rs.getString("nome"));
                lista.add(d);
            }
        } catch (SQLException e) { System.err.println("[DepartamentoDAO] " + e.getMessage()); }
        return lista;
    }

    public Departamento buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM departamentos WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Departamento d = new Departamento();
                d.setId(rs.getInt("id"));
                d.setNome(rs.getString("nome"));
                return d;
            }
        } catch (SQLException e) { System.err.println("[DepartamentoDAO] " + e.getMessage()); }
        return null;
    }
}
