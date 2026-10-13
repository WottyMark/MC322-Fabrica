package outros;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Estrategias.*;
import Estrategias.Demandas.*;
import PackInterfaces.Auditavel;
import PackMaquinas.Maquina;
import PackMateriaPrima.MateriaPrima;
import PackProdutos.*;

/**
 * 
 * Gerenciador de quesitos produtivos da fabrica.
 */
public class GerenciadorProducao {
    private final ArrayList<Demanda> demandas = new ArrayList<>();
    private final ArrayList<Produto> produtosFabricados = new ArrayList<>();
    private final ArrayList<Maquina> maquinas = new ArrayList<>();
    private EstrategiaProducao estrategiaAtual = new EstrategiaOrdemChegada();
    private EstrategiaProducao estrategiaAnterior = estrategiaAtual;
    private final MateriaPrima materiaPrima;
    private double budget;
    private Cenario cenario;
    private int idDemanda = 0;
    private int proximoLote = 1;
    private int proximoProduto = 1;
    private static final Scanner teclado = new Scanner(System.in);

    public GerenciadorProducao(Cenario cenario, MateriaPrima materiaPrima) {
        this.cenario=cenario; this.materiaPrima=materiaPrima; this.budget=cenario.getBudgetInicial();
    }


    /**
     * Registra maquina da fabrica para ser gerenciada
     * 
     * @param maquina
     */
    public void adicionarMaquina(Maquina maquina){ 
        if(maquina!=null){ 
            maquina.configurarCenario(cenario); 
            maquinas.add(maquina);
        } 
    }

    /**
     * Métodos escritores e leitores da estrategiaAtual.
     * 
     * @param nova Estrategia de producao a ser escolhida para atual.
     */
    public void setEstrategia(EstrategiaProducao nova){ if(nova!=null){ estrategiaAnterior=estrategiaAtual; estrategiaAtual=nova; } }
    public void resetEstrategia(){ estrategiaAtual=estrategiaAnterior; }
    public EstrategiaProducao getEstrategia(){ return estrategiaAtual; }


    /**
     * Setter e getter do cenario de simulacao.
     * 
     * @return Cenario sendo simulado.
     */
    public Cenario getCenario(){ return cenario; }
    public  void setCenario(Cenario cenario){this.cenario=cenario;}


    /**
     * Leitores de orcamento, demandas atuais e historico de produtos fabricados, como salvos no gerenciador.
     * 
     * @return Auto-explicativo de acordo com o nome do metodo.
     */
    public double getBudget(){ return budget; }
    public List<Demanda> getDemandas(){ return demandas; }
    public List<Produto> getProdutosFabricados(){ return produtosFabricados; }


    /**
     * Registro e atualizacao da lista de demandas do gerenciador.
     * 
     * @param produto Produto da demanda a ser adicionada.
     * @param id ID da demanda sendo atualizada.
     * @param quantidade Quantidade de produto.
     */
    public void registrarDemanda(Produto produto,int quantidade){ if(produto!=null && quantidade>0) demandas.add(new Demanda(++idDemanda,produto,quantidade)); }
    public boolean atualizarDemanda(int id,int quantidade){ for(Demanda d:demandas) if(d.getId()==id){ d.atualizarQuantidade(quantidade); return true; } return false; }


    /**
     * Inicia nova producao de acordo com a estrategia atual.
     */
    public void executarProximaProducao(){
        Demanda demanda=estrategiaAtual.selecionarDemanda(demandas,budget);
        if(demanda==null){ System.out.println("[INFO] Nenhuma demanda elegivel."); return; }
        fabricarDemanda(demanda);
    }


