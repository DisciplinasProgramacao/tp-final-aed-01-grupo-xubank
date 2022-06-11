public class Cliente{

    long cpf;
    String nome;
    Lista contasDoCliente;

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
        String dados = "Cliente: " + this.nome + "\nCPF: " + this.cpf + "\n";
        dados += contasDoCliente.toString();
        return dados;
    }
}