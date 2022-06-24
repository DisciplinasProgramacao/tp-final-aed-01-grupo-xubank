public class Lista {
    
    public Elemento prim, ult;

    public Lista(){
        this.prim = new Elemento(null);
        this.ult = prim;
    }

    /**
     * insere um objeto na lista
     * @param novo o objeto que sera inserido
     */
    public void inserir(Object novo){
        Elemento novoElemento = new Elemento(novo);
        ult.prox = novoElemento;
        ult = novoElemento;
    }

    /**
     * retira um objeto da lista
     * @return o objeto que sera retirado
     */
    public Object retirar(){
        Elemento aux = prim.prox;
        prim.prox = aux.prox;
        aux.prox = null;
        if(aux == ult)
            ult = prim;
        return aux.dado;
    }

    /**
     * busca um objeto na lista
     * @param outro o objeto mock que sera usado para buscar o objeto desejado
     * @return o objeto desejado
     */
    public Object buscar(Object outro){
        Elemento aux = prim.prox;
        while(aux != null){
            if(aux.dado.equals(outro))
                return aux.dado;
            aux = aux.prox;
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Elemento aux = prim.prox;
        while(aux!=null){
            sb.append(aux.dado.toString() + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }

    /**
     * verifica se a lista esta vazia
     * @return true se estiver vazia
     */
    public boolean vazia(){
        return prim == ult;
    }
}