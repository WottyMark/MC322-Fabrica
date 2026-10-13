package Estrategias;
import java.util.List;
import Estrategias.Demandas.Demanda;
public interface EstrategiaProducao {
    Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel);
    String getNomeEstrategia();
}
