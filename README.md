# Teste Prático — Iniflex

Aplicação de console desenvolvida em Java para o teste prático da Iniflex. Realiza o cadastro em memória de funcionários, a atualização de salários e a geração de relatórios por função, aniversário e ordem alfabética.

## Tecnologias

- **Java 21** — orientação a objetos, Collections, Stream API e API de datas.
- **Maven** — compilação, execução dos testes e empacotamento.
- **JUnit 5** — testes automatizados.

## Funcionalidades

| Requisito | Implementação |
| --- | --- |
| 1 e 2 | Modelagem de `Pessoa` e `Funcionario` com herança, `LocalDate` e `BigDecimal` |
| 3.1 | Cadastro dos dez funcionários na ordem e com os dados especificados |
| 3.2 | Remoção de João da lista |
| 3.3 | Exibição dos dados com datas no formato `dd/MM/yyyy` e números no padrão brasileiro |
| 3.4 | Aplicação de aumento salarial de 10% |
| 3.5 e 3.6 | Agrupamento e exibição por função com `Map<String, List<Funcionario>>` |
| 3.8 | Consulta de aniversariantes de outubro e dezembro |
| 3.9 | Identificação do funcionário de maior idade |
| 3.10 | Listagem em ordem alfabética |
| 3.11 | Cálculo do total dos salários |
| 3.12 | Cálculo da quantidade de salários mínimos por funcionário, com base em R$ 1.212,00 |

## Como executar

### Pré-requisitos

- JDK 21 ou superior (`java -version`).
- Acesso à internet na primeira compilação para baixar o Maven e as dependências de teste.

O Maven Wrapper está incluído no repositório e dispensa a instalação separada do Maven. Execute os comandos a seguir na raiz do projeto.

### Windows (PowerShell)

```powershell
.\mvnw.cmd clean package
java -jar target/iniflex-1.0.0.jar
```

### Linux e macOS

```bash
sh mvnw clean package
java -jar target/iniflex-1.0.0.jar
```

Os comandos compilam o projeto, executam os testes e geram o JAR. Ao iniciar a aplicação, os relatórios são exibidos no console, na sequência dos requisitos.

### IntelliJ IDEA

1. Abra o `pom.xml` como projeto Maven.
2. Configure o SDK do projeto com JDK 21 ou superior e aguarde a importação das dependências.
3. Abra `src/main/java/br/com/iniflex/Principal.java`.
4. Execute o método `main`. A classe principal é `br.com.iniflex.Principal`.

## Testes

Para executar somente os testes no Windows:

```powershell
.\mvnw.cmd test
```

No Linux e macOS:

```bash
sh mvnw test
```

A suíte contém oito testes que verificam os dados cadastrados, o reajuste e o arredondamento dos salários, o total da folha, o agrupamento por função, o filtro de aniversários, o cálculo de idade, a ordenação e a formatação dos relatórios. O fluxo completo também verifica a exclusão de João da saída.

## Estrutura do projeto

```text
src/
├── main/java/br/com/iniflex/
│   ├── Pessoa.java
│   ├── Funcionario.java
│   └── Principal.java
└── test/java/br/com/iniflex/
    └── PrincipalTest.java
```

- `Pessoa`: nome, data de nascimento e cálculo da idade.
- `Funcionario`: herda `Pessoa`, acrescenta salário e função e realiza os cálculos de aumento e salários mínimos.
- `Principal`: cadastra os dados e executa os relatórios na sequência do enunciado.
- `PrincipalTest`: testes automatizados com JUnit 5.

## Decisões de implementação

- Valores monetários são construídos a partir de texto com `BigDecimal`, evitando imprecisões de `double`.
- Cada salário aumentado é arredondado para duas casas decimais usando `RoundingMode.HALF_UP`. O total soma os valores já arredondados.
- A quantidade de salários mínimos também é arredondada para duas casas decimais. A base fixa de R$ 1.212,00 é a solicitada pelo teste.
- Todos os relatórios posteriores ao aumento usam os salários atualizados e excluem João.
- `LinkedHashMap` mantém as funções na ordem em que aparecem na lista. A ordenação alfabética usa `Collator` de português brasileiro e retorna outra lista.
- A idade usa `Period.between` e a data atual da execução, respeitando se o aniversário já ocorreu no ano.

## Resultados esperados

- Após a remoção: **9 funcionários**.
- Aniversariantes de outubro: **Maria e Miguel**. Não há aniversariantes de dezembro na tabela.
- Mais velho: **Caio**, nascido em **02/05/1961**. Sua idade depende da data de execução.
- Ordem alfabética: **Alice, Arthur, Caio, Heitor, Helena, Heloísa, Laura, Maria, Miguel**.
- Total após o aumento: **R$ 50.906,82**.
- Maria após o aumento: **R$ 2.210,38**, equivalente a **1,82 salários mínimos**.
