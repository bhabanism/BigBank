Rough build order (no concurrency)
1 docker-compose: ActiveMQ + interest-service.
2 interest-service: JPA Account (minimal fields), JMS config, queue name.
3 DTO InterestAccrualMessage + producer service: load all accounts → send messages.
4 Consumer @JmsListener: calculate → save.
5 REST POST /api/interest/accrual/run (+ security).
6 Gateway route + env vars.
7 Cache: plan eviction on banking (or accept stale for first test, then fix).
8 Verify: SQL updates, second GET /api/accounts shows new balances after eviction/TTL.


** New and Priority Topics
[x] Microservices
[] Spring Cache
[x] Docker
[] Kubernetes
[] Kafka or Spring JMS + Active MQ
[] Spring Cloud
[x] Microservices
[x] 1-2 demo micro frontend in angular
[x] Backend: Spring Security + login (foundation)


** Tasks
[] Spring Cache
[x] Spring 
[x] DB: role table + link to login (or customer); seed one admin with a real BCrypt hash.
[x] Backend: Spring Security, login endpoint, password check, role on principal.
[x] Backend: admin POST to create customer + login + roles (transactional).
[x] Frontend: login page → store session/JWT per your choice → HTTP interceptor.
[x] Frontend: admin registration page + guard.


** Learning Objectives (Old)
[x] DTOs
[x] Spring Security + roles + JWT (or session).
[x] Docker Compose for microservices
[x] Split auth vs accounts as two services when Phase 1–4 feel boring.
[x] Micro-frontend last: shell + one small remote for “admin panel.”
[] Internationalization and Localization
[] Packaging NPM, Maven
[] Angular login, guard, interceptor, role-based menu/routes.
[] OpenAPI for account/customer read APIs.
[] Pagination 



