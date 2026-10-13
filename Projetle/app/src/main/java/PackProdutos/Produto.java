package PackProdutos;

import java.util.ArrayList;
import PackInterfaces.*;
import PackMaquinas.Maquina;
import outros.Cenario;


public abstract class Produto implements Auditavel, Aleatorio{
    protected double minFalhaCenario; 
    protected double maxFalhaCenario;
    protected String id;
    protected String nome;
    protected StatusProduto status = StatusProduto.FUNDIDO;
    protected double quantidadeMateriaPrimaNecessaria;
    protected double qualidade;
    protected double probabilidadeFalhaAcumulada;
    protected ArrayList<Maquina> processoProducao;
    protected double tempoUsinagem;
    protected double tempoTratamentoSuperficial;
    private int lote;
    private static int totalProdutosFabricados = 0;
    
    public Produto (String id, String nome, double materiaPrima, double qualidade, ArrayList<Maquina> processo, double tempoUsinagem, double tempoTratamento, Cenario cenario) {
        this.id=id; 
        this.nome=nome; 
        this.quantidadeMateriaPrimaNecessaria=materiaPrima; 
        this.qualidade=qualidade;
        this.processoProducao=processo; 
        this.tempoUsinagem=tempoUsinagem; 
        this.tempoTratamentoSuperficial=tempoTratamento;
        configurarCenario(cenario);
    }


    public abstract void processar(StatusProduto proxStatus);
    public abstract double calcularTempoProducao(int demanda);
    public abstract String getTipo();
    public abstract Produto criarNovaUnidade(String novoId, int lote, Cenario cenario);


    public  void configurarCenario(Cenario cenario){
        this.minFalhaCenario=cenario.getFalhaProdutoMin() ;
        this.maxFalhaCenario=cenario.getFalhaProdutoMax();
    }
    public String getId() { return id; }
    public String getNome() { return nome; }
    public StatusProduto getStatus() { return status; }
    public void setStatus(StatusProduto status) { this.status = status; }
    public double getDemandaMateriaPrima() { return quantidadeMateriaPrimaNecessaria; }
    public double getQuantidadeMateriaPrimaPorUnidade() { return quantidadeMateriaPrimaNecessaria; }
    public double getQualidade() { return qualidade; }
    public double getProbabilidadeFalhaAcumulada() { return probabilidadeFalhaAcumulada; }
    public ArrayList<Maquina> getProcessoProducao() { return processoProducao; }
    public int getLote() { return lote; }
    protected void setLote(int lote) { this.lote = lote; }

    public void upProbabilidadeFalha() { probabilidadeFalhaAcumulada +=  (numeroAleatorio(this.minFalhaCenario, this.maxFalhaCenario)); }
    protected static void incrementarTotalProdutosFabricados() { totalProdutosFabricados++; }
    public static int getTotalProdutosFabricados() { return totalProdutosFabricados; }

    @Override
    public String gerarRelatorioDiagnostico() {
        return String.format("Produto: %s | Tipo: %s | Qualidade: %.2f | Risco: %.2f | Lote: %d",
                nome, getTipo(), qualidade, probabilidadeFalhaAcumulada, lote);
    }

    @Override
    public boolean precisaManutencao() {
        return probabilidadeFalhaAcumulada > 0.30;
    }
}
