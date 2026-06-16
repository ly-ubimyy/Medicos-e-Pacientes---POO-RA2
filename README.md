# Medicos-e-Pacientes---POO-RA2
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


