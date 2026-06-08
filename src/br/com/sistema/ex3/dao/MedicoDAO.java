package br.com.sistema.ex3.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex3.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    private Connection conn;

    public MedicoDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS medicos (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome         TEXT NOT NULL,
                    crm          TEXT NOT NULL UNIQUE,
                    especialidade TEXT
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[MedicoDAO] " + e.getMessage()); }
    }

    public boolean inserir(Medico m) {
        String sql = "INSERT INTO medicos (nome, crm, especialidade) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getCrm());
            ps.setString(3, m.getEspecialidade());
            ps.executeUpdate();
            System.out.println("[OK] Médico cadastrado.");
            return true;
        } catch (SQLException e) {
            System.err.println("[MedicoDAO] Erro ao inserir: " + e.getMessage());
            return false;
        }
    }

    public List<Medico> listarTodos() {
        List<Medico> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medicos ORDER BY nome")) {
            while (rs.next()) {
                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setNome(rs.getString("nome"));
                m.setCrm(rs.getString("crm"));
                m.setEspecialidade(rs.getString("especialidade"));
                lista.add(m);
            }
        } catch (SQLException e) { System.err.println("[MedicoDAO] " + e.getMessage()); }
        return lista;
    }

    public Medico buscarPorId(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM medicos WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setNome(rs.getString("nome"));
                m.setCrm(rs.getString("crm"));
                m.setEspecialidade(rs.getString("especialidade"));
                return m;
            }
        } catch (SQLException e) { System.err.println("[MedicoDAO] " + e.getMessage()); }
        return null;
    }
}
