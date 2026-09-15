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

## Fluxo de processamento

1. Criar uma lista com os dez funcionários na ordem da tabela.
2. Remover João e imprimir os nove funcionários com os salários originais.
3. Aplicar o aumento de 10% a cada funcionário restante.
4. Agrupar os funcionários por função e imprimir os grupos.
5. Consultar os aniversariantes de outubro e dezembro e identificar o funcionário mais velho.
6. Gerar uma lista em ordem alfabética, calcular o total salarial e a quantidade de salários mínimos.

A sequência define o estado usado por cada relatório: o item 3.3 apresenta os valores originais; os relatórios seguintes usam os valores reajustados. João é excluído antes de qualquer impressão ou cálculo da folha. Cada execução começa com uma nova lista, portanto o reajuste não se acumula entre execuções.

## Decisões técnicas e justificativas

### 1. Aplicação de console com dados em memória

O processamento tem entrada fixa e saída textual. Um método `main` permite executar toda a sequência e conferir os resultados diretamente, sem configuração de serviços externos. As dependências da aplicação em execução se limitam à biblioteca padrão do Java; JUnit é utilizado apenas nos testes.

Uma API com Spring Boot, persistência com JPA ou uma interface gráfica acrescentaria configuração e responsabilidades que o fluxo solicitado não utiliza. A lista em memória atende à execução única do exercício. Como consequência, alterações não são persistidas após o encerramento do programa.

### 2. Herança e encapsulamento

`Funcionario` estende `Pessoa`, conforme o requisito, reutilizando nome, data de nascimento e cálculo de idade. Salário e função pertencem à especialização `Funcionario`.

Os atributos são privados e acessados por getters. Nome, data de nascimento e função são `final`, pois o fluxo não prevê sua alteração. O salário é mutável, mas sua atualização ocorre pelo método `aumentarSalario`, que concentra a fórmula e o arredondamento.

Um setter genérico de salário permitiria substituir o valor sem passar pela regra de reajuste. A operação explícita torna a intenção da mudança visível no código. Composição seria uma alternativa de modelagem em outro contexto; neste projeto, a herança também atende à estrutura exigida pelo enunciado.

### 3. Responsabilidades e tamanho da solução

As operações individuais ficam no modelo: `Pessoa` calcula a idade e `Funcionario` realiza os cálculos salariais. `Principal` coordena a execução, as consultas sobre a coleção e a impressão. Métodos separados para agrupamento, filtro, ordenação e totalização permitem verificar esses comportamentos diretamente nos testes.

Essa organização mantém o projeto em três classes de produção. Camadas adicionais de serviço e repositório seriam úteis com múltiplas fontes de dados ou interfaces de entrada. No escopo atual, os métodos estáticos de `Principal` evitam uma estrutura adicional de instanciação, com a contrapartida de manter consultas e apresentação na mesma classe.

### 4. Valores monetários com `BigDecimal`

Os salários são criados a partir de representações textuais, como `new BigDecimal("2009.44")`. Assim, os valores decimais da tabela são preservados sem uma conversão intermediária para ponto flutuante binário.

Além de ser exigido pelo enunciado, `BigDecimal` permite definir explicitamente a precisão decimal e a regra de arredondamento. `double` e `float` introduziriam aproximações para vários valores decimais. Representar dinheiro como centavos em `long` seria possível, mas exigiria tratamento adicional para o percentual de aumento e para a divisão por salários mínimos.

Como `BigDecimal` é imutável, o resultado da multiplicação é atribuído novamente ao atributo `salario`.

### 5. Política de arredondamento

O enunciado define o percentual, mas não especifica a política de arredondamento. A implementação adota duas casas decimais e `RoundingMode.HALF_UP` para cada salário reajustado: arredonda para o valor mais próximo e, em um empate, afasta de zero.

```text
salário atualizado = arredondar(salário original × 1,10; 2 casas)

Laura: 3.017,45 × 1,10 = 3.319,195 → 3.319,20
Heitor: 1.582,72 × 1,10 = 1.740,992 → 1.740,99
```

