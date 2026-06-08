package br.com.sistema.ex4.model;

public class ContaBancaria {
    private int id;
    private String numeroConta;
    private double saldo;
    private int correntistaId;
    private String correntistaNome; // para exibição

    public ContaBancaria() {}
    public ContaBancaria(String numeroConta, double saldoInicial, int correntistaId) {
        this.numeroConta = numeroConta;
        this.saldo = saldoInicial;
        this.correntistaId = correntistaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public int getCorrentistaId() { return correntistaId; }
    public void setCorrentistaId(int correntistaId) { this.correntistaId = correntistaId; }
    public String getCorrentistaNome() { return correntistaNome; }
    public void setCorrentistaNome(String correntistaNome) { this.correntistaNome = correntistaNome; }

    @Override
    public String toString() {
        return String.format("Conta{id=%d, numero='%s', saldo=R$%.2f, correntista='%s'}",
                id, numeroConta, saldo,
                correntistaNome != null ? correntistaNome : String.valueOf(correntistaId));
    }
}
