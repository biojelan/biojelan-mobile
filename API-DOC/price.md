# Price - API Documentation

---

Base URL: `http://biojelan.id`

| Method | Endpoint       | Description                  |
|--------|----------------|------------------------------|
| GET    | `/api/price`   | Get daily price per liter    |
| POST   | `/api/price`   | Kilang update price per liter    |

---

1. Get Daily Price

Method: `GET`
Endpoint: `/api/price`


Response Body - Success:
```json
{
    "data": {
        "price_id": "price-001",
        "price_per_liter": 6500,
        "price_type": "KLIEN",
        "start_date": "2024-06-01",
        "end_date": null,
    },
    "message": "Success get daily price!"
}
```

2. Kilang Update Price

Method: `POST`
Endpoint: `/api/price`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "price_per_liter": 7000,
    "price_type": "KLIEN"
}
```

Response Body - Success:
```json
{
    "data": {
        "price_id": "price-002",
        "price_per_liter": 7000,
        "price_type": "KLIEN",
        "start_date": "2024-06-02",
        "end_date": null,
    },
    "message": "Success update daily price!"
}
```

