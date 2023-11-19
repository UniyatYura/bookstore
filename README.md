<a id="up"></a>

[finish of file](#down)

[__📚 Online Bookstore API__](https://github.com/UniyatYura/bookstore)

[![Посилання на фото](images/img_1.png)](https://github.com/UniyatYura/bookstore)

***

__Project description__

This application is designed for bookstores or libraries to manage their collection of books, categories, and user orders.

***

__Summary__

* Java 17 restful stateless web-Application that allows, depending on role, to explore and purchase books; to manage the book store.
* The project follows the Model-View-Controller (MVC) architectural pattern and is organized into multiple layers, making it a multi-tier architecture. The layers include presentation, business logic, data access, security, validation, configuration, and mapping.
* Application supports CRUD, REST, SOLID, ACID design patterns.
* Security implemented using JWT-tokens.
* Application supports pagination and sorting.
* Application containerized using docker to provide efficient data management.

***

__Features__
* Registration user with role "USER";
* Authentication user;
* Create/get list of books;
* Create/get categories of books;
* Add books to a shopping cart;
* Edit a shopping cart;
* Checkout an order for an authenticated user;
* Complete an order and get list of orders for an authenticated user;
* Under role ADMIN can edit statuses of an order

***

__Technologies__

* Java 17
* Maven
* Spring Boot
* Spring Data JPA
* Spring Security - roles concept, JWT
* Spring Boot Web
* Pagination, sorting, Swagger(Open API)
* JUnit
* Mockito
* MySql DB
* Criteria query in Spring Boot
* Soft delete concept
* Liquibase
* Custom data validation
* Docker

***

__Endpoints available for used api__

Available for non authenticated users:

* POST: /api/auth/register
* POST: /api/auth/login

Available for users with role USER:

* GET: /api/books
* GET: /api/books/{id}
* GET: /api/categories
* GET: /api/categories/{id}
* GET: /api/categories/{id}/books
* GET: /api/cart
* POST: /api/cart
* PUT: /api/cart/cart-items/{cartItemId}
* DELETE: /api/cart/cart-items/{cartItemId}
* GET: /api/orders
* POST: /api/orders
* GET: /api/orders/{orderId}/items
* GET: /api/orders/{orderId}/items/{itemId}

Available for users with role ADMIN:

* POST: /api/books/
* PUT: /api/books/{id}
* DELETE: /api/books/{id}
* POST: /api/categories
* PUT: /api/categories/{id}
* DELETE: /api/categories/{id}
* PATCH: /api/orders/{id}

***

__How to run project?__

Requirements:

To launch the application please make sure you have Docker installed. Then configure your database settings in the .env file. Then run application using

```
docker-compose up
```

[Reference on my GitHub](https://github.com/UniyatYura/bookstore)

[start of file](#up)

<a id="down"></a>
