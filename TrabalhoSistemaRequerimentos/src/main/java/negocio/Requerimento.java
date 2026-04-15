package negocio;

import java.util.Date;

public class Requerimento {
    private int id;
    private Aluno aluno;
    private Date dataHoraAbertura;
    private Date dataHoraEncerramento;
    private String observacao;
    private String status;
    private TipoRequerimento tipo;

    public Requerimento() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Date getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(Date dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }

    public Date getDataHoraEncerramento() {
        return dataHoraEncerramento;
    }

    public void setDataHoraEncerramento(Date dataHoraEncerramento) {
        this.dataHoraEncerramento = dataHoraEncerramento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TipoRequerimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoRequerimento tipo) {
        this.tipo = tipo;
    }

}
