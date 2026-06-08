package br.com.sistema.ex3.model;

import java.time.LocalDateTime;

public class Consulta {
    private int id;
    private int pacienteId;
    private int medicoId;
    private LocalDateTime dataHora;
    private String status; // AGENDADA | CANCELADA
    private String observacoes;

    // Campos de exibição (JOIN)
    private String pacienteNome;
    private String medicoNome;

    public Consulta() { this.status = "AGENDADA"; }
    public Consulta(int pacienteId, int medicoId, LocalDateTime dataHora, String observacoes) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = "AGENDADA";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getPacienteNome() { return pacienteNome; }
    public void setPacienteNome(String pacienteNome) { this.pacienteNome = pacienteNome; }
    public String getMedicoNome() { return medicoNome; }
    public void setMedicoNome(String medicoNome) { this.medicoNome = medicoNome; }

    @Override
    public String toString() {
        return String.format("Consulta{id=%d, paciente='%s', medico='%s', dataHora=%s, status='%s'}",
                id,
                pacienteNome != null ? pacienteNome : String.valueOf(pacienteId),
                medicoNome != null ? medicoNome : String.valueOf(medicoId),
                dataHora, status);
    }
}
