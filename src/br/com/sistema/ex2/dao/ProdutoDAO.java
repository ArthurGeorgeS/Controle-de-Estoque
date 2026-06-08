package br.com.sistema.ex2.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex2.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Connection conn;

    public ProdutoDAO() {
        this.conn = ConexaoBD.getConexao();
    }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome         TEXT    NOT NULL,
                    preco        REAL    NOT NULL,
                    quantidade   INTEGER NOT NULL DEFAULT 0,
                    categoria_id INTEGER NOT NULL,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[ProdutoDAO] Erro ao criar tabela: " + e.getMessage());
        }
    }

    public boolean inserir(Produto p) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade, categoria_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setInt(3, p.getQuantidade());
            ps.setInt(4, p.getCategoriaId());
            ps.executeUpdate();
            System.out.println("[OK] Produto cadastrado.");
            return true;
        } catch (SQLException e) {
            System.err.println("[ProdutoDAO] Erro ao inserir: " + e.getMessage());
            return false;
        }
    }

    /** Lista produtos de uma categoria específica (JOIN). */
    public List<Produto> listarPorCategoria(int categoriaId) {
        List<Produto> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, c.nome AS cat_nome
                FROM produtos p
                JOIN categorias c ON p.categoria_id = c.id
                WHERE p.categoria_id = ?
                ORDER BY p.nome
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProdutoDAO] Erro ao listar por categoria: " + e.getMessage());
        }
        return lista;
    }

    /** Atualiza a quantidade em estoque de um produto. */
    public boolean atualizarQuantidade(int produtoId, int novaQuantidade) {
        if (novaQuantidade < 0) {
            System.err.println("[ERRO] Quantidade não pode ser negativa.");
            return false;
        }
        String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, novaQuantidade);
            ps.setInt(2, produtoId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Estoque atualizado."); return true; }
            System.out.println("[AVISO] Produto não encontrado.");
        } catch (SQLException e) {
            System.err.println("[ProdutoDAO] Erro ao atualizar quantidade: " + e.getMessage());
        }
        return false;
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS cat_nome FROM produtos p JOIN categorias c ON p.categoria_id = c.id ORDER BY p.nome";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProdutoDAO] Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setPreco(rs.getDouble("preco"));
        p.setQuantidade(rs.getInt("quantidade"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCategoriaNome(rs.getString("cat_nome"));
        return p;
    }
}
