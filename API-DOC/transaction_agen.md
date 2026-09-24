# Transaction Agen to Driver - API Documentation

---

Base URL: `http://biojelan.id`

Transaksi ini digunakan untuk proses penjualan minyak dari Agen kepada pihak Kilang. Dalam aplikasi, Driver bertindak sebagai perwakilan Kilang yang membuat transaksi dan mengambil minyak dari Agen.

## Endpoint Driver
| Method | Endpoint                                               | Description                               | User Authorized |
| ------ | ------------------------------------------------------ | ----------------------------------------- | --------------- |
| POST   | `/api/driver/transaction`                              | Driver membuat transaksi baru             | Driver          |
| GET    | `/api/driver/transactions`                             | Driver melihat seluruh transaksi          | Driver          |
| POST   | `/api/driver/transaction/{transaction_id}/cancel`      | Driver mengajukan pembatalan transaksi    | Driver          |

## Endpoint Agen
| Method | Endpoint                                               | Description                               | User Authorized |
| ------ | ------------------------------------------------------ | ----------------------------------------- | --------------- |
| GET    | `/api/agen/transactions?status=PENDING`                | Agen melihat transaksi berdasarkan status | Agen            |
| POST   | `/api/agen/transaction/{transaction_id}/accept`        | Agen menerima transaksi                   | Agen            |
| POST   | `/api/agen/transaction/{transaction_id}/reject`        | Agen menolak transaksi                    | Agen            |
| GET    | `/api/agen/driver/transactions`                        | Agen melihat seluruh transaksi (dengan driver)  | Agen            |
| POST   | `/api/agen/transaction/{transaction_id}/cancel-accept` | Agen menyetujui pembatalan                | Agen            |
| POST   | `/api/agen/transaction/{transaction_id}/cancel-reject` | Agen menolak pembatalan                   | Agen            |


---
# Bagian Driver
## **1. Driver Create Transaction**
Endpoint: `/api/driver/transaction`
Method: `POST`
Authorization: `Bearer <token>`

Request Body:
```json
{
    "agen_email": "nama.agen@example.com",
    "volume_liter": 2,
    "transaction_note": "Minyak akan dijemput sore ini"
}
```
Alternatif menggunakan nomor telepon:
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
        "transaction_id": "trx-agen-001",
        "driver_id": "driver-001",
        "agen_id": "agen-001",
        "agen_name": "Nama Agen",
        "volume_liter": 2,
        "price": 8000,
        "total_price": 16000,
        "status": "PENDING",
        "transaction_note": "Minyak akan dijemput sore ini",
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
Catatan: hanya menampilkan data traksasinya sendiri yang (tidak bisa melihat transaksi driver lain).

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
            "driver_id": "driver-001",
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
---
## **3. Driver Request Cancel Transaction**
Endpoint: `/api/driver/transaction/{transaction_id}/cancel`
Method: `POST`
Example Endpoint: `/api/driver/transaction/trx-agen-001/cancel`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "CANCEL_REQUESTED",
    },
    "message": "Success request cancel transaction!"
}
```
---

---
# Bagian Agen
## **4. Agen Get Transaction Status**
Endpoint: `/api/agen/transactions?status=PENDING`
Method: `GET`
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
        }
    ],
    "message": "Success get pending transactions!"
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

## **5. Agen Accept Transaction**
Endpoint: `/api/agen/transaction/{transaction_id}/accept`
Method: `POST`
Example Endpoint: `/api/agen/transaction/trx-agen-001/accept`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "ACCEPTED",
    },
    "message": "Success accept transaction!"
}
```
---

## **6. Agen Reject Transaction**
Endpoint: `/api/agen/transaction/{transaction_id}/reject`
Method: `POST`
Example Endpoint: `/api/agen/transaction/trx-agen-001/reject`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "REJECTED",
    },
    "message": "Success reject transaction!"
}
```
---

## **7. Agen Get All Transaction**
Endpoint: `/api/agen/driver/transactions`
Method: `GET`
Authorization: `Bearer <token>`
Catatan: menampilkan seluruh transaksi agen yang sekarang dengan driver (tidak bisa melihat transaksi agen lain).

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
            "agen_id": "agen-001",
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
---

## **8. Agen Accept Cancel Transaction**
Endpoint: `/api/agen/transaction/{transaction_id}/cancel-accept`
Method: `POST`
Example Endpoint: `/api/agen/transaction/trx-agen-001/cancel-accept`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "CANCELLED",
    },
    "message": "Success accept cancel transaction!"
}
```
---

## **9. Agen Reject Cancel Transaction**
Endpoint: `/api/agen/transaction/{transaction_id}/cancel-reject`
Method: `POST`
Example Endpoint: `/api/agen/transaction/trx-agen-001/cancel-reject`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {
        "transaction_id": "trx-agen-001",
        "status": "ACCEPTED",
    },
    "message": "Success reject cancel transaction!"
}
```
## **Catatan**
* Hanya Driver yang dapat membuat transaksi.
* Hanya Agen tujuan yang dapat menerima atau menolak transaksi.
* Driver hanya dapat mengajukan pembatalan jika transaksi masih `PENDING` atau `ACCEPTED`.
* Agen hanya dapat menerima atau menolak pembatalan ketika status transaksi adalah `CANCEL_REQUESTED`.
* Backend harus memvalidasi kepemilikan transaksi berdasarkan `driver_id` dan `agen_id`.
* Harga dan `total_price` harus dihitung oleh backend.
* `transaction_id` digunakan di URL, bukan di request body.
* Untuk driver get all transaction itu hanya berlaku untuk driver itu sendiri
