package br.com.sistema.ex5.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex5.model.Avaliacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {

    private Connection conn;

    public AvaliacaoDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS avaliacoes (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    funcionario_id  INTEGER NOT NULL,
                    nota            REAL    NOT NULL CHECK(nota >= 0 AND nota <= 10),
                    comentario      TEXT,
                    data            TEXT    NOT NULL,
                    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[AvaliacaoDAO] " + e.getMessage()); }
    }

    public boolean inserir(Avaliacao a) {
        if (a.getNota() < 0 || a.getNota() > 10) {
            System.err.println("[ERRO] Nota deve estar entre 0 e 10.");
            return false;
        }
        String sql = "INSERT INTO avaliacoes (funcionario_id, nota, comentario, data) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getFuncionarioId());
            ps.setDouble(2, a.getNota());
            ps.setString(3, a.getComentario());
            ps.setString(4, a.getData().toString());
            ps.executeUpdate();
            System.out.println("[OK] Avaliação registrada.");
            return true;
        } catch (SQLException e) { System.err.println("[AvaliacaoDAO] " + e.getMessage()); return false; }
    }

    /** Retorna a média das avaliações de um funcionário. */
    public double calcularMedia(int funcionarioId) {
        String sql = "SELECT AVG(nota) AS media FROM avaliacoes WHERE funcionario_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("media");
        } catch (SQLException e) { System.err.println("[AvaliacaoDAO] " + e.getMessage()); }
        return 0.0;
    }

    public List<Avaliacao> listarPorFuncionario(int funcionarioId) {
        List<Avaliacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes WHERE funcionario_id = ? ORDER BY data";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Avaliacao a = new Avaliacao();
                a.setId(rs.getInt("id"));
                a.setFuncionarioId(rs.getInt("funcionario_id"));
                a.setNota(rs.getDouble("nota"));
                a.setComentario(rs.getString("comentario"));
                a.setData(LocalDate.parse(rs.getString("data")));
                lista.add(a);
            }
        } catch (SQLException e) { System.err.println("[AvaliacaoDAO] " + e.getMessage()); }
        return lista;
    }
}
