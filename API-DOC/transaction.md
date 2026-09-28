# Transaction Klien - API Documentation

---

Base URL: `http://biojelan.id`

Method - Endpoint : Description
1. `POST /api/agen-transaction` : Agen create new transaction 
2. `GET /api/klien-transaction/status` : Klien get transaction status
3. `POST /api/klien-transaction/accept` : Klien accept transaction 
4. `POST /api/klien-transaction/cancel` : Klien cancel transaction
5. `GET /api/agen-transactions` : Agen get all transaction
6. `GET /api/klien-transactions` : Klien get all transaction


---

### 1. Agen Create Transaction

Endpoint: `/api/agen-transaction`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "agen_id": "agen1",
    "klien_id": "klien1",
    "name": "Nama Klien",
    "volume_liter": 2,
    "price": 6500,
    "total_price": 13000,
}
```

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "xxx",
        "agen_id": "xxx",
        "klien_id": "klien1",
        "name": "Nama Klien",
        "volume_liter": 2,
        "price": 6500,
        "total_price": 13000,
        "status": "pending",
        "created_at": "2024-06-01T12:00:00Z",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success create transaction!"
}
```

### 2. Klien Get Transaction Status

Endpoint: `GET /api/klien-transaction/status`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "xxx",
        "agen_id": "xxx",
        "klien_id": "klien1",
        "name": "Nama Klien",
        "volume_liter": 2,
        "price": 6500,
        "total_price": 13000,
        "status": "pending",
        "created_at": "2024-06-01T12:00:00Z",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success get transaction status!"
}
```


### 3. Klien Accept Transaction

Endpoint: `/api/klien-transaction/accept`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "transaction_id": "xxx",
}
```

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "xxx",
        "status": "accepted",
    },
    "message": "Success accept transaction!"
}
```


### 4. Klien Cancel Transaction

Endpoint: `POST /api/klien-transaction/cancel`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "transaction_id": "xxx",
}
```

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "xxx",
        "status": "cancelled",
    },
    "message": "Success cancel transaction!"
}
```

5. Agen Get All Transaction

Endpoint: `GET /api/agen-transactions`
Method: `GET`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "agen_id": "agen1",
}
```

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "xxx",
            "agen_id": "agen1",
            "klien_id": "klien1",
            "klien_name": "Nama Klien",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "pending",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        },
        {
            "transaction_id": "yyy",
            "agen_id": "agen1",
            "klien_id": "klien2",
            "klien_name": "Nama Klien 2",
            "volume_liter": 3,
            "price": 7000,
            "total_price": 21000,
            "status": "accepted",
            "created_at": "2024-06-02T14:30:00Z",
            "updated_at": "2024-06-02T14:30:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```

6. Klien Get All Transaction

Endpoint: `GET /api/klien-transactions`
Method: `GET`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "klien_id": "klien1",
}
```

Response Body - Success:
```json
{
    "data": [
        {
            "transaction_id": "xxx",
            "agen_id": "agen1",
            "klien_id": "klien1",
            "agen_name": "Nama Agen 1",
            "volume_liter": 2,
            "price": 6500,
            "total_price": 13000,
            "status": "pending",
            "created_at": "2024-06-01T12:00:00Z",
            "updated_at": "2024-06-01T12:00:00Z"
        }
    ],
    "message": "Success get all transactions!"
}
```


