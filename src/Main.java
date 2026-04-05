import java.io.*;
import java.util.*;
import java.text.*;

public class Main {

    static final String ARQUIVO = "financas.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=============================");
        System.out.println("   CONTROLE DE FINANÇAS");
        System.out.println("=============================");

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Adicionar receita");
            System.out.println("2 - Adicionar despesa");
            System.out.println("3 - Ver saldo atual");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1:
                    adicionarTransacao("RECEITA");
                    break;
                case 2:
                    adicionarTransacao("DESPESA");
                    break;
                case 3:
                    verSaldo();
                    break;
                case 0:
                    System.out.println("\nAté logo!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    static void adicionarTransacao(String tipo) {
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        double valor = 0;
        boolean valorValido = false;

        while (!valorValido) {
            System.out.print("Valor (R$): ");
            try {
                valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valor <= 0) {
                    System.out.println("O valor deve ser maior que zero.");
                } else {
                    valorValido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Use apenas números (ex: 150.00).");
            }
        }

        // Pega a data atual
        String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        // Linha a salvar no arquivo: TIPO|DESCRICAO|VALOR|DATA
        String linha = tipo + "|" + descricao + "|" + valor + "|" + data;

        try (FileWriter fw = new FileWriter(ARQUIVO, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(linha);
            bw.newLine();
            System.out.println(tipo + " de R$ " + String.format("%.2f", valor) + " adicionada com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar no arquivo: " + e.getMessage());
        }
    }

    static void verSaldo() {
        double totalReceitas = 0;
        double totalDespesas = 0;

        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            System.out.println("\nNenhuma transação registrada ainda.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split("\\|");
                if (partes.length < 3) continue;

                String tipo = partes[0];
                double valor = Double.parseDouble(partes[2]);

                if (tipo.equals("RECEITA")) {
                    totalReceitas += valor;
                } else if (tipo.equals("DESPESA")) {
                    totalDespesas += valor;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.out.println("Erro ao processar valores do arquivo.");
            return;
        }

        double saldo = totalReceitas - totalDespesas;

        System.out.println("\n==============================");
        System.out.println("        RESUMO FINANCEIRO");
        System.out.println("==============================");
        System.out.printf("  Receitas:  R$ %10.2f%n", totalReceitas);
        System.out.printf("  Despesas:  R$ %10.2f%n", totalDespesas);
        System.out.println("------------------------------");
        System.out.printf("  Saldo:     R$ %10.2f%n", saldo);
        System.out.println("==============================");

        if (saldo < 0) {
            System.out.println("  ⚠ Atenção: saldo negativo!");
        } else if (saldo == 0) {
            System.out.println("  Saldo zerado.");
        } else {
            System.out.println("  Saldo positivo.");
        }
    }
}
