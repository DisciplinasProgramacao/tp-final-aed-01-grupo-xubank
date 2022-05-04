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
        Elemento aux = cntsCliente.prim.prox;   //aux recebe o primeiro elemento com dados na lista
        sb.append("Cliente: " + this.nome + ", CPF: " + this.cpf + "\n" + "Contas: " + "\n");
        while(aux != null){ //enquanto aux diferente de null
            sb.append("Conta número: " + aux.conta.num + ", Saldo: " + aux.conta.saldo + "\n");   //concatena os dados em uma string
            aux = aux.prox; //caminha com o aux
        }
        return sb.toString();   //retorna essa string
    }
}