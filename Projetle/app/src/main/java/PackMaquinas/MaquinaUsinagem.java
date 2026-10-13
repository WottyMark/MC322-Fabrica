package PackMaquinas;

import PackMateriaPrima.MateriaPrima;
import PackProdutos.Produto;
import PackProdutos.StatusProduto;
import outros.Cenario;

public class MaquinaUsinagem extends Maquina {
    public MaquinaUsinagem(String nome, int capacidadeMaxima, double custoOperacao, Cenario cenario) {
        super(nome, capacidadeMaxima, 0.05, custoOperacao, cenario);
    }

    @Override
    public int processar(Produto produto, MateriaPrima materiaPrima, int quantidade) {
        if (!estaLigada() || !podeOperar()) return 0;
        int alvo = limitarQuantidade(quantidade);
        int processados = 0;
        for (int i = 0; i < alvo && podeOperar(); i++) {
            if (verificarFalha()) produto.upProbabilidadeFalha();
            aplicarDesgaste();
            processados++;
        }
        if (processados > 0) produto.processar(StatusProduto.USINADO);
        return processados;
    }

    @Override public String getTipo() { return "Usinagem"; }
}
