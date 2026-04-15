package negocio;

public class Aluno {
    private String matricula;
    private Usuario usuario;
    private Curso curso;

    public Aluno() {

    }

    public Aluno(String matricula, Usuario usuario, Curso curso) {
        this.matricula = matricula;
        this.usuario = usuario;
        this.curso = curso;
    }

    public String getMatricula() {
        return matricula;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Curso getCurso() {
        return curso;
    }
}
