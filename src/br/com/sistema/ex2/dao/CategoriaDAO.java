package br.com.sistema.ex2.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex2.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection conn;

    public CategoriaDAO() {
        this.conn = ConexaoBD.getConexao();
    }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS categorias (
                    id        INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome      TEXT NOT NULL,
                    descricao TEXT
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Erro ao criar tabela: " + e.getMessage());
        }
    }

    public boolean inserir(Categoria c) {
        String sql = "INSERT INTO categorias (nome, descricao) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());
            ps.executeUpdate();
            System.out.println("[OK] Categoria cadastrada.");
            return true;
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Erro ao inserir: " + e.getMessage());
            return false;
        }
    }

    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nome";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("id"));
                cat.setNome(rs.getString("nome"));
                cat.setDescricao(rs.getString("descricao"));
                lista.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("id"));
                cat.setNome(rs.getString("nome"));
                cat.setDescricao(rs.getString("descricao"));
                return cat;
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Erro ao buscar: " + e.getMessage());
        }
        return null;
    }
}
