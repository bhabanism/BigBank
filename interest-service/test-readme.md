# Test 1 — Internal PATCH only (banking, no JMS)
Confirms banking writes balance before the full pipeline.

Field	Value
Method
PATCH
URL
http://localhost:8083/api/internal/accounts/ACC10001/interest
Headers
Content-Type: application/json
X-Internal-Api-Key: dev-internal-key-change-me (optional today; /api/internal/** is permitAll)
Body (raw JSON):

{
  "jobId": "postman-test-1",
  "interestAmount": 10.0
}
Expected: 200 with previousBalance, newBalance, accountNumber, jobId.

Use a real Active account number from your DB (ACC10001, ACC20001 in seed data). Closed / Frozen → 409.

# Test 2 — Login (JWT for accrual + list accounts)
Field	Value
Method
POST
URL
http://localhost:8080/api/auth/login
Body
{"username":"admin","password":"<your-admin-password>"}
Expected: 200 JSON like:

{
  "accessToken": "eyJ...",
  "tokenType": "Bearer"
}
Copy accessToken. In Postman: Authorization → Bearer Token → paste token (no Bearer prefix if Postman adds it automatically).

If login fails, use the admin password you seeded (not the placeholder in dbdata.sql unless you loaded that hash).

# Test 3 — List accounts (before)
Field	Value
Method
GET
URL
http://localhost:8080/api/accounts
Auth
Bearer token from Test 2
Note balances for Active accounts (e.g. ACC10001, ACC20001). Producer skips non-Active.

Direct banking (same auth): http://localhost:8083/api/accounts

# Test 4 — Full pipeline (JMS + interest-service → banking)
Field	Value
Method
POST
URL
http://localhost:8080/api/accounts/accrual/run
Auth
Bearer token
Body
none
Expected: 202 Accepted:

{
  "jobId": "550e8400-...",
  "status": "STARTED"
}
What happens:

Banking reads active accounts → sends messages to interest.accrual.queue
interest-service consumes → calculates interest → PATCH banking internal API
Banking updates DB + evicts accounts cache
Wait a few seconds, then Test 5.

Optional checks:

ActiveMQ: http://localhost:8161 → Queues → interest.accrual.queue (should drain to 0)
Logs: docker compose logs interest-service / banking-service
# Test 5 — List accounts (after)
Same as Test 3: GET http://localhost:8080/api/accounts

Expected: Active account balances increased (e.g. Savings ~2%, Checking ~0.5%). Money Market / unknown types → 0% in your calculator.

Example: ACC10001 Savings 5250.75 → +~105.02 at 2%.

# Test 6 — Internal PATCH via gateway (optional)
Gateway only routes /api/accounts to banking; internal path may not be proxied unless you added a route. Prefer Test 1 on 8083 for internal calls.

# Postman collection order (summary)
1. PATCH  :8083/api/internal/accounts/{accountNumber}/interest  (smoke test)
2. POST   :8080/api/auth/login
3. GET    :8080/api/accounts          (before)
4. POST   :8080/api/accounts/accrual/run
5. GET    :8080/api/accounts          (after)

# Common failures
Symptom	Likely cause
401 on POST accrual or GET accounts
Missing/invalid Bearer token
401 on login
Wrong username/password
404 on PATCH
Wrong accountNumber
409 on PATCH
Account not Active
202 but balances unchanged
interest-service down; ActiveMQ not connected; listener error — check docker compose logs interest-service
500 on accrual
Banking can’t reach ActiveMQ — check SPRING_ACTIVEMQ_BROKER_URL
Balances unchanged after accrual
interest can’t reach banking — APP_BANKING_BASE_URL=http://banking-service:8083 in compose; BankingAccountClient compile/runtime errors
Only some accounts updated
Only Active accounts; ACC10002 Closed etc. skipped