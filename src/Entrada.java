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

    /**
     * invalida o dado naquela posicao da tabela
     */
    public void invalidar(){
        this.valido = false;
    }

    /**
     * valida o dado naquela posicao da tabela
     */
    public void validar(){
        this.valido = true;
    }

    /**
     * checa se o dado daquela posicao da tabela esta valido
     * @return true se estiver
     */
    public boolean estahValido(){
        return this.valido;
    }

    /**
     * pega o dado daquela posicao da tabela
     * @return o dado na posicao da tabela
     */
    public Object getValor(){
        return this.dado;
    }
}
