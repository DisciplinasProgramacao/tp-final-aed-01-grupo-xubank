import java.text.DecimalFormat;

public class Cliente implements IComparavel{

    public long cpf;
    public String nome;
    public Lista contasDoCliente;
    public double saldoTotal = 0;

    public Cliente(long cpfNovo, String nomeNovo){
        this.cpf = cpfNovo;
        this.nome = nomeNovo;
        this.contasDoCliente = new Lista();
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
        String dados = "Cliente: " + this.nome + " | CPF: " + this.cpf + " | Saldo total: " + formatador.format(saldoTotal) + "\n";
        dados += contasDoCliente.toString();
        return dados;
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
}