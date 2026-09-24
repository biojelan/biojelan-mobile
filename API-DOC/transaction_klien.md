# **Transaction Klien to Agen - API Documentation**

---

Base URL: `http://biojelan.id`

Transaksi ini digunakan untuk proses penjualan minyak dari Klien kepada pihak Agen. Dalam aplikasi, Agen adalah yang membuat transaksi dan mengambil minyak dari Klien dan membayarkan sejumlah uang ke Klien sesuai dengan volume yang diberikan.

## Endpoint Klien
| Method | Endpoint                                                | Description                     | User Authorized |
| ------ | ------------------------------------------------------- | ------------------------------- | --------------- |
| GET    | `/api/klien/transactions?status=PENDING`                | Klien get transaction status    | Klien           |
| POST   | `/api/klien/transaction/{transaction_id}/accept`        | Klien accept transaction        | Klien           |
| POST   | `/api/klien/transaction/{transaction_id}/reject`        | Klien reject transaction        | Klien           |
| GET    | `/api/klien/transactions`                               | Klien get all transaction       | Klien           |
| POST   | `/api/klien/transaction/{transaction_id}/cancel-accept` | Klien accept cancel transaction | Klien           |
| POST   | `/api/klien/transaction/{transaction_id}/cancel-reject` | Klien reject cancel transaction | Klien           |

## Endpoint Agen
| Method | Endpoint                                                | Description                     | User Authorized |
| ------ | ------------------------------------------------------- | ------------------------------- | --------------- |
| POST   | `/api/agen/transaction`                                 | Agen create new transaction     | Agen            |
| GET    | `/api/agen/klien/transactions`                          | Agen get all klien transaction  | Agen            |
| GET    | `/api/agen/check-klien-email-phone`                     | Agen check klien email or phone | Agen            |
| POST   | `/api/agen/transaction/{transaction_id}/cancel`         | Agen request cancel transaction | Agen            |


---

# Bagian Klien
## **1. Klien Get Transaction Status**
Endpoint: `/api/klien/transactions?status=PENDING`
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
Status lain yang dapat digunakan:
```text
PENDING
ACCEPTED
REJECTED
CANCEL_REQUESTED
CANCELLED
```

---

## **2. Klien Accept Transaction**
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
---

## **3. Klien Reject Transaction**
Endpoint: `/api/klien/transaction/{transaction_id}/reject`
Example Endpoint: `/api/klien/transaction/trx-klien-001/reject`
Method: `POST`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "REJECTED"
    },
    "message": "Success reject transaction!"
}
```

---

## **4. Klien Get All Transaction**
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

---

## **5. Klien Accept Cancel Transaction**
Endpoint: `/api/klien/transaction/{transaction_id}/cancel-accept`
Example Endpoint: `/api/klien/transaction/trx-klien-001/cancel-accept`
Method: `POST`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "CANCELLED"
    },
    "message": "Success accept cancel transaction!"
}
```

---

## **6. Klien Reject Cancel Transaction**
Endpoint: `/api/klien/transaction/{transaction_id}/cancel-reject`
Example Endpoint: `/api/klien/transaction/trx-klien-001/cancel-reject`
Method: `POST`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "ACCEPTED"
    },
    "message": "Success reject cancel transaction!"
}
```
---
# Bagian Agen

## **7. Agen Create Transaction**
Endpoint: `/api/agen/transaction`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
*// opsi pilih email atau nomor hp klien*
{
    "klien_email": "nama.klien@example.com",
    "volume_liter": 2,
}
```
```json
{
    "klien_phone": "081234567890",
    "volume_liter": 2,
}
```
```json
*// opsi untuk klien baru, jika klien belum terdaftar di sistem*
{
    "klien_email": "guest.klien@gmail.com",
    "volume_liter": 2,
}
```
```json
*// request dengan tambahan note (opsional)*
{
    "klien_phone": "081234567890",
    "volume_liter": 2,
    "transaction_note": "Bismillah bisa"
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

---

## **8. Agen Get All Transaction**
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

---

## **9. Agen Check Klien Email or Phone**
Endpoint: `/api/agen/check-klien-email-phone`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
*// bisa pilih email atau nomor hp klien*
{
    "klien_email": "klien1@gmail.com",
    "klien_phone": "081234567890"
}
```
Response Body - Success:
```json
{
    "data": {
        "is_exist": true,
        "klien_id": "klien-001",
        "klien_name": "Nama Klien",
        "klien_email": "klien1@gmail.com",
        "klien_phone": "081234567890"
    },
    "message": "Success check klien email or phone!"
}
```

---

## **10. Agen Request Cancel Transaction**
Endpoint: `/api/agen/transaction/{transaction_id}/cancel`
Example Endpoint: `/api/agen/transaction/trx-klien-001/cancel`
Method: `POST`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-klien-001",
        "status": "PENDING"
    },
    "message": "Success request cancel transaction!"
}
```

## **Catatan**
* Hanya Agen yang dapat membuat transaksi.
* Hanya Klien yang dapat menerima atau menolak transaksi.
* Agen hanya dapat mengajukan pembatalan jika transaksi masih `PENDING` atau `ACCEPTED`.
* Klien hanya dapat menerima atau menolak pembatalan ketika status transaksi adalah `CANCEL_REQUESTED`.
* Backend harus memvalidasi kepemilikan transaksi berdasarkan `agen_id` dan `klien_id`.
* Harga dan `total_price` harus dihitung oleh backend.
* `transaction_id` digunakan di URL, bukan di request body.
* Untuk agen get all transaction itu hanya berlaku untuk agen itu sendiri.
