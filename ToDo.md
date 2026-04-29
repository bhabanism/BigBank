
[] Microservices

[x] 1-2 demo micro frontend in angular

Next step in detail (recommended order)
Step 1 — Backend: Spring Security + login (foundation)

Add spring-boot-starter-security.
Define SecurityFilterChain: permit /api/auth/login (and static/actuator as you wish), require authentication for /api/** (or at least for /api/admin/**).
Implement UserDetailsService using LoginInfoRepository.findByUsername, map LoginRole → GrantedAuthority (ROLE_ADMIN, ROLE_CUSTOMER, …).
Add POST /api/auth/login accepting JSON (username + password), validate with PasswordEncoder, return JWT or session cookie (choose one and stick to it).
Test with curl/Postman: login as admin → get token/session → call a dummy GET /api/admin/ping.
Why first: Without this, an admin registration endpoint is either open (unsafe) or you block everything and cannot test. Login must exist before guarded admin registration.

Step 2 — Backend: admin registration

Add AdminRegisterRequest (and maybe AdminRegisterResponse) DTOs with jakarta.validation.
Add AdminService.register(...): validate email/username not used → Customer → LoginInfo (BCrypt) → LoginRole for e.g. ROLE_CUSTOMER.
Add POST /api/admin/users with @PreAuthorize("hasRole('ADMIN')") (or equivalent).
Test: admin token → POST creates user; customer token → 403.
Step 3 — Angular

Login UI → store JWT (or rely on cookies for session).
Admin registration component (reactive form) → POST /api/admin/users with Authorization header.
Guard admin routes.


** Tasks

[] DB: role table + link to login (or customer); seed one admin with a real BCrypt hash.
[] Backend: Spring Security, login endpoint, password check, role on principal.
[] Backend: admin POST to create customer + login + roles (transactional).
[] Frontend: login page → store session/JWT per your choice → HTTP interceptor.
[] Frontend: admin registration page + guard.


** Learning Objectives
[x] DTOs
[x] Spring Security + roles + JWT (or session).
[] Caching on safe reads + eviction on transfer.
[] Docker Compose for app + Postgres (+ Redis if you cache).
[] Split auth vs accounts as two services when Phase 1–4 feel boring.
[] Micro-frontend last: shell + one small remote for “admin panel.”
[] Internationalization and Localization
[] Packaging NPM, Maven
[] Angular login, guard, interceptor, role-based menu/routes.
[] OpenAPI for account/customer read APIs.
[] Pagination 

** New and Priority Topics
- Microservices
- Redis
- Spring Cloud
- Docker & Kubernetes
- Kafka

