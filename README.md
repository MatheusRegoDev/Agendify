
# Agendify

> Sistema de Agendamento e Gestão de Filas
---
![Logo]()
---


## Visão Geral

O **Agendify** é um sistema web desenvolvida em **Spring Boot** e **React** voltada para a automação de agendamentos, controle de fluxos de triagem e gestão de filas de atendimento em tempo real. O sistema resolve o problema de estabelecimentos que necessitam de agendamento prévio, controle de presença e fluxos de atendimento dinâmicos divididos em até duas etapas independentes.

---

## Status do Projeto

![Badge em Desenvolvimento](https://img.shields.io/badge/Status-Em%20Desenvolvimento-orange?style=for-the-badge)
![Badge Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.x-brightgreen?style=for-the-badge)

Atualmente, o **Agendify** encontra-se na fase de **desenvolvimento das regras de negócio centrais e integração dos fluxos**. A arquitetura da API e o modelo de dados (entidades, mapeamento relacional e controle de acesso RBAC) já foram totalmente estruturados. 

---

## Funcionalidades do Sistema

O sistema é dividido em três grandes módulos operacionais, cobrindo desde o primeiro contato do cliente até o encerramento do seu atendimento presencial:

### 1. Gestão de Clientes e Agendamentos (Módulo Recepção)
* **Cadastro Simplificado:** Captura rápida de dados essenciais do cliente (Nome e Celular) em conformidade com políticas de privacidade.
* **Agendamento Inteligente:** Alocação de horários futuros na agenda com controle estrito de concorrência de horários.
* **Bloqueio por Operador (Regra de Negócio):** O sistema impede que uma mesma recepcionista realize mais de **20 agendamentos para o mesmo dia civil**, garantindo a distribuição de carga de trabalho.
* **Cancelamento Massivo Notificado (Regra de Negócio):** Em caso de imprevistos ou fechamento do estabelecimento, a recepcionista pode cancelar uma data inteira. O sistema invalida os agendamentos e dispara de forma assíncrona alertas via WhatsApp informando o motivo e o link para remarcação.

### 2. Controle e Triagem de Entrada (Módulo Fila)
* **Check-in de Presença:** Confirmação da chegada do cliente no dia previsto, ativando sua entrada na fila de espera virtual.
* **Triagem de Prioridade:** Separação automática dos clientes entre **Fila Normal** e **Fila Preferencial**, atendendo às legislações de prioridade de atendimento.
* **Algoritmo de Escalonamento:** A fila é consumida de forma inteligente, intercalando os clientes com direito à preferência e respeitando o tempo de chegada (timestamp).

### 3. Ciclo de Atendimento e Painel (Módulo Operacional)
* **Chamada Dinâmica:** O atendente solicita o próximo cliente através de um clique, alterando o status da fila em tempo real.
* **Atendimento Multi-Etapa:** Suporte a fluxos complexos de atendimento de até duas etapas. O primeiro atendente pode encerrar o processo ou encaminhar o cliente diretamente para o segundo atendente técnico/especialista.
* **Painel de Exibição Público (Sala de Espera):** Módulo atualizado instantaneamente via protocolo persistente (WebSocket/SSE) sempre que um atendente chama um cliente, exibindo na tela o nome do cliente e a mesa/guichê de destino.
* **Gestão Administrativa:** Painel exclusivo para administradores realizarem a contratação, cadastro e vinculação de perfis (`Roles`) de recepcionistas e atendentes.
## Tecnologias e Stack Utilizada

O projeto foi construído utilizando uma arquitetura desacoplada (Decoupled Architecture), onde o ecossistema do ecossistema Java robusto no Back-end serve uma aplicação Single Page Application (SPA) reativa e moderna no Front-end.

### Back-end (API REST)
* **Java 17:** Versão de suporte de longo termo (LTS) da linguagem.
* **Spring Boot 3.x:** Framework base para a aceleração e configuração da API.
  * **Spring Data JPA & Hibernate:** Abstração da camada de persistência e mapeamento das entidades do banco de dados.
  * **Spring Security & JWT (Json Web Token):** Mecanismos para autenticação de usuários e controle de acesso baseado em perfis corporativos (`ROLE_ADMIN`, `ROLE_RECEPTIONIST`, `ROLE_ATTENDANT`).
  * **Spring WebSocket:** Protocolo de comunicação bidirecional de baixa latência para a atualização em tempo real do painel de chamadas da sala de espera.
* **Lombok:** Biblioteca para eliminação de códigos boilerplate (Getters, Setters e Construtores).

### Front-end (Interface do Usuário)
* **React (com TypeScript):** Biblioteca base para a construção de interfaces SPA modulares e baseadas em componentes declarativos, utilizando tipagem estrita para garantir a consistência dos dados trafegados entre a API e a Interface.
* **Tailwind CSS:** Framework CSS utilitário para estilização ágil, responsiva e com design de componentes moderno.
* **React Router (TS):** Gerenciamento de rotas e navegação interna da aplicação (isolando as telas do painel público, recepção e painel do atendente de forma tipada).
* **Axios / Fetch API:** Cliente HTTP para consumo seguro e integrado dos endpoints da API Spring Boot.
* **SockJS-Client & @stomp/stompjs (com `@types`):** Bibliotecas para conexão cliente-servidor via WebSockets, permitindo o recebimento de eventos tipados em tempo real na tela do Painel sempre que um cliente for chamado.

### Infraestrutura, Dados e Ferramentas
* **PostgreSQL / MySQL:** Banco de dados relacional para armazenamento seguro dos históricos de atendimento, agendamentos e transações de usuários em conformidade com as propriedades ACID.
* **Flyway / Liquibase:** Ferramenta de migração de banco de dados, garantindo o versionamento do esquema (DDL) de forma controlada.
* **Docker:** Utilizado para a conteinerização e isolamento do ambiente de banco de dados e serviços locais, facilitando o deploy.
* **Maven:** Gerenciador de dependências e automação do build do projeto Java.## Documentação do Projeto

Esta seção reúne os artefatos de modelagem, arquitetura e requisitos que regem as regras de negócio e o comportamento do ecossistema **Agendify**.

---

Clique nos links abaixo para acessar os artefatos completos:

* **[Documento de Visão (Requisitos e Regras de Negócio)](./docs/documento-de-visao.md):** Contém o detalhamento de todos os Requisitos Funcionais (RF), Requisitos Não Funcionais (RNF) e o mapeamento das regras restritivas do sistema (como o teto de 20 agendamentos diários).
* **[Fluxo de Processos - BPMN](./docs/bpmn-processos.md):** Diagrama que ilustra o ciclo de vida do cliente no estabelecimento, desde a triagem na recepção ao atendimento em duas etapas. *Disponível em formato Mermaid para importação direta no Excalidraw.*
* **[Modelo de Domínio (Diagrama de Classes UML)](./docs/diagrama-classes.md):** Arquitetura lógica do banco de dados relacional e relacionamentos entre as entidades da API Spring Boot.
## Autor

- [@MatheusRegoDev](https://github.com/MatheusRegoDev)

