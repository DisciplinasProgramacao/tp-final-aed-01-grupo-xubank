public class Fila {
    public Caixa prim, ult;

    public Fila(){
        prim = new Caixa(null);
        ult = prim;
    }

    public void enfileirar(Operacao nova){
        Caixa aux = new Caixa(nova); //cria um novo elemento
        this.ult.prox = aux;    //ult.prox recebe o novo
        ult = aux; //ultimo pula pro novo
    }

    public Operacao desenfileirar(){
        Caixa aux = prim.prox;
        prim.prox = aux.prox;   //pula o primeiro elemento
        aux.prox = null;    //aux.prox recebe null
        if(aux == ult)  //se o aux era o ult
            ult = prim; //ult = prim
        return aux.operacao;   //retorna o objeto dentro do elemento
    }

    public String extrato(){    //gera o extrato de operacoes de uma conta
        StringBuilder sb = new StringBuilder();
        Caixa aux = prim.prox;
        sb.append("EXTRATO BANCARIO\n===================================================================\n");
        while(aux != null){
            sb.append(aux.operacao.dadosOperacao() + "\n");
            aux = aux.prox;
        }
        return sb.toString();
    }

    public boolean vazia(){
        return ult == prim;
    }
}
