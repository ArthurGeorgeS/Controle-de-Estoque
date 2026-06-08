package br.com.sistema.ex1.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex1.model.Cliente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações CRUD da entidade Cliente.
 */
public class ClienteDAO {

    private Connection conn;

    public ClienteDAO() {
        this.conn = ConexaoBD.getConexao();
    }

    /** Cria a tabela clientes se não existir. */
    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome          TEXT    NOT NULL,
                    cpf           TEXT    NOT NULL UNIQUE,
                    email         TEXT,
                    telefone      TEXT,
                    data_cadastro TEXT    NOT NULL
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Erro ao criar tabela: " + e.getMessage());
        }
    }

    /** Insere um novo cliente no banco. Retorna false se o CPF já existir. */
    public boolean inserir(Cliente c) {
        String sql = "INSERT INTO clientes (nome, cpf, email, telefone, data_cadastro) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefone());
            ps.setString(5, c.getDataCadastro().toString());
            ps.executeUpdate();
            System.out.println("[OK] Cliente cadastrado com sucesso.");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.err.println("[ERRO] CPF já cadastrado: " + c.getCpf());
            } else {
                System.err.println("[ClienteDAO] Erro ao inserir: " + e.getMessage());
            }
            return false;
        }
    }

    /** Lista todos os clientes cadastrados. */
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    /** Busca um cliente pelo CPF. Retorna null se não encontrado. */
    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Erro ao buscar por CPF: " + e.getMessage());
        }
        return null;
    }

    /** Atualiza email e/ou telefone de um cliente pelo CPF. */
    public boolean atualizar(String cpf, String novoEmail, String novoTelefone) {
        String sql = "UPDATE clientes SET email = ?, telefone = ? WHERE cpf = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoEmail);
            ps.setString(2, novoTelefone);
            ps.setString(3, cpf);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Cliente atualizado.");
                return true;
            }
            System.out.println("[AVISO] Nenhum cliente encontrado com o CPF informado.");
        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Erro ao atualizar: " + e.getMessage());
        }
        return false;
    }

    /** Remove um cliente pelo CPF. */
    public boolean remover(String cpf) {
        String sql = "DELETE FROM clientes WHERE cpf = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Cliente removido.");
                return true;
            }
            System.out.println("[AVISO] Cliente não encontrado.");
        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Erro ao remover: " + e.getMessage());
        }
        return false;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setEmail(rs.getString("email"));
        c.setTelefone(rs.getString("telefone"));
        c.setDataCadastro(LocalDate.parse(rs.getString("data_cadastro")));
        return c;
    }
}
