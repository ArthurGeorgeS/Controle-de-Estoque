package br.com.sistema.ex4.app;

import br.com.sistema.ex4.dao.ContaDAO;
import br.com.sistema.ex4.dao.CorrentistaDAO;
import br.com.sistema.ex4.dao.ExtratoDAO;
import br.com.sistema.ex4.model.ContaBancaria;
import br.com.sistema.ex4.model.Extrato;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para o Sistema Bancário Simplificado (Exercício 4).
 */
public class AppBanco {

    private final CorrentistaDAO corDAO = new CorrentistaDAO();
    private final ContaDAO contaDAO = new ContaDAO();
    private final ExtratoDAO extDAO = new ExtratoDAO();
    private final Scanner sc;

    public AppBanco(Scanner sc) {
        this.sc = sc;
        corDAO.criarTabela();
        extDAO.criarTabela();
        contaDAO.criarTabela();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n===== BANCO =====");
            System.out.println("1. Cadastrar correntista");
            System.out.println("2. Abrir conta");
            System.out.println("3. Depositar");
            System.out.println("4. Sacar");
            System.out.println("5. Consultar saldo");
            System.out.println("6. Ver extrato");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarCorrentista();
                case 2 -> abrirConta();
                case 3 -> depositar();
                case 4 -> sacar();
                case 5 -> consultarSaldo();
                case 6 -> verExtrato();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarCorrentista() {
        System.out.print("Nome: ");   String nome = sc.nextLine().trim();
        System.out.print("CPF: ");    String cpf  = sc.nextLine().trim();
        System.out.print("Email: ");  String email = sc.nextLine().trim();
        corDAO.inserir(new br.com.sistema.ex4.model.Correntista(nome, cpf, email));
    }

    private void abrirConta() {
        corDAO.listarTodos().forEach(c -> System.out.printf("  [%d] %s%n", c.getId(), c.getNome()));
        System.out.print("ID do correntista: ");     int corId = lerInt();
        if (corDAO.buscarPorId(corId) == null) { System.out.println("[ERRO] Correntista não encontrado."); return; }
        System.out.print("Número da conta: ");       String num = sc.nextLine().trim();
        System.out.print("Depósito inicial: R$ ");   double saldo = lerDouble();
        contaDAO.inserir(new ContaBancaria(num, saldo, corId));
    }

    private void depositar() {
        selecionarConta();
        System.out.print("ID da conta: ");   int id = lerInt();
        System.out.print("Valor: R$ ");      double v = lerDouble();
        contaDAO.depositar(id, v);
    }

    private void sacar() {
        selecionarConta();
        System.out.print("ID da conta: ");  int id = lerInt();
        System.out.print("Valor: R$ ");     double v = lerDouble();
        contaDAO.sacar(id, v);
    }

    private void consultarSaldo() {
        selecionarConta();
        System.out.print("ID da conta: ");
        ContaBancaria c = contaDAO.buscarPorId(lerInt());
        if (c == null) { System.out.println("Conta não encontrada."); return; }
        System.out.printf("Saldo atual da conta %s: R$%.2f%n", c.getNumeroConta(), c.getSaldo());
    }

    private void verExtrato() {
        selecionarConta();
        System.out.print("ID da conta: ");
        List<Extrato> lista = extDAO.listarPorConta(lerInt());
        if (lista.isEmpty()) { System.out.println("Nenhuma movimentação."); return; }
        System.out.println("--- EXTRATO ---");
        lista.forEach(e -> System.out.printf("  %-10s | R$%10.2f | %s%n",
                e.getTipoOperacao(), e.getValor(), e.getDataHora()));
    }

    private void selecionarConta() {
        contaDAO.listarTodas().forEach(c -> System.out.printf(
                "  [%d] Conta: %s | Titular: %s | Saldo: R$%.2f%n",
                c.getId(), c.getNumeroConta(), c.getCorrentistaNome(), c.getSaldo()));
    }

    private int lerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double lerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); }
        catch (NumberFormatException e) { return 0.0; }
    }
}
