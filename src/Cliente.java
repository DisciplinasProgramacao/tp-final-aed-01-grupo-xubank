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
        Elemento aux = this.contasDoCliente.prim.prox;
        while(aux != null){
            ContaBancaria qualquer = (ContaBancaria)aux.dado;
            if(numConta == qualquer.num)
                return qualquer;
            aux = aux.prox;
        }
        return null;
    }

    public void inserirNovaConta(ContaBancaria nova){
        this.contasDoCliente.inserir(nova);
    }

    /**
     * Atualiza o saldo total do cliente, acrescentando ou diminuindo o valor da operacao
     */
    public void atualizarSaldo(int codigo, double valor){
        if(codigo == 1)
            valor = -valor;
        
        this.saldo += valor;
    }
}