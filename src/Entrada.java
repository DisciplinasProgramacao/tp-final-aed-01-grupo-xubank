public class Entrada {
    
    public String chave;    //chave do dado
    public Cliente cliente; //dado que vai ser armazenado
    public boolean valido;  //boolean que diz se o dado ainda e valido

    public Entrada(){   //entrada vazia
        this.chave = "";
        this.cliente = null;
        this.valido = false;
    }

    public Entrada(String cpf, Cliente novo){
        this.chave = cpf;
        this.cliente = novo;
        this.validar();
    }

    public void invalidar(){    //deixa o dado invalido
        this.valido = false;
    }

    public void validar(){  //valida o dado
        this.valido = true;
    }

    public boolean checar(){   //checa se o dado e valido
        return this.valido;
    }

    public Cliente getValor(){  //retorna o dado naquela entrada
            return this.cliente;
    }
}
