public class Cliente{

    String cpf;
    ContaBancaria[] contas;
    Lista cntsCliente;

    public Cliente(String cdgPessoa, ContaBancaria[] contas){
        this.cpf = cdgPessoa;
        this.contas = contas;
        this.cntsCliente = new Lista();
    }
}