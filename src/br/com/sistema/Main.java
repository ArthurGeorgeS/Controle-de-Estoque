package br.com.sistema;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex1.app.AppClientes;
import br.com.sistema.ex2.app.AppEstoque;
import br.com.sistema.ex3.app.AppAgendamentos;
import br.com.sistema.ex4.app.AppBanco;
import br.com.sistema.ex5.app.AppAvaliacao;

import java.util.Scanner;

/**
 * Ponto de entrada do sistema. Exibe um menu geral que permite navegar
 * entre os cinco exercícios da lista.
 *
 * Requisitos:
 *  - Java 17+
 *  - SQLite JDBC driver no classpath (sqlite-jdbc-*.jar)
 *
 * Compilação (a partir de /projeto):
 *   javac -cp lib/sqlite-jdbc.jar -d out -sourcepath src $(find src -name "*.java")
 *
 * Execução:
 *   java -cp out:lib/sqlite-jdbc.jar br.com.sistema.Main
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        System.out.println("=========================================");
        System.out.println("  Sistema Integrado — APOO com BD");
        System.out.println("=========================================");

        do {
            System.out.println("\n----- MENU PRINCIPAL -----");
            System.out.println("1. Ex.1 — Cadastro de Clientes");
            System.out.println("2. Ex.2 — Controle de Estoque");
            System.out.println("3. Ex.3 — Agendamento de Consultas");
            System.out.println("4. Ex.4 — Sistema Bancário");
            System.out.println("5. Ex.5 — Avaliação de Funcionários");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> new AppClientes(sc).menu();
                case 2 -> new AppEstoque(sc).menu();
                case 3 -> new AppAgendamentos(sc).menu();
                case 4 -> new AppBanco(sc).menu();
                case 5 -> new AppAvaliacao(sc).menu();
                case 0 -> System.out.println("Encerrando sistema.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        ConexaoBD.fecharConexao();
        sc.close();
    }
}
