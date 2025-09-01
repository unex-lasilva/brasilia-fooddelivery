package app;

import java.math.BigDecimal;
import java.util.Objects;


public class ItemPedido {
    private final ItemCardapio item;
    private final int quantidade;
    private final BigDecimal precoUnitNoMomento;

    public ItemPedido(ItemCardapio item, int quantidade) {
        this.item = Objects.requireNonNull(item);
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");
        this.quantidade = quantidade;
        this.precoUnitNoMomento = item.getPreco();
    }

    public ItemCardapio getItem() { return item; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitNoMomento() { return precoUnitNoMomento; }

    public BigDecimal subtotal() {
        return precoUnitNoMomento.multiply(new BigDecimal(quantidade));
    }

    @Override
    public String toString() {
        return String.format("%dx %s (R$ %s) = R$ %s",
                quantidade, item.getNome(), precoUnitNoMomento, subtotal());
    }
}
