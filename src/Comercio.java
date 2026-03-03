import java.nio.charset.Charset;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Comercio {
	static final int MAX_NOVOS_PRODUTOS = 10;
	static String nomeArquivoDados;
	static Scanner teclado;
	static Produto[] produtosCadastrados;
	static int quantosProdutos;

	static void pausa(){
		System.out.println("Digite enter para continuar...");
		teclado.nextLine();
	}
	static void cabecalho(){
		System.out.println("AEDII COMÉRCIO DE COISINHAS");
		System.out.println("===========================");
	}
	static int menu(){
		cabecalho();
		System.out.println("1 - Listar todos os produtos");
		System.out.println("2 - Procurar e listar um produto");
		System.out.println("3 - Cadastrar novo produto");
		System.out.println("0 - Sair");
		System.out.print("Digite sua opção: ");
		
		return Integer.parseInt(teclado.nextLine());
	}

	static Produto[] lerProdutos(String nomeArquivoDados) {
		Produto[] vetorProdutos=new Produto[MAX_NOVOS_PRODUTOS];
		try{
			File arquivo = new File(nomeArquivoDados);
			if(!arquivo.exists()){
				quantosProdutos=0;
				return vetorProdutos;
			}
			Scanner leitorarquivo = new Scanner(arquivo);
			if(leitorarquivo.hasNextLine()){
				quantosProdutos=Integer.parseInt(leitorarquivo.nextLine().trim());
				vetorProdutos = new Produto[quantosProdutos+MAX_NOVOS_PRODUTOS];

				int index=0;
				while(leitorarquivo.hasNextLine()&&index<quantosProdutos){
					String linha=leitorarquivo.nextLine();
					vetorProdutos[index]=Produto.criarDoTexto(linha);
					index++;
				}
			}
			leitorarquivo.close();
		}catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Iniciando vetor vazio.");
            quantosProdutos = 0;
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            quantosProdutos = 0;
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        }

		return vetorProdutos;
	}

	static void listarTodosOsProdutos(){
		System.out.println("---LISTA DE PRODUTOS---");
		if(quantosProdutos==0){
			System.out.println("Nenhum produto cadastrado!");
		}
		else{
			for(int i=0;i<quantosProdutos;i++){
				System.out.println((i+1)+"."+produtosCadastrados[i].toString());
			}
			System.out.println("-------------------------\n");
		}
	}

	static void localizarProdutos(){
		System.out.print("Digite o nome exato do produto que deseja localizar: ");
        String busca = teclado.nextLine();
		Produto NaoPerecivelTemp = new ProdutoNaoPerecivel(busca, 1.0, 1.0);

		boolean encontrado=false;
		for(int i=0;i<quantosProdutos;i++){
			if(produtosCadastrados[i].equals(NaoPerecivelTemp)){
				System.out.println("Produto encontrado:");
				System.out.println(produtosCadastrados[i].toString());
				encontrado = true;
				break;
			}
		}
		if(!encontrado){
			System.out.println("Produto não localizado no sistema");
		}
	}

	static void cadastrarProduto(){
		if(quantosProdutos>=produtosCadastrados.length){
			System.out.println("Limite máximo de cadastros atingido para esta execução.");
			return;
		}
		try{
			System.out.println("\n--- Cadastro de Novo Produto ---");
            System.out.println("Tipo do produto (1 - Não Perecível | 2 - Perecível): ");
			int tipo = Integer.parseInt(teclado.nextLine());
			System.out.print("Descriçao: ");
			String desc = teclado.nextLine();
			System.out.print("Preço de Custo (Ex: 15.50): ");
			double custo = Double.parseDouble(teclado.nextLine().replace(",", "."));
			System.out.print("Margem de Lucro (Ex: 0.2 para 20%): ");
			double margem = Double.parseDouble(teclado.nextLine().replace(",", "."));

			if(tipo==1){
				produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(desc, custo, margem);
				quantosProdutos++;
				System.out.println("Produto cadastrado com sucesso!");
			}else if(tipo==2){
				System.out.print("Data de Validade (dd/mm/aaaa): ");
                String dataStr = teclado.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate validade = LocalDate.parse(dataStr, formatter);
				produtosCadastrados[quantosProdutos]=new ProdutoPerecivel(desc, custo, margem, validade);
				quantosProdutos++;
				System.out.println("Produto cadastrado com sucesso!");
			}
			else{
				System.out.println("Tipo inválido");
			}

		}catch (Exception e) {
            System.out.println("Erro ao cadastrar. Verifique os dados inseridos.");
        }
	}
/**
* Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
* @param nomeArquivo Nome do arquivo a ser gravado.
*/
	public static void salvarProdutos(String nomeArquivo){
		try(PrintWriter writer = new PrintWriter(new File(nomeArquivo))){
			writer.println(quantosProdutos);
			for(int i=0;i<quantosProdutos;i++){
				writer.println(produtosCadastrados[i].gerarDadosTexto());
			}
			System.out.println("\nDados salvos com sucesso no arquivo!");
		}catch (FileNotFoundException e) {
            System.out.println("\nErro ao tentar salvar o arquivo de dados.");
        }
	}


	public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
		nomeArquivoDados = "dadosProdutos.csv";
		produtosCadastrados = lerProdutos(nomeArquivoDados);
		int opcao = -1;
		
		do{
			opcao = menu();
			switch (opcao) {
			case 1 -> listarTodosOsProdutos();
			case 2 -> localizarProdutos();
			case 3 -> cadastrarProduto();
			}
			pausa();
		}while(opcao !=0);
		
		salvarProdutos(nomeArquivoDados);
		teclado.close();

	}
}
