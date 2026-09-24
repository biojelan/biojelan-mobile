# User - API Documentation

---

Base URL: `http://biojelan.id`

| Method | Endpoint       | Description                  |
|--------|----------------|------------------------------|
| GET    | `/api/user`      | Get a specific user            |
| PATCH  | `/api/user`      | Update authenticated user      |
| DELETE | `/api/user`      | Remove authenticated user      |
| GET    | `/api/user/agen` | Get user agen      |

---

### 1. Get User

Method: `GET`
Endpoint: `/api/user`
Authorization: `Bearer <token>`

Response Body - Success: (klien and kilang)
```json
{
    "data": {
        "user_id": "userid123",
        "role_id": 7,
        "name": "Syuhada Rantisi",
        "email": "oda@mail.com",
        "phone": "081319306262",
        "is_verified": true,
        "is_active": true,
        "agen": null
    },
    "message": "Success get user!"
}
```

Response Body - Success: (agen)
```json
{
    "data": {
        "user_id": "userid123",
        "role_id": 7,
        "name": "Syuhada Rantisi",
        "email": "oda@mail.com",
        "phone": "081319306262",
        "is_verified": true,
        "is_active": true,
        "agen": {
            "agen_id": "xxx",
            "address": "Bandarlampung",
            "latitude": 101.111,
            "longitude": 102.222,
            "bank_name": "MANDIRI",
            "account_number": "xxx",
            "open_at": "08:00",
            "close_at": "20:00",
            "open_days": [
                "senin",
                "selasa",
                "rabu",
                "kamis",
                "jumat",
                "sabtu",
                "minggu"
            ],
            "is_open": true,
            "stock_liter": 50
        }
    },
    "message": "Success get user!"
}
```

Response Body - Error:
```json
// user unauthorized
{
    "data": {},
    "message": "Failed get user! User unauthorized."
}
```

---

### 2. Update User

Method: `PATCH`
Endpoint: `/api/user`
Authorization: `Bearer <token>`

Request Body: (klien and kilang)
```json
{
    "name": "Test Update",
    "email": "testupdate@mail.com",
    "phone": "081319306263"
}
```

Request Body: (agen)
```json
{
    "name": "Test Update",
    "email": "testupdate@mail.com",
    "phone": "081319306263",
    "agen": {
        "address": "xxx",
        "latitude": 101.111,
        "longitude": 102.222,
        "bank_name": "MANDIRI",
        "account_number": "xxx",
        "open_at": "08:00",
        "close_at": "20:00",
        "open_days": [
            "senin",
            "selasa",
            "rabu",
            "kamis",
            "jumat",
            "sabtu",
            "minggu"
        ],
        "is_open": true,
    }
}
```

Response Body - Success: (klien and kilang)
```json
{
    "data": {
        "user_id": "userid123",
        "role_id": 7,
        "name": "Test Update",
        "email": "testupdate@mail.com",
        "phone": "081319306263",
        "is_verified": true,
        "is_active": true,
        "agen": null
    },
    "message": "Success update user!"
}
```

Response Body - Success: (agen)
```json
{
    "data": {
        "user_id": "userid123",
        "role_id": 6,
        "name": "Test Update",
        "email": "testupdate@mail.com",
        "phone": "081319306263",
        "is_verified": true,
        "is_active": true,
        "agen": {
            "agen_id": "xxx",
            "address": "xxx",
            "latitude": 101.111,
            "longitude": 102.222,
            "bank_name": "MANDIRI",
            "account_number": "xxx",
            "open_at": "08:00",
            "close_at": "20:00",
            "open_days": [
                "senin",
                "selasa",
                "rabu",
                "kamis",
                "jumat",
                "sabtu",
                "minggu"
            ],
            "is_open": true,
            "stock_liter": 50
        }
    },
    "message": "Success update user!"
}
```

Response Body - Error:
```json
// user unauthorized
{
    "data": {},
    "message": "Failed update user! User unauthorized."
}
```

---

### 3. Delete User

Method: `DELETE`
Endpoint: `/api/user`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {},
    "message": "Success delete user!"
}
```

Response Body - Error:
```json
// user unauthorized
{
    "data": {},
    "message": "Failed delete user! User unauthorized."
}
```

### 4. Get User Agen

Method: `GET`
Endpoint: `/api/user/agen`

Response Body - Success:
```json
{
    "data": [
        {
            "agen_id": "userid111",
            "role_id": 7,
            "name": "Test Agen",
            "phone": "081319306263",
            "address": "xxx",
            "latitude": 101.111,
            "longitude": 102.222,
            "open_at": "08:00",
            "close_at": "20:00",
            "is_open": true,
            "open_days": [
                "senin",
                "selasa",
                "rabu",
                "kamis",
                "jumat",
                "sabtu",
                "minggu"
            ]
        },
        {
            "agen_id": "userid222",
            "role_id": 7,
            "name": "Test Agen",
            "phone": "081319306263",
            "address": "xxx",
            "latitude": 101.111,
            "longitude": 102.222,
            "open_at": "08:00",
            "close_at": "20:00",
            "is_open": true,
            "open_days": [
                "senin",
                "selasa",
                "rabu",
                "kamis",
                "jumat",
                "sabtu",
                "minggu"
            ]
        }
    ],
    "message": "Success get user agen!"
}
```

Response Body - Error:
```json
// user unauthorized
{
    "data": {},
    "message": "Failed get user agen! User unauthorized."
}
```
