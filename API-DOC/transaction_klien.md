# Transaction Klien - API Documentation

---

Base URL: `http://biojelan.id`

| Method | Endpoint | Description | User Authorized |
|--------|----------|-------------|-----------------|
| POST   | `/api/agen/transaction` | Agen create new transaction  | Agen |
| GET    | `/api/klien/transaction/status` | Klien get transaction status | Klien |
| POST   | `/api/klien/transaction/accept` | Klien accept transaction  | Klien |
| POST   | `/api/klien/transaction/cancel` | Klien cancel transaction | Klien |
| GET    | `/api/agen/klien/transactions` | Agen get all klien transaction | Agen |
| GET    | `/api/klien/transactions` | Klien get all transaction | Klien |


---

### 1. Agen Create Transaction

Endpoint: `/api/agen/transaction`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "klien_email": "nama.klien@example.com",
    "volume_liter": 2,
}
```

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "agen_id": "agen-001",
        "klien_id": "klien-001",
        "klien_name": "Nama Klien",
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

### 2. Klien Get Transaction Status

Endpoint: `/api/klien/transaction?status=PENDING`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-klien-001",
            "agen_id": "agen-001",
            "klien_id": "klien-001",
            "klien_name": "Nama Klien",
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


### 3. Klien Accept Transaction

Endpoint: `/api/klien/transaction/{transaction_id}/accept`
Example Endpoint: `/api/klien/transaction/trx-klien-001/accept`
Method: `POST`
Authorization: `Bearer <token>`


Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "ACCEPTED"
    },
    "message": "Success accept transaction!"
}
```


### 4. Klien Cancel Transaction

Endpoint: `/api/klien/transaction/{transaction_id}/cancel`
Example Endpoint: `/api/klien/transaction/trx-klien-001/cancel`
Method: `POST`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "CANCELLED"
    },
    "message": "Success cancel transaction!"
}
```

### 5. Agen Get All Transaction

Endpoint: `/api/agen/klien/transactions`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-klien-001",
            "agen_id": "agen-001",
            "klien_id": "klien-001",
            "klien_name": "Nama Klien",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-klien-002",
            "agen_id": "agen-001",
            "klien_id": "klien-002",
            "klien_name": "Nama Klien 2",
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

### 6. Klien Get All Transaction

Endpoint: `/api/klien/transactions`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-klien-001",
            "agen_id": "agen-001",
            "klien_id": "klien-001",
            "agen_name": "Nama Agen 1",
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
