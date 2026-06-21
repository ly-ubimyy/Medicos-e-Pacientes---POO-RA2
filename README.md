# DOCUMENTAÇÃO DO SISTEMA – MÉDICOS E PACIENTES (P2)

## 1. Visão Geral do Sistema

O sistema “Médicos e Pacientes” tem como objetivo gerenciar e consultar informações de médicos, pacientes e consultas a partir de uma base de dados previamente carregada.

A aplicação é composta por duas versões de execução:

- **P1**: Responsável por leitura de arquivos CSV e criação da base de dados.
- **P2**: Responsável por carregar a base serializada e fornecer interface gráfica para consultas.

---

## 2. Arquitetura do Sistema

O sistema foi estruturado em três camadas principais:

### 2.1 Camada de Modelo (`modelo`)

Responsável pelas entidades do sistema:

- `Medico`
- `Paciente`
- `Consulta`
- `BaseDados`
- `PessoaAbstrata`
- `Identificavel`

Responsabilidades:

- Representar dados do domínio
- Garantir regras básicas de validação
- Armazenar relacionamentos entre entidades

---

### 2.2 Camada de Busca (`busca`)

Responsável pela lógica de consultas do sistema.

Classes principais:

- `BuscaMedico`
- `BuscaPaciente`
- `EscritorResultados`

Responsabilidades:

- Filtrar dados da `BaseDados`
- Implementar regras de negócio de consulta
- Formatar resultados para exibição
- Registrar logs em arquivo texto

✔ Exemplo de responsabilidade:

- Listar médicos de um paciente
- Listar consultas por período
- Identificar pacientes sem consulta recente

---

### 2.3 Camada de Interface Gráfica (`ui`)

Responsável pela interação com o usuário:

- `TelaInicial`
- `TelaMedico`
- `TelaPaciente`

Responsabilidades:

- Capturar entradas do usuário
- Exibir resultados
- Tratar exceções e erros
- Delegar lógica para camada `busca`

---

## 3. Persistência de Dados

O sistema utiliza dois mecanismos:

### 3.1 CSV (P1)

Classe: `LeitorCsv`

- Leitura inicial dos dados
- Criação da `BaseDados`

### 3.2 Serialização Binária (P2)

Classe: `GravadorBinario`

- Salvamento e carregamento de `BaseDados`
- Uso de `ObjectOutputStream` e `ObjectInputStream`

Justificativa:

- Evita reprocessamento de CSV no P2
- Melhora desempenho
- Mantém estado consistente

---

## 4. Tratamento de Exceções

Exceções personalizadas:

### 4.1 `RegistroNaoEncontradoException`

Usada quando:

- Médico não existe
- Paciente não encontrado
- Consulta inexistente

### 4.2 `DadoInvalidoException`

Usada em validações de:

- CPF inválido
- Código inválido
- Data/hora inválidas

Estratégia adotada:

- Exceções verificadas para controle de fluxo
- Mensagens amigáveis para o usuário na interface

---

## 5. Funcionalidades do Sistema

## 5.1 Funcionalidades do Médico

Implementadas em `BuscaMedico`:

- Listar pacientes de um médico
- Listar consultas em um período
- Listar pacientes sem consulta recente

Regras:

- Consultas são filtradas por médico
- Datas são ordenadas cronologicamente
- Validação de existência do médico obrigatória

---

## 5.2 Funcionalidades do Paciente

Implementadas em `BuscaPaciente`:

- Listar médicos associados ao paciente
- Listar consultas passadas com um médico
- Listar consultas futuras

Regras:

- CPF normalizado (apenas dígitos)
- Separação entre consultas futuras e passadas
- Remoção de duplicados em listas

---

## 6. Escrita de Logs

Classe: `EscritorResultados`

Função:

- Registrar todas as consultas realizadas

Arquivos:

- `resultados/log_medico.txt`
- `resultados/log_paciente.txt`

Conteúdo do log:

- Tipo da busca
- Data e hora
- Resultado formatado

---

## 7. Interface Gráfica (Swing)

### 7.1 Estrutura

- `TelaInicial`: seleção de perfil
- `TelaMedico`: consultas médicas
- `TelaPaciente`: consultas do paciente

### 7.2 Padrão visual adotado

