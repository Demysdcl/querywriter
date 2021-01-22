# querywriter

It is a simple project in kotlin to create query e skip some error like: 
forget space, forget comma, forget to close parentheses e etc.

It is intuitive and improve the legibility


```kt
val internalQuery = QueryWriter()
    .select("student_id")
    .from("course_student")
    .query()

val query = QueryWriter()
    .select("name, phone, email")
    .from("student")
    .where("id")
    .inValues(internalQuery)
    .query()
```
