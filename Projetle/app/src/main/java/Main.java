import java.util.*;
import Estrategias.*;
import Estrategias.Demandas.Demanda;
import PackMaquinas.*;
import PackMateriaPrima.MateriaPrima;
import PackProdutos.*;
import outros.*;

public class Main {
    private static final Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("RL SOLUCOES - Fabrica de componentes para motores");
        System.out.println("\"Movendo o futuro, peca por peca\"");
        System.out.println("Desenvolvido por: Rodrigo Gonçaves e Lucas Marques"); 
        System.out.println("============================================================");

        Cenario cenario = escolherCenarioPrimeiraVez();
        MateriaPrima aluminio = new MateriaPrima("AL2618","Aluminio",100.0,"kg",10.0);

        MaquinaUsinagem torno = new MaquinaUsinagem("Torno CNC",50,10.0,cenario);
        MaquinaTratamentoSuperficial tratamento = new MaquinaTratamentoSuperficial("Linha de Tratamento",50,7.5,cenario);
        MaquinaInspecao inspecao = new MaquinaInspecao("Inspecao Dimensional",50,5.0,cenario);
        ArrayList<Maquina> processo = new ArrayList<>(List.of(torno,tratamento,inspecao));

        ArrayList<Produto> catalogo = new ArrayList<>();
        catalogo.add(new Cabecote("CAT-CAB","Cabecote",8.0,0.,processo,4.0,2.0, cenario));
        catalogo.add(new Virabrequim("CAT-VIR","Virabrequim",10.0,0.,processo,5.0,2.5, cenario));
        catalogo.add(new Pistao("CAT-PIS","Pistao",2.0,0.,processo,2.0,1.0, cenario));
        catalogo.add(new Bloco("CAT-BLO","Bloco do Motor",15.0,0.,processo,7.0,3.0, cenario));

        GerenciadorProducao gerenciadorProducao = new GerenciadorProducao(cenario,aluminio);
        gerenciadorProducao.adicionarMaquina(torno); gerenciadorProducao.adicionarMaquina(tratamento); gerenciadorProducao.adicionarMaquina(inspecao);

