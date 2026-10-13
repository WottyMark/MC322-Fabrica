package PackMaquinas;


import PackInterfaces.*;
import PackMateriaPrima.MateriaPrima;
import PackProdutos.Produto;
import outros.Cenario;
import outros.GerenciadorProducao;

public abstract class Maquina implements Auditavel, Aleatorio {
    private String nome;
    private double health;
    private boolean ligada;
    private int capacidadeMaxima;
    private double probabilidadeFalha;
    private double custoOperacao;
    private double multiplicadorFalha;
    private double multiplicadorDesgaste;


    protected Maquina(String nome, int capacidadeMaxima, double probabilidadeFalha,double custoOperacao, Cenario cenario) {
        this.nome = nome;
        this.health = 100.0;
        this.ligada = false;
        this.capacidadeMaxima = capacidadeMaxima;
        this.probabilidadeFalha = probabilidadeFalha;
        this.custoOperacao = custoOperacao;
        configurarCenario(cenario);
    }

    public abstract int processar(Produto produto, MateriaPrima materiaPrima, int quantidade);
    public abstract String getTipo();

    public void ligar() { ligada = true; }
    public void desligar() { ligada = false; }
    public boolean estaLigada() { return ligada; }
    public String getNome() { return nome; }
    public double getCustoOperacao() { return custoOperacao; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public double getSaude() { return health; }

    public void configurarCenario(Cenario cenario) {
        this.multiplicadorFalha = cenario.getMultiplicadorFalha();
        this.multiplicadorDesgaste = cenario.getMultiplicadorDesgaste();
    }

    public boolean podeOperar() { return health > 0.0; }
    protected double getMultiplicadorFalhaCenario() { return multiplicadorFalha; }

    protected boolean verificarFalha() {
        double fatorSaude = 1.0 + (100.0 - health) / 100.0;
        double chance = (probabilidadeFalha * multiplicadorFalha * fatorSaude);
        return numeroAleatorio(0,1) < chance;
    }

    protected void aplicarDesgaste() {
        health -= numeroAleatorio(0.0, 3.0) * multiplicadorDesgaste;
        if (health < 0.0) health = 0.0;
    }

    protected int limitarQuantidade(int quantidade) {
        return Math.max(0, Math.min(quantidade, capacidadeMaxima));
    }

    public boolean reparar(Cenario cenario, GerenciadorProducao g) {
        if(g.gastar(getCustoOperacao()*10)){
            double vida = cenario.getDescricao().equals("Ideal") ? numeroAleatorio(80, 100):numeroAleatorio(50, 80);
            if(vida>health)health=vida;
            return true;
        }
        return false;
    }

    @Override
    public boolean precisaManutencao() { return health < 20.0; }

    @Override
    public String gerarRelatorioDiagnostico() {
        return String.format("Maquina: %s | Tipo: %s | Saude: %.1f/100 | Manutencao: %s",nome, getTipo(), health, precisaManutencao() ? "SIM" : "NAO");
    }
}
