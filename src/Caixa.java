public class Caixa {
    public Operacao operacao;
    public Caixa prox;

    public Caixa(Operacao nova){
        this.operacao = nova;
        this.prox = null;
    }
}
