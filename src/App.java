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

    public static TabHash carregarClientes() throws FileNotFoundException{
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

    public static TabHash carregarContas() throws FileNotFoundException{
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
        quantContas = quant;
        return novasContas;
    }

    public static void carregarOperacoes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoOperacoes));

        while(arquivo.hasNextLine()){

            String dados[] = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            int codigo = Integer.parseInt(dados[1]);
            double valor = Double.parseDouble(dados[2]);
            String data = (dados[3]);

            Operacao nova = new Operacao(numero, codigo, valor, data);
            inserirOperacaoNaConta(nova);
        }
        arquivo.close();
    }

    public static void inserirOperacaoNaConta(Operacao nova){
        ContaBancaria aux = new ContaBancaria(nova.num, -1, 0);
        ContaBancaria desejada = (ContaBancaria)contas.buscar(aux);
        desejada.operacoes.inserir(nova);
    }
    
    public static void salvarDadosClientes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoClientes, false);
        Entrada[] dados = clientes.dados;
        escritor.append(quantClientes + "\n");
        for(int i = 0; i < dados.length; i++){
            if(dados[i].checarValidez()){
                Cliente aux = (Cliente)dados[i].dado;
                escritor.append(aux.cpf + ";" + aux.nome + "\n");
            }
        }
        escritor.close();
    }
    
    public static void salvarDadosContas() throws IOException{
        FileWriter escritor = new FileWriter(arquivoContas, false);
        Entrada[] dados = contas.dados;
        escritor.append(quantContas + "\n");
        for(int i = 0; i < dados.length; i++){
            if(dados[i].checarValidez()){
                ContaBancaria aux = (ContaBancaria)dados[i].dado;
                escritor.append(aux.num + ";" + aux.cpf + ";" + aux.saldo + "\n");
            }
        }
        escritor.close();
    }

    public static void salvarDadosOperacoes() throws IOException{
        FileWriter escritor = new FileWriter(arquivoOperacoes, false);
        Entrada[] dados = contas.dados;
        for(int i = 0; i < dados.length; i++) {
            if(dados[i].dado != null){
                ContaBancaria conta = (ContaBancaria)dados[i].dado;
                Elemento aux = conta.operacoes.prim.prox;
                while(aux != null){
                    Operacao operacao = (Operacao)aux.dado;
                    escritor.append(operacao.num + ";" + operacao.codigo + ";" + operacao.valor + ";" + operacao.data + "\n");
                    aux = aux.prox;
                }
            }
        }
        escritor.close();
    }

    public static void ordenar(IComparavel[] dados, int inicio, int fim){
        if(inicio < fim){
            int part = particao(dados, inicio, fim);
            ordenar(dados, inicio, part - 1);
            ordenar(dados, part + 1, fim);
        }
    }

    public static int particao(IComparavel[] dados, int inicio, int fim){
        IComparavel pivot = dados[fim];
        int part = inicio - 1;

        for (int i = inicio; i < fim; i++)
            if(dados[i].menorQue(pivot)){
                part++;
                trocar(dados, part, i);
            }
        part++;
        trocar(dados, part, fim);
        return part;
    }

    static void trocar(IComparavel[] dados, int pos1, int pos2){
        IComparavel aux = dados[pos1];
        dados[pos1] = dados[pos2];
        dados[pos2] = aux;
    }

    public static void cadastrarContaNaListaDoCliente(Scanner teclado, ContaBancaria conta){
        Cliente aux = new Cliente(conta.cpf, "");
        Cliente desejado = (Cliente) clientes.buscar(aux);
        if(desejado == null)
            cadastrarNovoCliente(teclado, conta.cpf);

        desejado.contasDoCliente.inserir(conta);
        desejado.saldoTotal += conta.saldo;
    }

    public static void cadastrarNovoCliente(Scanner teclado, long cpfCliente){
        String nome = lerTeclado("Nome do cliente: ", teclado);
        Cliente novo = new Cliente(cpfCliente, nome);
        clientes.inserir(cpfCliente, novo);
        quantClientes++;
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
        clientes = carregarClientes();
        contas = carregarContas();
        carregarOperacoes();
        int opcao, num;
        long cpf;

        /*int j = 0;
        Cliente[] vetor = new Cliente[quantClientes];

        for(int i = 0; i < clientes.tam; i++){
            if(clientes.dados[i].checarValidez()){
                vetor[j] = (Cliente)clientes.dados[i].dado;
                j++;
            }
        }

        for(int i = vetor.length - 1; i >= vetor.length - 10; i--){
            System.out.println(vetor[i].saldoTotal);
        }

        ordenar(vetor, 0, vetor.length - 1);

        for(int i = vetor.length - 1; i > vetor.length - 10; i--){
            System.out.println(vetor[i].toString());
        }*/

        do{
            limparTela();
            opcao = mostrarMenu(teclado);

            switch(opcao){
                case 1:
                limparTela();

                num = Integer.parseInt(lerTeclado("número da conta: ", teclado));
                ContaBancaria contaAux = new ContaBancaria(num, -1, 0);
                ContaBancaria contaDesejada = (ContaBancaria)contas.buscar(contaAux);

                if(contaDesejada != null){
                    limparTela();
                    System.out.println(contaDesejada.toString());
                }
                else{
                    System.out.println("Conta não cadastrada");
                }
                pausar(teclado);
                break;

                case 2:
                limparTela();

                cpf = Long.parseLong(lerTeclado("CPF: ", teclado));
                Cliente clienteAux = new Cliente(cpf, "");
                Cliente desejado = (Cliente)clientes.buscar(clienteAux);

                if(desejado != null){
                    limparTela();
                    System.out.println(desejado.toString());
                }
                else{
                    System.out.println("Cliente não cadastrado");
                }
                pausar(teclado);
                break;

                case 3:
                ContaBancaria desejada;
                int novoNumero;
                do{
                    limparTela();
                    novoNumero =  Integer.parseInt(lerTeclado("Número: ", teclado));
                    contaAux = new ContaBancaria(novoNumero, -1, 0);
                    desejada = (ContaBancaria) contas.buscar(contaAux);
                    if(desejada != null){
                        System.out.println("Conta já existe, digite outro número");
                        pausar(teclado);
                    }
                }while(desejada != null);
                cpf = Long.parseLong(lerTeclado("CPF: ", teclado));
                ContaBancaria nova = new ContaBancaria(novoNumero, cpf, 0);
                contas.inserir(novoNumero, nova);
                quantContas++;
                cadastrarContaNaListaDoCliente(teclado, nova);
                System.out.println("Conta cadastrada");
                pausar(teclado);
                break;
                
                case 4:
                limparTela();
                System.out.println("Abrindo o arquivo");
                ContaBancaria[] ordenadas = new ContaBancaria[quantContas];
                int j = 0;
                for(int i = 0; i < contas.tam; i++){
                    if(contas.dados[i].checarValidez()){
                        ordenadas[j] = (ContaBancaria) contas.dados[i].dado;
                        j++;
                    }
                }
                ordenar(ordenadas, 0, quantContas - 1);
                File ordenado = new File("contas-ordenadas.txt");
                FileWriter gravador = new FileWriter(ordenado);
                for(ContaBancaria conta : ordenadas)
                    gravador.append(conta.toString() + "\n");
                gravador.close();
                java.awt.Desktop.getDesktop().open((ordenado));
                System.out.println("Arquivo aberto");
                pausar(teclado);
                ordenado.delete();
                break;

                case 5:
                limparTela();
                num = Integer.parseInt(lerTeclado("numero da conta: ", teclado));
                contaAux = new ContaBancaria(num, -1, 0);
                desejada = (ContaBancaria)contas.buscar(contaAux);
                if(desejada != null){
                    limparTela();
                    System.out.println(desejada.imprimirExtratoDaConta());
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
        salvarDadosContas();
    }
}