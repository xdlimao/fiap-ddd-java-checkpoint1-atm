package main;

import java.util.Scanner;

public class Atm {
	String nome;
	String senha;
	double saldo = 0.0;
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		Atm atm_programa = new Atm();

		atm_programa.definirNome();
		atm_programa.definirSenha();
		boolean status_autenticacao = atm_programa.processoAutenticacao();
		if (status_autenticacao == true) {
			boolean execucao_programa = true;
			while (execucao_programa) {
				int opcao = atm_programa.mostrarMenu();
				switch (opcao) {
				case 1:
					atm_programa.consultarSaldo();
					break;
				case 2:
					atm_programa.fazerDeposito();
					break;
				case 3:
					atm_programa.fazerSaque();
					break;
				case 4:
					System.out.println("O FIAP Bank agradece sua preferência!");
					execucao_programa = false;
					break;
				default:
					System.out.println("Valor inválido! Selecione um valor presente no menu.");
					break;
				}
			}
		}

		atm_programa.scanner.close();
	}

	public void definirNome() {
		System.out.println("============== NOME ==============");
		System.out.println("Insira o seu nome completo: ");
		nome = scanner.nextLine().trim();
	}

	public void definirSenha() {
		System.out.println("============== SENHA ==============");
		System.out.println("A senha deve ter:");
		System.out.println("\t* 8 caracteres minimo;");
		System.out.println("\t* Ao menor um número;");
		System.out.println("\t* Ao menos uma letra maiúscula;");
		System.out.println("\t* Ao menos um caracter especial da lista => !@#$%^&()-_+=?>< <=.");

		while (true) {
			System.out.println("Insira a sua senha:");
			String senha_inicial = scanner.nextLine();
			if (senha_inicial.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_+=?><]).{8,}$")) {
				System.out.println("Senha registrada com sucesso!");
				senha = senha_inicial;
				break;
			} else {
				System.out.println("Senha não atendeu a todos os critérios... Tente novamente.");
			}
		}

	}

	public boolean processoAutenticacao() {
		System.out.println("=========== AUTENTICAÇÃO ===========");
		System.out.println(String.format("Bem-vindo, %s! Vamos começar o processo de autenticação.", nome));

		for (int i = 0; i < 3; i++) {
			System.out.println(
					String.format("Por favor, insira a sua senha para concluir o acesso. (Tentativa %d de 3): ", i));
			String input_senha = scanner.nextLine();

			if (input_senha.equals(senha)) {
				System.out.println("Senha validada! Iniciando login...");
				return true;
			} else {
				System.out.println("Senha não válida... Tente novamente.");
			}
		}
		return false;
	}

	public int mostrarMenu() {
		System.out.println("MENU======================\n");
		System.out.println("1 -> Consultar Saldo");
		System.out.println("2 -> Fazer Depósito");
		System.out.println("3 -> Fazer Saque");
		System.out.println("4 -> Sair");
		System.out.println("\n==========================");
		return scanner.nextInt();
	}

	public void consultarSaldo() {
		System.out.println(String.format("Saldo atual: %.02f", saldo));
	}

	public void fazerDeposito() {
		System.out.println("Insira o valor do depósito (Valor negativo ou zero cancelam a operação): ");
		double valor = scanner.nextDouble();
		if (valor > 0) {
			System.out.println("Valor depositado!");
			saldo += valor;
		} else {
			System.out.println("Valor cancelado.");
		}
	}

	public void fazerSaque() {
		System.out.println("Insira o valor do saque (Valor negativo, zero ou acima do saldo disponível cancelam a operação): ");
		double valor = scanner.nextDouble();
		if (valor > 0 && valor <= saldo) {
			System.out.println("Valor sacado!");
			saldo -= valor;
		} else {
			System.out.println("Valor cancelado.");
		}
	}
}