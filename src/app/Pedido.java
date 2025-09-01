package app;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int numero;
    private final Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();
    private LocalDateTime dataHoraCriacao;
    private StatusPedido status = null; // só vira ACEITO após confirmação

    Pedido(int numero, Cliente cliente) {
        this.numero = numero;
        this.cliente = cliente;
    }

    void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    void confirmar() {
        if (itens.isEmpty()) throw new IllegalStateException("Pedido sem itens não pode ser confirmado");
        this.dataHoraCriacao = LocalDateTime.now();
        this.status = StatusPedido.ACEITO;
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public LocalDateTime getDataHoraCriacao() { return dataHoraCriacao; }
    public StatusPedido getStatus() { return status; }

    public BigDecimal total() {
        return itens.stream()
                .map(ItemPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void avancarStatus() {
        if (status == null) throw new IllegalStateException("Pedido ainda não confirmado");
        switch (status) {
            case ACEITO -> status = StatusPedido.PREPARANDO;
            case PREPARANDO -> status = StatusPedido.FEITO;
            case FEITO -> status = StatusPedido.AGUARDANDO_ENTREGADOR;
            case AGUARDANDO_ENTREGADOR -> status = StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> status = StatusPedido.ENTREGUE;
            case ENTREGUE -> throw new IllegalStateException("Pedido já está ENTREGUE");
        }
        // Hook para futuras notificações
        if (status == StatusPedido.SAIU_PARA_ENTREGA) {
            NotificadorPedidos.notifySaiuParaEntrega(this);
        }
    }

    public void definirStatus(StatusPedido novo) {
        // valida transições rígidas
        if (status == null && novo != StatusPedido.ACEITO)
            throw new IllegalStateException("Primeiro status deve ser ACEITO");
        if (status != null) {
            boolean permitido =
                (status == StatusPedido.ACEITO && novo == StatusPedido.PREPARANDO) ||
                (status == StatusPedido.PREPARANDO && novo == StatusPedido.FEITO) ||
                (status == StatusPedido.FEITO && novo == StatusPedido.AGUARDANDO_ENTREGADOR) ||
                (status == StatusPedido.AGUARDANDO_ENTREGADOR && novo == StatusPedido.SAIU_PARA_ENTREGA) ||
                (status == StatusPedido.SAIU_PARA_ENTREGA && novo == StatusPedido.ENTREGUE);
            if (!permitido) throw new IllegalStateException("Transição de status inválida: " + status + " -> " + novo);
        }
        this.status = novo;
        if (status == StatusPedido.SAIU_PARA_ENTREGA) {
            NotificadorPedidos.notifySaiuParaEntrega(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d | Cliente: %s | Itens: %d | Total: R$ %s | Status: %s | Criado: %s",
                numero, cliente.getNome(), itens.size(), total(), status, dataHoraCriacao);
    }
}
