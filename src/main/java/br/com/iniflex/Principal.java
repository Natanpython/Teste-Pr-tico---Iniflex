package br.com.iniflex;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final BigDecimal AUMENTO = new BigDecimal("0.10");
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {
        // 3.1 e 3.2: cadastro na ordem da tabela e remoção de João.
        List<Funcionario> funcionarios = criarFuncionarios();
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        titulo("3.3 - Funcionários (antes do aumento)");
        imprimirFuncionarios(funcionarios);

        // 3.4: os próximos relatórios usam os salários atualizados.
        funcionarios.forEach(funcionario -> funcionario.aumentarSalario(AUMENTO));

        // 3.5: LinkedHashMap preserva a ordem de aparição das funções.
        Map<String, List<Funcionario>> porFuncao = agruparPorFuncao(funcionarios);
        titulo("3.6 - Funcionários por função (com aumento de 10%)");
        porFuncao.forEach((funcao, grupo) -> {
            System.out.println("\nFunção: " + funcao);
            imprimirFuncionarios(grupo);
        });

        // O enunciado não contém o item 3.7.
        titulo("3.8 - Aniversariantes dos meses 10 e 12");
        imprimirFuncionarios(aniversariantes(funcionarios));

        titulo("3.9 - Funcionário com maior idade");
        Funcionario maisVelho = encontrarMaisVelho(funcionarios);
        System.out.printf("%s - %d anos%n", maisVelho.getNome(), maisVelho.calcularIdade(LocalDate.now()));

        titulo("3.10 - Funcionários em ordem alfabética");
        imprimirFuncionarios(ordenarPorNome(funcionarios));

        titulo("3.11 - Total dos salários");
        System.out.println("R$ " + formatarNumero(totalSalarios(funcionarios)));

        titulo("3.12 - Quantidade de salários mínimos (base: R$ 1.212,00)");
        funcionarios.forEach(funcionario -> System.out.printf("%s: %s salários mínimos%n",
                funcionario.getNome(), formatarNumero(funcionario.calcularSalariosMinimos(SALARIO_MINIMO))));
    }

    public static List<Funcionario> criarFuncionarios() {
        return new ArrayList<>(List.of(
                funcionario("Maria", "18/10/2000", "2009.44", "Operador"),
                funcionario("João", "12/05/1990", "2284.38", "Operador"),
                funcionario("Caio", "02/05/1961", "9836.14", "Coordenador"),
                funcionario("Miguel", "14/10/1988", "19119.88", "Diretor"),
                funcionario("Alice", "05/01/1995", "2234.68", "Recepcionista"),
                funcionario("Heitor", "19/11/1999", "1582.72", "Operador"),
                funcionario("Arthur", "31/03/1993", "4071.84", "Contador"),
                funcionario("Laura", "08/07/1994", "3017.45", "Gerente"),
                funcionario("Heloísa", "24/05/2003", "1606.85", "Eletricista"),
                funcionario("Helena", "02/09/1996", "2799.93", "Gerente")
        ));
    }

    private static Funcionario funcionario(String nome, String nascimento, String salario, String funcao) {
        return new Funcionario(nome, LocalDate.parse(nascimento, DATA), new BigDecimal(salario), funcao);
    }

    public static Map<String, List<Funcionario>> agruparPorFuncao(List<Funcionario> funcionarios) {
        return funcionarios.stream().collect(Collectors.groupingBy(
                Funcionario::getFuncao, LinkedHashMap::new, Collectors.toList()));
    }

    public static List<Funcionario> aniversariantes(List<Funcionario> funcionarios) {
        return funcionarios.stream().filter(funcionario -> {
            int mes = funcionario.getDataNascimento().getMonthValue();
            return mes == 10 || mes == 12;
        }).toList();
    }

    public static Funcionario encontrarMaisVelho(List<Funcionario> funcionarios) {
        return funcionarios.stream().min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElseThrow(() -> new IllegalArgumentException("A lista de funcionários está vazia."));
    }

    public static List<Funcionario> ordenarPorNome(List<Funcionario> funcionarios) {
        Collator collator = Collator.getInstance(PT_BR);
        return funcionarios.stream().sorted(Comparator.comparing(Funcionario::getNome, collator)).toList();
    }

    public static BigDecimal totalSalarios(List<Funcionario> funcionarios) {
        return funcionarios.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static String formatarNumero(BigDecimal valor) {
        NumberFormat formato = NumberFormat.getNumberInstance(PT_BR);
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return formato.format(valor);
    }

    private static void imprimirFuncionarios(List<Funcionario> funcionarios) {
        System.out.printf("%-12s | %-15s | %13s | %s%n", "Nome", "Data nascimento", "Salário (R$)", "Função");
        funcionarios.forEach(funcionario -> System.out.printf("%-12s | %-15s | %13s | %s%n",
                funcionario.getNome(), funcionario.getDataNascimento().format(DATA),
                formatarNumero(funcionario.getSalario()), funcionario.getFuncao()));
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }
}
