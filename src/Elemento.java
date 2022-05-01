public class Elemento {

    public ContaBancaria conta;
    public Elemento prox;

    public Elemento(ContaBancaria nova){
        this.conta = nova;
        this.prox = null;
    }   
}