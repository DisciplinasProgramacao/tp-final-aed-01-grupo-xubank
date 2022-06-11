public class Lista {
    
    public Elemento prim, ult;

    public Lista(){
        this.prim = new Elemento(null);
        this.ult = prim;
    }

    public void inserir(ContaBancaria nova){
        Elemento novo = new Elemento(nova);
        ult.prox = novo;
        ult = novo;
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

    /*public Operacao desenfileirar(){
        Elemento aux = prim.prox;
        prim.prox = aux.prox;
        aux.prox = null;
        if(aux == ult)
            ult = prim;
        return aux.operacao;
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

    public String extrato(){
        StringBuilder sb = new StringBuilder();
        Caixa aux = prim.prox;
        sb.append("EXTRATO BANCARIO\n===================================================================\n");
        while(aux != null){
            sb.append(aux.operacao.dadosOperacao() + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }*/

    public boolean vazia(){
        return prim == ult;
    }
}