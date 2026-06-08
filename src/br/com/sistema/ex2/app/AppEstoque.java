package br.com.sistema.ex2.app;

import br.com.sistema.ex2.dao.CategoriaDAO;
import br.com.sistema.ex2.dao.ProdutoDAO;
import br.com.sistema.ex2.model.Categoria;
import br.com.sistema.ex2.model.Produto;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para o Sistema de Controle de Estoque (Exercício 2).
 */
public class AppEstoque {

    private final CategoriaDAO catDAO = new CategoriaDAO();
    private final ProdutoDAO prodDAO = new ProdutoDAO();
    private final Scanner sc;

    public AppEstoque(Scanner sc) {
        this.sc = sc;
        catDAO.criarTabela();
        prodDAO.criarTabela();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n===== ESTOQUE =====");
            System.out.println("1. Cadastrar categoria");
            System.out.println("2. Cadastrar produto");
            System.out.println("3. Listar produtos por categoria");
            System.out.println("4. Atualizar quantidade em estoque");
            System.out.println("5. Listar todas as categorias");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarCategoria();
                case 2 -> cadastrarProduto();
                case 3 -> listarPorCategoria();
                case 4 -> atualizarEstoque();
                case 5 -> listarCategorias();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarCategoria() {
        System.out.print("Nome da categoria: ");    String nome = sc.nextLine().trim();
        System.out.print("Descrição: ");            String desc = sc.nextLine().trim();
        catDAO.inserir(new Categoria(nome, desc));
    }

    private void cadastrarProduto() {
        listarCategorias();
        System.out.print("ID da categoria: ");      int catId = lerInt();
        if (catDAO.buscarPorId(catId) == null) { System.out.println("[ERRO] Categoria não encontrada."); return; }
        System.out.print("Nome do produto: ");      String nome = sc.nextLine().trim();
        System.out.print("Preço: ");                double preco = lerDouble();
        System.out.print("Quantidade inicial: ");   int qtd = lerInt();
        prodDAO.inserir(new Produto(nome, preco, qtd, catId));
    }

    private void listarPorCategoria() {
        listarCategorias();
        System.out.print("ID da categoria: ");
        int id = lerInt();
        List<Produto> lista = prodDAO.listarPorCategoria(id);
        if (lista.isEmpty()) { System.out.println("Nenhum produto nessa categoria."); return; }
        lista.forEach(p -> System.out.printf("  [%d] %s | R$%.2f | Qtd: %d%n",
                p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()));
    }

    private void atualizarEstoque() {
        prodDAO.listarTodos().forEach(p -> System.out.printf("  [%d] %s (qtd atual: %d)%n",
                p.getId(), p.getNome(), p.getQuantidade()));
        System.out.print("ID do produto: ");        int id = lerInt();
        System.out.print("Nova quantidade: ");       int qtd = lerInt();
        prodDAO.atualizarQuantidade(id, qtd);
    }

    private void listarCategorias() {
        List<Categoria> cats = catDAO.listarTodas();
        if (cats.isEmpty()) { System.out.println("Nenhuma categoria cadastrada."); return; }
        cats.forEach(c -> System.out.printf("  [%d] %s%n", c.getId(), c.getNome()));
    }

    private int lerInt() {
        try { int v = Integer.parseInt(sc.nextLine().trim()); return v; }
        catch (NumberFormatException e) { return -1; }
    }

    private double lerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); }
        catch (NumberFormatException e) { return 0.0; }
    }
}
