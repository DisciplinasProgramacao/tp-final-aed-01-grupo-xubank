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

    public String tipoDeOperacao(int cdg){
        String tipo = "";
        if(cdg == 0)
            tipo = "Deposito";
        if(cdg == 1)
            tipo = "Saque";
        return tipo;
    }

    @Override
    public String toString(){
        String s = new String("Conta número: " + this.num + " | Operacao: " + tipoDeOperacao(this.codigo) + " | Valor: " + this.valor + " | Realizado: " + this.data);
        return s;
    }
}