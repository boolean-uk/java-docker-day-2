# Endpoints Overview

## FollowController

### Retrieve Followers List
- **Endpoint:** `/follows/followers/list/{followId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the list of followers for a specific user.

### Retrieve Following List
- **Endpoint:** `/follows/following/list/{userId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the list of users that a specific user is following.

### Get Number of Followers
- **Endpoint:** `/follows/followers/{userId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the number of followers for a specific user.

### Get Number of Following
- **Endpoint:** `/follows/following/{userId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the number of users that a specific user is following.

### Follow User
- **Endpoint:** `/follows/{followId}`
- **Method:** `POST`
- **Access:** USER
- **Description:** Follow another user.

### Unfollow User
- **Endpoint:** `/follows/{followId}`
- **Method:** `DELETE`
- **Access:** USER
- **Description:** Unfollow another user.

### Admin Follow User
- **Endpoint:** `/follows/{followId}/users/{userId}`
- **Method:** `POST`
- **Access:** ADMIN
- **Description:** Admin action - Make a user follow another user.

### Admin Unfollow User
- **Endpoint:** `/follows/{followId}/users/{userId}`
- **Method:** `DELETE`
- **Access:** ADMIN
- **Description:** Admin action - Make a user unfollow another user.

## PostController

### Retrieve Post
- **Endpoint:** `/posts/{postId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve a specific post.

### Retrieve User Posts
- **Endpoint:** `/posts`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve posts for the authenticated user.

### Create Post
- **Endpoint:** `/posts`
- **Method:** `POST`
- **Access:** USER
- **Description:** Create a new post for the authenticated user.

### Delete Post
- **Endpoint:** `/posts/{postId}`
- **Method:** `DELETE`
- **Access:** USER
- **Description:** Delete a specific post owned by the authenticated user.

### Update Post
- **Endpoint:** `/posts/{postId}`
- **Method:** `PUT`
- **Access:** USER
- **Description:** Update a specific post owned by the authenticated user.

### Admin Retrieve User Posts
- **Endpoint:** `/posts/users/{userId}`
- **Method:** `GET`
- **Access:** ADMIN
- **Description:** Admin action - Retrieve posts for a specific user.

### Admin Create User Post
- **Endpoint:** `/posts/users/{userId}`
- **Method:** `POST`
- **Access:** ADMIN
- **Description:** Admin action - Create a new post for a specific user.

### Admin Delete User Post
- **Endpoint:** `/posts/users/{postId}`
- **Method:** `DELETE`
- **Access:** ADMIN
- **Description:** Admin action - Delete a specific post owned by a user.

### Admin Update User Post
- **Endpoint:** `/posts/users/{postId}`
- **Method:** `PUT`
- **Access:** ADMIN
- **Description:** Admin action - Update a specific post owned by a user.

## LikeController

### Retrieve Likes for Post
- **Endpoint:** `/likes/posts/{postId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the number of likes on a specific post.

### Like Post
- **Endpoint:** `/likes/posts/{postId}`
- **Method:** `POST`
- **Access:** USER
- **Description:** Like a specific post.

### Unlike Post
- **Endpoint:** `/likes/posts/{postId}`
- **Method:** `DELETE`
- **Access:** USER
- **Description:** Unlike a specific post.

### Admin Like Post
- **Endpoint:** `/likes/posts/{postId}/users/{userId}`
- **Method:** `POST`
- **Access:** ADMIN
- **Description:** Admin action - Like a specific post on behalf of a user.

### Admin Unlike Post
- **Endpoint:** `/likes/posts/{postId}/users/{userId}`
- **Method:** `DELETE`
- **Access:** ADMIN
- **Description:** Admin action - Unlike a specific post on behalf of a user.

## RepostController

### Retrieve Reposts for Post
- **Endpoint:** `/reposts/posts/{postId}`
- **Method:** `GET`
- **Access:** USER
- **Description:** Retrieve the number of reposts for a specific post.

### Repost Post
- **Endpoint:** `/reposts/posts/{postId}`
- **Method:** `POST`
- **Access:** USER
- **Description:** Repost a specific post.

### Unrepost Post
- **Endpoint:** `/reposts/posts/{postId}`
- **Method:** `DELETE`
- **Access:** USER
- **Description:** Unrepost a specific post.

### Admin Repost Post
- **Endpoint:** `/reposts/posts/{postId}/users/{userId}`
- **Method:** `POST`
- **Access:** ADMIN
- **Description:** Admin action - Repost a specific post on behalf of a user.

### Admin Unrepost Post
- **Endpoint:** `/reposts/posts/{postId}/users/{userId}`
- **Method:** `DELETE`
- **Access:** ADMIN
- **Description:** Admin action - Unrepost a specific post on behalf of a user.

## AuthController

### Authenticate User
- **Endpoint:** `/auth/signin`
- **Method:** `POST`
- **Access:** PUBLIC
- **Description:** Authenticate and generate a JWT token for the user.

### Register User
- **Endpoint:** `/auth/signup`
- **Method:** `POST`
- **Access:** PUBLIC
- **Description:** Register a new user.