- Layouts: `BorderLayout`, `BoxLayout`, `FlowLayout`
- Componentes Swing padrão
- Tratamento de eventos via `ActionListener`

### 7.3 Tratamento de erros

- Uso de `JOptionPane.showMessageDialog`
- Separação de lógica e interface

---

## 8. Decisões de Projeto

### 8.1 Separação de responsabilidades

- UI não contém lógica de negócio
- Camada `busca` centraliza regras

### 8.2 Reutilização de código

- Formatação centralizada em `EscritorResultados`
- Métodos auxiliares para evitar duplicação

### 8.3 Consistência de dados

- CPF sempre normalizado
- Consultas ordenadas por data e horário

---

## 9. Possíveis melhorias futuras

- Persistência em banco de dados (ex: PostgreSQL)
- Filtros avançados na interface
- Sistema de autenticação (login médico/paciente)
- Paginação de resultados
- Exportação para PDF

---

## 10. Conclusão

O sistema atende aos requisitos propostos, implementando separação clara entre modelo, lógica de negócio e interface gráfica, além de persistência de dados e tratamento adequado de exceções.

---

## Medicos-e-Pacientes---POO-RA2 - Requisitos do Projeto
Médicos e Pacientes - POO RA2

Considere uma aplicação que gerencia dados sobre médicos, pacientes e consulas,
sendo que:

1. Cada médico possui os seguintes dados:
   
    a. Nome (string)
   
    b. Código (valor inteiro): número único de identificação do médico
   
    c. Lista de pacientes
   

3. Cada paciente possui os seguintes dados:
   
    a. Nome (string)
   
    b. CPF (número de 9 dígitos e mais 2 dígitos de controle)
   
    c. Lista de consultas
   

5. Cada consulta possui os seguintes dados:
   
    a. Data
   
    b. Horário
   
    c. Referência para um paciente (por exemplo, o CPF do paciente)
   
    d. Referência para um médico (por exemplo, o código do médico)
   

=================================================================================================================================================


A aplicação deve possuir duas interfaces de interação humano-computador em modo
console:

A. Interface do Médico permite realizar as seguintes operações de pesquisa:

    1. Quais são todos os pacientes de um determinado médico?
    
    2. Quais são todas as consultas agendadas para um determinado médico
    em determinado período (definido por uma data inicial e uma data final),
    na ordem crescente dos horários? (O período pode cobrir tanto o passado
    como o futuro.)
    
    3. Quais são os pacientes de um determinado médico que não o consulta há
    mais que um determinado tempo (em meses)?
    
    
B. Interface do Paciente permite realizar as seguintes operações de pesquisa:

    1. Quais são todos os médicos de um paciente, isto é, médicos que o
    paciente já se consultou ou tem consulta agendada?
    
    2. Quando ocorreram todas as consultas de um determinado paciente com
    determinado médico? (Somente consultas realizadas no passado são
    consideradas.)
    
    3. Quais são todas as consultas agendadas que um determinado paciente
    possui? (Somente consultas agendadas para o futuro são consideradas.)
    
    
Implemente a aplicação usando a linguagem Java e de acordo com o paradigma de
orientação a objetos, tal que haja uma classe para representar os médicos, outra para
represenar os pacientes e outra para representar as consultas, conforme definido acima.

Além disso, a aplicação deverá ter mais duas classes para fazer a interface humano-
computador via console, sendo uma para o médico e outra para o paciente.

Os dados referentes aos objetos das classes que representam médicos, pacientes e
consultas deverão ser lidos de arquivos no formato csv (comma separated values).

Os seguintes arquivos deverão existir:

    1. Um arquivo com dados sobre médicos: somente o nome e o código de cada
    médico. Pode haver qualquer quantidade de médicos.
    
    2. Um arquivo com dados sobre pacientes: somente o nome e o CPF de cada
    paciente. Pode haver qualquer quantidade de pacientes.
    
    3. Um arquivo com dados sobre consultas: data, horário, código do médico e CPF do
    paciente em cada consulta. Pode haver qualquer quantidade de consultas.

    
O resultado de cada pesquisa realizada poderá ser exibido diretamente na tela do
computador, como também poderá ser escrito em um arquivo texto, à escolha do usuário.

Se a opção for por escrever o resultado em arquivo, o usuário deverá informar o nome do
arquivo a ser gerado.


