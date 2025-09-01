package app;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CentralDados {

    private static CentralDados instancia;

    public static CentralDados getInstancia() {
        if (instancia == null) instancia = new CentralDados();
        return instancia;
    }

    private final AtomicInteger seqCliente = new AtomicInteger(1000);
    private final AtomicInteger seqItem    = new AtomicInteger(5000);
    private final AtomicInteger seqPedido  = new AtomicInteger(1);

    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();
    private final Map<Integer, ItemCardapio> itens = new LinkedHashMap<>();
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();

    private CentralDados() {}

    //Clientes
    public Cliente cadastrarCliente(String nome, String telefone) {
        int id = seqCliente.getAndIncrement();
        Cliente c = new Cliente(id, nome, telefone);
        clientes.put(id, c);
        return c;
    }
    public Collection<Cliente> listarClientes() { return clientes.values(); }
    public Cliente buscarCliente(int id) { return clientes.get(id); }

    //Cardápio
    public ItemCardapio cadastrarItem(String nome, BigDecimal preco) {
        int id = seqItem.getAndIncrement();
        ItemCardapio i = new ItemCardapio(id, nome, preco);
        itens.put(id, i);
        return i;
    }
    public Collection<ItemCardapio> listarItens() { return itens.values(); }
    public ItemCardapio buscarItem(int id) { return itens.get(id); }

    //Pedidos
    int proximoNumeroPedido() { return seqPedido.getAndIncrement(); }
    void salvarPedido(Pedido p) { pedidos.put(p.getNumero(), p); }
    public Pedido buscarPedido(int numero) { return pedidos.get(numero); }
    public Collection<Pedido> listarPedidos() { return pedidos.values(); }

    //Relatórios
    public BigDecimal totalArrecadado() {
        return pedidos.values().stream()
                .map(Pedido::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
