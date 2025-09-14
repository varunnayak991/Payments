# Invoice Payment Sample Application

### About
A Sample Invoice generator and payment aggregator with support for over-due days and late penalties.

* Invoice APIs for Create Read Update and Deletion of invoices.
* Payment APIs for making payments against Invoices.
* Overdue APIs for consolidating all overdue payments and moving validity, applying penalties

### How To
To build and run the application using docker compose run the command

A Prebuilt Image is deployed to Docker Hub at

[varunnayak1991/payments-app](https://hub.docker.com/repository/docker/varunnayak1991/payments-app/)

Pull the image and run or build the image locally using

```markdown
docker-compose up --build
```

To Test the APIs

* [Swagger](http://localhost:8080/swagger-ui/index.html)
*  [Open-API specs](http://localhost:8080/v3/api-docs)
*  curl

### Basic API Operations

Following example commands demonstrate the API workflows

* To Create an Invoice: 

```markdown
curl -X 'POST' \
'http://localhost:8080/invoice' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"amount": 100,
"due_date": "2025-09-12"
}'
```

* To Get all Invoices

```markdown
curl -X 'GET' \
  'http://localhost:8080/invoice' \
  -H 'accept: */*'
```

* To Make a payment against an Invoice

```markdown
curl -X 'POST' \
'http://localhost:8080/invoice/796edaf9-0ae3-4de1-a5a6-db06c0575ebf/payments' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"amount": 100
}'
```
* To process all overdue invoices

```markdown
curl -X 'GET' \
'http://localhost:8080/invoice/process-overdue' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"late_fee": 50,
"overdue_days": 10
}'
```
