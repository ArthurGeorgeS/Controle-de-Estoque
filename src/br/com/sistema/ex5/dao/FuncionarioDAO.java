package br.com.sistema.ex5.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex5.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private Connection conn;

    public FuncionarioDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS funcionarios (
                    id               INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome             TEXT    NOT NULL,
                    cargo            TEXT,
                    departamento_id  INTEGER NOT NULL,
                    FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[FuncionarioDAO] " + e.getMessage()); }
    }

    public boolean inserir(Funcionario f) {
        String sql = "INSERT INTO funcionarios (nome, cargo, departamento_id) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCargo());
            ps.setInt(3, f.getDepartamentoId());
            ps.executeUpdate();
            System.out.println("[OK] Funcionário cadastrado.");
            return true;
        } catch (SQLException e) { System.err.println("[FuncionarioDAO] " + e.getMessage()); return false; }
    }

    /**
     * Lista funcionários de um departamento, ordenados pela média de avaliações (decrescente).
     * Usa LEFT JOIN para incluir funcionários sem avaliações (média = 0).
     */
    public List<Funcionario> listarPorDepartamentoOrdenadoPorMedia(int departamentoId) {
        List<Funcionario> lista = new ArrayList<>();
        String sql = """
                SELECT f.*, d.nome AS dep_nome, COALESCE(AVG(a.nota), 0) AS media
                FROM funcionarios f
                JOIN departamentos d ON f.departamento_id = d.id
                LEFT JOIN avaliacoes a ON a.funcionario_id = f.id
                WHERE f.departamento_id = ?
                GROUP BY f.id
                ORDER BY media DESC
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departamentoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[FuncionarioDAO] " + e.getMessage()); }
        return lista;
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = """
                SELECT f.*, d.nome AS dep_nome, COALESCE(AVG(a.nota), 0) AS media
                FROM funcionarios f
                JOIN departamentos d ON f.departamento_id = d.id
                LEFT JOIN avaliacoes a ON a.funcionario_id = f.id
                GROUP BY f.id ORDER BY f.nome
                """;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[FuncionarioDAO] " + e.getMessage()); }
        return lista;
    }

    public Funcionario buscarPorId(int id) {
        String sql = """
                SELECT f.*, d.nome AS dep_nome, COALESCE(AVG(a.nota), 0) AS media
                FROM funcionarios f
                JOIN departamentos d ON f.departamento_id = d.id
                LEFT JOIN avaliacoes a ON a.funcionario_id = f.id
                WHERE f.id = ? GROUP BY f.id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[FuncionarioDAO] " + e.getMessage()); }
        return null;
    }

    private Funcionario mapear(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setCargo(rs.getString("cargo"));
        f.setDepartamentoId(rs.getInt("departamento_id"));
        f.setDepartamentoNome(rs.getString("dep_nome"));
        f.setMediaAvaliacoes(rs.getDouble("media"));
        return f;
    }
}
