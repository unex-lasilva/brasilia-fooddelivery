package app;

import java.math.BigDecimal;

public class ItemCardapio {
    private final int id;
    private final String nome;
    private final BigDecimal preco;

    public ItemCardapio(int id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }

    @Override
    public String toString() {
        return String.format("[%d] %s - R$ %s", id, nome, preco);
    }
}
