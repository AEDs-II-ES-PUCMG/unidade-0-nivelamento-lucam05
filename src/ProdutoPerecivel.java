import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        
            if(validade.isBefore(LocalDate.now())){
                throw new IllegalArgumentException("A data de validade não pode ser anterior ao dia atual.");
            }
            this.dataDeValidade = validade;
    }

    @Override
    public double valorDeVenda(){
        LocalDate hoje = LocalDate.now();
        double precoFinal = super.valorDeVenda();
        long diasParaVencer=ChronoUnit.DAYS.between(hoje,dataDeValidade);
        if (hoje.isAfter(dataDeValidade)) {
            throw new IllegalStateException("Produto vencido. Venda não permitida.");
        }
        if(diasParaVencer<=PRAZO_DESCONTO){
            precoFinal-=(precoFinal*DESCONTO);
        }

        return precoFinal;
    }
    public String gerarDadosTexto() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format(Locale.US,"1,%s,%.2f,%2f,%s", getDescricao(),getPrecoCusto(),getMargemLucro(),dataDeValidade.format(formatter));

    }

    public String toString(){
        return super.toString() + " | Validade: "+dataDeValidade;
    }

}
