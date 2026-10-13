package PackProdutos;

import java.util.ArrayList;
import PackMaquinas.Maquina;
import outros.Cenario;

public class Pistao extends Produto {
    public Pistao(String id, String nome, double materiaPrima, double qualidade, ArrayList<Maquina> processo, double tempoUsinagem, double tempoTratamento, Cenario cenario) {
        super(id, nome, materiaPrima, qualidade, processo, tempoUsinagem, tempoTratamento, cenario);
    }
    @Override public double calcularTempoProducao(int demanda) { return demanda*(tempoUsinagem+tempoTratamentoSuperficial); }
    @Override public String getTipo() { return "Pistao"; }
    @Override public void processar(StatusProduto proxStatus) {
        this.status=proxStatus;
        if (proxStatus == StatusProduto.FINALIZADO) incrementarTotalProdutosFabricados();
    }
    @Override public Produto criarNovaUnidade(String novoId, int lote, Cenario cenario ) {
        Pistao p = new Pistao(novoId, nome, quantidadeMateriaPrimaNecessaria, numeroAleatorio(0.8,0.9), processoProducao, tempoUsinagem, tempoTratamentoSuperficial, cenario);
        p.setLote(lote);
        return p;
    }
}
