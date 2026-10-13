package Estrategias;
import java.util.List;
import Estrategias.Demandas.*;
public class EstrategiaOrdemChegada implements EstrategiaProducao {
    public Demanda selecionarDemanda(List<Demanda> demandas,double orcamento){ 
        for(Demanda d:demandas){ 
            if(d.getStatus()==StatusDemanda.PENDENTE){ 
                return d;
            }
        }
        return null; 
    }
    public String getNomeEstrategia(){ return "Ordem de Chegada"; }
}
