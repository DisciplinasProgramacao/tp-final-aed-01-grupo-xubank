public class Entrada {
    
    public String chave;
    public Cliente cliente;
    public boolean valido;

    public Entrada(){
        this.chave = "";
        this.cliente = null;
        this.valido = false;
    }

    public Entrada(String cpf, Cliente novo){
        this.chave = cpf;
        this.cliente = novo;
        this.validar();
    }

    public void invalidar(){
        this.valido = false;
    }

    public void validar(){
        this.valido = true;
    }

    public boolean checar(){
        return this.valido;
    }

    public Cliente getValor(){
            return this.cliente;
    }
}