    /**
     * Fabrica a demanda selecionada pela estrategia.
     * 
     * @param demanda Demanda selecionada.
     */
    public void fabricarDemanda(Demanda demanda){
        if(demanda==null || demanda.getStatus()!=StatusDemanda.PENDENTE) return;

        int quantidade=demanda.getQuantidadeRestante();
        double materiaNecessaria=demanda.calcularMateriaPrimaNecessaria();
        double custo=calcularCustoProducao(demanda,quantidade);
        
        if(!materiaPrima.verificarDisponibilidade(materiaNecessaria)){

            int op;
             System.out.printf("\n[NOK] Materia-prima insuficiente. Dejesa compara %.2fKg de Aluminio para realizar a demanda ?\n", materiaNecessaria-materiaPrima.getQuantidade()); 
             do {
                System.out.printf("1 - Comprar %.2fKg de Aluminio\n", materiaNecessaria-materiaPrima.getQuantidade());
                System.out.println("2 - Não produzir a demanda e voltar ao menu");
                System.out.println("3 - Cancelar demanda");
                op=lerInt("Escolha: ");
                switch(op){
                    case 1: {System.out.println(comprarMateriaPrima(materiaNecessaria-materiaPrima.getQuantidade())?"[OK] Compra realizada.":"[NOK] Compra invalida ou budget insuficiente."); break;}
                    case 2: {System.out.println("[OK] Voltando para o menu");return;}
                    case 3: {System.out.println("[OK] Demanda Cancelada");demanda.cancelar();return; }
                    default: System.out.println("Opcao invalida.");break;
                }
            }while (op!=1);
        }

        if(custo>budget){ System.out.println("[NOK] Budget insuficiente. Demanda Pendente."); demanda.pendente(); return; }
        demanda.iniciar();
        int lote=proximoLote++;
        int concluidos=0;




        for(int i=0;i<quantidade;i++){

            Produto unidade=demanda.getProduto().criarNovaUnidade("P"+(proximoProduto++),lote, cenario);
            if(!materiaPrima.verificarDisponibilidade(unidade.getDemandaMateriaPrima())) break;
            boolean sucesso=true;

            for(Maquina maquina:unidade.getProcessoProducao()){
                if(budget<maquina.getCustoOperacao()){ 
                    sucesso=false; 
                    break; 
                }
                
                if (!maquina.podeOperar()) {
                    int op;
                    System.out.println("[NOK] Maquina quebrada, produção parada. Deseja repara a "+maquina.getNome()+"?");
                    System.out.println("1 - Sim");
                    System.out.println("2 - Não");
                    op=lerInt("Escolha");
                    switch (op) {
                        case 1:
                                    if(maquina.reparar(cenario, this)){
                                        System.out.println("[OK] Maquina reparada com sucesso");
                                    }else{
                                        System.out.println("[NOK] Budget insuficiente. Reparo não realizado");
                                        sucesso=false;
                                    }
                            break;

                        case 2:
                                    System.out.println("[OK] Reparo não realizado");
                                    sucesso=false;
                            break;
                    
                        default:
                                    System.out.println("[NOK] Opção invalida, reparo não realizado");
                                    sucesso=false;
                            break;
                    }
                
                
                
                }

                maquina.ligar();
                
                budget-=maquina.getCustoOperacao();
                int feitos=maquina.processar(unidade,materiaPrima,1); maquina.desligar();
                if(feitos<1){ 
                    sucesso=false;
                    break; 
                }
            }
            
            if(sucesso){ 
                materiaPrima.consumir(unidade.getDemandaMateriaPrima());
                unidade.processar(StatusProduto.FINALIZADO); 
                produtosFabricados.add(unidade); 
                concluidos++; 
            }

            

        }
        demanda.registrarProduzidos(concluidos);
        if(demanda.getStatus()!=StatusDemanda.CONCLUIDA){ 
            System.out.println("[NOK] Producao interrompida; Demanda "+demanda.getId()+" ainda possui "+demanda.getQuantidadeRestante()+" "+demanda.getProduto().getNome()+"(s) pendentes."); 
            demanda.pendente();
        }else{
            System.out.println("[OK] Demanda "+demanda.getId()+" concluida: "+concluidos+" unidade(s).");
        }
    }

    /**
     * Compra nova materia prima caso seja possivel de acordo com orcamento disponivel.
     * 
     * @param q Quantidade de materia prima necessaria.
     * @return Verdadeiro se foi comprado, falso caso contrario.
     */
    public boolean comprarMateriaPrima(double q){ 
        if(q<=0)return false; 
        double custo=q*materiaPrima.getCusto(); 
        if(custo>budget)return false; 
        budget-=custo; 
        materiaPrima.adicionarEstoque(q); 
        return true; 
    }


    public void exibirBudget(){ System.out.printf("Budget atual: R$ %.2f%n",budget); }
    public void exibirEstoque(){ System.out.printf("%s: %.2f %s | custo/unidade: R$ %.2f%n",materiaPrima.getNome(),materiaPrima.getQuantidade(),materiaPrima.getUnidade(),materiaPrima.getCusto()); }
    
    public void exibirArmazem(){
        if(produtosFabricados.isEmpty()){ 
            System.out.println("Armazem vazio."); 
            return; 
        }
        for(Produto p:produtosFabricados) {
            System.out.printf("%s | %s | lote %d | qualidade %.2f | risco %s%n",p.getId(),p.getNome(),p.getLote(),p.getQualidade(),p.precisaManutencao()?"ATENCAO":"NORMAL");
        }
        System.out.println("Quantidade total: "+produtosFabricados.size());
    }

    /**
     * Gera relatorio de itens Auditaveis e imprime.
     */
    public void gerarAuditoriaGeral(){
        ArrayList<Auditavel> itens=new ArrayList<>(); itens.addAll(maquinas); itens.addAll(produtosFabricados);
        if(itens.isEmpty()){ 
            System.out.println("Nenhum item para auditar."); 
            return; 
        }
        for(Auditavel a:itens) {
            System.out.println(a.gerarRelatorioDiagnostico());
        }
    }


    /**
     * Calcula o custo de producao total de um produto.
     * 
     * @param d Demanda analizada.
     * @param q Quantidade da demanda.
     * @return Custo unitario calculado multiplicado pela quantidade.
     */
    private double calcularCustoProducao(Demanda d,int q){ 
        double custo=0; 
        for(Maquina m:d.getProduto().getProcessoProducao()) {
            custo+=m.getCustoOperacao();  
        }
        return custo*q;
    }

    /**
     * Leitor de entrada do usuario, com fator de seguranca para receber apenas entradas numericas inteiras.
     * 
     * @param msg Mensagem pedindo entrada do usuario.
     * @return Numero entrado.
     */
    private static int lerInt(String msg){
         while(true){ 
            System.out.print(msg); 
            if(teclado.hasNextInt())return teclado.nextInt();
            teclado.next(); 
            System.out.println("Digite apenas numeros inteiros."); 
       }
    }

    /**
     * Checa se um gasto esta dentro do orcamento e remove essa quantia do orcamento caso esteja.
     * 
     * @param valor Valor do gasto.
     * @return Verdadeiro se gasto foi feito, falso caso contrario.
     */
    public boolean gastar(double valor){
        if(getBudget()>=valor){
            budget-=valor; 
            return true;
        }else{
            return false;
        }
    }

}