import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class App {

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DecimalFormat formatador = new DecimalFormat("0.00");
    public static final String arquivoClientes = "Banco_Clientes2022.txt";
    public static final String arquivoContas = "Banco_Contas2022.txt";
    public static final String arquivoOperacoes = "Banco_Operacoes2022.txt";
    public static TabHash clientes;
    public static TabHash contas;
    public static Lista operacoes;
    public static int quantContas;
    public static int quantClientes;

    /**
     * carrega dados de um arquivo de clientes
     * @return uma tabela hash de clientes
     * @throws FileNotFoundException
     */
    public static TabHash carregarClientes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoClientes));
        int quant = Integer.parseInt(arquivo.nextLine());
        int tam = (int)(quant * 1.33);
        TabHash novosClientes = new TabHash(tam);

        for(int i = 0; i < quant; i++){

            String dados[] = arquivo.nextLine().split(";");
            String cpf = dados[0];
            String nome = dados[1];

            Cliente novo = new Cliente(cpf, nome);
            novosClientes.inserir(cpf, novo);
        }
        arquivo.close();
        quantClientes = quant;
        return novosClientes;
    }

    /**
     * carrega dados de um arquivo de contas
     * @return uma tabela hash de contas
     * @throws FileNotFoundException
     */
    public static TabHash carregarContas() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoContas));
        int quant = Integer.parseInt(arquivo.nextLine());
        int tam = (int)(quant * 1.33);
        TabHash novasContas = new TabHash(tam);

        for(int i = 0; i < quant; i++){

            String dados[] = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            String cpf = dados[1];
            double saldo = Double.parseDouble(dados[2]);

            Cliente donoDaConta = localizarCliente(cpf);
            ContaBancaria nova = new ContaBancaria(numero, cpf, saldo);
            donoDaConta.inserirNovaConta(nova);
            novasContas.inserir(Integer.toString(numero), nova);
        }
        arquivo.close();
        quantContas = quant;
        return novasContas;
    }

    /**
     * carrega dados de um arquivo de operacoes
     * @return uma lista de operacoes
     * @throws FileNotFoundException
     */
    public static void carregarOperacoes() throws FileNotFoundException{
        Scanner arquivo = new Scanner(new File(arquivoOperacoes));

        while(arquivo.hasNextLine()){

            String dados[] = arquivo.nextLine().split(";");

            int numero = Integer.parseInt(dados[0]);
            int codigo = Integer.parseInt(dados[1]);
            double valor = Double.parseDouble(dados[2]);
            String data = (dados[3]);

            Operacao nova = new Operacao(numero, codigo, valor, data);
            ContaBancaria desejada = localizarConta(numero);
            desejada.inserirOperacaoNaConta(nova);
        }
        arquivo.close();
    }

    /**
     * salva os dados da tabela no arquivo de clientes
     * @throws IOException
     */
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
    
    /**
     * salva os dados da tabela no arquivo de contas
     * @throws IOException
     */
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

    /**
     * salva as operacoes das contas no arquivo de operacoes
     * @throws IOException
     */
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

    /**
     * cria um objeto mock com o cpf do cliente desejado e busca o cliente na tabela hash
     * @param cpfDoCliente cpf do cliente desejado
     * @return o cliente desejado
     */
    public static Cliente localizarCliente(String cpfDoCliente){
        Cliente requerido = (Cliente)clientes.buscar(new Cliente(cpfDoCliente, ""));
        return requerido;
    }

    /**
     * cria um objeto mock com o numero da conta desejada e busca a conta na tabela hash
     * @param num o numero da conta desejada
     * @return a conta desejada
     */
    public static ContaBancaria localizarConta(int num){
        ContaBancaria requerida = (ContaBancaria)contas.buscar(new ContaBancaria(num, "", 0));
        return requerida;
    }

    /**
     * percorre a tabela de contas, procurando a que tem o maior saldo
     * @return a conta com maior saldo
     */
    public static ContaBancaria encontrarContaComMaiorSaldo(){
        ContaBancaria contaMaiorSaldo = new ContaBancaria(0, "", Double.MIN_VALUE);
        for(int i = 0; i < contas.dados.length; i++){
            if(contas.dados[i].estahValido()){
                ContaBancaria essaConta = (ContaBancaria) contas.dados[i].dado;
                if(essaConta.maiorSaldo(contaMaiorSaldo)){
                contaMaiorSaldo = essaConta;
                }
            }
        }
        return contaMaiorSaldo;
    }

    /**
     * percorre a tabela de clientes, somando o saldo total dos clientes
     * @return o saldo total dividido pela quantidade de clientes, ou seja, o saldo medio
     */
    public static double calcularSaldoMedio (){
        double saldoTotal = 0;
        for(int i = 0; i < clientes.dados.length; i++){
            if(clientes.dados[i].estahValido()){
                Cliente esseCliente = (Cliente)clientes.dados[i].dado;
                saldoTotal += esseCliente.saldoTotal;
            }
        }
        return saldoTotal / quantClientes;
    }

    /**
     * ordena o vetor utilizando shellsort
     * @param dados o vetor que sera ordenado
     */
    public static void ordenar(IComparavel[] dados){
        int n = dados.length;
        for (int salto = n/2; salto > 0; salto /= 2){
            for (int i = salto; i < n; i++){
                IComparavel aux = dados[i];
                int j;
                for (j = i; j >= salto && dados[j - salto].maiorQue(aux); j -= salto)
                    dados[j] = dados[j - salto];
  
                dados[j] = aux;
            }
        }
    }

    /**
     * primeiro menu do programa
     * @param teclado ler do teclado
     * @return a opcao que o usuario digitou
     */
    public static int mostrarMenu(Scanner teclado){
        System.out.println("CONTAS BANCARIAS XUBANK");
        System.out.println("==============================");
        System.out.println("1 - Opcoes de Admin");
        System.out.println("2 - Operar Conta de Um Cliente");
        System.out.println("0 - Sair");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    /**
     * menu para admins
     * @param teclado ler do teclado
     * @return a opcao que o admin digitou
     */
    public static int menuAdmin(Scanner teclado){
        System.out.println("OPCOES DE ADMIN");
        System.out.println("=========================================");
        System.out.println("1 - Exibir Contas Ordenadas");
        System.out.println("2 - Dados da Conta Com Maior Saldo");
        System.out.println("3 - Saldo Medio dos Clientes do Banco");
        System.out.println("4 - Checar Os 10 Clientes Com Maior Saldo");
        System.out.println("5 - Cadastrar Novo Cliente");
        System.out.println("0 - Voltar Ao Menu Anterior");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    /**
     * menu do cliente
     * @param teclado ler do teclado
     * @return a opcao que o cliente digitou
     */
    public static int menuCliente(Scanner teclado){
        System.out.println("OPERAR CONTA DE UM CLIENTE");
        System.out.println("==========================================");
        System.out.println("1 - Checar Extrato de uma Conta");
        System.out.println("2 - Extrato de Posicao Financeira");
        System.out.println("3 - Adicionar Nova Conta ao Cliente");
        System.out.println("4 - Realizar Operacao de Saque ou Deposito");
        System.out.println("0 - Voltar Ao Menu Anterior");

        int opcao = Integer.parseInt(teclado.nextLine());
        return opcao;
    }

    /**
     * limpa o console
     */
    public static void limparTela(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    /**
     * pausa ate o usuario pressionar enter
     * @param teclado ler do teclado
     */
    public static void pausar(Scanner teclado){
        System.out.print("Tecle ENTER para continuar. "); 
        teclado.nextLine();
    }

    /**
     * le do teclado
     * @param mensagem o que vc deseja que o usuario te informe
     * @param teclado ler do teclado
     * @return a resposta do usuario
     */
    public static String lerTeclado(String mensagem, Scanner teclado){
        System.out.print(mensagem + " ");
        return teclado.nextLine();
    }

    public static void main(String[] args) throws Exception{
        Scanner teclado = new Scanner(System.in);
        clientes = carregarClientes();
        contas = carregarContas();
        carregarOperacoes();
        int opcao, num, opc, j;
        String cpf;

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
                        System.out.println("...");
                        File ordenado = new File("contas-ordenadas.txt");
                        FileWriter gravador = new FileWriter(ordenado, false);
                        ContaBancaria[] contasOrdenadas = new ContaBancaria[quantContas];
                        j = 0;
                        for(int i = 0; i < contas.tam; i++){
                            if(contas.dados[i].estahValido()){
                                contasOrdenadas[j] = (ContaBancaria)contas.dados[i].dado;
                                j++;
                            }
                        }
                        ordenar(contasOrdenadas);

                        for(ContaBancaria conta : contasOrdenadas)
                            gravador.append(conta.toString() + "\n");

                        gravador.close();
                        java.awt.Desktop.getDesktop().open((ordenado));
                        System.out.println("Arquivo aberto");
                        pausar(teclado);
                        ordenado.delete();
                        break;

                        case 2:
                        limparTela();
                        ContaBancaria maiorSaldo = encontrarContaComMaiorSaldo();
                        System.out.println("CONTA COM MAIOR SALDO");
                        System.out.println("========================================");
                        System.out.println(maiorSaldo.toString());
                        pausar(teclado);
                        break;

                        case 3:
                        limparTela();
                        System.out.println("SALDO MÉDIO");
                        System.out.println("========================================");
                        double saldoMedio = calcularSaldoMedio();
                        System.out.println("O Saldo Medio dos Clientes e: " + formatador.format(saldoMedio));
                        pausar(teclado);
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
                        ordenar(maisRicos);
                        System.out.println("OS DEZ CLIENTES COM MAIOR SALDO");
                        System.out.println("===============================");
                        j = 1;
                        for(int i = maisRicos.length - 1; i >= maisRicos.length - 10; i--){
                            System.out.print(j + " - " + maisRicos[i].dadosCliente());
                            j++;
                        }
                        pausar(teclado);
                        break;

                        case 5:
                        Cliente desejado;
                        do{
                            limparTela();
                            cpf = lerTeclado("CPF: ", teclado);
                            desejado = localizarCliente(cpf);
                            if(desejado != null){
                                System.out.println("Cliente já existe");
                                pausar(teclado);
                            }
                        }while(desejado != null);

                        String nome = lerTeclado("Nome do cliente: ", teclado);
                        Cliente novo = new Cliente(cpf, nome);
                        clientes.inserir(cpf, novo);
                        System.out.println("Cliente cadastrado");
                        quantClientes++;
                        pausar(teclado);
                        break;

                        default:
                        limparTela();
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
                    cpf = lerTeclado("CPF do Cliente: ", teclado);
                    requerido = localizarCliente(cpf);
                    if(requerido == null)
                        System.out.println("Cliente não encontrado");
                    pausar(teclado);
                }while(requerido == null);

                do{
                limparTela();
                opc = menuCliente(teclado);
                    switch(opc){

                        case 1:
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

                        case 2:
                        limparTela();
                        System.out.println("EXTRATO DE POSICAO FINANCEIRA");
                        System.out.println("=============================");
                        System.out.println(requerido.toString());
                        pausar(teclado);
                        break;

                        case 3:
                        limparTela();
                        System.out.println("ADICIONAR NOVA CONTA");
                        System.out.println("====================");
                        ContaBancaria nova = new ContaBancaria((quantContas + 1000), requerido.cpf, 0);
                        contas.inserir(Integer.toString(nova.num), nova);
                        requerido.inserirNovaConta(nova);
                        System.out.println("Nova conta criada:\n" + nova.toString());
                        quantContas++;
                        pausar(teclado);
                        break;

                        case 4:
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

                        default:
                        limparTela();
                        break;
                    }
                }while(opc != 0);
                break;
                default:
                limparTela();
                break;
            }
        }while(opcao != 0);

        teclado.close();
        salvarDadosOperacoes();
        salvarDadosClientes();
        salvarDadosContas();
    }
}