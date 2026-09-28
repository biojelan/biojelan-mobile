# Authentication - API Documentation

---

Base URL: `http://biojelan.id`

| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| POST   | /api/register                 | Register new user            |
| POST   | /api/login                    | Login user                   |
| DELETE | /api/logout                   | Logout user                  |
| PATCH  | /api/update-password          | Update user password         |
| POST   | /api/forgot-password          | Forgot user password         |
| POST   | /api/kilang-login             | Login kilang dashboard       |

---

## 1. Register
| POST   | `/api/register`                 | Register new user            |
| POST   | `/api/login`                    | Login user                   |
| DELETE | `/api/logout`                   | Logout user                  |
| PATCH  | `/api/update-password`          | Update user password         |
| POST   | `/api/forgot-password`          | Forgot user password         |
| POST   | `/api/kilang-login`             | Login kilang dashboard       |

---

### 1. Register

Method: `POST`
Endpoint: `/api/register`

Request Body:
```json
{
    "name": "Syuhada Rantisi",
    "email": "oda@mail.com",
    "password": "password123",
    "password_confirmation": "password123"
}
```

Response Body - Success:
```json
{
    "success": true,
    "data": {
        "token": "<token>",
        "name": "Syuhada Rantisi",
        "email": "oda@mail.com"
    },
    "message": "Success create user!"
}
```

Response Body - Error:
```json
// invalid password
{
    "data": [],
    "message": "Failed create user! Invalid password."
}
// email already exists
{
    "data": [],
    "data": {},
    "message": "Failed create user! Invalid password."
}
```

```json
// email already exists
{
    "data": {},
    "message": "Failed create user! Email already exists."
}
```

---

## 2. Login
### 2. Login

Method: `POST`
Endpoint: `/api/login`

Request Body:
```json
{
"email": "test@example.com",
"password": "test123"
}
```

Response Body - Success:
```json
{
    "success": true,
    "data": {
        "token": "<token>",
        "name": "Test User",
        "email": "test@example.com"
    },
    "message": "Success login!"
}
```

Response Body - Error:
```json
// user not registered
{
    "data": [],
    "message": "Failed login! User not registered."
}
// if wrong password
{
    "data": [],
    "data": {},
    "message": "Failed login! User not registered."
}
```

```json
// if wrong password
{
    "data": {},
    "message": "Failed login! Wrong password."
}
```

---

## 3. Logout
### 3. Logout

Method: `DELETE`
Endpoint: `/api/logout`
Authorization: `Bearer <token>`

Response Body - Success:
```json
{
    "data": {},
    "message": "Success logout!"
}
```

Response Body - Error:
```json
// if user not authorized
{
    "data": [],
    "data": {},
    "message": "Failed logout! User unauthorized."
}
```

---

## 4. Update Password
### 4. Update Password

Method: `PATCH`
Endpoint: `/api/update-password`
Authorization: `Bearer <token>`

Request Body:
```json
{
  "password": "test123",
  "new_password": "123test",
  "new_password_confirmed": "123test"
  "password_confirmation": "123test"
}
```

Response Body - Success:
```json
{
	"data": {},
	"message": "Success update password!"
}
```

Response Body - Error:
```json
{
    "data": {},
    "message": "Failed update password! Wrong password."
}
```

Response Body - Error:
```json
{
    "data": {},
    "message": "Failed update password! Invalid confirmed password."
    "message": "Failed update password! Invalid password confirmation."
}
```

Response Body - Error:
```json
{
    "data": {},
    "message": "Failed update password! User unauthorized."
}
```


## 5. Forgot Password

### 5. Forgot Password

Method: `POST`
Endpoint: `/api/forgot-password`

Request Body:
```json
{
  "email": "test@mail.com"
}
```

Response Body - Success:
```json
{
	"data": {},
	"message": "Success send link reset password to email!"
}
```

Response Body - Error:
```json
{
	"data": {},
	"message": "Failed send link reset password! Email not found."
}
```


---

## 6. Kilang Login
### 6. Kilang Login (Web Dashboard)


Method: `POST`
Endpoint: `/api/kilang-login`

Request Body:
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

Response Body - Success:
```json
{
	"success": true,
	"data": {
		"token": "<token>",
		"name": "Admin User",
		"email": "admin@example.com"
	},
	"message": "Success login!"
}
```

Response Body - Error:
```json
// user not registered
{
    "data": [],
    "message": "Failed login! User not registered."
}
// if wrong password
{
    "data": [],
    "message": "Failed login! Wrong password."
}
// if not admin
{
    "data": [],
    "data": {},
    "message": "Failed login! User not registered."
}
```

```json
// if wrong password
{
    "data": {},
    "message": "Failed login! Wrong password."
}
```

```json
// if not admin
{
    "data": {},
    "message": "Failed login! User unauthorized."
}

```

