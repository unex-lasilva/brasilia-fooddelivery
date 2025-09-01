package app;

import java.util.ArrayList;
import java.util.List;


public class PedidoBuilder {
    private final CentralDados db;
    private Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();

    public PedidoBuilder(CentralDados db) {
        this.db = db;
    }

    public PedidoBuilder paraCliente(Cliente c) {
        this.cliente = c;
        return this;
    }

    public PedidoBuilder adicionarItem(ItemCardapio item, int quantidade) {
        itens.add(new ItemPedido(item, quantidade));
        return this;
    }

    public Pedido confirmar() {
        if (cliente == null) throw new IllegalStateException("Cliente não definido");
        if (itens.isEmpty()) throw new IllegalStateException("Nenhum item adicionado");
        int numero = db.proximoNumeroPedido();
        Pedido pedido = new Pedido(numero, cliente);
        for (ItemPedido it : itens) pedido.adicionarItem(it);
        pedido.confirmar();
        db.salvarPedido(pedido);
        return pedido;
    }
}
