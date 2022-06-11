public class Cliente{

    String cpf;
    String nome;
    Lista cntsCliente;

    public Cliente(String cpfNovo, String nomeNovo){
        this.cpf = cpfNovo;
        this.nome = nomeNovo;
        this.cntsCliente = new Lista();
    }

    public String contasCliente(){
        StringBuilder sb = new StringBuilder();
        Elemento aux = cntsCliente.prim.prox;
        sb.append("Cliente: " + this.nome + ", CPF: " + this.cpf + "\n" + "Contas: " + "\n");
        while(aux != null){
            sb.append("Conta número: " + aux.conta.num + ", Saldo: " + aux.conta.saldo + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }
}