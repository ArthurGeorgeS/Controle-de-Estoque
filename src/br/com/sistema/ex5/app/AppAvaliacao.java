package br.com.sistema.ex5.app;

import br.com.sistema.ex5.dao.AvaliacaoDAO;
import br.com.sistema.ex5.dao.DepartamentoDAO;
import br.com.sistema.ex5.dao.FuncionarioDAO;
import br.com.sistema.ex5.model.Avaliacao;
import br.com.sistema.ex5.model.Departamento;
import br.com.sistema.ex5.model.Funcionario;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para o Sistema de Avaliação de Funcionários (Exercício 5).
 */
public class AppAvaliacao {

    private final DepartamentoDAO depDAO = new DepartamentoDAO();
    private final FuncionarioDAO  funDAO = new FuncionarioDAO();
    private final AvaliacaoDAO    avDAO  = new AvaliacaoDAO();
    private final Scanner sc;

    public AppAvaliacao(Scanner sc) {
        this.sc = sc;
        depDAO.criarTabela();
        funDAO.criarTabela();
        avDAO.criarTabela();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n===== AVALIAÇÃO DE FUNCIONÁRIOS =====");
            System.out.println("1. Cadastrar departamento");
            System.out.println("2. Cadastrar funcionário");
            System.out.println("3. Registrar avaliação");
            System.out.println("4. Ver média de avaliações de um funcionário");
            System.out.println("5. Listar funcionários por departamento (ordenado por média)");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarDepartamento();
                case 2 -> cadastrarFuncionario();
                case 3 -> registrarAvaliacao();
                case 4 -> verMedia();
                case 5 -> listarPorDepartamento();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarDepartamento() {
        System.out.print("Nome do departamento: ");
        depDAO.inserir(new Departamento(sc.nextLine().trim()));
    }

    private void cadastrarFuncionario() {
        listarDepartamentos();
        System.out.print("ID do departamento: "); int depId = lerInt();
        if (depDAO.buscarPorId(depId) == null) { System.out.println("[ERRO] Departamento não encontrado."); return; }
        System.out.print("Nome: ");   String nome  = sc.nextLine().trim();
        System.out.print("Cargo: ");  String cargo = sc.nextLine().trim();
        funDAO.inserir(new Funcionario(nome, cargo, depId));
    }

    private void registrarAvaliacao() {
        funDAO.listarTodos().forEach(f -> System.out.printf("  [%d] %-20s | %s | média atual: %.2f%n",
                f.getId(), f.getNome(), f.getDepartamentoNome(), f.getMediaAvaliacoes()));
        System.out.print("ID do funcionário: ");  int funId = lerInt();
        if (funDAO.buscarPorId(funId) == null) { System.out.println("[ERRO] Funcionário não encontrado."); return; }
        System.out.print("Nota (0 a 10): ");       double nota = lerDouble();
        System.out.print("Comentário: ");           String com  = sc.nextLine().trim();
        avDAO.inserir(new Avaliacao(funId, nota, com));
    }

    private void verMedia() {
        funDAO.listarTodos().forEach(f -> System.out.printf("  [%d] %s%n", f.getId(), f.getNome()));
        System.out.print("ID do funcionário: ");
        int id = lerInt();
        Funcionario f = funDAO.buscarPorId(id);
        if (f == null) { System.out.println("Funcionário não encontrado."); return; }
        double media = avDAO.calcularMedia(id);
        System.out.printf("Média de %s: %.2f%n", f.getNome(), media);

        List<Avaliacao> avs = avDAO.listarPorFuncionario(id);
        avs.forEach(a -> System.out.printf("  - %.1f em %s: %s%n", a.getNota(), a.getData(), a.getComentario()));
    }

    private void listarPorDepartamento() {
        listarDepartamentos();
        System.out.print("ID do departamento: ");
        int depId = lerInt();
        List<Funcionario> lista = funDAO.listarPorDepartamentoOrdenadoPorMedia(depId);
        if (lista.isEmpty()) { System.out.println("Nenhum funcionário neste departamento."); return; }
        System.out.println("--- Ranking por média ---");
        for (int i = 0; i < lista.size(); i++) {
            Funcionario f = lista.get(i);
            System.out.printf("  %dº %-20s | %s | média: %.2f%n",
                    i + 1, f.getNome(), f.getCargo(), f.getMediaAvaliacoes());
        }
    }

    private void listarDepartamentos() {
        List<Departamento> deps = depDAO.listarTodos();
        if (deps.isEmpty()) System.out.println("Nenhum departamento cadastrado.");
        else deps.forEach(d -> System.out.printf("  [%d] %s%n", d.getId(), d.getNome()));
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
