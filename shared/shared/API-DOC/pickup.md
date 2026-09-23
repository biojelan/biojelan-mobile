# Pickup Driver - API Documentation


---

Base URL: `http://biojelan.id`

| Method | Endpoint | Description | User Authorized |
|--------|----------|-------------|-----------------|
| POST   | `/api/kilang/pickup/status` | Kilang create assigned driver pickup | Kilang |
| PATCH   | `/api/driver/pickup/status` | Driver update pickup status | Driver |
| GET   | `/api/agen/pickup/status` | Agen get driver pickup status | Agen |
| GET   | `/api/driver/pickup/status` | Driver get pickup status | Driver |


---
### 1. Kilang Create Assigned Driver Pickup 
### (Web Dashboard)

Endpoint: `/api/kilang/pickup/status`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "driver_id": "driver-001",
    "agen_id": "agen-001",
    "date": "2024-06-01",
    "status": "ASSIGNED"
}
```

Response Body - Success:
```json
{
    "data": {
        "pickup_id": "pkp-001",
        "driver_id": "driver-001",
        "agen_id": "agen-001",
        "date": "2024-06-01",
        "status": "ASSIGNED",
        "created_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success created assigned driver pickup!"
}
```

---
### 2. Driver Update Pickup Status

Endpoint: `/api/driver/pickup/status`
Method: `PATCH`
Authorization: `Bearer <token>`

Request Body:
```json
// status: ASSIGNED, OTW, COMPLETED, CANCELLED
{
    "pickup_id": "pkp-001",
    "status": "OTW"
}
```

Response Body - Success:
```json
{
    "data": {
        "pickup_id": "pkp-001",
        "status": "OTW",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success update pickup status!"
}
```

---
### 3. Agen Get Driver Pickup Status

Endpoint: `/api/agen/pickup/status`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "pickup_id": "pkp-001",
        "status": "OTW",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success get pickup status!"
}
```

---
### 4. Driver Get Pickup Status

Endpoint: `/api/driver/pickup/status`
Method: `GET`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "pickup_id": "pkp-001",
        "status": "OTW",
        "updated_at": "2024-06-01T12:00:00Z"
    },
    "message": "Success get pickup status!"
}
```
