package br.com.sistema.ex3.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex3.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private Connection conn;

    public PacienteDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS pacientes (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome     TEXT NOT NULL,
                    cpf      TEXT NOT NULL UNIQUE,
                    telefone TEXT
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[PacienteDAO] " + e.getMessage()); }
    }

    public boolean inserir(Paciente p) {
        String sql = "INSERT INTO pacientes (nome, cpf, telefone) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getTelefone());
            ps.executeUpdate();
            System.out.println("[OK] Paciente cadastrado.");
            return true;
        } catch (SQLException e) {
            System.err.println("[PacienteDAO] Erro ao inserir: " + e.getMessage());
            return false;
        }
    }

    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pacientes ORDER BY nome")) {
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                lista.add(p);
            }
        } catch (SQLException e) { System.err.println("[PacienteDAO] " + e.getMessage()); }
        return lista;
    }

    public Paciente buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM pacientes WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                return p;
            }
        } catch (SQLException e) { System.err.println("[PacienteDAO] " + e.getMessage()); }
        return null;
    }
}
