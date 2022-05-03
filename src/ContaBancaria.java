public class ContaBancaria {

    int num;
    String cpf;
    double saldo;

    public ContaBancaria(int numero, String cpf, double sld){
        this.num = numero;
        this.cpf = cpf;
        this.saldo = sld;
    }

    public String dadosConta(){
        String linhaConta = "Conta Número: " + this.num + ", Saldo: " + this.saldo;
        return linhaConta;  //retorna uma string com os dados da conta
    }
}