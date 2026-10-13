package PackMaquinas;

import PackMateriaPrima.MateriaPrima;
import PackProdutos.Produto;
import PackProdutos.StatusProduto;
import outros.Cenario;

public class MaquinaInspecao extends Maquina {
    public MaquinaInspecao(String nome, int capacidadeMaxima, double custoOperacao, Cenario cenario) {
        super(nome, capacidadeMaxima, 0.03, custoOperacao, cenario);
    }

    @Override
    public int processar(Produto produto, MateriaPrima materiaPrima, int quantidade) {
        if (!estaLigada() || !podeOperar()) return 0;
        int alvo = limitarQuantidade(quantidade);
        int aprovados = 0;
        for (int i = 0; i < alvo && podeOperar(); i++) {
            aplicarDesgaste();
            boolean maquinaFalhou = verificarFalha();
            double chanceRejeicao = (produto.getQualidade() * 0.05 + produto.getProbabilidadeFalhaAcumulada()) * getMultiplicadorFalhaCenario();
            if (!maquinaFalhou && numeroAleatorio(0,1) >= chanceRejeicao) aprovados++;
        }
        if (aprovados > 0) produto.processar(StatusProduto.INSPECIONADO);
        return aprovados;
    }

    @Override public String getTipo() { return "Inspecao"; }
}
