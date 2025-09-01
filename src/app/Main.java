package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Ponto de Entrada

public class Main {

    private static final Fachada fachada = new Fachada();

    public static void main(String[] args) {
        seed(); 
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    \n==== FoodDelivery CLI ====
                    1) Cadastrar cliente
                    2) Listar clientes
                    3) Cadastrar item do cardápio
                    4) Listar itens do cardápio
                    5) Criar novo pedido
                    6) Avançar status de um pedido
                    7) Definir status de um pedido (seguindo fluxo)
                    8) Listar pedidos por status
                    9) Relatório de vendas (simplificado)
                    10) Relatório de vendas (detalhado)
                    0) Sair
                    Escolha: """);
            String op = sc.nextLine().trim();
            try {
                switch (op) {
                    case "1" -> cadastrarCliente(sc);
                    case "2" -> System.out.println(fachada.listarClientes());
                    case "3" -> cadastrarItem(sc);
                    case "4" -> System.out.println(fachada.listarItensCardapio());
                    case "5" -> criarPedido(sc);
                    case "6" -> avancarStatus(sc);
                    case "7" -> definirStatus(sc);
                    case "8" -> listarPorStatus(sc);
                    case "9" -> System.out.println(fachada.gerarRelatorioSimplificado());
                    case "10" -> System.out.println(fachada.gerarRelatorioDetalhado());
                    case "0" -> { System.out.println("Até logo!"); return; }
                    default -> System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarCliente(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Telefone: ");
        String tel = sc.nextLine();
        var c = fachada.registrarNovoCliente(nome, tel);
        System.out.println("Cliente cadastrado: " + c);
    }

    private static void cadastrarItem(Scanner sc) {
        System.out.print("Nome do item: ");
        String nome = sc.nextLine();
        System.out.print("Preço (ex: 19.90): ");
        BigDecimal preco = new BigDecimal(sc.nextLine());
        var i = fachada.cadastrarNovoItem(nome, preco);
        System.out.println("Item cadastrado: " + i);
    }

    private static void criarPedido(Scanner sc) {
        System.out.println("Clientes:\n" + fachada.listarClientes());
        System.out.print("ID do cliente: ");
        int idCliente = Integer.parseInt(sc.nextLine());

        List<int[]> itensQts = new ArrayList<>();
        while (true) {
            System.out.println("Cardápio:\n" + fachada.listarItensCardapio());
            System.out.print("ID do item (ou vazio para finalizar): ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) break;
            int idItem = Integer.parseInt(s);
            System.out.print("Quantidade: ");
            int qt = Integer.parseInt(sc.nextLine());
            itensQts.add(new int[]{idItem, qt});
        }
        var p = fachada.criarPedido(idCliente, itensQts);
        System.out.println("Pedido criado: " + p);
    }

    private static void avancarStatus(Scanner sc) {
        System.out.print("Número do pedido: ");
        int num = Integer.parseInt(sc.nextLine());
        fachada.avancarStatusPedido(num);
        System.out.println("Status avançado com sucesso.");
    }

    private static void definirStatus(Scanner sc) {
        System.out.print("Número do pedido: ");
        int num = Integer.parseInt(sc.nextLine());
        System.out.println("""
                Escolha o novo status:
                1-ACEITO 2-PREPARANDO 3-FEITO 4-AGUARDANDO_ENTREGADOR 5-SAIU_PARA_ENTREGA 6-ENTREGUE
                """);
        int op = Integer.parseInt(sc.nextLine());
        StatusPedido st = StatusPedido.values()[op-1];
        fachada.definirStatusPedido(num, st);
        System.out.println("Status definido com sucesso.");
    }

    private static void listarPorStatus(Scanner sc) {
        System.out.println("""
                Status:
                1-ACEITO 2-PREPARANDO 3-FEITO 4-AGUARDANDO_ENTREGADOR 5-SAIU_PARA_ENTREGA 6-ENTREGUE
                """);
        int op = Integer.parseInt(sc.nextLine());
        StatusPedido st = StatusPedido.values()[op-1];
        String res = fachada.listarPedidosPorStatus(st);
        System.out.println(res.isBlank() ? "Sem pedidos nesse status." : res);
    }

    private static void seed() {
        
        fachada.registrarNovoCliente("Ana", "7199999-0001");
        fachada.registrarNovoCliente("Bruno", "7199999-0002");
        fachada.cadastrarNovoItem("Burger Clássico", new BigDecimal("22.90"));
        fachada.cadastrarNovoItem("Batata Média", new BigDecimal("9.90"));
        fachada.cadastrarNovoItem("Refrigerante Lata", new BigDecimal("6.50"));
    }
}
