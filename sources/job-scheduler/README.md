# Job scheduler

## API Swagger Documentation

- [swagger](http://localhost:8080/swagger-ui/index.html)

## Ideas
- between action and output there should be a transformer

## Tasks
- [x] CRUD
- [x] swagger
- [ ] logging (aop + endpoints)
- [ ] unit-test of the endpoint e2e (update endpoint)
- [ ] add endpoint to trigger job manually
- [ ] store next trigger time in the DB and restore on load
- [ ] load schedule
- [ ] impl quote of the day action
- [ ] impl fan-out http-call
- [ ] impl picture of the day
- [ ] impl HTTP diff
- [ ] impl binance order checker
- [ ] secure endpoints with basic auth
- [ ] deploy 2 k8s
- [ ] add oauth with Auth0 & support multiple users