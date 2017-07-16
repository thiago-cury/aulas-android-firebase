package thiagocury.eti.br.excadastroclientefirebaseapi19;

/**
 * Created by thiagocury on 15/07/17.
 */

public class Cliente {

    private String key;
    private String nome;
    private int RG;
    private double renda;

    public Cliente() {
    }

    public Cliente(String key, String nome, int RG, double renda) {
        this.key = key;
        this.nome = nome;
        this.RG = RG;
        this.renda = renda;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRG() {
        return RG;
    }

    public void setRG(int RG) {
        this.RG = RG;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "key='" + key + '\'' +
                ", nome='" + nome + '\'' +
                ", RG=" + RG +
                ", renda=" + renda +
                '}';
    }
}