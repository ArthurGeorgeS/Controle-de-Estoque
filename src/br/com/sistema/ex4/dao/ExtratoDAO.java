package br.com.sistema.ex4.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex4.model.Extrato;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExtratoDAO {

    private Connection conn;

    public ExtratoDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS extrato (
                    id             INTEGER PRIMARY KEY AUTOINCREMENT,
                    conta_id       INTEGER NOT NULL,
                    tipo_operacao  TEXT    NOT NULL,
                    valor          REAL    NOT NULL,
                    data_hora      TEXT    NOT NULL,
                    FOREIGN KEY (conta_id) REFERENCES contas(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[ExtratoDAO] " + e.getMessage()); }
    }

    public boolean registrar(Extrato e) {
        String sql = "INSERT INTO extrato (conta_id, tipo_operacao, valor, data_hora) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getContaId());
            ps.setString(2, e.getTipoOperacao());
            ps.setDouble(3, e.getValor());
            ps.setString(4, e.getDataHora().toString());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("[ExtratoDAO] Erro ao registrar: " + ex.getMessage());
            return false;
        }
    }

    public List<Extrato> listarPorConta(int contaId) {
        List<Extrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM extrato WHERE conta_id = ? ORDER BY data_hora";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Extrato e = new Extrato();
                e.setId(rs.getInt("id"));
                e.setContaId(rs.getInt("conta_id"));
                e.setTipoOperacao(rs.getString("tipo_operacao"));
                e.setValor(rs.getDouble("valor"));
                e.setDataHora(LocalDateTime.parse(rs.getString("data_hora")));
                lista.add(e);
            }
        } catch (SQLException e) { System.err.println("[ExtratoDAO] " + e.getMessage()); }
        return lista;
    }
}
