import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App {

    static final String arquivoContas = "contas-bancarias.txt";
    static final String arquivoClientes = "clientes.txt";
    static final String arquivoOperacoes = "operacoes.txt";
    static TabHash clientes;
    static TabHash contas;
    static Lista operacoes;
    static int quantContas;
    static int quantClientes;

    public static TabHash listarClientes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoClientes));
        int quant = Integer.parseInt(arquivo.nextLine());
        int tam = (int)(quant * 1.25);
        TabHash novosClientes = new TabHash(tam);

        for(int i = 0; i < quant; i++){

            String dados[] = arquivo.nextLine().split(";");

            long cpf = Long.parseLong(dados[0]);
            String nome = dados[1];

            Cliente novo = new Cliente(cpf, nome);
            novosClientes.inserir(cpf, novo);
        }
        arquivo.close();
        quantClientes = quant;
        return novosClientes;
    }

    public static TabHash listarContas() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoContas));
        int quant = Integer.parseInt(arquivo.nextLine());
        int tam = (int)(quant * 1.25);
        TabHash novasContas = new TabHash(tam);

        for(int i = 0; i < quant; i++){

            String dados[] = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            long cpf = Long.parseLong(dados[1]);
            double saldo = Double.parseDouble(dados[2].replace(",", "."));

            ContaBancaria nova = new ContaBancaria(numero, cpf, saldo);
            novasContas.inserir(numero, nova);
            cadastrarContaNaListaDoCliente(null, nova);
        }
        arquivo.close();
        quantClientes = quant;
        return novasContas;
    }

    /*
     String[] dados = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            String cpf = (dados[1]);
            double saldo = Double.parseDouble(dados[2].replace(",", "."));

            ContaBancaria nova = new ContaBancaria(numero, cpf, saldo);
            novasContas.inserir(nova);
            cadastrarContaNaListaDoCliente(null, nova);
     */

    /*public static Fila enfileirarOperacoes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoOperacoes));
        Fila novasOperacoes = new Fila();

        while(arquivo.hasNextLine()){

            String dados[] = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            int codigo = Integer.parseInt(dados[1]);
            double valor = Double.parseDouble(dados[2]);
            String data = (dados[3]);

            Operacao nova = new Operacao(numero, codigo, valor, data);
            novasOperacoes.enfileirar(nova);
        }
        arquivo.close();
        return novasOperacoes;
    }*/

    public static void carregarListaDeCadaCliente(){
        if(!contas.vazia()){
            Elemento aux = contas.prim.prox;
            while(aux != null){
                ContaBancaria nova = aux.conta;
                Cliente novo = clientes.buscar(nova.cpf);
                novo.cntsCliente.inserir(nova);
                aux = aux.prox;
            }
        }
    }

    /*public static void carregarOperacoesDeCadaConta(){
        if(!operacoes.vazia()){
            Caixa aux = operacoes.prim.prox;
            while(aux != null){
                ContaBancaria temp = contas.buscar(aux.operacao.num);
                aux = aux.prox;
                temp.operacoes.enfileirar(operacoes.desenfileirar());
            }
        }
    }*/

    public static void salvarDadosOperacoes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoContas, false);
        Elemento aux = contas.prim.prox;
        while(aux != null){
            if(!aux.conta.operacoes.vazia()){
                Caixa temp = aux.conta.operacoes.prim.prox;
                while(temp != null){
                    escritor.append(temp.operacao.num + ";" + temp.operacao.codigo + ";" + temp.operacao.valor + ";" + temp.operacao.data + "\n");
                    temp = temp.prox;
                }
            }
            aux = aux.prox;
        }
        escritor.close();
    }
    
    public static void salvarDadosClientes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoClientes, false);
        Entrada[] dados = clientes.dados;
        escritor.append(quantClientes + "\n");
        for(int i = 0; i < dados.length; i++){
            if(dados[i].valido){
                escritor.append(dados[i].cliente.cpf + ";" + dados[i].cliente.nome + "\n");
            }
        }
        escritor.close();
    }
    
    public static void salvarDadosContas() throws IOException{
        FileWriter escritor = new FileWriter(arquivoContas, false);
        Elemento aux = contas.prim.prox;
        while(aux != null){
            escritor.append(aux.conta.num + ";" + aux.conta.cpf + ";" + aux.conta.saldo + "\n");
            aux = aux.prox;
        }
        escritor.close();
    }

    public static void ordenar(ContaBancaria[] dados, int inicio, int fim){
        if(inicio < fim){
            int part = particao(dados, inicio, fim);
            ordenar(dados, inicio, part - 1);
            ordenar(dados, part + 1, fim);
        }
    }

    public static int particao(ContaBancaria[] dados, int inicio, int fim){
        int pivot = dados[fim].num;
        int part = inicio - 1;

        for (int i = inicio; i < fim; i++)
            if(dados[i].num < pivot){
                part++;
                trocar(dados, part, i);
            }
        part++;
        trocar(dados, part, fim);
        return part;
    }

    static void trocar(ContaBancaria[] dados, int pos1, int pos2){
        ContaBancaria aux = dados[pos1];
        dados[pos1] = dados[pos2];
        dados[pos2] = aux;
    }

    public static void cadastrarContaNaListaDoCliente(Scanner teclado, ContaBancaria conta){
        Cliente aux = new Cliente(conta.cpf, "");
        Cliente desejado = (Cliente) clientes.buscar(aux);
        //clientes.buscar(conta.cpf);
        /*if(aux == null){
            String nome = lerTeclado("Nome do cliente: ", teclado);
            aux = new Cliente(conta.cpf, nome);
            clientes.inserir(conta.cpf, aux);
            quantClientes++;
        }*/
        aux.cntsCliente.inserir(conta);
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
        clientes = listarClientes();
        contas = listarContas();
        System.out.println(" ");
        /*
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

                num = Integer.parseInt(lerTeclado("número da conta: ", teclado));
                ContaBancaria contaDesejada = contas.buscar(num);

                if(contaDesejada != null){
                    limparTela();
                    System.out.println(contaDesejada.dadosConta());
                }
                else
                    System.out.println("Conta não cadastrada");
                pausar(teclado);
                break;

                case 2:
                limparTela();
                cpf = lerTeclado("CPF: ", teclado);
                Cliente desejado = clientes.buscar(cpf);
                if(desejado != null){
                    limparTela();
                    System.out.println(desejado.contasCliente());
                }
                else
                    System.out.println("Cliente não cadastrado");
                pausar(teclado);
                break;

                case 3: 
                ContaBancaria aux;
                int novoNumero;
                do{
                    limparTela();
                    novoNumero =  Integer.parseInt(lerTeclado("Número: ", teclado));
                    aux = contas.buscar(novoNumero);
                    if(aux != null){
                        System.out.println("Conta já existe, digite outro número");
                        pausar(teclado);
                    }
                }while(aux != null);
                cpf = lerTeclado("CPF: ", teclado);
                saldo = 0;
                ContaBancaria nova = new ContaBancaria(novoNumero, cpf, saldo);
                contas.inserir(nova);
                quantContas++;
                cadastrarContaNaListaDoCliente(teclado, nova);
                System.out.println("Conta cadastrada");
                pausar(teclado);
                break;
                
                case 4:
                limparTela();
                System.out.println("Abrindo o arquivo");
                ContaBancaria[] ordenadas = new ContaBancaria[quantContas];
                Elemento temp = contas.prim.prox;
                int i = 0;
                while(temp != null){
                    ordenadas[i] = temp.conta;
                    temp = temp.prox;
                    i++;
                }
                ordenar(ordenadas, 0, quantContas - 1);
                File ordenado = new File("contas-ordenadas.txt");
                FileWriter gravador = new FileWriter(ordenado);
                for(ContaBancaria conta : ordenadas)
                    gravador.append(conta.dadosConta() + "\n");
                gravador.close();
                java.awt.Desktop.getDesktop().open((ordenado));
                System.out.println("Arquivo aberto");
                pausar(teclado);
                ordenado.delete();
                break;

                case 5:
                limparTela();
                num = Integer.parseInt(lerTeclado("numero da conta: ", teclado));
                ContaBancaria desejada = contas.buscar(num);
                if(desejada != null){
                    limparTela();
                    System.out.println(desejada.operacoes.extrato());
                }
                else
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
        salvarDadosContas();*/
    }
}