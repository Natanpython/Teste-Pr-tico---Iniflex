package br.com.iniflex;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PrincipalTest {
    private List<Funcionario> funcionariosAtualizados() {
        var lista = Principal.criarFuncionarios();
        lista.removeIf(f -> f.getNome().equals("João"));
        lista.forEach(f -> f.aumentarSalario(new BigDecimal("0.10")));
        return lista;
    }

    @Test
    void cadastroPreservaOrdemEDadosDaTabela() {
        var lista = Principal.criarFuncionarios();
        assertEquals(List.of("Maria", "João", "Caio", "Miguel", "Alice", "Heitor", "Arthur", "Laura", "Heloísa", "Helena"),
                lista.stream().map(Funcionario::getNome).toList());
        assertEquals(new BigDecimal("48563.31"), Principal.totalSalarios(lista));
        assertEquals(LocalDate.of(2000, 10, 18), lista.getFirst().getDataNascimento());
    }

    @Test
    void aumentoArredondaCentavosETotalExcluiJoao() {
        var lista = funcionariosAtualizados();
        assertEquals(9, lista.size());
        assertEquals(List.of("2210.38", "10819.75", "21031.87", "2458.15", "1740.99", "4479.02", "3319.20", "1767.54", "3079.92"),
                lista.stream().map(f -> f.getSalario().toPlainString()).toList());
        assertEquals(new BigDecimal("50906.82"), Principal.totalSalarios(lista));
        assertEquals(new BigDecimal("1.82"), lista.getFirst().calcularSalariosMinimos(new BigDecimal("1212.00")));
    }

    @Test
    void agrupaTodosOsFuncionariosPorFuncao() {
        var grupos = Principal.agruparPorFuncao(funcionariosAtualizados());
        assertEquals(7, grupos.size());
        assertEquals(List.of("Maria", "Heitor"), grupos.get("Operador").stream().map(Funcionario::getNome).toList());
        assertEquals(List.of("Laura", "Helena"), grupos.get("Gerente").stream().map(Funcionario::getNome).toList());
        assertEquals(9, grupos.values().stream().mapToInt(List::size).sum());
    }

    @Test
    void filtraOutubroEDezembro() {
        var lista = funcionariosAtualizados();
        assertEquals(List.of("Maria", "Miguel"), Principal.aniversariantes(lista).stream().map(Funcionario::getNome).toList());
        lista.add(new Funcionario("Dezembro", LocalDate.of(2000, 12, 1), BigDecimal.ONE, "Teste"));
        assertEquals(3, Principal.aniversariantes(lista).size());
    }

    @Test
    void identificaMaisVelhoEConsideraDiaDoAniversario() {
        var caio = Principal.encontrarMaisVelho(funcionariosAtualizados());
        assertEquals("Caio", caio.getNome());
        assertEquals(64, caio.calcularIdade(LocalDate.of(2026, 5, 1)));
        assertEquals(65, caio.calcularIdade(LocalDate.of(2026, 5, 2)));
    }

    @Test
    void ordenaSemAlterarListaOriginal() {
        var lista = funcionariosAtualizados();
        assertEquals(List.of("Alice", "Arthur", "Caio", "Heitor", "Helena", "Heloísa", "Laura", "Maria", "Miguel"),
                Principal.ordenarPorNome(lista).stream().map(Funcionario::getNome).toList());
        assertEquals("Maria", lista.getFirst().getNome());
    }

    @Test
    void formataValoresNoPadraoBrasileiro() {
        assertEquals("21.031,87", Principal.formatarNumero(new BigDecimal("21031.87")));
        assertEquals("1.741,00", Principal.formatarNumero(new BigDecimal("1741")));
    }

    @Test
    void executaTodosOsRelatoriosComDatasEValoresFormatados() {
        var buffer = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try (var saida = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(saida);
            Principal.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String texto = buffer.toString(StandardCharsets.UTF_8);
        assertFalse(texto.contains("João"));
        assertTrue(texto.contains("18/10/2000"));
        assertTrue(texto.contains("2.009,44"));
        assertTrue(texto.contains("2.210,38"));
        assertTrue(texto.contains("R$ 50.906,82"));
        assertTrue(texto.contains("Maria: 1,82 salários mínimos"));
        for (String item : List.of("3.3", "3.6", "3.8", "3.9", "3.10", "3.11", "3.12")) {
            assertTrue(texto.contains("=== " + item + " -"));
        }
    }
}
