package br.com.iniflex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Funcionario extends Pessoa {
    private BigDecimal salario;
    private final String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public BigDecimal getSalario() { return salario; }

    public String getFuncao() { return funcao; }

    // A taxa 0.10 representa 10%. Arredondamento monetário para centavos.
    public void aumentarSalario(BigDecimal taxa) {
        salario = salario.multiply(BigDecimal.ONE.add(taxa)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularSalariosMinimos(BigDecimal salarioMinimo) {
        return salario.divide(salarioMinimo, 2, RoundingMode.HALF_UP);
    }
}
