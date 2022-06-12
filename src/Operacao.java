public class Operacao {
    public int num;
    public int codigo;
    public double valor;
    public String data;

    public Operacao(int numConta, int codOpera, double valor, String dia){
        this.num = numConta;
        this.codigo = codOpera;
        this.valor = valor;
        this.data = dia;
    }

    public String checarOperacao(){
        String tipo = "";
        if(this.codigo == 0)
            tipo = "Deposito";
        if(this.codigo == 1)
            tipo = "Saque";
        return tipo;
    }

    @Override
    public String toString(){
        String s = new String("Conta número: " + this.num + ", Operacao: " + checarOperacao() + ", Valor: " + this.valor + ", Data: " + this.data);
        return s;
    }
}