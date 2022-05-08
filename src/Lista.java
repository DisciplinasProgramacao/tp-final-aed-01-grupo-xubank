public class Lista {
    
    public Elemento prim, ult;

    public Lista(){
        this.prim = new Elemento(null);
        this.ult = prim;
    }

    public void inserir(ContaBancaria nova){
        Elemento novo = new Elemento(nova); //cria um novo elemento
        ult.prox = novo;    //ult.prox recebe o novo
        ult = novo; //ultimo pula pro novo
    }

    public ContaBancaria buscar(int numConta){
        Elemento aux = prim.prox;   //auxiliar recebe o primeiro elemento que nao esta vazio da fila
        while(aux != null){ //enquanto auxiliar for diferente de null
            if(aux.conta.num == numConta) //se o aux for igual a conta buscada
                return aux.conta;   //retorna a conta
            aux = aux.prox; //se nao, caminha com o auxiliar
        }
        return null;    //se nao encontrar, retorna null
    }

    public String imprimir(){
        StringBuilder sb = new StringBuilder(); //inicia o string builder
        Elemento aux = prim.prox;   //aux comeca no primeiro elemento nao vazio
        while(aux != null){ //enquanto aux for diferente de null
            sb.append(aux.conta.dadosConta() + "\n");   //grava os dados em uma string
            aux = aux.prox; //caminha com o auxiliar
        }
        return sb.toString();   //retorna uma string
    }

    public boolean vazia(){
        return prim == ult; //se a fila estiver vazia retorna true, se nao retorna false
    }
}