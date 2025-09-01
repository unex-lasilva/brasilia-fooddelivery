package app;

import java.math.BigDecimal;

public class RelatorioVendas {

    public static String gerarSimplificado(CentralDados db) {
        int qtdPedidos = db.listarPedidos().size();
        BigDecimal total = db.totalArrecadado();
        return """
               === RELATÓRIO DE VENDAS (SIMPLIFICADO) ===
               Total de pedidos: %d
               Valor total arrecadado: R$ %s
               """.formatted(qtdPedidos, total);
    }

    public static String gerarDetalhado(CentralDados db) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE VENDAS (DETALHADO) ===\n");
        db.listarPedidos().forEach(p -> {
            sb.append("Pedido #").append(p.getNumero()).append(" - Cliente: ")
              .append(p.getCliente().getNome()).append(" - Status: ")
              .append(p.getStatus()).append(" - Total: R$ ").append(p.total()).append("\n");
            p.getItens().forEach(it -> sb.append("   - ").append(it).append("\n"));
        });
        sb.append("TOTAL ARRECADADO: R$ ").append(db.totalArrecadado()).append("\n");
        return sb.toString();
    }
}
