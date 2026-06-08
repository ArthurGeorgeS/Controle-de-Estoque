package br.com.sistema.ex1.app;

import br.com.sistema.ex1.dao.ClienteDAO;
import br.com.sistema.ex1.model.Cliente;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para o Sistema de Cadastro de Clientes (Exercício 1).
 */
public class AppClientes {

    private final ClienteDAO dao = new ClienteDAO();
    private final Scanner sc;

    public AppClientes(Scanner sc) {
        this.sc = sc;
        dao.criarTabela();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n===== CLIENTES =====");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por CPF");
            System.out.println("4. Atualizar e-mail/telefone");
            System.out.println("5. Remover cliente");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> buscar();
                case 4 -> atualizar();
                case 5 -> remover();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.print("Nome: ");        String nome = sc.nextLine().trim();
        System.out.print("CPF: ");         String cpf  = sc.nextLine().trim();
        System.out.print("E-mail: ");      String email = sc.nextLine().trim();
        System.out.print("Telefone: ");    String tel  = sc.nextLine().trim();
        dao.inserir(new Cliente(nome, cpf, email, tel));
    }

    private void listar() {
        List<Cliente> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("Nenhum cliente cadastrado."); return; }
        lista.forEach(c -> System.out.printf("  [%d] %s | CPF: %s | %s | %s | desde %s%n",
                c.getId(), c.getNome(), c.getCpf(), c.getEmail(), c.getTelefone(), c.getDataCadastro()));
    }

    private void buscar() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        Cliente c = dao.buscarPorCpf(cpf);
        if (c == null) System.out.println("Cliente não encontrado.");
        else System.out.println("  " + c);
    }

    private void atualizar() {
        System.out.print("CPF do cliente: ");  String cpf = sc.nextLine().trim();
        System.out.print("Novo e-mail: ");     String email = sc.nextLine().trim();
        System.out.print("Novo telefone: ");   String tel = sc.nextLine().trim();
        dao.atualizar(cpf, email, tel);
    }

    private void remover() {
        System.out.print("CPF do cliente a remover: ");
        dao.remover(sc.nextLine().trim());
    }

    private int lerInt() {
        try { int v = Integer.parseInt(sc.nextLine().trim()); return v; }
        catch (NumberFormatException e) { return -1; }
    }
}
