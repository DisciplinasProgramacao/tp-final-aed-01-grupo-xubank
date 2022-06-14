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

    /**
     * transforma o codigo de operacao, 0 ou 1, em uma String
     * @return uma String, que, baseada no codigo da operacao, diz se foi um saque ou um deposito
     */
    public String tipoDeOperacao(){
        if(this.codigo == 0)
            return "Deposito";
        else
            return "Saque";
    }

    @Override
    public String toString(){
        return "Conta número: " + this.num + " | Operacao: " + tipoDeOperacao() + " | Valor: " + this.valor + " | Realizado: " + this.data;
    }
}