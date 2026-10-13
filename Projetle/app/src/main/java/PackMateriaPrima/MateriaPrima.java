package PackMateriaPrima;

public class MateriaPrima {
    private String id;
    private String nome;
    private double quantidade;
    private String unidade;
    private double custoPorUnidade;

    public MateriaPrima(String id, String nome, double quantidade, String unidade, double custoPorUnidade) {
        this.id=id; 
        this.nome=nome; 
        this.quantidade=quantidade; 
        this.unidade=unidade; 
        this.custoPorUnidade=custoPorUnidade;
    }
    public boolean consumir(double q) {
        if (!verificarDisponibilidade(q) || q < 0){
            return false; 
        }
        quantidade -= q; 
        return true; 
    }
    public void adicionarEstoque(double q) { 
        if (q > 0) 
            quantidade += q; 
        }
    public boolean verificarDisponibilidade(double q) { return q >= 0 && quantidade >= q; }
    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getQuantidade() { return quantidade; }
    public String getUnidade() { return unidade; }
    public double getCusto() { return custoPorUnidade; }
}
