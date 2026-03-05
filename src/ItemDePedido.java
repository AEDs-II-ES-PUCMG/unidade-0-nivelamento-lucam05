public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    private int quantidade;
    private double precoVenda;
    private Produto[] produtos;
    private static final int MAX_PRODUTOS = 10;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
    	produtos = new Produto[MAX_PRODUTOS];
    	quantidade = 0;
    	precoVenda = produtos[MAX_PRODUTOS].valorDeVenda();
    }
	public boolean incluirItemProduto(Produto novo) {
		
		if (quantidade < MAX_PRODUTOS) {
			produtos[quantidade++] = novo;
			return true;
		}
		return false;
	}
	public double precoVenda() {
		return produtos[MAX_PRODUTOS].valorDeVenda();
	}
    

    public double calcularSubtotal() {

        return 0;
    }

    

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {
    	ItemDePedido outro = (ItemDePedido)obj;
        return true;
    }
}
