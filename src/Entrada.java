public class Entrada {
    
    public String chave;
    public Object dado;
    public boolean valido;

    public Entrada(){
        this.chave = "";
        this.dado = null;
        this.valido = false;
    }

    public Entrada(String novaChave, Object novo){
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
