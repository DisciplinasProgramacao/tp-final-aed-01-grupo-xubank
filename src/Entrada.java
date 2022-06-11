public class Entrada {
    
    public long chave;
    public Object dado;
    public boolean valido;

    public Entrada(){
        this.chave = -1;
        this.dado = null;
        this.valido = false;
    }

    public Entrada(long cpf, Object novo){
        this.chave = cpf;
        this.dado = novo;
        this.validar();
    }

    public void invalidar(){
        this.valido = false;
    }

    public void validar(){
        this.valido = true;
    }

    public boolean checarValidez(){
        return this.valido;
    }

    public Object getValor(){
        return this.dado;
    }
}
