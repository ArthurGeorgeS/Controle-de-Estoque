package br.com.sistema.ex4.model;

public class Correntista {
    private int id;
    private String nome;
    private String cpf;
    private String email;

    public Correntista() {}
    public Correntista(String nome, String cpf, String email) {
        this.nome = nome; this.cpf = cpf; this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("Correntista{id=%d, nome='%s', cpf='%s'}", id, nome, cpf);
    }
}
