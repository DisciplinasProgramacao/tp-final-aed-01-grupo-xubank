import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App {

    static final String arquivoContas = "contas-bancarias.txt";   //nome do arquivo de contas
    static final String arquivoClientes = "clientes.txt";   //nome do arquivo de clientes
    static final String arquivoOperacoes = "operacoes.txt"; //nome do arquivo de operacoes
    static TabHash clientes;    //tabela hash ded clientes
    static Lista contas;    //lista de contas
    static Fila operacoes;  //fila de operacoes
    static int quantContas; //quantidade de contas
    static int quantClientes;   //quantidade de clientes

    public static Lista listarContas() throws FileNotFoundException{
        Lista novasContas = new Lista();
        Scanner arquivo = new Scanner(new File(arquivoContas));   //cria e le o arquivo
        int qtd = Integer.parseInt(arquivo.nextLine()); //le a quantidade de contas

        for(int i = 0; i < qtd; i++){
            String[] dados = arquivo.nextLine().split(";");   //le a linha do arquivo e separa os dados no ";"

            int numero = Integer.parseInt(dados[0]); //primeiro dado: numero da conta
            String cpf = (dados[1]);    //segundo dado: cpf
            double saldo = Double.parseDouble(dados[2]);    //terceiro dado: saldo inicial da conta

            ContaBancaria nova = new ContaBancaria(numero, cpf, saldo);    //cria a nova conta
            novasContas.inserir(nova);  //insere a nova conta em uma lista de contas
        }
        arquivo.close();
        quantContas = qtd;  //conta a quantidade de contas
        return novasContas; //retorna a lista de contas
    }

    public static TabHash listarClientes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoClientes));  //cria e le o arquivo
        int quant = Integer.parseInt(arquivo.nextLine());
        int tam = (int)(quant * 1.25); //cria a tabela com 25% de sobra
        TabHash novosClientes = new TabHash(tam);    //cria uma tabela hash de clientes

        for(int i = 0; i < quant; i++){ //enquanto tiver uma proxima linha no arquivo

            String dados[] = arquivo.nextLine().split(";");  //le a linha do arquivo e separa os dados no ";"

            String cpf = dados[0];  //primeiro dado: cpf
            String nome = dados[1]; //segundo dado: nome

            Cliente novo = new Cliente(cpf, nome);    //cria o novo cliente
            novosClientes.inserir(cpf, novo);   //insere o novo cliente na tabela
        }
        arquivo.close();
        quantClientes = quant;  //conta a quantidade de clientes
        return novosClientes;   //retorna uma tabela hash de clientes
    }

    public static Fila enfileirarOperacoes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoOperacoes));  //cria e le o arquivo
        Fila novasOperacoes = new Fila();   //cria a fila de operacoes

        while(arquivo.hasNextLine()){   //enquanto existir linha no arquivo

            String dados[] = arquivo.nextLine().split(";");  //le a linha do arquivo e separa os dados no ";"

            int numero = Integer.parseInt(dados[0]);    //dado 1: numero da conta
            int codigo = Integer.parseInt(dados[1]);    //dado 2: codigo da operacao
            double valor = Double.parseDouble(dados[2]);    //dado 3: valor da operacao
            String data = (dados[3]);   //dado 4: dia que a operacao foi realizada

            Operacao nova = new Operacao(numero, codigo, valor, data);  //cria nova operacao
            novasOperacoes.enfileirar(nova);    //joga na fila de operacoes
        }
        arquivo.close();
        return novasOperacoes;//retorna a fila
    }

    public static void carregarListaDeCadaCliente(){
        if(!contas.vazia()){//se a lista de contas nao estiver vazia
            Elemento aux = contas.prim.prox;
            while(aux != null){
                ContaBancaria nova = aux.conta;
                Cliente novo = clientes.buscar(nova.cpf);  //procura a posicao na tabela
                novo.cntsCliente.inserir(nova);  //insere a conta na lista de contas daquele cliente
                aux = aux.prox;
            }
        }
    }

    public static void carregarOperacoesDeCadaConta(){
        if(!operacoes.vazia()){//se a fila de operacoes nao estiver vazia
            Caixa aux = operacoes.prim.prox;
            while(aux != null){
                ContaBancaria temp = contas.buscar(aux.operacao.num);   //busca a conta que corresponde a respectiva operacao
                aux = aux.prox;
                temp.operacoes.enfileirar(operacoes.desenfileirar());   //enfileira a operacao na fila daquela conta
            }
        }
    }

    public static void salvarDadosOperacoes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoContas, false);   //cria o escritor para sobrescrever o arquivo
        Elemento aux = contas.prim.prox;
        while(aux != null){ //enquanto aux nao for null
            if(!aux.conta.operacoes.vazia()){   //se a fila nao estiver vazia
                Caixa temp = aux.conta.operacoes.prim.prox;
                while(temp != null){    //enquanto temp nao for null
                    escritor.append(temp.operacao.num + ";" + temp.operacao.codigo + ";" + temp.operacao.valor + ";" + temp.operacao.data + "\n");  //sobrescreve o arquivo
                    temp = temp.prox;   //caminha com o temp
                }
            }
            aux = aux.prox; //caminha com o aux
        }
        escritor.close();
    }
    
    public static void salvarDadosClientes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoClientes, false);   //cria o escritor para sobrescrever o arquivo
        Entrada[] dados = clientes.dados;
        escritor.append(quantClientes + "\n");   //grava a quantidade de clientes
        for(int i = 0; i < dados.length; i++){  //enquanto i menor que tam da tabela
            if(dados[i].valido){    //se o dado for valido
                escritor.append(dados[i].cliente.cpf + ";" + dados[i].cliente.nome + "\n");    //sobrescreve o arquivo com os dados
            }
        }
        escritor.close();
    }
    
    public static void salvarDadosContas() throws IOException{
        FileWriter escritor = new FileWriter(arquivoContas, false);   //cria o escritor para sobrescrever o arquivo
        Elemento aux = contas.prim.prox;
        escritor.append(quantContas + "\n");    //grava a quantidade de contas
        while(aux != null){ //enquanto aux nao for null
            escritor.append(aux.conta.num + ";" + aux.conta.cpf + ";" + aux.conta.saldo + "\n");    //sobrescreve o arquivo de dados
            aux = aux.prox; //caminha com o aux
        }
        escritor.close();
    }

    public static void ordenar(ContaBancaria[] dados, int inicio, int fim){
        if(inicio < fim){
            int part = particao(dados, inicio, fim);    //executa o algoritmo do inicio ao fim
            ordenar(dados, inicio, part - 1);   //executa do inicio ate part - 1
            ordenar(dados, part + 1, fim);  //executa da posicao part + 1 ate o fim
        }
    }

    public static int particao(ContaBancaria[] dados, int inicio, int fim){
        int pivot = dados[fim].num; //pivot recebe num da conta na posicao final
        int part = inicio - 1;  //particao comeca antes do inicio

        for (int i = inicio; i < fim; i++)  //para cada indice do inicio ao fim
            if(dados[i].num < pivot){   //se o num da conta na posicao i for menor que o pivot
                part++; //caminha com a particao
                trocar(dados, part, i); //troca os dados na posicao i e part
            }
        part++; //caminha com a particao
        trocar(dados, part, fim);   //troca o pivot com a particao
        return part;    //retorna a particao
    }

    static void trocar(ContaBancaria[] dados, int pos1, int pos2){  //troca 2 posicoes em um vetor
        ContaBancaria aux = dados[pos1];
        dados[pos1] = dados[pos2];
        dados[pos2] = aux;
    }

    public static void cadastrarContaNaListaDoCliente(Scanner teclado, ContaBancaria conta){
        Cliente aux = clientes.buscar(conta.cpf);   //busca o cliente
        if(aux == null){    //se o cliente nao existir
            String nome = lerTeclado("Nome do cliente: ", teclado); //pede o nome do cliente
            aux = new Cliente(conta.cpf, nome); //cria o novo cliente
            clientes.inserir(conta.cpf, aux);   //insere o cliente na tabela hash
            quantClientes++;    //soma + 1 no contador de clientes
        }
        aux.cntsCliente.inserir(conta); //insere a conta na lista de contas do respectivo cliente
    }

    public static int mostrarMenu(Scanner teclado){
        System.out.println("CONTAS BANCARIAS XUBANK");
        System.out.println("===========================");
        System.out.println("1 - Consultar conta");
        System.out.println("2 - Consultar cliente");
        System.out.println("3 - Adicionar conta");
        System.out.println("4 - Exibir contas ordenadas");
        System.out.println("5 - Checar Extrato");
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
        contas = listarContas();
        clientes = listarClientes();
        operacoes = enfileirarOperacoes();
        carregarOperacoesDeCadaConta();
        carregarListaDeCadaCliente();

        int opcao, num;
        double saldo;
        String cpf;

        do{
            limparTela();
            opcao = mostrarMenu(teclado);

            switch(opcao){
                case 1:
                limparTela();

                num = Integer.parseInt(lerTeclado("número da conta: ", teclado));    //recebe o cpf do cliente que deseja buscar
                ContaBancaria contaDesejada = contas.buscar(num);   //busca a cliente desejado

                if(contaDesejada != null){ //se o cliente existir mostra os dados da conta
                    limparTela();
                    System.out.println(contaDesejada.dadosConta());
                }
                else    //se nao cliente nao cadastrado
                    System.out.println("Conta não cadastrada");
                pausar(teclado);
                break;

                case 2:
                limparTela();
                cpf = lerTeclado("CPF: ", teclado); //le o cpf
                Cliente desejado = clientes.buscar(cpf);    //busca o cliente
                if(desejado != null){ //se o cliente existir mostra os dados do cliente
                    limparTela();
                    System.out.println(desejado.contasCliente());
                }
                else    //se nao cliente nao cadastrado
                    System.out.println("Cliente não cadastrado");
                pausar(teclado);
                break;

                case 3: 
                ContaBancaria aux;
                int novoNumero;
                do{ //faca
                    limparTela();
                    novoNumero =  Integer.parseInt(lerTeclado("Número: ", teclado));    //recebe o número da conta
                    aux = contas.buscar(novoNumero);    //busca a conta com esse numero
                    if(aux != null){    //se ja existir
                        System.out.println("Conta já existe, digite outro número");
                        pausar(teclado);
                    }
                }while(aux != null);    //enquanto ja existir uma conta com esse numero
                cpf = lerTeclado("CPF: ", teclado);    //recebe o cpf da nova conta
                saldo = 0;
                ContaBancaria nova = new ContaBancaria(novoNumero, cpf, saldo);    //cria uma nova conta, com o cpf recebido e saldo inicial 0
                contas.inserir(nova);   //insere a nova conta na lista de contas
                quantContas++;
                cadastrarContaNaListaDoCliente(teclado, nova);  //cadastra a nova conta na lista do respectivo cliente
                System.out.println("Conta cadastrada");
                pausar(teclado);
                break;
                
                case 4:
                limparTela();
                System.out.println("Abrindo o arquivo");
                ContaBancaria[] ordenadas = new ContaBancaria[quantContas]; //cria um vetor de contas
                Elemento temp = contas.prim.prox;
                int i = 0;
                while(temp != null){
                    ordenadas[i] = temp.conta;  //passa as contas da lista para o vetor
                    temp = temp.prox;
                    i++;
                }
                ordenar(ordenadas, 0, quantContas - 1); //ordena o vetor
                File ordenado = new File("contas-ordenadas.txt");
                FileWriter gravador = new FileWriter(ordenado);
                for(ContaBancaria conta : ordenadas)
                    gravador.append(conta.dadosConta() + "\n"); //grava o vetor ordenado num arquivo
                gravador.close();
                java.awt.Desktop.getDesktop().open((ordenado)); //executa o arquivo
                System.out.println("Arquivo aberto");
                pausar(teclado);
                ordenado.delete();  //exclui o arquivo
                break;

                case 5:
                limparTela();
                num = Integer.parseInt(lerTeclado("numero da conta: ", teclado));   //le o numero da conta que deseja buscar
                ContaBancaria desejada = contas.buscar(num);    //busca a conta na lista de contas
                if(desejada != null){   //se ela existir
                    limparTela();
                    System.out.println(desejada.operacoes.extrato());   //mostra o extrato com as operacoes
                }
                else    //se nao
                    System.out.println("Conta não encontrada");
                pausar(teclado);
                break;

                default:
                    limparTela();
                break;
            }
        }while(opcao!=0);

        teclado.close();
        salvarDadosOperacoes();
        salvarDadosClientes();
        salvarDadosContas();
    }
}