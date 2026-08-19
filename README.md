![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

# Concorrencia - Spring Data JPA
 
Aplicação Spring experimental criada para simular condições de corrida (Race Conditions), analisar cenários de Overbooking e demonstrar soluções práticas de concorrência utilizando Lock Otimista e Lock Pessimista no Spring Data JPA.
 
## Sobre o Teste
 
Em sistemas de alta demanda (como venda de ingressos de shows, reservas de passagens ou e-commerce durante a Black Friday), múltiplos usuários tentam comprar ou alterar o mesmo recurso no exato mesmo milissegundo.
 
Sem o devido tratamento de concorrência na camada de persistência, ocorrem problemas graves de **integridade de dados**, como o *Overbooking* (vários clientes comprando o mesmo assento) e o *Lost Update* (uma alteração sobrescrevendo a outra silenciosamente).
 
Esta aplicação disponibiliza três estratégias de salvamento isoladas em `ReservaService`. Cada estratégia é validada por um método próprio de uma classe de testes JUnit dedicada (`ReservaConcorrenciaTest`), que chama a service diretamente e dispara múltiplas threads simultâneas contra o mesmo assento para medir o comportamento de cada estratégia sob concorrência.
 
---
 
## Estratégias de Concorrência Analisadas
 
### 1. Sem Lock — `ReservaService.salvarSemLock()`
* **Mecanismo:** Leitura e gravação padrão sem qualquer controle de versão ou bloqueio.
* **Comportamento sob Carga:** Múltiplas threads leem a entidade no banco ao mesmo tempo em estado `DISPONIVEL`. As validações de negócio passam para todas elas e múltiplos `INSERT`s/`UPDATE`s são confirmados no banco de dados.
* **Resultado:** **Vulnerável a Overbooking** (múltiplas reservas gravadas para um único assento).
### 2. Lock Otimista — `ReservaService.salvarComLockOtimista()`
* **Mecanismo:** Utiliza a anotação `@Version` do JPA na entidade monitorada (`Assento`).
* **Comportamento sob Carga:** Não bloqueia a leitura no banco. Ao tentar realizar o `COMMIT`, o Hibernate compara a versão da entidade em memória com a versão atualizada no banco. Se outra thread alterou a versão nesse intervalo, uma `ObjectOptimisticLockingFailureException` é disparada.
* **Tratamento no Teste:** A exceção é capturada no bloco `catch` do teste e agrupada no log como `ObjectOptimisticLockingFailureException`.
* **Resultado:** **100% Protegido**. Apenas 1 transação tem sucesso no commit; as colisões simultâneas são abortadas e tratadas.
### 3. Lock Pessimista — `ReservaService.salvarComLockPessimista()`
* **Mecanismo:** Utiliza `@Lock(LockModeType.PESSIMISTIC_WRITE)` executando um `SELECT ... FOR UPDATE` no banco de dados.
* **Comportamento sob Carga:** Bloqueia a linha da tabela (`row-level lock`) assim que a primeira thread faz a leitura. As demais threads aguardam na fila até que a primeira transação finalize o `COMMIT` ou `ROLLBACK`.
* **Tratamento no Teste:** A primeira thread reserva o assento; as threads seguintes na fila leem o assento já atualizado como `RESERVADO` e falham na validação de negócio feita por `ReservaValidator`, sendo capturadas e agrupadas no log do teste.
* **Resultado:** **100% Protegido**. Garante fila ordenada de execução diretamente no banco de dados.
---
 
## Tecnologias Utilizadas
 
- Java 25
- Spring Boot 4.1.0 (Spring Framework 7)
- Spring Data JPA
- PostgreSQL
- Maven
---
 
## Pré-Requisitos
 
Antes de começar, tenha instalado em sua máquina:
 
- [Java 21] ou superior
- [Maven] (ou use o wrapper `./mvnw` incluso no projeto)
- [PostgreSQL] instalado e em execução
- Uma IDE de sua preferência (o projeto foi desenvolvido usando IntelliJ, mas qualquer uma com suporte a Java/Maven funciona)
---
 
## Como Configurar o Projeto
 
Estes passos preparam o ambiente para rodar os testes (não é necessário subir a aplicação como servidor, já que não há controllers/endpoints expostos).
 
1. Clone o repositório:
```
git clone https://github.com/joaofranciscoms/reservas-api-teste-concorrencia.git
```
 
2. Crie um banco de dados no PostgreSQL chamado `reservations-platform`.

3. Abra o projeto na sua IDE e aguarde a instalação automática das dependências.
  
4. Altere as credenciais de acesso no arquivo `src/main/resources/application.yml` somente se o username e o password padrão `postgres` do banco de dados tenha sido alterada. Caso nenhuma alteração tenha sido feita deixe a parte do .yml que corresponde a esses campos inalterada. Dessa forma:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reservations-platform
    username: postgres
    password: postgres
```
> **Por que as credenciais estão expostas?** As credenciais do banco de dados (`username`/`password`) aparecem expostas por padrão no `application.yml` de propósito. A ideia é que, após clonar o repositório, a API já suba pronta para uso, sem exigir configuração adicional de variáveis de ambiente — basta ter o PostgreSQL instalado com o banco `reservations-platform` criado.
>
> Todos esses valores são apenas defaults e podem ser sobrescritos.
>
> **Importante**: esses valores padrão servem apenas para ambiente local de desenvolvimento/testes. Em produção, é essencial sobrescrever essas variáveis com credenciais seguras, nunca utilizando os valores expostos aqui.
---
 
## Testando via JUnit (Sem Lock, Lock Otimista, Lock Pessimista)
 
Com a aplicação já configurada (não é necessário estar rodando para este método, os testes sobem seu próprio contexto):
 
1. Acesse a classe de testes, localizada no diretório `test`, logo abaixo do diretório `resources`.
![Classe de testes](imagens/captura-classe-teste.png)
 
2. Execute um dos métodos de teste conforme o cenário desejado: `semLock`, `lockOtimista` ou `lockPessimista`.
![Métodos de teste](imagens/captura-metodos-disponiveis.png)
 
O resultado aparece no log ao final da execução:
 
**Sem Lock**
 
![Resultado teste sem lock](imagens/captura-resultado-teste-sem-lock.png)
 
**Lock Otimista**
 
![Resultado teste lock otimista](imagens/captura-resultado-teste-otimista.png)
 
**Lock Pessimista**
 
![Resultado teste lock pessimista](imagens/captura-resultado-teste-pessimista.png)
 
Também é possível acompanhar o resultado diretamente no banco de dados:
 
**Sem Lock**
 
![Resultado no banco do teste sem lock](imagens/captura-resultado-banco-sem-lock.png)
 
**Lock Otimista ou Pessimista**
 
![Resultado no banco lock otimista ou pessimista](imagens/captura-resultado-banco-otimista-pessimista.png)
 
---
 
## Tabela Comparativa de Resultados (Benchmark)
 
Resultados obtidos ao disparar uma bateria de **50 Threads simultâneas** direcionadas para o mesmo assento:
 
| Estratégia | Trava Utilizada | Carga Simultânea (Quantidade de Threads) | Reservas Gravadas no Banco | Exceções Registradas no Log do Teste | Diagnóstico da Aplicação |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Sem Lock** | Nenhuma | 50 | **20+ (Overbooking)** | Nenhuma (falhas silenciosas de integridade) | **Falha de Integridade:** Leituras sujas paralelas permitiram reservas duplicadas. |
| **Lock Otimista** | `@Version` | 50 | **1 (Sucesso)** | `ObjectOptimisticLockingFailureException` (colisões)<br>exceção de `ReservaValidator` (regra) | **Sucesso:** As colisões exatas no commit foram capturadas como `ObjectOptimisticLockingFailureException` e o restante foi barrado por regra de negócio. |
| **Lock Pessimista** | `FOR UPDATE` | 50 | **1 (Sucesso)** | `AssentoIndisponivelException` (49x) | **Sucesso:** A trava de linha enfileirou as requisições e as demais caíram na validação de assento indisponível. |
