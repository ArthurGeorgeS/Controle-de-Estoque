package br.com.sistema.ex5.model;

public class Funcionario {
    private int id;
    private String nome;
    private String cargo;
    private int departamentoId;
    private String departamentoNome; // para exibição
    private double mediaAvaliacoes;  // calculada via SQL

    public Funcionario() {}
    public Funcionario(String nome, String cargo, int departamentoId) {
        this.nome = nome; this.cargo = cargo; this.departamentoId = departamentoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public int getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(int departamentoId) { this.departamentoId = departamentoId; }
    public String getDepartamentoNome() { return departamentoNome; }
    public void setDepartamentoNome(String departamentoNome) { this.departamentoNome = departamentoNome; }
    public double getMediaAvaliacoes() { return mediaAvaliacoes; }
    public void setMediaAvaliacoes(double mediaAvaliacoes) { this.mediaAvaliacoes = mediaAvaliacoes; }

    @Override
    public String toString() {
        return String.format("Funcionario{id=%d, nome='%s', cargo='%s', depto='%s', media=%.2f}",
                id, nome, cargo,
                departamentoNome != null ? departamentoNome : String.valueOf(departamentoId),
                mediaAvaliacoes);
    }
}