        int op;
        do {
            cabecalho(gerenciadorProducao);
            System.out.println("1 - Demandas");
            System.out.println("2 - Fabricacao");
            System.out.println("3 - Consultar estoque/armazem");
            System.out.println("4 - Comprar materia-prima");
            System.out.println("5 - Gerenciar estrategia");
            System.out.println("6 - Auditoria");
            System.out.println("7 - Alterar cenario");
            System.out.println("8 - Realizar manutenção");
            System.out.println("0 - Sair");
            op=lerInt("Escolha: ");
            switch(op){
                case 1: menuDemandas(gerenciadorProducao,catalogo);break;
                case 2: gerenciadorProducao.executarProximaProducao();break;
                case 3: gerenciadorProducao.exibirEstoque(); gerenciadorProducao.exibirArmazem();break; 
                case 4: 
                    double q=lerDouble("Quantidade de aluminio para comprar: ");
                    System.out.println(gerenciadorProducao.comprarMateriaPrima(q)?"[OK] Compra realizada.":"[NOK] Compra invalida ou budget insuficiente.");
                    break; 
                case 5: menuEstrategia(gerenciadorProducao);break;
                case 6: gerenciadorProducao.gerarAuditoriaGeral();break;
                case 7: 
                    cenario=escolherCenario(); 
                    gerenciadorProducao.setCenario(cenario);
                    for (Maquina maquina : processo) {
                        maquina.configurarCenario(cenario);
                    }
                    break;
                case 8: menuManutencao(cenario, gerenciadorProducao, processo);break;
                case 0: System.out.println("Encerrando a fabrica.");break;
                default: System.out.println("Opcao invalida.");break;
            }
        } while(op!=0);
    }

    private static Cenario escolherCenarioPrimeiraVez(){
        while(true){
            System.out.println("0 - Cenario de testes (Ideal, com dinheiro infinito)"); 
            System.out.println("1 - Cenario Ideal"); 
            System.out.println("2 - Cenario Apocaliptico");
             int op=lerInt("Escolha: ");

              if(op==1||op==2||op==0){
                return Cenario.escolha(op); 
              }

            System.out.println("Opcao invalida."); 
        }
    }


    private static Cenario escolherCenario(){
        while(true){
            System.out.println("1 - Cenario Ideal"); 
            System.out.println("2 - Cenario Apocaliptico");
             int op=lerInt("Escolha: ");

              if(op==1||op==2||op==0){
                return Cenario.escolha(op); 
              }

            System.out.println("Opcao invalida."); 
        }
    }
    
    private static void cabecalho(GerenciadorProducao gerenciarProducao){
        System.out.println("\n============================================================");
        System.out.println("ESTRATEGIA ATUAL: "+gerenciarProducao.getEstrategia().getNomeEstrategia());
        System.out.println("CENARIO ATIVO: "+gerenciarProducao.getCenario().getDescricao());
        System.out.printf("BUDGET ATUAL: R$ %.2f%n",gerenciarProducao.getBudget());
        System.out.println("============================================================");
    }
    
    private static void menuManutencao(Cenario cenario,GerenciadorProducao gerenciadorProducao,ArrayList<Maquina> maquinas){
        int op;
        System.out.println("Qual maquina dejesa reparar?");
        for (int i=0;i<maquinas.size();i++) {
            System.out.println((i+1)+" - " + maquinas.get(i).getNome());
        }
        op=lerInt("Escolha: ");
        if(op-1>maquinas.size()){
            System.out.println("Opção invalida.");
        } else {
            if(maquinas.get(op-1).reparar(cenario, gerenciadorProducao)){
                System.out.println("[OK] Maquina reparada com sucesso");
            }else{
                System.out.println("[NOK] Budget insuficiente. Reparo não realizado");
            }
        }
    }


    private static void menuDemandas(GerenciadorProducao gerenciarProducao,ArrayList<Produto> catalogo){
        System.out.println("\n1 - Registrar demanda");
        System.out.println("2 - Atualizar demanda");
        System.out.println("3 - Listar demandas");
        System.out.println("0 - Voltar");

        int op=lerInt("Escolha: ");
        int produto;
        int qnt;
        int id;

        switch(op){ 
        case 1:
            System.out.println("");
            for(int i=0;i<catalogo.size();i++){
                System.out.printf("%d - %s\n", i+1, catalogo.get(i).getNome()); 
            }

            produto=lerInt("Produto: ");
            qnt=lerInt("Quantidade: "); 
            if(produto>=1&&produto<=catalogo.size()&&qnt>0){
                gerenciarProducao.registrarDemanda(catalogo.get(produto-1),qnt);
                System.out.println("[OK] Demanda registrada.");
            }else{
                System.out.println("Dados invalidos."); 
            }
        break;

        case 2:
            id=lerInt("ID da demanda: "); 
            qnt=lerInt("Quantidade: "); 
            System.out.println(gerenciarProducao.atualizarDemanda(id,qnt)?"[OK] Atualizada.":"[NOK] Demanda nao encontrada.");
         break;

        case 3:
            if(gerenciarProducao.getDemandas().isEmpty()){
                System.out.println("Nenhuma demanda."); 
            }else{
                for(Demanda d:gerenciarProducao.getDemandas()){
                    System.out.println(d.getDemanda()); 
                }
            }
        break;
        }
    }

    private static void menuEstrategia(GerenciadorProducao gerenciarProducao){
        System.out.println("1 - Ordem de Chegada"); 
        System.out.println("2 - Maior Demanda");
        System.out.println("3 - Maximo de Produtos");
        int op=lerInt("Escolha: ");

        switch(op){ 
            case 1: gerenciarProducao.setEstrategia(new EstrategiaOrdemChegada());break; 
            case 2: gerenciarProducao.setEstrategia(new EstrategiaMaiorDemanda()); break;
            case 3: gerenciarProducao.setEstrategia(new EstrategiaMaximoProdutos()); break;
            default: System.out.println("Opcao invalida."); break;}
    }
    
    private static int lerInt(String msg){
         while(true){ 
            System.out.print(msg); 
            if(teclado.hasNextInt()){
                return teclado.nextInt(); 
            }
            teclado.next(); 
            System.out.println("Digite apenas numeros inteiros."); 
        } 
    }
    
    private static double lerDouble(String msg){ 
        while(true){ 
            System.out.print(msg); 
            if(teclado.hasNextDouble()){
                return teclado.nextDouble(); 
            }
            teclado.next(); 
            System.out.println("Digite apenas numeros."); 
        } 
    }
}
