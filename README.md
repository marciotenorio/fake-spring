# Fake Spring

Um middleware que implementa alguns dos [Remoting Patterns](https://voelter.de/data/presentations/remotingPatterns.pdf) dos autores Uwe Zdun, Markus Völter e Michael Kircher, com o objetivo de entender algumas abordagens consolidadas para a criação de frameworks modernos como [Spring](https://spring.io/) e vislumbar como as coisas funcionam debaixo do capô!

Projeto feito para a 2º unidade da disciplina de Programação Distribuída, ministrada pelo Prof. Dr. Nélio Alessandro Azevedo Cacho.
 
 
 ## Implementado utilizando protocolo TCP
 
 ### Basic Remoting Pattern
 
 - ServerRequestHandler
 - Invoker
 - Ciclo de vida do objeto por requisição
 - Marshaller
 
 ### Identification Remoting Pattern
 
 - Identificação da chamada remota para as classes de negócio fornecidas
 
 
 ### Extension Pattern
 
 - Criação de hooks para a possibilidade de adição de libs externas ao middleware
