package br.com.sistema.ex4.model;

import java.time.LocalDateTime;

public class Extrato {
    private int id;
    private int contaId;
    private String tipoOperacao; // DEPOSITO | SAQUE
    private double valor;
    private LocalDateTime dataHora;

    public Extrato() {}
    public Extrato(int contaId, String tipoOperacao, double valor) {
        this.contaId = contaId;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getContaId() { return contaId; }
    public void setContaId(int contaId) { this.contaId = contaId; }
    public String getTipoOperacao() { return tipoOperacao; }
    public void setTipoOperacao(String tipoOperacao) { this.tipoOperacao = tipoOperacao; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    @Override
    public String toString() {
        return String.format("Extrato{tipo='%s', valor=R$%.2f, dataHora=%s}", tipoOperacao, valor, dataHora);
    }
}
