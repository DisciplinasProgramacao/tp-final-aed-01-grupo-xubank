import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class App {

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
            Cliente aux = localizarCliente(nova.cpf);
            aux.inserirNovaConta(nova);
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
            if(dados[i].estahValido()){
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
            if(dados[i].estahValido()){
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

    public static Cliente cadastrarNovoCliente(Scanner teclado, long cpfCliente){
        String nome = lerTeclado("Nome do cliente: ", teclado);
        Cliente novo = new Cliente(cpfCliente, nome);
        clientes.inserir(cpfCliente, novo);
        quantClientes++;
        return novo;
    }

    public static ContaBancaria localizarConta(int num){
        ContaBancaria mock = new ContaBancaria(num, 00000000000, 0);
        ContaBancaria requerida = (ContaBancaria)contas.buscar(mock);
        return requerida;
    }

    public static Cliente localizarCliente(long cpfDoCliente){
        Cliente mock = new Cliente(cpfDoCliente, "");
        Cliente requerido = (Cliente)clientes.buscar(mock);
        return requerido;
    }

    /*public static int mostrarMenu(Scanner teclado){
        System.out.println("CONTAS BANCARIAS XUBANK");
        System.out.println("===========================");
        System.out.println("1 - Consultar Conta");
        System.out.println("2 - Consultar Cliente");
        System.out.println("3 - Adicionar Conta");
        System.out.println("4 - Exibir Contas Ordenadas");
        System.out.println("5 - Checar Extrato");
        System.out.println("6 - Realizar Operacao");
        System.out.println("0 - Sair");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }*/

    public static int mostrarMenu(Scanner teclado){
        System.out.println("CONTAS BANCARIAS XUBANK");
        System.out.println("==============================");
        System.out.println("1 - Opcoes de Admin");
        System.out.println("2 - Operar Conta de Um Cliente");
        System.out.println("0 - Sair");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    public static int menuAdmin(Scanner teclado){
        System.out.println("OPCOES DE ADMIN");
        System.out.println("=========================================");
        System.out.println("1 - Exibir Contas Ordenadas");
        System.out.println("2 - Dados da Conta Com Maior Saldo");
        System.out.println("3 - Saldo Medio dos Clientes do Banco");
        System.out.println("4 - Checar Os 10 Clientes Com Maior Saldo");
        System.out.println("0 - Voltar Ao Menu Anterior");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    public static int menuCliente(Scanner teclado){
        System.out.println("OPERAR CONTA DE UM CLIENTE");
        System.out.println("==========================================");
        System.out.println("1 - Checar Dados do Cliente");
        System.out.println("2 - Checar Extrato de uma Conta");
        System.out.println("3 - Adicionar Nova Conta ao Cliente");
        System.out.println("4 - Checar Dados de uma Conta do Cliente");
        System.out.println("5 - Realizar Operacao de Saque ou Deposito");
        System.out.println("0 - Voltar Ao Menu Anterior");

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
        int opcao, num, opc;
        long cpf;

        do{
            limparTela();
            opcao = mostrarMenu(teclado);

            switch(opcao){
                case 1:
                do{
                    limparTela();
                    opc = menuAdmin(teclado);

                    switch(opc){
                        case 1:
                        limparTela();
                        System.out.println("Abrindo o arquivo");
                        ContaBancaria[] ordenadas = new ContaBancaria[quantContas];
                        int j = 0;
                        for(int i = 0; i < contas.tam; i++){
                            if(contas.dados[i].estahValido()){
                                ordenadas[j] = (ContaBancaria) contas.dados[i].dado;
                                j++;
                            }
                        }
                        ordenar(ordenadas, 0, quantContas - 1);
                        File ordenado = new File("contas-ordenadas.txt");
                        FileWriter gravador = new FileWriter(ordenado, false);
                        for(ContaBancaria conta : ordenadas)
                            gravador.append(conta.toString() + "\n");

                        gravador.close();
                        java.awt.Desktop.getDesktop().open((ordenado));
                        System.out.println("Arquivo aberto");
                        pausar(teclado);
                        ordenado.delete();
                        break;

                        case 4:
                        limparTela();
                        Cliente[] maisRicos = new Cliente[quantClientes];
                        j = 0;
                        for(int i = 0; i < clientes.tam; i++){
                            if(clientes.dados[i].estahValido()){
                                maisRicos[j] = (Cliente) clientes.dados[i].dado;
                                j++;
                            }
                        }
                        ordenar(maisRicos, 0, maisRicos.length - 1);
                        System.out.println("OS DEZ CLIENTES COM MAIOR SALDO");
                        System.out.println("===============================");
                        for(int i = maisRicos.length - 1; i >= maisRicos.length - 10; i--)
                            System.out.println(maisRicos[i].toString());

                        pausar(teclado);
                        break;
                    }
                }while(opc != 0);
                break;
                case 2:
                Cliente requerido;
                do{
                    limparTela();
                    System.out.println("OPERAR CONTA DE UM CLIENTE");
                    System.out.println("==========================");
                    cpf = Long.parseLong(lerTeclado("CPF do Cliente: ", teclado));
                    requerido = localizarCliente(cpf);
                    if(requerido == null)
                        System.out.println("Cliente não encontrado");
                    pausar(teclado);
                }while(requerido == null && cpf != 00000000000);

                do{
                limparTela();
                opc = menuCliente(teclado);
                    switch(opc){
                        case 1:
                        limparTela();
                        System.out.println(requerido.toString());
                        pausar(teclado);
                        break;

                        case 2:
                        limparTela();
                        num = Integer.parseInt(lerTeclado("Numero da Conta: ", teclado));
                        limparTela();
                        ContaBancaria contaRequerida = requerido.buscarConta(num);
                        if(contaRequerida != null)
                            System.out.println(contaRequerida.imprimirExtratoDaConta());
                        else
                            System.out.println("Conta não pertence ao cliente");
                        pausar(teclado);
                        break;

                        case 3:
                        ContaBancaria aux;
                        do{
                            limparTela();
                            System.out.println("ADICIONAR NOVA CONTA");
                            System.out.println("====================");
                            num = Integer.parseInt(lerTeclado("Numero da Conta: ", teclado));
                            aux = localizarConta(num);
                            if(aux != null){
                                System.out.println("Conta já existe");
                                pausar(teclado);
                            }
                        }while(aux!=null);
                        ContaBancaria nova = new ContaBancaria(num, requerido.cpf, 0);
                        contas.inserir(nova.num, nova);
                        requerido.inserirNovaConta(nova);
                        System.out.println("Nova conta criada");
                        pausar(teclado);
                        break;

                        case 4:
                        limparTela();
                        num = Integer.parseInt(lerTeclado("Numero da Conta: ", teclado));
                        limparTela();
                        contaRequerida = requerido.buscarConta(num);
                        if(contaRequerida != null)
                            System.out.println(contaRequerida.toString());
                        else
                            System.out.println("Conta não encontrada");
                        pausar(teclado);
                        break;

                        case 5:
                        ContaBancaria desejada;
                        limparTela();
                        System.out.println("REALIZAR OPERACAO");
                        System.out.println("===========================");
                        num = Integer.parseInt(lerTeclado("numero da conta: ", teclado));
                        desejada = requerido.buscarConta(num);
                        if(desejada == null){
                            System.out.println("Conta não pertence ao cliente");
                        }
                        else{
                            int codigo;
                            do{
                                limparTela();
                                codigo = Integer.parseInt(lerTeclado("0 para deposito e 1 para saque: ", teclado));
                            }while(codigo != 0 && codigo != 1);
                            double valor = Double.parseDouble(lerTeclado("valor: ", teclado));
                            Operacao op = new Operacao(num, codigo, valor, dtf.format(LocalDateTime.now()));
                            requerido.realizarOperacaoEmContaDoCliente(op);
                            System.out.println(op.tipoDeOperacao() + " realizado no valor de: " + op.valor + "\nNovo saldo da conta: " + desejada.saldo);
                        }
                        pausar(teclado);
                        break;
                    }
                }while(opc != 0);
                break;
                default:
                limparTela();
                break;
            }
        }while(opcao != 0);

            /*switch(opcao){
                case 1:
                limparTela();

                num = Integer.parseInt(lerTeclado("número da conta: ", teclado));
                ContaBancaria contaDesejada = localizarConta(num);

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
                Cliente desejado = localizarCliente(cpf);

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
                    desejada = localizarConta(novoNumero);
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
                desejada = localizarConta(num);
                if(desejada != null){
                    limparTela();
                    System.out.println(desejada.imprimirExtratoDaConta());
                }
                else
                    System.out.println("Conta não encontrada");
                pausar(teclado);
                break;

                case 6:
                do{
                    limparTela();
                    num = Integer.parseInt(lerTeclado("numero da conta: ", teclado));
                    desejada = localizarConta(num);
                    if(desejada==null){
                        System.out.println("Conta não existe.");
                    }
                    pausar(teclado);
                }while(desejada==null);
                limparTela();
                int codigo = Integer.parseInt(lerTeclado("0 para deposito e 1 para saque: ", teclado));
                double valor = Double.parseDouble(lerTeclado("valor: ", teclado));
                Operacao op = desejada.novaOperacao(codigo, valor);
                System.out.println(op.tipoDeOperacao() + " realizado no valor de: " + valor + "\nNovo saldo da conta: " + desejada.saldo);
                Cliente donoDaConta = localizarCliente(desejada.cpf);
                donoDaConta.atualizarSaldo(codigo, valor);
                pausar(teclado);
                break;

                default:
                    limparTela();
                break;
            }
        }while(opcao!=0);*/

        teclado.close();
        salvarDadosOperacoes();
        salvarDadosClientes();
        salvarDadosContas();
    }
}