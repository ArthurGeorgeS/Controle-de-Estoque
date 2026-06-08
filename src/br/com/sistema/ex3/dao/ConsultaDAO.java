package br.com.sistema.ex3.dao;

import br.com.sistema.banco.ConexaoBD;
import br.com.sistema.ex3.model.Consulta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    private Connection conn;

    public ConsultaDAO() { this.conn = ConexaoBD.getConexao(); }

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS consultas (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    paciente_id INTEGER NOT NULL,
                    medico_id   INTEGER NOT NULL,
                    data_hora   TEXT    NOT NULL,
                    status      TEXT    NOT NULL DEFAULT 'AGENDADA',
                    observacoes TEXT,
                    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
                    FOREIGN KEY (medico_id)   REFERENCES medicos(id)
                );
                """;
        try (Statement stmt = conn.createStatement()) { stmt.execute(sql); }
        catch (SQLException e) { System.err.println("[ConsultaDAO] " + e.getMessage()); }
    }

    public boolean agendar(Consulta c) {
        String sql = "INSERT INTO consultas (paciente_id, medico_id, data_hora, status, observacoes) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPacienteId());
            ps.setInt(2, c.getMedicoId());
            ps.setString(3, c.getDataHora().toString());
            ps.setString(4, c.getStatus());
            ps.setString(5, c.getObservacoes());
            ps.executeUpdate();
            System.out.println("[OK] Consulta agendada.");
            return true;
        } catch (SQLException e) {
            System.err.println("[ConsultaDAO] Erro ao agendar: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelar(int consultaId) {
        String sql = "UPDATE consultas SET status = 'CANCELADA' WHERE id = ? AND status = 'AGENDADA'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, consultaId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Consulta cancelada."); return true; }
            System.out.println("[AVISO] Consulta não encontrada ou já cancelada.");
        } catch (SQLException e) { System.err.println("[ConsultaDAO] " + e.getMessage()); }
        return false;
    }

    public List<Consulta> listarPorMedico(int medicoId) {
        return listarComFiltro("medico_id", medicoId);
    }

    public List<Consulta> listarPorPaciente(int pacienteId) {
        return listarComFiltro("paciente_id", pacienteId);
    }

    private List<Consulta> listarComFiltro(String campo, int valor) {
        List<Consulta> lista = new ArrayList<>();
        String sql = String.format("""
                SELECT c.*, p.nome AS pac_nome, m.nome AS med_nome
                FROM consultas c
                JOIN pacientes p ON c.paciente_id = p.id
                JOIN medicos   m ON c.medico_id   = m.id
                WHERE c.%s = ?
                ORDER BY c.data_hora
                """, campo);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, valor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { System.err.println("[ConsultaDAO] " + e.getMessage()); }
        return lista;
    }

    private Consulta mapear(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getInt("id"));
        c.setPacienteId(rs.getInt("paciente_id"));
        c.setMedicoId(rs.getInt("medico_id"));
        c.setDataHora(LocalDateTime.parse(rs.getString("data_hora")));
        c.setStatus(rs.getString("status"));
        c.setObservacoes(rs.getString("observacoes"));
        c.setPacienteNome(rs.getString("pac_nome"));
        c.setMedicoNome(rs.getString("med_nome"));
        return c;
    }
}
