public class ContaBancaria {

    int num;
    long cpf;
    double saldo;
    Lista operacoes;

    public ContaBancaria(int numero, long cpf, double sld){
        this.num = numero;
        this.cpf = cpf;
        this.saldo = sld;
        this.operacoes = new Lista();
    }

    @Override
    public boolean equals(Object outro){
        ContaBancaria outraConta = (ContaBancaria)outro;
        if(this.num == outraConta.num)
            return true;
        
        return false;
    }

    @Override
    public int hashCode(){
        return this.num;
    }

    @Override
    public String toString(){
        String dadosConta = "Conta Número: " + this.num + ", CPF: " + this.cpf + ", Saldo: " + this.saldo;
        return dadosConta;
    }

    public String imprimirExtratoDaConta(){
        String extrato = ("EXTRATO BANCARIO\n===================================================================\n");
        extrato += operacoes.toString();
        return extrato;
    }
}