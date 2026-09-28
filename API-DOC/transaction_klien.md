# **Clients-to-Agent Transaction - API Documentation**

---
Base URL: `http://biojelan.id`
This transaction is used for selling oil from Clients to an Agent. In the application, the Agent creates the transaction, collects oil from Clients, and pays Clients based on the volume provided.

## Endpoint Clients
| Method | Endpoint                                                | Description                     | User Authorized |
| ------ | ------------------------------------------------------- | ------------------------------- | --------------- |
| GET    | `/api/client/transactions?status=PENDING`                | Clients get transaction status    | Clients           |
| POST   | `/api/client/transaction/{transaction_id}/accept`        | Clients accept a transaction        | Clients           |
| POST   | `/api/client/transaction/{transaction_id}/reject`        | Clients reject a transaction        | Clients           |
| GET    | `/api/client/transactions`                               | Clients get all transactions       | Clients           |
| POST   | `/api/client/transaction/{transaction_id}/cancel-accept` | Clients accept cancellation | Clients           |
| POST   | `/api/client/transaction/{transaction_id}/cancel-reject` | Clients reject cancellation | Clients           |
## Endpoint Agent
| Method | Endpoint                                                | Description                     | User Authorized |
| ------ | ------------------------------------------------------- | ------------------------------- | --------------- |
| POST   | `/api/agent/transaction`                                 | Agent creates a new transaction     | Agent            |
| GET    | `/api/agent/clients/transactions`                          | Agent gets all clients transactions  | Agent            |
| POST    | `/api/agent/check-clients-email`                     | Agent checks clients email  | Agent            |
| POST    | `/api/agent/check-clients-phone`                     | Agent checks clients phone | Agent            |
| POST   | `/api/agent/transaction/{transaction_id}/cancel`         | Agent requests transaction cancellation | Agent            |
---

# Section Clients
## **1. Clients Get Transaction Status**
Endpoint: `/api/client/transactions?status=PENDING`
Method: `GET`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-clients-001",
            "agen_id": "agent-001",
            "client_id": "clients-001",
            "client_name": "Clients Name",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
    ],
    "message": "Success get transaction status pending!"
}
```
Other available statuses:
```text
PENDING
ACCEPTED
REJECTED
CANCEL_REQUESTED
CANCELLED
```
---
## **2. Clients Accept Transaction**
Endpoint: `/api/client/transaction/{transaction_id}/accept`
Example Endpoint: `/api/client/transaction/trx-clients-001/accept`
Method: `POST`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "status": "ACCEPTED"
    },
    "message": "Success accept transaction!"
}
```
---
## **3. Clients Reject Transaction**
Endpoint: `/api/client/transaction/{transaction_id}/reject`
Example Endpoint: `/api/client/transaction/trx-clients-001/reject`
Method: `POST`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "status": "REJECTED"
    },
    "message": "Success reject transaction!"
}
```
---
## **4. Clients Get All Transaction**
Endpoint: `/api/client/transactions`
Method: `GET`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-clients-001",
            "agen_id": "agent-001",
            "client_id": "clients-001",
            "agen_name": "Agent Name 1",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```
---
## **5. Clients Accept Cancel Transaction**
Endpoint: `/api/client/transaction/{transaction_id}/cancel-accept`
Example Endpoint: `/api/client/transaction/trx-clients-001/cancel-accept`
Method: `POST`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "status": "CANCELLED"
    },
    "message": "Success accept cancel transaction!"
}
```
---
## **6. Clients Reject Cancel Transaction**
Endpoint: `/api/client/transaction/{transaction_id}/cancel-reject`
Example Endpoint: `/api/client/transaction/trx-clients-001/cancel-reject`
Method: `POST`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "status": "ACCEPTED"
    },
    "message": "Success reject cancel transaction!"
}
```
---
# Section Agent
## **7. Agent Create Transaction**
Endpoint: `/api/agent/transaction`
Method: `POST`
Authorization: `Bearer <token>`
Request Body:
```json
*// choose either the clients email or phone number*
{
    "client_email": "clients.name@example.com",
    "volume_liter": 2,
}
```
```json
{
    "client_phone": "081234567890",
    "volume_liter": 2,
}
```
```json
*// option for new clients who are not yet registered in the system*
{
    "client_email": "guest.client@biojelan.id",
    "volume_liter": 2,
}
```
```json
*// request with an additional optional note*
{
    "client_phone": "081234567890",
    "volume_liter": 2,
    "transaction_note": "It can be done with God's blessing"
}
```
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "agen_id": "agent-001",
        "client_id": "clients-001",
        "client_name": "Clients Name",
        "volume_liter": 2,
        "price": 6500,
        "total_price": 13000,
        "status": "PENDING",
        "created_at": "2024-06-01T12:00:00Z",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success create transaction!"
}
```
---
## **8. Agent Get All Transaction**
Endpoint: `/api/agent/clients/transactions`
Method: `GET`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-clients-001",
            "agen_id": "agent-001",
            "client_id": "clients-001",
            "client_name": "Clients Name",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-clients-002",
            "agen_id": "agent-001",
            "client_id": "clients-002",
            "client_name": "Clients Name 2",
            "volume_liter": 3,
            "price": 7000,
            "total_price": 21000,
            "status": "ACCEPTED",
            "created_at": "2024-06-02T14:30:00Z",
            "updated_at": "2024-06-02T14:30:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```
---
## **9. Agent Check Clients Email**
Endpoint: `/api/agent/check-clients-email`
Method: `POST`
Authorization: `Bearer <token>`
Request Body:
```json
*// choose either the clients email or phone number*
{
    "client_email": "client1@gmail.com",
}
```
Response Body - Success:
```json
{
    "data": {
        "is_exist": true,
        "client_id": "clients-001",
        "client_name": "Clients Name",
        "client_email": "client1@gmail.com",
        "client_phone": "081234567890"
    },
    "message": "Success check clients email or phone!"
}
```

## **10. Agent Check Clients Phone**
Endpoint: `/api/agent/check-clients-phone`
Method: `POST`
Authorization: `Bearer <token>`
Request Body:
```json
*// choose either the clients email or phone number*
{
    "client_phone": "081234567890"
}
```
Response Body - Success:
```json
{
    "data": {
        "is_exist": true,
        "client_id": "clients-001",
        "client_name": "Clients Name",
        "client_email": "client1@gmail.com",
        "client_phone": "081234567890"
    },
    "message": "Success check clients email or phone!"
}
```

---
## **11. Agent Request Cancel Transaction**
Endpoint: `/api/agent/transaction/{transaction_id}/cancel`
Example Endpoint: `/api/agent/transaction/trx-clients-001/cancel`
Method: `POST`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-clients-001",
        "status": "CANCEL_REQUESTED"
    },
    "message": "Success request cancel transaction!"
}
```
## **Notes**
* Only Agents can create transactions.
* Only Clients can accept or reject a transaction.
* Agents can request cancellation only when the transaction is still `PENDING` or `ACCEPTED`.
* Clients can accept or reject cancellation only when the transaction status is `CANCEL_REQUESTED`.
* The backend must validate transaction ownership based on `agent_id` and `clients_id`.
* The backend must calculate `price` and `total_price`.
* `transaction_id` is used in the URL, not in the request body.
* The get all transactions endpoint applies only to the requesting agent.
