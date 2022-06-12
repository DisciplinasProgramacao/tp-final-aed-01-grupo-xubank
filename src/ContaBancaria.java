import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContaBancaria implements IComparavel{

    public int num;
    public long cpf;
    public double saldo;
    public Lista operacoes;

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
        String dadosConta = "Conta Número: " + this.num + " | CPF: " + this.cpf + " | Saldo: " + this.saldo;
        return dadosConta;
    }

    public String imprimirExtratoDaConta(){
        String extrato = ("EXTRATO BANCARIO\n===================================================================\n");
        extrato += operacoes.toString();
        return extrato;
    }

    @Override
    public boolean maiorQue(IComparavel outro) {
        ContaBancaria outraConta = (ContaBancaria)outro;
        if(this.num > outraConta.num)
            return true;
            
        return false;
    }

    @Override
    public boolean menorQue(IComparavel outro) {
        ContaBancaria outraConta = (ContaBancaria)outro;
        if(this.num < outraConta.num)
            return true;

        return false;
    }

    public Operacao realizarOperacao(int codigo, double valor){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Operacao nova = new Operacao(this.num, codigo, valor, dtf.format(LocalDateTime.now()));
        this.operacoes.inserir(nova);
        switch(codigo){
        case 0:
        this.saldo = this.saldo + valor;
        break;

        case 1:
        this.saldo = this.saldo - valor;
        break;
        }
        return nova;
    }
}