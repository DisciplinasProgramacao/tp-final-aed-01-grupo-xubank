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

    public void quickSort(int inicio, int fim){
        if(inicio < fim){   //se inicio < fim
            int part = particao(inicio, fim);   //part = particao do inicio e o fim
            quickSort(inicio, part - 1);    //executa do inicio a particao -1
            quickSort(part + 1, fim);   //executa da particao + 1 ate o fim
        }
    }

    public int particao(int inicio, int fim){
        while(!dados[fim].valido)   //se os dados na posicao fim nao forem validos
            fim--;  //fim anda pra tras
        long pivot = Long.parseLong(dados[fim].chave);  //pivot recebe os dados na posicao fim
        int part = inicio - 1;

        for (int i = inicio; i < fim; i++){ //para cada i, do inicio ao fim
            if(dados[i].valido){    //se os dados forem validos
                if(Long.parseLong(dados[i].chave) < pivot){ //e se os dados na posicao i forem menores que o pivot
                    part++; //avanca a particao
                    trocar(part, i);    //troca os dados na posicao da particao com os dados na posicao i
                }
                dados[i].chave = String.valueOf(dados[i].chave);    //transforma o cpf novamente em string
            }
        }
        part++; //caminha com a particao
        trocar(part, fim);  //troca com o dado na posicao do fim
        dados[part].chave = String.valueOf(dados[part].chave);  //transforma o dado da posicao particao em string denovo
        return part;    //retorna a particao
    }

    public void trocar(int pos1, int pos2){
        Entrada aux = dados[pos1];
        dados[pos1] = dados[pos2];
        dados[pos2] = aux;
    }

    public String imprimir(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < dados.length; i++)   //enquanto i menor que o tamanho da tabela
            if(dados[i].valido) //se o dado for valido
                sb.append(dados[i].cliente.imprimir() + "\n");  //concatena os dados em uma string
        
        return sb.toString();   //retorna essa string
    }
}
