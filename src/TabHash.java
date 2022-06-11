public class TabHash {

    public final String nomeArquivo = "contas-bancarias.txt";
    public final int tam;
    public Entrada[] dados;

    public TabHash(int n){
        this.tam = n;
        this.dados = new Entrada[tam];

        for(int i = 0; i < this.tam; i++)
            dados[i] = new Entrada();
    }

    public int mapear(Object qualquer){
        return qualquer.hashCode() % tam;
    }
    
    public int localizar(Object desejado){
        int pos = mapear(desejado);
        while(dados[pos].checarValidez() && !dados[pos].dado.equals(desejado))
            pos = (pos + 1) % tam;

        return pos;
    }

    public void inserir(long chave, Object novo){
        Entrada nova = new Entrada(chave, novo);
        int pos = localizar(novo);
        dados[pos] = nova;
    }

    public Object buscar(Object desejado){
        int pos = localizar(desejado);
        return dados[pos].getValor();
    }
}
