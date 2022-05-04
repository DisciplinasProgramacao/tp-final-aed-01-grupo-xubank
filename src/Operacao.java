public class Operacao {
    int num;
    int codigo;
    double valor;
    String data;

    public Operacao(int numConta, int codOpera, double valor, String dia){
        this.num = numConta;
        this.codigo = codOpera;
        this.valor = valor;
        this.data = dia;
    }

    public String checarOperacao(){
        String tipo = "";
        if(this.codigo == 0)    //se o codigo da operacao for 0 a operacao e um deposito
            tipo = "Deposito";
        if(this.codigo == 1)    //se for 1 e um saque
            tipo = "Saque";
        return tipo;
    }

    public String dadosOperacao(){  //grava os dados da operacao em uma string
        String s = new String("Conta número: " + this.num + ", Operacao: " + checarOperacao() + ", Valor: " + this.valor + ", Data: " + this.data);
        return s;
    }
}