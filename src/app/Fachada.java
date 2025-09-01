package app;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


public class Fachada {
    private final CentralDados db = CentralDados.getInstancia();

    //Clientes
    public Cliente registrarNovoCliente(String nome, String telefone) {
        return db.cadastrarCliente(nome, telefone);
    }
    public String listarClientes() {
        return db.listarClientes().stream()
                .map(Object::toString).collect(Collectors.joining("\n"));
    }

    //Cardápio
    public ItemCardapio cadastrarNovoItem(String nome, BigDecimal preco) {
        return db.cadastrarItem(nome, preco);
    }
    public String listarItensCardapio() {
        return db.listarItens().stream()
                .map(Object::toString).collect(Collectors.joining("\n"));
    }

    //Pedidos
    public Pedido criarPedido(int idCliente, List<int[]> itensQts) {
        Cliente c = db.buscarCliente(idCliente);
        if (c == null) throw new IllegalArgumentException("Cliente não encontrado: " + idCliente);
        PedidoBuilder b = new PedidoBuilder(db).paraCliente(c);
        for (int[] par : itensQts) {
            int idItem = par[0];
            int qt = par[1];
            ItemCardapio it = db.buscarItem(idItem);
            if (it == null) throw new IllegalArgumentException("Item não encontrado: " + idItem);
            b.adicionarItem(it, qt);
        }
        return b.confirmar();
    }

    public void avancarStatusPedido(int numero) {
        Pedido p = db.buscarPedido(numero);
        if (p == null) throw new IllegalArgumentException("Pedido não encontrado: " + numero);
        p.avancarStatus();
    }

    public void definirStatusPedido(int numero, StatusPedido novo) {
        Pedido p = db.buscarPedido(numero);
        if (p == null) throw new IllegalArgumentException("Pedido não encontrado: " + numero);
        p.definirStatus(novo);
    }

    public String listarPedidosPorStatus(StatusPedido status) {
        return db.listarPedidos().stream()
                .filter(p -> p.getStatus() == status)
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    //Relatórios
    public String gerarRelatorioSimplificado() {
        return RelatorioVendas.gerarSimplificado(db);
    }
    public String gerarRelatorioDetalhado() {
        return RelatorioVendas.gerarDetalhado(db);
    }
}
