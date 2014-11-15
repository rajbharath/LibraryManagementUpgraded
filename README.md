This is a library management system with below primary functionalities.

user authentication
add a book to library
remove a book from library
borrow a book
returning the borrowed book
searching the book by its name

The below services are built as part of this system

1. Authentication Service
2. Administrative Service
3. Reading Service
4. Search Service

Authentication Service:
-----------------------
Responsibility:
This service is used for authenticating the user by means of username and password.

Dependencies:
It has User Repository for retrieving the user information.

Administrative Service:
-----------------------
Responsibility:
Administrative Services are only authorized for those who has the permissions to ADD_BOOK and REMOVE_BOOK.

Functionalities:
1.It can add a book to the library
2.It can remove a book from the library

Dependencies:
It has Book repository for storing and retrieving book information.
Book repository is consideres as an aggregated repository which contains both publisher repository and author repository.


ReadingService:
--------------
Responsibility:
This service is used for borrowing and returning the book by a user who has the permissions to BORROW_BOOK,RETURN_BOOK

Functionalities:
1. It can borrow a book for a particular user
2. It can return a book which is already borrowed by an user

Dependencies:
It has Reading Repository to store and retrieve all the readings of all users.
Considering Reading repo needs book information to store and retrieve, it is being an aggregated repository.

The above services use corresponding repositories to retrieve/store their models from/to the datasource.


BookSearchService:
--------------------
Responsibilities:
It is used for searching the books those satisfying the given criteria.

Functionalities:
1. It can search library for the books by its name

Dependencies:
It has book repository as the dependency.

Service Manager:
----------------
Responsibility:
Creating the services

Dependency:
Repository Factory and DataSource

Repository Factory:
-------------------
Responsibility:
Creating the repository with the datasource

Dependency:
BaseData source

BaseDataSource:
-----------------
Responsibility:
It used to get the connection from the given data source.

Dependency:
It is dependent on the driver name,db url,username, password.

Functionalities:
It can provide connection to the data source.