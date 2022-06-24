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

    /**
     * mapeia o objeto para os limites da tabela, baseado em seu codigo
     * @param qualquer objeto que sera mapeado
     * @return a posicao que o objeto caiu
     */
    public int mapear(Object qualquer){
        return qualquer.hashCode() % tam;
    }
    
    /**
     * localiza uma posicao para o objeto na tabela
     * @param desejado objeto que sera procurado
     * @return a posicao daquele objeto
     */
    public int localizar(Object desejado){
        int pos = mapear(desejado);
        int col = 1;
        while(dados[pos].estahValido() && !dados[pos].dado.equals(desejado)){
            pos = (pos + col * col) % tam;
            col++;
        }

        return pos;
    }

    /**
     * insere um novo objeto na tabela
     * @param chave a chave desse objeto
     * @param novo o objeto que sera inserido
     */
    public void inserir(String chave, Object novo){
        Entrada nova = new Entrada(chave, novo);
        int pos = localizar(novo);
        dados[pos] = nova;
    }

    /**
     * busca um objeto na tabela
     * @param desejado um objeto mock para busca do objeto requerido
     * @return o objeto encontrado
     */
    public Object buscar(Object desejado){
        int pos = localizar(desejado);
        return dados[pos].getValor();
    }
}
