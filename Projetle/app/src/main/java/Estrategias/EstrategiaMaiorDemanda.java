package Estrategias;
import java.util.List;
import Estrategias.Demandas.*;
public class EstrategiaMaiorDemanda implements EstrategiaProducao {
    public Demanda selecionarDemanda(List<Demanda> demandas,double orcamento){ 
        Demanda melhor=null;
         for(Demanda d:demandas){
            if(d.getStatus()==StatusDemanda.PENDENTE && (melhor==null || d.getQuantidadeRestante()>melhor.getQuantidadeRestante())) {
                melhor=d;
           }
        }
        return melhor; 
    }
    public String getNomeEstrategia(){ return "Maior Demanda"; }
}