O total é calculado pela soma dos salários individuais já arredondados. Essa escolha mantém a soma coerente com os valores exibidos para cada pessoa. Arredondar somente o total produziria, neste conjunto de dados, um resultado diferente:

| Estratégia | Total |
| --- | --- |
| Somar os salários reajustados e arredondados individualmente — adotada | R$ 50.906,82 |
| Aplicar 10% ao total original sem João e arredondar apenas ao final | R$ 50.906,83 |

`HALF_EVEN` seria outra política possível para desempatar arredondamentos, mas não foi estabelecida como regra deste exercício. A política adotada é explícita no código e verificada pelos testes.

A quantidade de salários mínimos usa `divide` com escala 2 e a mesma regra. Isso também define como apresentar divisões que não resultam em um decimal finito. O resultado é uma razão com duas casas, não uma contagem inteira. A base de **R$ 1.212,00** permanece fixa conforme o enunciado.

### 6. Datas com `LocalDate` e idade com `Period`

A data de nascimento representa um dia do calendário, sem horário ou fuso. `LocalDate` expressa esse conceito diretamente. Um tipo como `LocalDateTime` acrescentaria uma informação de horário desnecessária ao modelo.

A idade é calculada por `Period.between(dataNascimento, dataReferencia).getYears()`. Subtrair apenas os anos das duas datas poderia antecipar a idade de alguém cujo aniversário ainda não ocorreu. O método recebe a data de referência como parâmetro para permitir testes determinísticos; somente o ponto de execução fornece `LocalDate.now()`, usando a data do ambiente.

Para encontrar o mais velho, a consulta seleciona a menor data de nascimento com `min`. Ordenar toda a lista para obter somente o primeiro elemento exigiria trabalho adicional. Se a lista estiver vazia, o método lança `IllegalArgumentException` com uma mensagem explícita. O fluxo principal trabalha com os nove funcionários restantes.

### 7. Coleções e preservação da ordem

| Escolha | Justificativa | Alternativa considerada |
| --- | --- | --- |
| `ArrayList` para o cadastro | Preserva a ordem da tabela e permite remover funcionários | `List.of` isoladamente não permite remoção; um `Set` não expressa a necessidade de uma lista de registros |
| `removeIf` para excluir João | Declara o critério de remoção diretamente, sem controlar índices durante a iteração | Remoção por posição dependeria da localização do funcionário na tabela |
| `LinkedHashMap` no agrupamento | Preserva a ordem de primeira aparição das funções, tornando a saída previsível | `HashMap` não garante essa ordem; `TreeMap` ordenaria pelas chaves |
| `List<Funcionario>` em cada grupo | Mantém todos os funcionários da mesma função na ordem de encontro | Um único valor por chave descartaria os demais funcionários da função |

O agrupamento usa `Collectors.groupingBy` com a função como chave. As listas dos grupos contêm referências aos mesmos funcionários da lista principal, sem cópias dos objetos. Como o reajuste ocorre antes do agrupamento, os grupos já apresentam os salários atualizados.

A remoção compara exatamente o nome `João`. Para os dados fornecidos, isso identifica o registro desejado; em um cadastro com homônimos, seria necessário um identificador único.

### 8. Consultas com streams sequenciais

Filtro, agrupamento, ordenação e soma usam a Stream API para expressar a operação realizada sobre a coleção. A totalização combina `map` e `reduce`, com `BigDecimal.ZERO` como valor inicial.

Laços tradicionais também atenderiam aos requisitos. A escolha por streams mantém as consultas curtas e próximas de sua intenção. O processamento é sequencial: para dez registros, paralelismo acrescentaria coordenação sem uma necessidade prática de distribuição do trabalho.

Os resultados de `aniversariantes` e `ordenarPorNome` são listas não modificáveis produzidas por `Stream.toList()`. Isso evita alterações estruturais acidentais nesses resultados, mas não torna os funcionários imutáveis: eles continuam sendo os mesmos objetos, com salário mutável.

### 9. Ordenação e apresentação em português brasileiro

