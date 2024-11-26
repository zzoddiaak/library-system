# library-system

Инструкция по запуску:
Для начала у вас должнен быть установлен PostgreSQL,
в ней должны быть созданы БД library-service и book-service(Можно и другие, но все изменения внесены в application.properties)
А так же возможно нужно будет поменять значение в spring.datasource.username и spring.datasource.password

Далее нам нужно будет перейти в сервис book (cd book)
Там мы запускаем команду - mvn clean install
Далее запускаем команду - mvn spring-boot:run

Проделываем так же для library:
нам нужно будет перейти в сервис library (cd library)
Там мы запускаем команду - mvn clean install
Далее запускаем команду - mvn spring-boot:run

Postman запросы:
Для book сервиса
POTS запрос - http://localhost:8080/myapp/api/auth/login?username=myuser&password=mypassword (Получаем токен)
Далее этот токен нужно использовать для аутентификации Bearer Token, в следующих запросах

POTS запрос - http://localhost:8080/myapp/api/books (Добавление книги)
Нам нужно будет ввести в Body параметры - вот пример 
{

    {
    "title": "Мастер и Маргарита",
    "author": "Михаил Булгаков",
    "isbn": "1234567890123",
    "description": 1967,
    "genre": "Фантастика"
    }
}

GET запрос - http://localhost:8080/myapp/api/books (Получение всех книг)
GET запрос - http://localhost:8080/myapp/api/books/2 (Получение книги по id)
PUT запрос - http://localhost:8080/myapp/api/books/2 (Обновление книги по id)
Нам нужно будет ввести в Body параметры - вот пример 
{

    {
    "isbn": "1234567890623",
    "title": "Мастер и Мастер",
    "genre": "Фантастика",
    "description": 1967,
    "author": "Михаил Булгаков"
    }
    
}
DELETE запрос - http://localhost:8080/myapp/api/books/2 (Удаление книги по id)

GET запрос - http://localhost:8080/myapp/api/books/isbn/9783161484100 (Поиск книги по isbn)

Postman запросы:
Для library сервиса

POTS запрос - http://localhost:8081/api/auth/login?username=myuser&password=mypassword (Получаем токен)
Далее этот токен нужно использовать для аутентификации Bearer Token, в следующих запросах

GET запрос - http://localhost:8081/api/library/free-books (Получение свободных книг)
PUT запрос - http://localhost:8081/api/library/1 (Обновление)
Нам нужно будет ввести в Body параметры - вот пример 
{

    {
    "borrowTime": "2024-11-25T14:00:00",
    "returnTime": "2024-12-25T14:00:00"
    }
    
}
