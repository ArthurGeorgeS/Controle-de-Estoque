package br.com.sistema.ex3.model;

public class Paciente {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;

    public Paciente() {}
    public Paciente(String nome, String cpf, String telefone) {
        this.nome = nome; this.cpf = cpf; this.telefone = telefone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return String.format("Paciente{id=%d, nome='%s', cpf='%s', tel='%s'}", id, nome, cpf, telefone);
    }
}
