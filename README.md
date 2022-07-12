﻿ # Social-Network-FE

### **LIST API**
1. Image:
- [GET] `/api/v1/images/`
- [GET] `/api/v1/images/{id}/`
- [POST] `/api/v1/images/` (using form-data)
    - imageFile
    - type
    - id
- [PUT] `/api/v1/images/{id}/` (using form-data)
- [DELETE] `/api/v1/images/{id}/`
2. Video:
- [GET] `/api/v1/videos/` 
- [GET] `/api/v1/videos/{id}/` (using form-data)
    - videoFile
- [POST] `/api/v1/videos/` (using form-data)
- [PUT] `/api/v1/videos/{id}/`
- [DELETE] `/api/v1/videos/{id}/`
3. User:
- [POST] `/api/v1/register` 
    - email
    - username
    - firstName
    - lastName
    - password
- [POST] `/api/v1/login`
    - username
    - password
- [GET] `/api/v1/users` 
- [PUT] `/api/v1/users` 
    - firstName
    - lastName
    - phone
    - description
- [GET] `/api/v1/users/{id}` 
- [GET] `/api/v1/users/refresh-token` (Authorization Bearer refresh token) 

