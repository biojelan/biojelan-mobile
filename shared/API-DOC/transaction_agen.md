# Transaction Agen - API Documentation

---

Base URL: `http://biojelan.id`

| Method | Endpoint | Description | User Authorized |
|--------|----------|-------------|-----------------|
| POST   | `/api/driver/transaction` | Driver create new transaction  | Driver |
| GET    | `/api/agen/transaction/status` | Agen get transaction status | Agen |
| POST   | `/api/agen/transaction/accept` | Agen accept transaction  | Agen |
| POST   | `/api/agen/transaction/cancel` | Agen cancel transaction  | Agen |
| GET    | `/api/driver/transactions` | Driver get all transaction | Driver |
| GET    | `/api/agen/transactions` | Agen get all transaction | Agen |



---

### 1. Driver Create Transaction

Method: `POST`
Endpoint: `/api/driver/transaction`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "agen_email": "nama.agen@example.com",
    "volume_liter": 2,
}
```

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "driver_id": "driver-001",
        "agen_id": "agen-001",
        "agen_name": "Nama Agen",
        "volume_liter": 2,
        "price": 8000,
        "total_price": 16000,
        "status": "PENDING",
        "created_at": "2024-06-01T12:00:00Z",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success create transaction!"
}
```



### 2. Agen Get Transaction Status

Method: `GET`
Endpoint: `/api/agen/transaction?status=PENDING`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-agen-001",
            "driver_id": "driver-001",
            "agen_id": "agen-001",
            "agen_name": "Nama Agen",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
    ],
    "message": "Success get transaction status pending!"
}
```

### 3. Agen Accept Transaction

Method: `POST`
Endpoint: `/api/agen/transaction/{transaction_id}/accept`
Example Endpoint: `/api/agen/transaction/trx-agen-001/accept`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "ACCEPTED",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success accept transaction!"
}
```

### 4. Agen Cancel Transaction

Method: `POST`
Endpoint: `/api/agen/transaction/{transaction_id}/cancel`
Example Endpoint: `/api/agen/transaction/trx-agen-001/cancel`
Authorization: `Bearer <token>`


Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "CANCELLED",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success cancel transaction!"
}
```

### 5. Driver Get All Transaction

Method: `GET`
Endpoint: `/api/driver/transactions`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-driver-001",
            "agen_id": "agen-001",
            "agen_name": "Nama Agen",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-driver-002",
            "agen_id": "agen-002",
            "agen_name": "Nama Agen 2",
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

### 6. Agen Get All Transaction

Method: `GET`
Endpoint: `/api/agen/transactions`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "trx-agen-001",
            "driver_id": "driver-001",
            "agen_id": "agen-001",
            "agen_name": "Nama Agen",
            "volume_liter": 2,
            "price": 8000,
            "total_price": 16000,
            "status": "PENDING",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "trx-agen-002",
            "driver_id": "driver-002",
            "agen_id": "agen-002",
            "agen_name": "Nama Agen 2",
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
