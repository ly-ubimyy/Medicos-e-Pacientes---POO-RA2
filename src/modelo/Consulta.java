package modelo;

import excecoes.DadoInvalidoException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Consulta implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate data;
    private LocalTime horario;
    private int codigoMedico;
    private String cpfPaciente;

    public static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public Consulta(String data, String horario, int codigoMedico, String cpfPaciente)
            throws DadoInvalidoException {
        this.codigoMedico = codigoMedico;
        this.cpfPaciente  = cpfPaciente.replaceAll("[^0-9]", "");
        this.data = parseData(data);
        this.horario= parseHorario(horario);
        try
        {
            validarIdentificador();
        }
        catch (DadoInvalidoException e)
        {
            System.err.println("[Consulta] Dado inválido ao criar consulta: " + e.getMessage());
            throw e;
        }
    }

    private LocalDate parseData(String s) throws DadoInvalidoException {
        try
        {
            return LocalDate.parse(s.trim(), FMT_DATA);
        }
        catch (DateTimeParseException e)
        {
            throw new DadoInvalidoException(
                    "Data de consulta inválida: '" + s + "'. Formato esperado: dd/MM/yyyy");
        }
    }

    private LocalTime parseHorario(String s) throws DadoInvalidoException {
        try
        {
            return LocalTime.parse(s.trim(), FMT_HORA);
        }
        catch (DateTimeParseException e)
        {
            throw new DadoInvalidoException(
                    "Horário de consulta inválido: '" + s + "'. Formato esperado: HH:mm");
        }
    }

    @Override
    public String getIdentificador() {
        return data.format(FMT_DATA) + "_" + horario.format(FMT_HORA)
                + "_med" + codigoMedico + "_pac" + cpfPaciente;
    }

    @Override
    public String getNome() {
        return "Consulta " + data.format(FMT_DATA) + " " + horario.format(FMT_HORA);
    }

    public void validarIdentificador() throws DadoInvalidoException {
        if (codigoMedico <= 0)
        {
            throw new DadoInvalidoException(
                    "Código do médico na consulta deve ser positivo. Recebido: " + codigoMedico);
        }
        if (cpfPaciente == null || !cpfPaciente.matches("\\d{11}"))
        {
            throw new DadoInvalidoException(
                    "CPF do paciente na consulta é inválido: '" + cpfPaciente + "'");
        }
    }

    public boolean isFutura() {
        LocalDate hoje = LocalDate.now();
        return data.isAfter(hoje) ||
                (data.isEqual(hoje) && horario.isAfter(LocalTime.now()));
    }

    public boolean isPassada() {
        return !isFutura();
    }

    public boolean estaEntreDatas(LocalDate inicio, LocalDate fim) {
        return !data.isBefore(inicio) && !data.isAfter(fim);
    }

    public LocalDate getData() { return data; }
    public LocalTime getHorario() { return horario; }
    public int getCodigoMedico() { return codigoMedico; }
    public String getCpfPaciente() { return cpfPaciente; }

    @Override
    public String toString() {
        return String.format("Consulta: %s às %s | Médico: %d | Paciente: %s",
                data.format(FMT_DATA), horario.format(FMT_HORA), codigoMedico, cpfPaciente);
    }
}
