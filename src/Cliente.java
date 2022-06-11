public class Cliente{

    long cpf;
    String nome;
    Lista cntsCliente;

    public Cliente(long cpfNovo, String nomeNovo){
        this.cpf = cpfNovo;
        this.nome = nomeNovo;
        this.cntsCliente = new Lista();
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

    /*public String contasCliente(){
        StringBuilder sb = new StringBuilder();
        Elemento aux = cntsCliente.prim.prox;
        sb.append("Cliente: " + this.nome + ", CPF: " + this.cpf + "\n" + "Contas: " + "\n");
        while(aux != null){
            sb.append("Conta número: " + aux.conta.num + ", Saldo: " + aux.conta.saldo + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }*/
}