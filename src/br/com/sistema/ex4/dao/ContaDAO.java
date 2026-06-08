package br.com.sistema.ex4.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex4.model.ContaBancaria;
import br.com.sistema.ex4.model.Extrato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para ContaBancaria. Gerencia depósito, saque e consulta de saldo,
 * registrando cada operação na tabela extrato atomicamente.
 */
public class ContaDAO {

    private Connection conn;
    private ExtratoDAO extratoDAO;

    public ContaDAO() {
        this.conn = ConexaoBD.getConexao();
        this.extratoDAO = new ExtratoDAO();
    }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS contas (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    numero_conta    TEXT    NOT NULL UNIQUE,
                    saldo           REAL    NOT NULL DEFAULT 0,
                    correntista_id  INTEGER NOT NULL,
                    FOREIGN KEY (correntista_id) REFERENCES correntistas(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[ContaDAO] " + e.getMessage()); }
    }

    public boolean inserir(ContaBancaria c) {
        String sql = "INSERT INTO contas (numero_conta, saldo, correntista_id) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNumeroConta());
            ps.setDouble(2, c.getSaldo());
            ps.setInt(3, c.getCorrentistaId());
            ps.executeUpdate();
            System.out.println("[OK] Conta criada: " + c.getNumeroConta());
            return true;
        } catch (SQLException e) {
            System.err.println("[ContaDAO] Erro ao criar conta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza depósito: atualiza saldo e registra no extrato.
     */
    public boolean depositar(int contaId, double valor) {
        if (valor <= 0) { System.err.println("[ERRO] Valor de depósito deve ser positivo."); return false; }
        String sql = "UPDATE contas SET saldo = saldo + ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, valor);
            ps.setInt(2, contaId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                extratoDAO.registrar(new Extrato(contaId, "DEPOSITO", valor));
                System.out.printf("[OK] Depósito de R$%.2f realizado.%n", valor);
                return true;
            }
            System.out.println("[ERRO] Conta não encontrada.");
        } catch (SQLException e) { System.err.println("[ContaDAO] Erro no depósito: " + e.getMessage()); }
        return false;
    }

    /**
     * Realiza saque: verifica saldo, atualiza e registra no extrato.
     * Impede saque com saldo insuficiente.
     */
    public boolean sacar(int contaId, double valor) {
        if (valor <= 0) { System.err.println("[ERRO] Valor de saque deve ser positivo."); return false; }
        ContaBancaria conta = buscarPorId(contaId);
        if (conta == null) { System.err.println("[ERRO] Conta não encontrada."); return false; }
        if (conta.getSaldo() < valor) {
            System.err.printf("[ERRO] Saldo insuficiente. Saldo atual: R$%.2f | Valor solicitado: R$%.2f%n",
                    conta.getSaldo(), valor);
            return false;
        }
        String sql = "UPDATE contas SET saldo = saldo - ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, valor);
            ps.setInt(2, contaId);
            ps.executeUpdate();
            extratoDAO.registrar(new Extrato(contaId, "SAQUE", valor));
            System.out.printf("[OK] Saque de R$%.2f realizado.%n", valor);
            return true;
        } catch (SQLException e) { System.err.println("[ContaDAO] Erro no saque: " + e.getMessage()); }
        return false;
    }

    public ContaBancaria buscarPorId(int id) {
        String sql = """
                SELECT c.*, co.nome AS cor_nome
                FROM contas c JOIN correntistas co ON c.correntista_id = co.id
                WHERE c.id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { System.err.println("[ContaDAO] " + e.getMessage()); }
        return null;
    }

    public List<ContaBancaria> listarTodas() {
        List<ContaBancaria> lista = new ArrayList<>();
        String sql = """
                SELECT c.*, co.nome AS cor_nome
                FROM contas c JOIN correntistas co ON c.correntista_id = co.id
                ORDER BY c.numero_conta
                """;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ContaDAO] " + e.getMessage()); }
        return lista;
    }

    private ContaBancaria mapear(ResultSet rs) throws SQLException {
        ContaBancaria c = new ContaBancaria();
        c.setId(rs.getInt("id"));
        c.setNumeroConta(rs.getString("numero_conta"));
        c.setSaldo(rs.getDouble("saldo"));
        c.setCorrentistaId(rs.getInt("correntista_id"));
        c.setCorrentistaNome(rs.getString("cor_nome"));
        return c;
    }
}
