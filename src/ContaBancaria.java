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
        return "Conta Número: " + this.num + " | CPF: " + this.cpf + " | Saldo: " + this.saldo;
    }

    public String imprimirExtratoDaConta(){
        String extrato = ("EXTRATO BANCARIO\n===================================================================\n");
        extrato += operacoes.toString();
        extrato += "Saldo total da conta: " + this.saldo + "\n";
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

    /**
     * realiza uma nova operacao e adiciona a mesma a fila de operacoes desssa conta
     * @param codigo codigo da operacao, sendo, 0 para deposito e 1 para saque
     * @param valor o valor a ser depositado ou retirado da conta
     * @return a operacao que foi adicionada a fila de operacoes desta conta
     */
    public Operacao novaOperacao(int codigo, double valor){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String hoje = dtf.format(LocalDateTime.now());
        Operacao nova = new Operacao(this.num, codigo, valor, hoje);
        realizarOperacao(nova);
        this.operacoes.inserir(nova);
        return nova;
    }

    /**
     * realiza uma operacao nessa conta
     * @param operacao uma operacao que sera realizada
     */
    public void realizarOperacao(Operacao operacao){
        double valorOperacao = operacao.valor;
        if(operacao.codigo == 1)
            valorOperacao = -operacao.valor;
        this.saldo += valorOperacao;
    }
}