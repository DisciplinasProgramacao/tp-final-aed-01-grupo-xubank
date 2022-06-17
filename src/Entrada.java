public class Entrada {
    
    public long chave;
    public Object dado;
    public boolean valido;

    public Entrada(){
        this.chave = -1;
        this.dado = null;
        this.valido = false;
    }

    public Entrada(long novaChave, Object novo){
        this.chave = novaChave;
        this.dado = novo;
        this.validar();
    }

    public void invalidar(){
        this.valido = false;
    }

    public void validar(){
        this.valido = true;
    }

    public boolean estahValido(){
        return this.valido;
    }

    public Object getValor(){
        return this.dado;
    }
}
