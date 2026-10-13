package outros;

/**
 * 
 * Enum dos cenarios de simulacao possivel.
 */
public enum Cenario {
    TESTES("Ideal", Double.POSITIVE_INFINITY, 0.55, 0.55, 0.1, 0.3),
    IDEAL("Ideal", 10000.0, 0.55, 0.55, 0.1, 0.3),
    APOCALIPTICO("Apocaliptico", 1500.0, 2.20, 2.40, 0.2, 0.5);

    private final String descricao;
    private final double budgetInicial;
    private final double multiplicadorFalha;
    private final double multiplicadorDesgaste;
    private final double FalhaProdutoMin;
    private final double FalhaProdutoMax;

    Cenario(String descricao, double budgetInicial, double multiplicadorFalha, double multiplicadorDesgaste, double FalhaProdutoMin, double FalhaProdutoMax) {
        this.descricao = descricao;
        this.budgetInicial = budgetInicial;
        this.multiplicadorFalha = multiplicadorFalha;
        this.multiplicadorDesgaste = multiplicadorDesgaste;
        this.FalhaProdutoMin = FalhaProdutoMin;
        this.FalhaProdutoMax = FalhaProdutoMax;
    }

    public String getDescricao() { return descricao; }
    public double getBudgetInicial() { return budgetInicial; }
    public double getMultiplicadorFalha() { return multiplicadorFalha; }
    public double getMultiplicadorDesgaste() { return multiplicadorDesgaste; }
    public double getFalhaProdutoMin(){return FalhaProdutoMin;}
    public double getFalhaProdutoMax(){return FalhaProdutoMax;};


    /**
     * Escolhe o cenario baseado na entrada do usuario.
     * 
     * @param opcao Entrada do usuario.
     * @return Cenario que foi escolhido.
     */
    public static Cenario escolha(int opcao) {
        Cenario cenario=IDEAL;
        switch (opcao){
            case 0:
                cenario=TESTES;
            break;

            case 1:
                cenario=IDEAL;
            break;

            case 2:
                cenario=APOCALIPTICO;    
            break;
        }
        return cenario;
    }
}
