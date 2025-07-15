# API Documentation

---

## Authentication & Authorization

**All protected endpoints require a valid JWT token in the HTTP Authorization header:**

```
Authorization: Bearer <your-jwt-token>
```

- Obtain the token by logging in via `POST /api/auth/login`.
- Include this header in all requests to protected endpoints (all except `/api/auth/login`).
- Example curl usage:
  ```sh
  curl -H "Authorization: Bearer <your-jwt-token>" http://localhost:8080/api/users
  ```

---

# Attachment API Documentation

## Base Path
`/api/attachments`

---

## 1. Create Attachment Metadata

**POST** `/api/attachments`

Creates a new attachment metadata record (does not upload a file).

### Request Body (application/json)
```
{
  "requestId": 123,
  "fileName": "example.pdf",
  "s3Key": "uuid_example.pdf",
  "contentType": "application/pdf"
}
```

### Response (200 OK)
```
{
  "id": 1,
  "requestId": 123,
  "fileName": "example.pdf",
  "s3Key": "uuid_example.pdf",
  "contentType": "application/pdf"
}
```

---

## 2. Upload Attachment File

**POST** `/api/attachments/upload`

Uploads a file to S3 and creates attachment metadata.

### Request (multipart/form-data)
- `file`: The file to upload
- `requestId`: The maintenance request ID

#### Example (curl):
```
curl -X POST \
  -F "file=@/path/to/file.pdf" \
  -F "requestId=123" \
  http://localhost:8080/api/attachments/upload
```

### Response (201 Created)
```
{
  "id": 1,
  "requestId": 123,
  "fileName": "file.pdf",
  "s3Key": "uuid_file.pdf",
  "contentType": "application/pdf"
}
```

### Error Responses
- `400 Bad Request`: File is empty or missing
- `500 Internal Server Error`: Upload failed

---

## 3. Download Attachment File

**GET** `/api/attachments/download/{id}`

Downloads the file from S3 by attachment ID.

### Path Parameter
- `id`: Attachment ID

#### Example (curl):
```
curl -X GET http://localhost:8080/api/attachments/download/1 -o file.pdf
```

### Response (200 OK)
- Content-Type: as stored (e.g., `application/pdf`)
- Content-Disposition: `attachment; filename="file.pdf"`
- Body: File bytes

### Error Responses
- `404 Not Found`: Attachment not found
- `500 Internal Server Error`: Download failed

---

## 4. Delete Attachment

**DELETE** `/api/attachments/{id}`

Deletes an attachment metadata record (does not delete file from S3).

### Path Parameter
- `id`: Attachment ID

### Response (204 No Content)

### Error Responses
- `404 Not Found`: Attachment not found

---

## 5. Get Attachment by ID

**GET** `/api/attachments/{id}`

Fetches attachment metadata by ID.

### Path Parameter
- `id`: Attachment ID

### Response (200 OK)
```
{
  "id": 1,
  "requestId": 123,
  "fileName": "file.pdf",
  "s3Key": "uuid_file.pdf",
  "contentType": "application/pdf"
}
```

### Error Responses
- `404 Not Found`: Attachment not found

---

## 6. Get Attachments by Request ID

**GET** `/api/attachments/by-request?requestId={requestId}`

Fetches all attachments for a given maintenance request.

### Query Parameter
- `requestId`: Maintenance request ID

### Response (200 OK)
```
[
  {
    "id": 1,
    "requestId": 123,
    "fileName": "file.pdf",
    "s3Key": "uuid_file.pdf",
    "contentType": "application/pdf"
  },
  // ... more attachments ...
]
``` 

---

# Auth API Documentation

## Base Path
`/api/auth`

### 1. Login
**POST** `/api/auth/login`

Authenticate user and issue JWT token.

#### Request Body (application/json)
```
{
  "username": "user1",
  "password": "password123"
}
```

#### Response (200 OK)
```
{
  "token": "<jwt-token>",
  "roles": ["EMPLOYEE", "ADMIN"]
}
```

#### Error Responses
- `400 Bad Request`: Invalid credentials or missing fields

---

# Audit Log API Documentation

## Base Path
`/api/audit-logs`

### 1. Create Audit Log
**POST** `/api/audit-logs`

Create a new audit log entry.

#### Request Body (application/json)
```
{
  "userId": 1,
  "action": "LOGIN",
  "timestamp": "2024-05-01T12:00:00Z"
}
```

#### Response (200 OK)
```
{
  "id": 10,
  "userId": 1,
  "action": "LOGIN",
  "timestamp": "2024-05-01T12:00:00Z"
}
```

### 2. Get Audit Logs by User
**GET** `/api/audit-logs/by-user?userId={userId}`

Fetch all audit logs for a user.

