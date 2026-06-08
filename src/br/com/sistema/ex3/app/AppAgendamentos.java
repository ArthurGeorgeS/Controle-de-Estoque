package br.com.sistema.ex3.app;

import br.com.sistema.ex3.dao.ConsultaDAO;
import br.com.sistema.ex3.dao.MedicoDAO;
import br.com.sistema.ex3.dao.PacienteDAO;
import br.com.sistema.ex3.model.Consulta;
import br.com.sistema.ex3.model.Medico;
import br.com.sistema.ex3.model.Paciente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para o Sistema de Agendamento (Exercício 3).
 */
public class AppAgendamentos {

    private final PacienteDAO pacDAO = new PacienteDAO();
    private final MedicoDAO medDAO = new MedicoDAO();
    private final ConsultaDAO conDAO = new ConsultaDAO();
    private final Scanner sc;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AppAgendamentos(Scanner sc) {
        this.sc = sc;
        pacDAO.criarTabela();
        medDAO.criarTabela();
        conDAO.criarTabela();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n===== AGENDAMENTO =====");
            System.out.println("1. Cadastrar paciente");
            System.out.println("2. Cadastrar médico");
            System.out.println("3. Agendar consulta");
            System.out.println("4. Cancelar consulta");
            System.out.println("5. Consultas por médico");
            System.out.println("6. Consultas por paciente");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarPaciente();
                case 2 -> cadastrarMedico();
                case 3 -> agendar();
                case 4 -> cancelar();
                case 5 -> listarPorMedico();
                case 6 -> listarPorPaciente();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarPaciente() {
        System.out.print("Nome: ");      String nome = sc.nextLine().trim();
        System.out.print("CPF: ");       String cpf  = sc.nextLine().trim();
        System.out.print("Telefone: ");  String tel  = sc.nextLine().trim();
        pacDAO.inserir(new Paciente(nome, cpf, tel));
    }

    private void cadastrarMedico() {
        System.out.print("Nome: ");           String nome = sc.nextLine().trim();
        System.out.print("CRM: ");            String crm  = sc.nextLine().trim();
        System.out.print("Especialidade: ");  String esp  = sc.nextLine().trim();
        medDAO.inserir(new Medico(nome, crm, esp));
    }

    private void agendar() {
        System.out.println("Pacientes:");
        pacDAO.listarTodos().forEach(p -> System.out.printf("  [%d] %s%n", p.getId(), p.getNome()));
        System.out.print("ID do paciente: ");  int pacId = lerInt();
        if (pacDAO.buscarPorId(pacId) == null) { System.out.println("[ERRO] Paciente não encontrado."); return; }

        System.out.println("Médicos:");
        medDAO.listarTodos().forEach(m -> System.out.printf("  [%d] %s (%s)%n", m.getId(), m.getNome(), m.getEspecialidade()));
        System.out.print("ID do médico: ");    int medId = lerInt();
        if (medDAO.buscarPorId(medId) == null) { System.out.println("[ERRO] Médico não encontrado."); return; }

        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        String dtStr = sc.nextLine().trim();
        LocalDateTime dt;
        try { dt = LocalDateTime.parse(dtStr, FMT); }
        catch (DateTimeParseException e) { System.out.println("[ERRO] Formato de data inválido."); return; }

        System.out.print("Observações: ");  String obs = sc.nextLine().trim();
        conDAO.agendar(new Consulta(pacId, medId, dt, obs));
    }

    private void cancelar() {
        System.out.print("ID da consulta a cancelar: ");
        conDAO.cancelar(lerInt());
    }

    private void listarPorMedico() {
        medDAO.listarTodos().forEach(m -> System.out.printf("  [%d] %s%n", m.getId(), m.getNome()));
        System.out.print("ID do médico: ");
        List<Consulta> lista = conDAO.listarPorMedico(lerInt());
        imprimirConsultas(lista);
    }

    private void listarPorPaciente() {
        pacDAO.listarTodos().forEach(p -> System.out.printf("  [%d] %s%n", p.getId(), p.getNome()));
        System.out.print("ID do paciente: ");
        List<Consulta> lista = conDAO.listarPorPaciente(lerInt());
        imprimirConsultas(lista);
    }

    private void imprimirConsultas(List<Consulta> lista) {
        if (lista.isEmpty()) { System.out.println("Nenhuma consulta encontrada."); return; }
        lista.forEach(c -> System.out.printf(
                "  [%d] Paciente: %-20s | Médico: %-20s | %s | %s%n",
                c.getId(), c.getPacienteNome(), c.getMedicoNome(),
                c.getDataHora().format(FMT), c.getStatus()));
    }

    private int lerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
