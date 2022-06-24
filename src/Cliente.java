import java.text.DecimalFormat;

public class Cliente implements IComparavel{

    public String cpf;
    public String nome;
    public Lista contasDoCliente;
    public double saldoTotal;

    public Cliente(String cpfNovo, String nomeNovo){
        this.cpf = cpfNovo;
        this.nome = nomeNovo;
        this.contasDoCliente = new Lista();
        this.saldoTotal = 0;
    }

    @Override
    public boolean equals(Object outro){
        Cliente outroCliente = (Cliente)outro;
        if(this.cpf.equals(outroCliente.cpf))
            return true;

        return false;
    }

    @Override
    public int hashCode(){
        int codigo = Integer.parseInt(this.cpf.substring(1, 7));
        return codigo;
    }
    
    @Override
    public String toString(){
        String dados = dadosCliente();
        return dados += contasDoCliente.toString();
    }

    @Override
    public boolean maiorQue(IComparavel outro) {
        Cliente outroCliente = (Cliente)outro;
        if(this.saldoTotal > outroCliente.saldoTotal)
            return true;

        return false;
    }

    @Override
    public boolean menorQue(IComparavel outro) {
        Cliente outroCliente = (Cliente)outro;
        if(this.saldoTotal < outroCliente.saldoTotal)
            return true;

        return false;
    }

    /**
     * monta uma String apenas com alguns dados do cliente
     * @return essa String
     */
    public String dadosCliente(){
        DecimalFormat formatador = new DecimalFormat("0.00");
        return "[Cliente: " + this.nome + " | CPF: " + this.cpf + " | Saldo total: " + formatador.format(saldoTotal) + "]\n";
    }

    /**
     * busca uma conta na lista de contas do cliente
     * @param numConta o numero da conta desejada
     * @return essa conta
     */
    public ContaBancaria buscarConta(int numConta){
        ContaBancaria buscada = (ContaBancaria) this.contasDoCliente.buscar(new ContaBancaria(numConta, "", 0));
        return buscada;
    }

    /**
     * insere uma nova conta na lista de contas do cliente
     * @param nova a conta que sera inserida
     */
    public void inserirNovaConta(ContaBancaria nova){
        this.contasDoCliente.inserir(nova);
        this.saldoTotal += nova.saldo;
    }

    /**
     * Calcula o saldo total do cliente, percorrendo sua lista de contas e somando o saldo de cada conta.
     */
    public double calcularSaldoTotal(){
        double saldoTotal = 0;
        Elemento aux = this.contasDoCliente.prim.prox;
        while(aux != null){
            ContaBancaria essaConta = (ContaBancaria)aux.dado;
            saldoTotal += essaConta.saldo;
            aux = aux.prox;
        }
        return saldoTotal;
    }

    /**
     * realiza a operacao em uma conta deste cliente e depois atualiza o saldo total do mesmo
     * @param op uma operacao que sera realizada
     */
    public void realizarOperacaoEmContaDoCliente(Operacao op){
        ContaBancaria contaOp = this.buscarConta(op.num);
        contaOp.realizarOperacao(op);
        this.saldoTotal = this.calcularSaldoTotal();
    }
}