#### Response (200 OK)
```
[
  {
    "id": 10,
    "userId": 1,
    "action": "LOGIN",
    "timestamp": "2024-05-01T12:00:00Z"
  }
]
```

---

# Notification API Documentation

## Base Path
`/api/notifications`

### 1. Create Notification
**POST** `/api/notifications`

Create a new notification for a user.

#### Request Body (application/json)
```
{
  "userId": 1,
  "message": "Your request has been approved.",
  "read": false
}
```

#### Response (200 OK)
```
{
  "id": 5,
  "userId": 1,
  "message": "Your request has been approved.",
  "read": false
}
```

### 2. Mark Notification as Read
**POST** `/api/notifications/{id}/mark-read`

Mark a notification as read by ID.

#### Response (204 No Content)

### 3. Get Notification by ID
**GET** `/api/notifications/{id}`

Fetch a notification by ID.

#### Response (200 OK)
```
{
  "id": 5,
  "userId": 1,
  "message": "Your request has been approved.",
  "read": true
}
```

### 4. Get Unread Notifications by User
**GET** `/api/notifications/unread?userId={userId}`

Fetch all unread notifications for a user.

#### Response (200 OK)
```
[
  {
    "id": 6,
    "userId": 1,
    "message": "New comment on your request.",
    "read": false
  }
]
```

---

# Request Status History API Documentation

## Base Path
`/api/status-history`

### 1. Create Status History
**POST** `/api/status-history`

Create a new status history entry for a maintenance request.

#### Request Body (application/json)
```
{
  "requestId": 123,
  "status": "IN_PROGRESS",
  "timestamp": "2024-05-01T12:00:00Z"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "requestId": 123,
  "status": "IN_PROGRESS",
  "timestamp": "2024-05-01T12:00:00Z"
}
```

### 2. Get Status History by ID
**GET** `/api/status-history/{id}`

Fetch a status history entry by ID.

#### Response (200 OK)
```
{
  "id": 1,
  "requestId": 123,
  "status": "IN_PROGRESS",
  "timestamp": "2024-05-01T12:00:00Z"
}
```

### 3. Get Status History by Request
**GET** `/api/status-history/by-request?requestId={requestId}`

Fetch all status history entries for a maintenance request.

#### Response (200 OK)
```
[
  {
    "id": 1,
    "requestId": 123,
    "status": "IN_PROGRESS",
    "timestamp": "2024-05-01T12:00:00Z"
  }
]
```

---

# Maintenance Request API Documentation

## Base Path
`/api/requests`

### 1. Create Maintenance Request
**POST** `/api/requests`

Create a new maintenance request.

#### Request Body (application/json)
```
{
  "title": "Fix AC",
  "description": "The AC is not working.",
  "categoryId": 2,
  "locationId": 3,
  "requestedBy": 1
}
```

#### Response (200 OK)
```
{
  "id": 10,
  "title": "Fix AC",
  "description": "The AC is not working.",
  "categoryId": 2,
  "locationId": 3,
  "requestedBy": 1,
  "status": "OPEN"
}
```

### 2. Update Maintenance Request
**PUT** `/api/requests/{id}`

Update an existing maintenance request.

#### Request Body (application/json)
```
{
  "title": "Fix AC (urgent)",
  "description": "The AC is not working and it's urgent.",
  "categoryId": 2,
  "locationId": 3,
  "requestedBy": 1
}
```

#### Response (200 OK)
```
{
  "id": 10,
  "title": "Fix AC (urgent)",
  "description": "The AC is not working and it's urgent.",
  "categoryId": 2,
  "locationId": 3,
  "requestedBy": 1,
  "status": "OPEN"
}
```

### 3. Delete Maintenance Request
**DELETE** `/api/requests/{id}`

Delete a maintenance request by ID.

#### Response (204 No Content)

### 4. Get Maintenance Request by ID
**GET** `/api/requests/{id}`

Fetch a maintenance request by ID.

#### Response (200 OK)
```
{
  "id": 10,
  "title": "Fix AC",
  "description": "The AC is not working.",
  "categoryId": 2,
  "locationId": 3,
  "requestedBy": 1,
  "status": "OPEN"
}
```

### 5. Get Maintenance Requests by Status
**GET** `/api/requests/by-status?status={status}`

Fetch maintenance requests by status.

#### Response (200 OK)
```
[
  {
    "id": 10,
    "title": "Fix AC",
    "status": "OPEN"
  }
]
```

### 6. Get Maintenance Requests by Assigned User
**GET** `/api/requests/by-assigned?userId={userId}`

Fetch maintenance requests assigned to a user.

#### Response (200 OK)
```
[
  {
    "id": 11,
    "title": "Fix Projector",
    "status": "IN_PROGRESS"
  }
]
```

### 7. Get All Maintenance Requests
**GET** `/api/requests`