A ordenação usa `Collator` configurado com `pt-BR`, levando em consideração regras linguísticas na comparação dos nomes. A comparação natural de `String` segue a ordem dos caracteres Unicode, que pode não corresponder à ordem alfabética esperada para nomes com acentos.

O método retorna outra lista ordenada e preserva a ordem da lista principal. Essa decisão permite que o relatório alfabético tenha sua própria apresentação sem alterar a sequência usada pelos relatórios seguintes.

Datas e números são formatados apenas na saída. `DateTimeFormatter` usa `dd/MM/yyyy`, e `NumberFormat` usa `pt-BR` com duas casas decimais. Dessa forma, um salário continua sendo `BigDecimal` durante os cálculos e só se torna texto, como `21.031,87`, ao ser exibido. Substituições manuais de pontos e vírgulas misturariam apresentação e manipulação dos valores.

### 10. Compilação e empacotamento

O `pom.xml` configura `maven.compiler.release` como `21` e a codificação dos fontes como UTF-8. O Maven Wrapper fixa a distribuição do Maven utilizada, reduzindo diferenças de configuração entre ambientes.

O plugin de JAR registra `br.com.iniflex.Principal` no manifesto, permitindo iniciar a aplicação com `java -jar`. Como não existem dependências externas de execução, o artefato não precisa incorporar bibliotecas de terceiros. JUnit tem escopo `test` e não integra o JAR da aplicação.

## Estratégia de validação

Os testes verificam resultados observáveis e exemplos calculados previamente. Não utilizam banco de dados, rede ou mocks, pois as operações são locais e podem ser exercitadas diretamente.

| Cenário | Verificação |
| --- | --- |
| Cadastro | Ordem dos dez nomes, soma dos salários originais e data de nascimento de Maria |
| Reajuste | Nove funcionários, cada salário atualizado, total após arredondamento e razão salarial de Maria |
| Agrupamento | Sete funções, integrantes de Operador e Gerente e contagem total dos registros agrupados |
| Aniversários | Maria e Miguel na base original e inclusão de um registro de dezembro exclusivo do teste |
| Idade | Caio como mais velho e idade no dia anterior e no dia do aniversário |
| Ordenação | Sequência alfabética esperada e preservação da primeira posição da lista original |
| Formatação | Separadores brasileiros e manutenção de duas casas decimais |
| Execução completa | Ausência de João, títulos dos relatórios e exemplos de datas e valores na saída |

O registro adicional de dezembro é necessário porque a tabela original não contém aniversariantes nesse mês: testar apenas os dados originais não detectaria um filtro que consultasse somente outubro. As datas fixas no teste de idade evitam que o resultado dependa do dia em que a suíte é executada.

O teste do fluxo completo captura `System.out` e restaura a saída original em um bloco `finally`. Ele verifica trechos relevantes do relatório, sem exigir correspondência de todos os espaços usados no alinhamento das colunas.

## Escopo e limitações

- A entrada é a tabela fixa definida no código; não há leitura interativa, importação de arquivos ou persistência.
- Os construtores e cálculos pressupõem dados válidos. Não há validação de nomes nulos, datas futuras, salários negativos ou base salarial menor ou igual a zero. Uma entrada externa exigiria essas validações.
- A função é `String`, conforme o requisito. O agrupamento depende de grafia consistente; não há normalização de funções.
- O método de reajuste aplica o percentual ao salário atual a cada chamada. O fluxo principal o chama uma vez por funcionário.
- A suíte cobre os cenários descritos, sem afirmar cobertura integral de entradas inválidas, empates ou regras específicas para aniversários em 29 de fevereiro.

## Resultados esperados

- Após a remoção: **9 funcionários**.
- Aniversariantes de outubro: **Maria e Miguel**. Não há aniversariantes de dezembro na tabela.
- Mais velho: **Caio**, nascido em **02/05/1961**. Sua idade depende da data de execução.
- Ordem alfabética: **Alice, Arthur, Caio, Heitor, Helena, Heloísa, Laura, Maria, Miguel**.
- Total após o aumento: **R$ 50.906,82**.
- Maria após o aumento: **R$ 2.210,38**, equivalente a **1,82 salários mínimos**.
