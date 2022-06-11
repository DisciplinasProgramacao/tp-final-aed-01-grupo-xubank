public class ContaBancaria {

    int num;
    String cpf;
    double saldo;
    Lista operacoes;

    public ContaBancaria(int numero, String cpf, double sld){
        this.num = numero;
        this.cpf = cpf;
        this.saldo = sld;
        this.operacoes = new Lista();
    }

    public String dadosConta(){
        String linhaConta = "Conta Número: " + this.num + ", CPF: " + this.cpf + ", Saldo: " + this.saldo;
        return linhaConta;  //retorna uma string com os dados da conta
    }
}