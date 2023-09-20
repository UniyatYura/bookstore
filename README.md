# bookstore

Spring Boot intro
We are going to implement an app for Online Book store. We will implement it step by step. In this app we will have the following domain models (entities):

User: Contains information about the registered user including their authentication details and personal information.
Role: Represents the role of a user in the system, for example, admin or user.
Book: Represents a book available in the store.
Category: Represents a category that a book can belong to.
ShoppingCart: Represents a user's shopping cart.
CartItem: Represents an item in a user's shopping cart.
Order: Represents an order placed by a user.
OrderItem: Represents an item in a user's order.
Project description
NOTE: Keep in mind, this is a general description of the project, that we will implement during this module. We will implement it part by part. For this purpose you needs to create a new GitHub repo where your project will be. Then you can share this repo link in your CV, and it will be a proof of work for you potential interviewers

People involved:
Shopper (User): Someone who looks at books, puts them in a basket (shopping cart), and buys them.
Manager (Admin): Someone who arranges the books on the shelf and watches what gets bought.
Things Shoppers Can Do:
Join and sign in:
Join the store.
Sign in to look at books and buy them.
Look at and search for books:
Look at all the books.
Look closely at one book.
Find a book by typing its name.
Look at bookshelf sections:
See all bookshelf sections.
See all books in one section.
Use the basket:
Put a book in the basket.
Look inside the basket.
Take a book out of the basket.
Buying books:
Buy all the books in the basket.
Look at past receipts.
Look at receipts:
See all books on one receipt.
Look closely at one book on a receipt.
Things Managers Can Do:
Arrange books:
Add a new book to the store.
Change details of a book.
Remove a book from the store.
Organize bookshelf sections:
Make a new bookshelf section.
Change details of a section.
Remove a section.
Look at and change receipts:
Change the status of a receipt, like "Shipped" or "Delivered".
HW
Infrastructure requirements
Add checkstyle plugin to your repo. (As an example, please see this project's pom.xml file) For this, you need to add
In the project root directory create a new file with name checkstyle.xml
copy the content of this file and put it into the checkstyle.xml created on the previous step
Add the following property to the <properties>...</properties> section in the pom.xml file
 <maven.checkstyle.plugin.configLocation>checkstyle.xml</maven.checkstyle.plugin.configLocation>

Add the following plugin into the <build> <plugins> .... </plugins> </build> section:
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.1</version>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <configLocation>${maven.checkstyle.plugin.configLocation}</configLocation>
        <encoding>UTF-8</encoding>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <linkXRef>false</linkXRef>
    </configuration>
</plugin>

Create a .github directory in your project root directory. Inside this directory create a new directory workflows. (as an example please see this project's structure)
Inside the workflows directory create a new file ci.yml and paste this code into it:
name: Java CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'adopt'
          cache: maven
      - name: Build with Maven
        run: mvn --batch-mode --update-snapshots verify

Commit these changes to the GitHub. You can commit infrastructure changes directly into the master (main) brach. But the next changes should be only in PRs
Requirements
In this HW, we will work only with the first entity - Book. Your task is to create a new repository, add the simple Spring Boot app with:

Book entity with fields:

id (Long, PK)
title (String, not null)
author (String, not null)
isbn (String, not null, unique)
price (BigDecimal, not null)
description (String)
coverImage (String)
BookRepository interface with methods and BookRepositoryImpl class:

Book save(Book book);
List findAll();
BookService interface with methods and BookServiceImpl class:

Book save(Book book);
List findAll();
Class with @SpringBootApplication annotation that will have a CommandLineRunner bean.

Do not forget to use these properties:

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

IMPORTANT
Once you will add a MySQL to your project, CI checks on the GitHub may not pass. For this, please add the following:

Create a new folder resources in the src/test directory
Create a new file application.properties in the src/test/resources folder
Add the following content to the application.properties file
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

Add the following dependency to the pom.xml file:
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>

Now builds should pass. We will learn more about this config in next topics.

HINT:
It is a good practice to remove the branch (from which you have opened a PR, not the main or master branch) once you merge the PR. This action can be automated. To do this, you need to go to the Settings tab in your GitHub repository, select General, scroll down, and choose the checkbox next to Automatically delete head branches: img_remove_branch

Share the link to the PR where Book's related code changes will be added.
