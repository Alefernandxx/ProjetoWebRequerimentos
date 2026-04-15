package negocio;

public class Curso {
    private int id;
    private String nome;
    private String site;
    private String turno;
    private int duracao;

    public Curso() {
    }

    public Curso(int id, String nome, String site, String turno, int duracao) {
        this.id = id;
        this.nome = nome;
        this.site = site;
        this.turno = turno;
        this.duracao = duracao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSite() {
        return site;
    }

    public String getTurno() {
        return turno;
    }

    public int getDuracao() {
        return duracao;
    }

}