Fetch all maintenance requests.

#### Response (200 OK)
```
[
  {
    "id": 10,
    "title": "Fix AC",
    "status": "OPEN"
  },
  {
    "id": 11,
    "title": "Fix Projector",
    "status": "IN_PROGRESS"
  }
]
```

### 8. Assign Maintenance Request
**POST** `/api/requests/{requestId}/assign?userId={userId}`

Assign a maintenance request to a user.

#### Response (204 No Content) 

---

# User API Documentation

## Base Path
`/api/users`

### 1. Create User
**POST** `/api/users`

Create a new user.

#### Request Body (application/json)
```
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "roles": ["EMPLOYEE"]
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "roles": ["EMPLOYEE"]
}
```

### 2. Update User
**PUT** `/api/users/{id}`

Update an existing user.

#### Request Body (application/json)
```
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "roles": ["ADMIN"]
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "roles": ["ADMIN"]
}
```

### 3. Delete User
**DELETE** `/api/users/{id}`

Delete a user by ID.

#### Response (204 No Content)

### 4. Get User by ID
**GET** `/api/users/{id}`

Fetch a user by ID.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "roles": ["EMPLOYEE"]
}
```

### 5. Get User by Email
**GET** `/api/users/by-email?email={email}`

Fetch a user by email.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "roles": ["EMPLOYEE"]
}
```

### 6. Get All Users
**GET** `/api/users`

Fetch all users.

#### Response (200 OK)
```
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "roles": ["EMPLOYEE"]
  }
]
```

### 7. Assign Role to User
**POST** `/api/users/{userId}/assign-role?roleName={roleName}`

Assign a role to a user.

#### Response (204 No Content)

---

# Role API Documentation

## Base Path
`/api/roles`

### 1. Create Role
**POST** `/api/roles`

Create a new role.

#### Request Body (application/json)
```
{
  "name": "ADMIN"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "ADMIN"
}
```

### 2. Update Role
**PUT** `/api/roles/{id}`

Update an existing role.

#### Request Body (application/json)
```
{
  "name": "SUPER_ADMIN"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "SUPER_ADMIN"
}
```

### 3. Delete Role
**DELETE** `/api/roles/{id}`

Delete a role by ID.

#### Response (204 No Content)

### 4. Get Role by ID
**GET** `/api/roles/{id}`

Fetch a role by ID.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "ADMIN"
}
```

### 5. Get Role by Name
**GET** `/api/roles/by-name?name={name}`

Fetch a role by name.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "ADMIN"
}
```

### 6. Get All Roles
**GET** `/api/roles`

Fetch all roles.

#### Response (200 OK)
```
[
  {
    "id": 1,
    "name": "ADMIN"
  }
]
```

---

# Category API Documentation

## Base Path
`/api/categories`

### 1. Create Category
**POST** `/api/categories`

Create a new category.

#### Request Body (application/json)
```
{
  "name": "Electrical"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Electrical"
}
```

### 2. Update Category
**PUT** `/api/categories/{id}`

Update an existing category.

#### Request Body (application/json)
```
{
  "name": "Plumbing"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Plumbing"
}
```

### 3. Delete Category
**DELETE** `/api/categories/{id}`

Delete a category by ID.

#### Response (204 No Content)

### 4. Get Category by ID
**GET** `/api/categories/{id}`

Fetch a category by ID.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Electrical"
}
```

### 5. Get Category by Name
**GET** `/api/categories/by-name?name={name}`

Fetch a category by name.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Electrical"
}
```

### 6. Get All Categories
**GET** `/api/categories`

Fetch all categories.

#### Response (200 OK)
```
[
  {
    "id": 1,
    "name": "Electrical"
  }
]
```

---

# Location API Documentation

## Base Path
`/api/locations`

### 1. Create Location
**POST** `/api/locations`

Create a new location.

#### Request Body (application/json)
```
{
  "name": "Conference Room"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Conference Room"
}
```

### 2. Update Location
**PUT** `/api/locations/{id}`

Update an existing location.

#### Request Body (application/json)
```
{
  "name": "Main Lobby"
}
```

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Main Lobby"
}
```

### 3. Delete Location
**DELETE** `/api/locations/{id}`

Delete a location by ID.

#### Response (204 No Content)

### 4. Get Location by ID
**GET** `/api/locations/{id}`

Fetch a location by ID.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Conference Room"
}
```

### 5. Get Location by Name
**GET** `/api/locations/by-name?name={name}`

Fetch a location by name.

#### Response (200 OK)
```
{
  "id": 1,
  "name": "Conference Room"
}
```

### 6. Get All Locations
**GET** `/api/locations`

Fetch all locations.

#### Response (200 OK)
```
[
  {
    "id": 1,
    "name": "Conference Room"
  }
]
``` 