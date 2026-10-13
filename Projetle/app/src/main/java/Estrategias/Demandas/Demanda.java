package Estrategias.Demandas;

import PackProdutos.Produto;

/**
 * 
 * Registra cada demanda solicitada.
 */
public class Demanda {
    private int id;
    private Produto tipoProduto;
    private int quantidadeProdutos;
    private int quantidadeProduzida;
    private StatusDemanda status;

    public Demanda(int id, Produto tipoProduto, int quantidadeProdutos) {
        this.id=id; 
        this.tipoProduto=tipoProduto; 
        this.quantidadeProdutos=quantidadeProdutos;
        this.quantidadeProduzida=0; 
        this.status=StatusDemanda.PENDENTE;
    }

    /**
     * Métodos para ler os parâmetros privados da classe
     * 
     * @return Parâmetros da classe
     */
    public int getId() { return id; }
    public Produto getProduto() { return tipoProduto; }
    public int getQuantidadeProdutos() { return quantidadeProdutos; }
    public int getQuantidadeProduzida() { return quantidadeProduzida; }
    public int getQuantidadeRestante() { return (quantidadeProdutos-quantidadeProduzida); }
    public StatusDemanda getStatus() { return status; }

    /**
     * Atualiza quantidade de produtos na demanda.
     * 
     * @param qnt Nova quantidade de produtos a serem produzidos na demanda.
     */
    public void atualizarQuantidade(int qnt) { 
        if (status==StatusDemanda.PENDENTE && qnt>0) {
            quantidadeProdutos=qnt; 
        }
    }

    /**
     * Calcula o total de materia prima necessaria para fabricar a demanda, baseado no produto
     * 
     * @return Quantidade de produtos que faltam serem produzidos multiplicada pelo custo do produto
     */
    public double calcularMateriaPrimaNecessaria() { 
        return getQuantidadeRestante()*tipoProduto.getDemandaMateriaPrima(); 
    }

    /**
     * Estima o custo do processo de producao do produto na demanda.
     * 
     * @return Custo de processos do produto multiplicado pela quantidade de produtos a serem produzidos.
     */
    public double estimarCustoOperacao() { 
        return tipoProduto.getProcessoProducao().stream().mapToDouble(m->m.getCustoOperacao()).sum()*getQuantidadeRestante(); 
    }

    /**
     * Calcula se o processo para fabricacao da demanda esta dentro do orcamento.
     * 
     * @param orcamento Orcamento disponivel para processos.
     * @return Verdadeiro se o orcamento cubrir o custo, falso caso contrário.
     */
    public boolean ehFinanceiramenteViavel(double orcamento) { 
        return estimarCustoOperacao() <= orcamento; 
    }
    
    
    public void iniciar() { 
        if (status==StatusDemanda.PENDENTE) {
            status=StatusDemanda.EM_PRODUCAO;
        }
    }
    
    /**
     * Registra quantos produtos da demanda ja foram produzidos.
     * 
     * @param qnt Quantidade ja produzida.
     */
    public void registrarProduzidos(int qnt){
        if (status!=StatusDemanda.EM_PRODUCAO || qnt<=0) {
            return; 
        }
        quantidadeProduzida+=qnt; 
        if (quantidadeProduzida>=quantidadeProdutos) {
            status=StatusDemanda.CONCLUIDA; 
        }
    }

    /**
     * Cancela a demanda por completo.
     */
    public void cancelar() { 
        if (status!=StatusDemanda.CONCLUIDA) {
            status=StatusDemanda.CANCELADA; 
        }
    }

    /**
     * Registra a demanda como pendente.
     */
    public void  pendente() { 
        if (status!=StatusDemanda.CONCLUIDA) {
            status=StatusDemanda.PENDENTE; 
        }
    }

    /**
     * Le todas as informacoes necessarias sobre a demanda.
     * 
     * @return Texto detalhando informacoes da demanda.
     */
    public String getDemanda() { return id+" | Produto: "+tipoProduto.getNome()+" | Quantidade: "+quantidadeProdutos+" | Produzida: "+quantidadeProduzida+" | Status: "+status.getDescricao(); }
}
