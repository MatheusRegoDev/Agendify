# DOCUMENTO DE VISÃO DE PRODUTO — AGENDIFY

## 1. Introdução e Objetivo do Produto
O Agendify é um ecossistema de software focado na otimização operacional de estabelecimentos que combinam a necessidade de agendamentos prévios com a triagem e gestão física de filas presenciais. O sistema visa mitigar o tempo de ócio de profissionais, gerenciar os picos de tráfego de clientes com filas prioritárias/normais divididas em até duas etapas independentes, e prover transparência informativa por meio de painéis digitais e notificações integradas.

---

## 2. Perfis de Usuários (Atores)
* **Administrador (ROLE_ADMIN):** Responsável por gerenciar a equipe, cadastrar recepcionistas e atendentes, além de auditar métricas gerais do negócio.
* **Recepcionista (ROLE_RECEPTIONIST):** Responsável pelo cadastro de clientes, controle do calendário diário de agendamentos, disparo de informativos, confirmação de presença (check-in) e encaminhamento para as filas virtuais.
* **Atendente (ROLE_ATTENDANT):** Responsável por consumir as filas virtuais, iniciar os atendimentos físicos, rotear processos para outros especialistas (etapa 2) e encerrar o ciclo do cliente.

---

## 3. Regras de Negócio (RN)
As Regras de Negócio definem as premissas e restrições estritas do domínio da aplicação:

* **RN01 — Teto Diário de Agendamentos por Operador:** Um operador com perfil de Recepcionista só pode efetuar e salvar, no máximo, 20 agendamentos por dia civil no sistema. Tentativas subsequentes devem ser bloqueadas com uma exceção de validação.
* **RN02 — Cancelamento Massivo Coletivo:** Ao sofrer o cancelamento completo de uma data operacional, o sistema deve invalidar todos os agendamentos daquele dia específico e disparar, de forma assíncrona, notificações via WhatsApp a todos os celulares associados àquela data detalhando o motivo.
* **RN03 — Ciclo Multi-Etapa Limitado:** O ciclo de atendimento físico possui um teto de duas etapas consecutivas. O Atendente 1 pode encerrar o fluxo ou encaminhar para o Atendente 2. O Atendente 2 obrigatoriamente encerra o processo, impedindo loops na fila.
* **RN04 — Priorização Legal de Fila:** A ordenação interna da fila presencial deve obrigatoriamente intercalar chamadas dando precedência para a Fila Preferencial (idosos, gestantes, PCD) em detrimento da Fila Normal, conforme a legislação vigente.

---

## 4. Requisitos Funcionais (RF)
Os Requisitos Funcionais descrevem o que o sistema deve fazer (as rotas e ações da API):

* **RF01 — Controle de Acesso Baseado em Perfis (RBAC):** O sistema deve isolar o tráfego de dados por escopo de autenticação (JWT). Rotas do Administrador não podem ser interceptadas por Recepcionistas ou Atendentes.
* **RF02 — Manutenção Cadastral de Clientes:** O sistema deve permitir que a Recepcionista capture os dados básicos de novos clientes, exigindo unicamente o Nome Completo e um Número de Telefone Celular válido.
* **RF03 — Agendamento e Reagendamento:** Permitir a alocação de horários futuros vinculando um cliente, um horário e uma data válida. Deve permitir também a alteração de status para "Cancelado" ou "Remarcado".
* **RF04 — Check-in e Triagem de Entrada:** Permitir que a recepcionista confirme a presença do cliente no dia previsto, efetuando a inserção imediata dele na fila virtual categorizada como Normal ou Preferencial.
* **RF05 — Gestão de Chamada Operacional:** Disponibilizar aos atendentes a ação de "Chamar Próximo Cliente", fazendo com que o algoritmo consuma a fila ativa com base no tipo de prioridade e timestamp de chegada.
* **RF06 — Atualização do Painel Público:** O sistema deve atualizar dinamicamente a tela/televisão da sala de esperas, exibindo o nome do cliente chamado e a identificação/mesa do atendente designado.
