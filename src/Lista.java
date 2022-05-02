public class Lista {
    
    public Elemento prim, ult;

    public Lista(){
        this.prim = new Elemento(null);
        this.ult = prim;
    }

    public void InserirNaOrdem(ContaBancaria nova){
        if(vazia())
            enfileirar(nova);
        else{
            Elemento aux = prim;
            while(aux.prox != null && nova.num > aux.prox.conta.num)
                aux = aux.prox;
            Elemento novo = new Elemento(nova);
            novo.prox = aux.prox;
            aux.prox = novo;
            if(aux == ult)
                ult = novo;
        }
    }

    public void enfileirar(ContaBancaria nova){
        Elemento novo = new Elemento(nova);
        ult.prox = novo;
        ult = novo;
    }

    public ContaBancaria desenfileirar(){
        Elemento aux = prim.prox;
        prim.prox = aux.prox;
        aux.prox = null;
        if(aux == ult)
            ult = prim;
        return aux.conta;
    }

    public ContaBancaria buscar(int numConta){
        Elemento aux = prim.prox;
        while(aux != null){
            if(aux.conta.num == numConta)
                return aux.conta;
            aux = aux.prox;
        }
        return null;
    }

    public String imprimir(){
        StringBuilder sb = new StringBuilder();
        Elemento aux = prim.prox;
        while(aux != null){
            sb.append(aux.conta.dadosConta() + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }

    public boolean vazia(){
        return prim == ult;
    }
}