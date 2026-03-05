import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	private String descricao;
	private double precoCusto;
	private double margemLucro;
	private int quantidadeEmEstoque;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}

	public String getDescricao(){return descricao;}
	public double getPrecoCusto(){return precoCusto;}
	public double getMargemLucro(){return margemLucro;}
	public int getQuantidadeEmEstoque(){return quantidadeEmEstoque;}
	
	 /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
	
	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
	/**
	* Igualdade de produtos: caso possuam o mesmo nome/descrição.
	* @param obj Outro produto a ser comparado
	* @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
	*/
		@Override
	public boolean equals(Object obj){
	Produto outro = (Produto)obj;
	return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}
	/**
* Gera uma linha de texto a partir dos dados do produto
* @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
*/
	public abstract String gerarDadosTexto();

	static Produto criarDoTexto(String linha){
		Produto novoProduto = null;
		String[] partes = linha.split(";");
		int tipo=Integer.parseInt(partes[0].trim());
		String desc = partes[1].trim();

		double custo = Double.parseDouble(partes[2].trim().replace(",", "."));
        double margem = Double.parseDouble(partes[3].trim().replace(",", "."));

		if(tipo==1){
			novoProduto = new ProdutoNaoPerecivel(desc, custo, margem);
		}else if(tipo == 2){
			String dataStr = partes[4].trim();
			DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate validade = LocalDate.parse(dataStr,formatter);
			novoProduto = new ProdutoPerecivel(desc, custo, margem, validade);
		}
		

		return novoProduto;
	}

}