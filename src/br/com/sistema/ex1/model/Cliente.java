package br.com.sistema.ex1.model;

import java.time.LocalDate;

/**
 * Classe que representa um cliente da loja.
 * Atributos encapsulados com getters e setters.
 */
public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataCadastro;

    public Cliente() {
        this.dataCadastro = LocalDate.now();
    }

    public Cliente(String nome, String cpf, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    @Override
    public String toString() {
        return String.format("Cliente{id=%d, nome='%s', cpf='%s', email='%s', telefone='%s', cadastro=%s}",
                id, nome, cpf, email, telefone, dataCadastro);
    }
}
