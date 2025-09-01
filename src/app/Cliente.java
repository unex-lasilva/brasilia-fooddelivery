package app;

public class Cliente {
    private final int id;
    private final String nome;
    private final String telefone;

    public Cliente(int id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }

    @Override
    public String toString() {
        return String.format("[%d] %s (tel: %s)", id, nome, telefone);
    }
}
