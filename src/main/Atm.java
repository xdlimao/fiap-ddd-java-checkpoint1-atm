package main;

import java.util.Scanner;
import java.lang.Thread;
import java.math.BigDecimal;

public class Atm {
	String nome;
	String senha;
	BigDecimal saldo = new BigDecimal("0.0");
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Começando inicialização do sistema...");
		Atm atm_programa = new Atm();
		System.out.println("Carregando módulos...");
		atm_programa.carregamentoInterno();

		atm_programa.definirNome();
		atm_programa.definirSenha();

		boolean status_autenticacao = atm_programa.processoAutenticacao();
		atm_programa.carregamentoInterno();

		if (status_autenticacao == true) {
			boolean execucao_programa = true;
			String informacao_output = "";

			while (execucao_programa) {
				int opcao = atm_programa.interagirMenu(informacao_output);

				switch (opcao) {
				case 1:
					informacao_output = atm_programa.consultarSaldo();
					break;
				case 2:
					informacao_output = atm_programa.fazerDeposito();
					break;
				case 3:
					informacao_output = atm_programa.fazerSaque();
					break;
				case 4:
					System.out.println("\nO FIAP Bank agradece sua preferência! Fechando sessão...");
					execucao_programa = false;
					break;
				default:
					informacao_output = "Valor inválido! Selecione um valor presente no menu.";
					break;
				}
			}
		}

		atm_programa.scanner.close();
	}

	private void limparTela() {
		for (int i = 0; i < 500; i++) {
			System.out.println();
		}
	}

	private void carregamentoInterno() {
		try {
			Thread.sleep(2500);
		} catch (Exception e) {
			throw new RuntimeException("Aconteceu algo no seu ambiente onde o Java está rodando.");
		}
	}

	private BigDecimal inputSanitizadoDinheiro() {
		String input = scanner.next() // + Processo de sanitização
						.replaceAll("[^0-9\\.]", "")
						.replaceAll("\\.", ",")
						.replaceFirst("\\,", ".")
						.replaceAll("[^0-9\\.]", "");
		
		return new BigDecimal(String.format("%.02f", Double.parseDouble(input.isEmpty() || input.equals(".")? "0" : input)).replaceFirst("\\,", "."));
	}

	public void definirNome() {
		this.limparTela();
		System.out.println("""

					============== NOME ==============
					|   Insira o seu nome completo   |
					==================================

				""");

		System.out.print("-> ");
		nome = scanner.nextLine().trim();
	}

	public void definirSenha() {
		this.limparTela();
		System.out.println("""

					===============================SENHA================================
					|       Insira uma senha válida, seguindo as regras abaixo:        |
					| * 8 caracteres minimo                                            |
					| * Ao menos um número                                             |
					| * Ao menos uma letra maiúscula                                   |
					| * Ao menos um caractere especial da lista => !@#$%^&()-_+=?>< <= |
					====================================================================

				""");

		while (true) {
			System.out.print("-> ");
			
			String senha_inicial = scanner.nextLine();
			if (senha_inicial.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_+=?><]).{8,}$")) {
				System.out.println("\nSenha registrada com sucesso!");
				senha = senha_inicial;
				break;
			} else {
				System.out.println("\nSenha não atendeu a todos os critérios... Tente novamente.");
			}
		}

	}

	public boolean processoAutenticacao() {
		this.limparTela();
		System.out.println(String.format("""

                          ============================= AUTENTICAÇÃO ==============================
                          | Bem-vindo, %s! Vamos começar o processo de autenticação.
                          =========================================================================

				""", nome.split(" ")[0]));

		for (int i = 0; i < 3; i++) {
			System.out.println(String.format("Por favor, insira a sua senha para concluir o acesso. (Tentativa %d de 3)", i+1));
			System.out.print("-> ");
			
			String input = scanner.nextLine();

			if (input.equals(senha)) {
				System.out.println("\nSenha validada! Iniciando login...");
				return true;
			} else {
				System.out.println("\nSenha inválida...");
			}
		}
		System.out.println("\nAcesso bloqueado. Tente novamente mais tarde ou entre em contato com o banco.");
		return false;
	}

	public int interagirMenu(String input) {
		this.limparTela();

		System.out.println(String.format("""

					============MENU===========
					|  1 -> Consultar Saldo   |
					|  2 -> Fazer Depósito    |
					|  3 -> Fazer Saque       |
					|  4 -> Sair              |
					===========================

					%s
				""", input));
		System.out.print("-> ");
		String opcao = scanner.next().replaceAll("\\D", "");
		return Integer.parseInt(opcao.isEmpty() ? "0" : opcao);
	}

	public String consultarSaldo() {
		return String.format("Saldo atual: R$ %.02f", saldo);
	}

	public String fazerDeposito() {
		this.limparTela();

		System.out.println("""

						=====================================================================
						|                    Insira o valor do depósito                     |
						| * Valor zero cancela a operação.                                  |
						| * O campo é sanitizado e salva apenas os centésimos.              |
						| * É aceito apenas números e o primeiro "." no valor               |
						=====================================================================

				""");
		System.out.print("-> ");

		BigDecimal valor = this.inputSanitizadoDinheiro();

		if (valor.floatValue() > 0) {
			saldo = saldo.add(valor);
			return String.format("Valor de R$ %.02f depositado!", valor);
		} else {
			return "Valor de depósito cancelado.";
		}
	}

	public String fazerSaque() {

		System.out.println("""

						=====================================================================
						|                    Insira o valor do saque                        |
						| * Valor zero cancela a operação.                                  |
						| * O campo é sanitizado e salva apenas os centésimos.              |
						| * É aceito apenas números e o primeiro "." no valor               |
						=====================================================================

				""");
		System.out.print("-> ");


		BigDecimal valor = this.inputSanitizadoDinheiro();

		if (valor.floatValue() > 0 && valor.floatValue() <= saldo.floatValue()) {
			saldo = saldo.subtract(valor);
			return  String.format("Valor de R$ %.02f sacado!", valor);
		} else {
			return "Valor cancelado.";
		}
	}
}