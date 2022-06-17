import java.text.DecimalFormat;

public class Cliente implements IComparavel{

    public long cpf;
    public String nome;
    public Lista contasDoCliente;
    public double saldo;

    public Cliente(long cpfNovo, String nomeNovo){
        this.cpf = cpfNovo;
        this.nome = nomeNovo;
        this.contasDoCliente = new Lista();
        this.saldo = 0;
    }

    @Override
    public boolean equals(Object outro){
        Cliente outroCliente = (Cliente)outro;
        if(this.cpf == outroCliente.cpf)
            return true;

        return false;
    }

    @Override
    public int hashCode(){
        return (int)(this.cpf / 100000);
    }
    
    @Override
    public String toString(){
        DecimalFormat formatador = new DecimalFormat("0.00");
        String dados = "Cliente: " + this.nome + " | CPF: " + this.cpf + " | Saldo total: " + formatador.format(saldo) + "\n";
        return dados += contasDoCliente.toString();
    }

    @Override
    public boolean maiorQue(IComparavel outro) {
        Cliente outroCliente = (Cliente)outro;
        if(this.saldo > outroCliente.saldo)
            return true;

        return false;
    }

    @Override
    public boolean menorQue(IComparavel outro) {
        Cliente outroCliente = (Cliente)outro;
        if(this.saldo < outroCliente.saldo)
            return true;

        return false;
    }

    public ContaBancaria buscarConta(int numConta){
        ContaBancaria mock = new ContaBancaria(numConta, 00000000000, 0);
        ContaBancaria buscada = (ContaBancaria) this.contasDoCliente.buscar(mock);
        return buscada;
    }

    public void inserirNovaConta(ContaBancaria nova){
        this.contasDoCliente.inserir(nova);
        this.saldo += nova.saldo;
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
        this.saldo = this.calcularSaldoTotal();
    }
}