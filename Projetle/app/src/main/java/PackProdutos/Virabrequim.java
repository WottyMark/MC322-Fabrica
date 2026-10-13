package PackProdutos;

import java.util.ArrayList;
import PackMaquinas.Maquina;
import outros.Cenario;

public class Virabrequim extends Produto {
    public Virabrequim(String id, String nome, double materiaPrima, double qualidade, ArrayList<Maquina> processo, double tempoUsinagem, double tempoTratamento, Cenario cenario) {
        super(id, nome, materiaPrima, qualidade, processo, tempoUsinagem, tempoTratamento, cenario);
    }
    @Override public double calcularTempoProducao(int demanda) { return demanda*(tempoUsinagem+tempoTratamentoSuperficial); }
    @Override public String getTipo() { return "Virabrequim"; }
    @Override public void processar(StatusProduto proxStatus) {
        this.status=proxStatus;
        if (proxStatus == StatusProduto.FINALIZADO) incrementarTotalProdutosFabricados();
    }
    @Override public Produto criarNovaUnidade(String novoId, int lote, Cenario cenario) {
        Virabrequim p = new Virabrequim(novoId, nome, quantidadeMateriaPrimaNecessaria, numeroAleatorio(0.5,0.6), processoProducao, tempoUsinagem, tempoTratamentoSuperficial, cenario);
        p.setLote(lote);
        return p;
    }
}
