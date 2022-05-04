public class TabHash {

    public final String nomeArquivo = "contas-bancarias.txt";
    public final int tam;   //tamanho da tabela
    public Entrada[] dados;

    public TabHash(int n){
        this.tam = n;
        this.dados = new Entrada[tam];

        for(int i = 0; i < this.tam; i++)
            dados[i] = new Entrada();   //preenche a tabela com entradas null
    }

    public int calcularCodigo(String chave){    //calcula o codigo da chave
        String codigo = chave.substring(0, 6);  //os 6 primeiros digitos do cpf
        return Integer.parseInt(codigo);    //retorna o codigo
    }

    public int mapear(int codigo){
        return codigo % tam;    //posicao no mapa e o resto da divisao do codigo pelo tamanho
    }
    
    public int localizar(String key){
        int pos = mapear(calcularCodigo(key));//descobre a posicao
        int indiceSondagem = 1; //indice para iniciar a sondagem quadratica
        while(dados[pos].valido && !key.equals(dados[pos].chave)){  //enquanto entrada for valida e a chave for diferente da chave desejada
            pos = mapear(pos + indiceSondagem * indiceSondagem);    //realiza sondagem quadratica e mapeia novamente
            indiceSondagem++;   //indice de sondagem soma + 1
        }
        return pos; //quando acha uma posicao vazia ou com a chave igual, retorna essa posicao
    }

    public void inserir(String chave, Cliente novo){
        Entrada nova = new Entrada(chave, novo);    //cria nova entrada
        int pos = localizar(chave); //localiza a posicao
        dados[pos] = nova;  //posiciona a entrada na respectiva posicao
    }

    public Cliente buscar(String chave){
        int pos = localizar(chave); //localiza a posicao da chave
        return dados[pos].getValor();   //retorna o dado dentro da entrada
    }
}
