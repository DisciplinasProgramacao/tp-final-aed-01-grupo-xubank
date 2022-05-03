public class Cliente{

    String cpf;
    Lista cntsCliente;

    public Cliente(String cpfNovo){
        this.cpf = cpfNovo;
        this.cntsCliente = new Lista();
    }

    public String imprimir(){
        StringBuilder sb = new StringBuilder();
        Elemento aux = cntsCliente.prim.prox;   //aux recebe o primeiro elemento com dados na lista
        sb.append("Cliente: " + this.cpf + "\n");
        while(aux != null){ //enquanto aux diferente de null
            sb.append(aux.conta.dadosConta() + "\n");   //concatena os dados em uma string
            aux = aux.prox; //caminha com o aux
        }
        return sb.toString();   //retorna essa string
    }
}