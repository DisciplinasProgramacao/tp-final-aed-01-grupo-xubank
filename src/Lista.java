public class Lista {
    
    public Elemento prim, ult;

    public Lista(){
        this.prim = new Elemento(null);
        this.ult = prim;
    }

    public void inserir(Object novo){
        Elemento novoElemento = new Elemento(novo);
        ult.prox = novoElemento;
        ult = novoElemento;
    }

    public Object retirar(){
        Elemento aux = prim.prox;
        prim.prox = aux.prox;
        aux.prox = null;
        if(aux == ult)
            ult = prim;
        return aux.dado;
    }

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

    public boolean vazia(){
        return prim == ult;
    }
}