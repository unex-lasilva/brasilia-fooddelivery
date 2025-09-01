package app;

/**
 * Ponto único para possíveis notificações futuras.
 * (sem interfaces/herança agora; hoje apenas imprime no console)
 */
public class NotificadorPedidos {
    public static void notifySaiuParaEntrega(Pedido p) {
        System.out.println("[NOTIFICAÇÃO] Pedido #" + p.getNumero() + " saiu para entrega.");
        // futuro: enviar e-mail / log / webhook etc.
    }
}
