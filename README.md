# Teste Prático — Iniflex

Aplicação Java de console que executa todos os requisitos do teste, com os dados da tabela fornecida. Não requer banco de dados ou servidor.

## Requisitos e execução

- JDK 21 ou superior (`java -version`).
- Acesso à internet na primeira compilação para baixar o Maven e as dependências de teste.

No Windows (PowerShell), na pasta do projeto:

```powershell
.\mvnw.cmd clean package
java -jar target/iniflex-1.0.0.jar
```

No Linux/macOS:

```bash
sh mvnw clean package
java -jar target/iniflex-1.0.0.jar
```

O Maven Wrapper já está incluído: não é necessário instalar Maven separadamente. No IntelliJ, abra o `pom.xml` como projeto Maven, selecione um JDK 21 ou superior e execute o método `main` de `Principal`.

Para executar apenas os testes:

```powershell
.\mvnw.cmd test
```

## Organização

- `Pessoa`: nome, data de nascimento e cálculo da idade.
- `Funcionario`: herda `Pessoa`, acrescenta salário e função e realiza os cálculos de aumento e salários mínimos.
- `Principal`: cadastra os dados e executa os relatórios na sequência do enunciado.
- `PrincipalTest`: testes automatizados com JUnit 5.

Os arquivos Java ficam em `src/main/java/br/com/iniflex`, e os testes em `src/test/java/br/com/iniflex`.

## Atendimento ao enunciado

| Item | Implementação |
| --- | --- |
| 1 e 2 | Classes `Pessoa` e `Funcionario`, com `LocalDate`, `BigDecimal` e herança |
| 3.1 | Cadastro dos dez funcionários na ordem e com os dados da tabela |
| 3.2 | Remoção de João antes dos relatórios |
| 3.3 | Impressão dos dados com datas `dd/MM/yyyy` e números em `pt-BR` |
| 3.4 | Atualização dos salários com aumento de 10% |
| 3.5 e 3.6 | Agrupamento em `Map<String, List<Funcionario>>` e impressão por função |
| 3.8 | Filtro de aniversariantes de outubro e dezembro |
| 3.9 | Nome e idade do funcionário com a data de nascimento mais antiga |
| 3.10 | Ordenação alfabética por nome |
| 3.11 | Soma dos salários após o aumento |
| 3.12 | Divisão do salário atualizado por R$ 1.212,00 |

O enunciado pula o item 3.7; a numeração foi mantida.

## Decisões de implementação

- Valores monetários são construídos a partir de texto com `BigDecimal`, evitando imprecisões de `double`.
- Cada salário aumentado é arredondado para duas casas decimais usando `RoundingMode.HALF_UP`. O total soma os valores já arredondados.
- A quantidade de salários mínimos também é arredondada para duas casas decimais. A base fixa de R$ 1.212,00 é a solicitada pelo teste.
- Todos os relatórios posteriores ao aumento usam os salários atualizados e excluem João.
- `LinkedHashMap` mantém as funções na ordem em que aparecem na lista. A ordenação alfabética usa `Collator` de português brasileiro e retorna outra lista.
- A idade usa `Period.between` e a data atual da execução, respeitando se o aniversário já ocorreu no ano.
- O nome da classe é `Funcionario`, sem acento no identificador; os dados e textos preservam os acentos.

## Resultados esperados

- Após a remoção: **9 funcionários**.
- Aniversariantes de outubro: **Maria e Miguel**. Não há aniversariantes de dezembro na tabela.
- Mais velho: **Caio**, nascido em **02/05/1961**. Sua idade depende da data de execução.
- Ordem alfabética: **Alice, Arthur, Caio, Heitor, Helena, Heloísa, Laura, Maria, Miguel**.
- Total após o aumento: **R$ 50.906,82**.
- Maria após o aumento: **R$ 2.210,38**, equivalente a **1,82 salários mínimos**.

Os testes cobrem cadastro, remoção no fluxo completo, salários atualizados, total, agrupamento, outubro e dezembro, idade antes e no aniversário, ordenação, formatação e saída do programa.

## Entrega

Publique os fontes, o `pom.xml`, o Maven Wrapper e este README no seu repositório GitHub e envie o link do repositório na etapa Mão na Massa. Verifique se os avaliadores têm acesso ao repositório. A pasta `target` é gerada durante a compilação e não precisa ser versionada.
