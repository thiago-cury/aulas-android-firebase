package thiagocury.eti.br.exloginfirebase;

/**
 * Created by Aluno on 15/08/2017.
 */

public class Usuario {

    private String login;
    private String senha;

    public Usuario() {
    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
