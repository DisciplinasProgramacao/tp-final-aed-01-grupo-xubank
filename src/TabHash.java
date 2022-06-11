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

    public int calcularCodigo(String chave){
        String codigo = chave.substring(0, 6);
        return Integer.parseInt(codigo);
    }

    public int mapear(int codigo){
        return codigo % tam;
    }
    
    public int localizar(String key){
        int pos = mapear(calcularCodigo(key));
        int col = 1;
        while(dados[pos].valido && !key.equals(dados[pos].chave)){
            pos = (pos + col * col) % this.tam;
            col++;
        }
        return pos;
    }

    public void inserir(String chave, Cliente novo){
        Entrada nova = new Entrada(chave, novo);
        int pos = localizar(chave);
        dados[pos] = nova;
    }

    public Cliente buscar(String chave){
        int pos = localizar(chave);
        return dados[pos].getValor();
    }
}
