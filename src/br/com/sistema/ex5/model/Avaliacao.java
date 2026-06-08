package br.com.sistema.ex5.model;

import java.time.LocalDate;

public class Avaliacao {
    private int id;
    private int funcionarioId;
    private double nota;          // 0.0 a 10.0
    private String comentario;
    private LocalDate data;

    public Avaliacao() { this.data = LocalDate.now(); }
    public Avaliacao(int funcionarioId, double nota, String comentario) {
        this.funcionarioId = funcionarioId;
        this.nota = nota;
        this.comentario = comentario;
        this.data = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }
    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    @Override
    public String toString() {
        return String.format("Avaliacao{id=%d, func=%d, nota=%.1f, data=%s}", id, funcionarioId, nota, data);
    }
}
