package br.com.sistema.ex2.model;

/**
 * Representa um produto do estoque. Possui relacionamento N:1 com Categoria.
 */
public class Produto {

    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private int categoriaId;
    private String categoriaNome; // para exibição (JOIN)

    public Produto() {}

    public Produto(String nome, double preco, int quantidade, int categoriaId) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }

    @Override
    public String toString() {
        return String.format("Produto{id=%d, nome='%s', preco=R$%.2f, qtd=%d, categoria='%s'}",
                id, nome, preco, quantidade, categoriaNome != null ? categoriaNome : String.valueOf(categoriaId));
    }
}
