# Agent-to-Driver Transaction - API Documentation

---

Base URL: `http://biojelan.id`
This transaction is used for selling oil from an Agent to a Refinery. In the application, a Driver represents the Refinery, creates the transaction, and collects oil from the Agent.

## Endpoint Driver
| Method | Endpoint                                               | Description                               | User Authorized |
| ------ | ------------------------------------------------------ | ----------------------------------------- | --------------- |
| POST   | `/api/driver/transaction`                              | Driver creates a new transaction             | Driver          |
| GET    | `/api/driver/transactions`                             | Driver gets all transactions          | Driver          |
| POST   | `/api/driver/transaction/{transaction_id}/cancel`      | Driver requests transaction cancellation    | Driver          |
## Endpoint Agent
| Method | Endpoint                                               | Description                               | User Authorized |
| ------ | ------------------------------------------------------ | ----------------------------------------- | --------------- |
| GET    | `/api/agent/transactions?status=PENDING`                | Agent gets transactions by status | Agent            |
| POST   | `/api/agent/transaction/{transaction_id}/accept`        | Agent accepts a transaction                   | Agent            |
| POST   | `/api/agent/transaction/{transaction_id}/reject`        | Agent rejects a transaction                    | Agent            |
| GET    | `/api/agent/driver/transactions`                        | Agent gets all transactions with drivers  | Agent            |
| POST   | `/api/agent/transaction/{transaction_id}/cancel-accept` | Agent accepts cancellation                | Agent            |
| POST   | `/api/agent/transaction/{transaction_id}/cancel-reject` | Agent rejects cancellation                   | Agent            |

---

# Section Driver
## **1. Driver Create Transaction**
Endpoint: `/api/driver/transaction`
Method: `POST`
Authorization: `Bearer <token>`
Request Body:
```json
{
    "agen_email": "agent.name@example.com",
    "volume_liter": 2,
    "transaction_note": "The oil will be picked up this afternoon"
}
```
Alternative using a phone number:
```json
{
    "agen_phone": "081234567890",
    "volume_liter": 2
}
```
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "driver_id": "driver-001",
        "agen_id": "agent-001",
        "agen_name": "Agent Name",
        "volume_liter": 2,
        "price": 8000,
        "total_price": 16000,
        "status": "PENDING",
        "transaction_note": "The oil will be picked up this afternoon",
        "created_at": "2024-06-01T12:00:00Z",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success create transaction!"
}
```
---
## **2. Driver Get All Transaction**
Endpoint: `/api/driver/transactions`
Method: `GET`
Authorization: `Bearer <token>`
Note: displays only the requesting driver own transactions and cannot access other driver transactions.
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-agent-001",
            "driver_id": "driver-001",
            "agen_id": "agent-001",
            "agen_name": "Agent Name",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-agent-002",
            "driver_id": "driver-001",
            "agen_id": "agent-002",
            "agen_name": "Agent Name 2",
            "volume_liter": 3,
            "price": 9000,
            "total_price": 27000,
            "status": "ACCEPTED",
            "created_at": "2024-06-02T12:00:00Z",
            "updated_at": "2024-06-02T12:00:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```
---
## **3. Driver Request Cancel Transaction**
Endpoint: `/api/driver/transaction/{transaction_id}/cancel`
Method: `POST`
Example Endpoint: `/api/driver/transaction/trx-agent-001/cancel`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "status": "CANCEL_REQUESTED",
    },
    "message": "Success request cancel transaction!"
}
```
---
---
# Section Agent
## **4. Agent Get Transaction Status**
Endpoint: `/api/agent/transactions?status=PENDING`
Method: `GET`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-agent-001",
            "driver_id": "driver-001",
            "agen_id": "agent-001",
            "agen_name": "Agent Name",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        }
    ],
    "message": "Success get pending transactions!"
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
## **5. Agent Accept Transaction**
Endpoint: `/api/agent/transaction/{transaction_id}/accept`
Method: `POST`
Example Endpoint: `/api/agent/transaction/trx-agent-001/accept`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "status": "ACCEPTED",
    },
    "message": "Success accept transaction!"
}
```
---
## **6. Agent Reject Transaction**
Endpoint: `/api/agent/transaction/{transaction_id}/reject`
Method: `POST`
Example Endpoint: `/api/agent/transaction/trx-agent-001/reject`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "status": "REJECTED",
    },
    "message": "Success reject transaction!"
}
```
---
## **7. Agent Get All Transaction**
Endpoint: `/api/agent/driver/transactions`
Method: `GET`
Authorization: `Bearer <token>`
Note: displays all current agent transactions with drivers and cannot access other agent transactions.
Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-agent-001",
            "driver_id": "driver-001",
            "agen_id": "agent-001",
            "agen_name": "Agent Name",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-agent-002",
            "driver_id": "driver-002",
            "agen_id": "agent-001",
            "agen_name": "Agent Name 2",
            "volume_liter": 3,
            "price": 9000,
            "total_price": 27000,
            "status": "ACCEPTED",
            "created_at": "2024-06-02T12:00:00Z",
            "updated_at": "2024-06-02T12:00:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```
---
## **8. Agent Accept Cancel Transaction**
Endpoint: `/api/agent/transaction/{transaction_id}/cancel-accept`
Method: `POST`
Example Endpoint: `/api/agent/transaction/trx-agent-001/cancel-accept`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "status": "CANCELLED",
    },
    "message": "Success accept cancel transaction!"
}
```
---
## **9. Agent Reject Cancel Transaction**
Endpoint: `/api/agent/transaction/{transaction_id}/cancel-reject`
Method: `POST`
Example Endpoint: `/api/agent/transaction/trx-agent-001/cancel-reject`
Authorization: `Bearer <token>`
Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agent-001",
        "status": "ACCEPTED",
    },
    "message": "Success reject cancel transaction!"
}
```
## **Notes**
* Only Drivers can create transactions.
* Only the target Agent can accept or reject a transaction.
* Drivers can request cancellation only when the transaction is still `PENDING` or `ACCEPTED`.
* Agents can accept or reject cancellation only when the transaction status is `CANCEL_REQUESTED`.
* The backend must validate transaction ownership based on `driver_id` and `agent_id`.
* The backend must calculate `price` and `total_price`.
* `transaction_id` is used in the URL, not in the request body.
* The get all transactions endpoint applies only to the requesting driver.
