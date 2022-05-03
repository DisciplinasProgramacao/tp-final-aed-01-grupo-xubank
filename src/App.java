import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class App {

    static Random aleatorio = new Random(System.nanoTime());
    static final String nomeArquivo = "contas-bancarias.txt";   //nome do arquivo de dados
    static TabHash clientes;    //tabela hash ded clientes

    public static TabHash carregarDados() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(nomeArquivo));  //cria e le o arquivo
        TabHash novosClientes = new TabHash(110000);    //cria uma lista de contas

        while(arquivo.hasNextLine()){ //enquanto tiver uma proxima linha no arquivo

            String linha = arquivo.nextLine();   //le a linha do arquivo
            String dados[] = linha.split(";");  //separa os dados no ";"

            int numero = Integer.parseInt(dados[0]); //primeiro dado: numero da conta
            String cpf = (dados[1]);    //segundo dado: cpf
            double saldo = Double.parseDouble(dados[2]);    //terceiro dado: saldo inicial da conta

            ContaBancaria novaConta = new ContaBancaria(numero, cpf, saldo);    //cria a nova conta
            adicionarContas(novaConta, novosClientes);  //joga a conta na lista de contas do respectivo cliente
        }
        arquivo.close();
        return novosClientes;   //retorna uma tabela hash de clientes
    }

    public static void adicionarContas(ContaBancaria nova, TabHash dados){
        Cliente novo = dados.buscar(nova.cpf);  //procura a posicao na tabela
        if(novo == null){   //se nao existir nenhum  cliente naquela posicao
            novo = new Cliente(nova.cpf);   //cria o cliente
            dados.inserir(novo.cpf, novo);  //insere ele na tabela
        }
        novo.cntsCliente.enfileirar(nova);  //lista a conta na lista de contas daquele cliente
    }

    public static int mostrarMenu(Scanner teclado){
        System.out.println("CONTAS BANCARIAS XUBANK");
        System.out.println("=======================");
        System.out.println("1 - Consultar contas de um cliente");
        System.out.println("2 - Adicionar conta");
        System.out.println("3 - Exibir clientes ordenados pelo cpf");
        System.out.println("0 - Sair");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    public static void limparTela(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void pausar(Scanner teclado){
        System.out.print("Tecle ENTER para continuar. "); 
        teclado.nextLine();
    }

    public static String lerTeclado(String mensagem, Scanner teclado){
        System.out.print(mensagem + " ");
        return teclado.nextLine();
    }

    public static void main(String[] args) throws Exception{
        Scanner teclado = new Scanner(System.in);
        clientes = carregarDados();
        int opcao;

        do{
            limparTela();
            opcao = mostrarMenu(teclado);

            switch(opcao){
                case 1:
                    limparTela();
                    Cliente clienteDesejado;
                    String cpfDesejado = lerTeclado("CPF do cliente: ", teclado);    //recebe o cpf do cliente que deseja buscar
                    clienteDesejado = clientes.buscar(cpfDesejado);   //busca a cliente desejado

                    limparTela();
                    if(clienteDesejado != null) //se o cliente existir mostra os dados da conta
                        System.out.println(clienteDesejado.imprimir());
                    else    //se nao: cliente nao cadastrado
                        System.out.println("Cliente não cadastrado.");
                    pausar(teclado);
                break;

                case 2: 
                    limparTela();
                    int novoNumero =  aleatorio.nextInt(100000, 1000000);
                    String cpf = lerTeclado("CPF: ", teclado);    //recebe o cpf da nova conta
                    double sld = 0;
                    ContaBancaria novaConta = new ContaBancaria(novoNumero, cpf, sld);    //cria uma nova conta, com o cpf recebido e saldo inicial 0
                    adicionarContas(novaConta, clientes);   //adiciona a conta a lista de contas do respectivo cliente
                    System.out.println("Conta cadastrada.");
                    pausar(teclado);
                break;
                /*
                case 3:
                    limparTela();
                    File arqOrdenado = new File("contas-ordena");    //abre o arquivo
                    if(arqOrdenado.exists()){    //se o arquivo existir e nenhuma conta tiver sido feita na lista
                        System.out.println("Abrindo arquivo...");
                        java.awt.Desktop.getDesktop().open((arqOrdenado));  //executa o arquivo
                        System.out.println("Arquivo aberto!");
                    }
                    else{   //se nao existir ou houver alteracoes
                        FileWriter gravador = new FileWriter(arqOrdenado, false);   //abre o gravador de arquivo
                        System.out.println("Abrindo arquivo...");
                        Lista contasOrdenadas = ordenar(contas);    //ordena as contas em uma nova lista
                        gravador.append(contasOrdenadas.imprimir());    //grava no arquivo as contas em ordem
                        contasOrdenadas = new Lista();  //apaga a lista
                        gravador.close();
                        java.awt.Desktop.getDesktop().open((arqOrdenado));  //executa o arquivo
                        System.out.println("Arquivo aberto!");
                    }
                    arqOrdenado.deleteOnExit();
                    pausar(teclado);
                break;
                    */
                default:
                    limparTela();
                break;
            }
        }while(opcao!=0);

        teclado.close();
        clientes.salvarDados();
    }
}