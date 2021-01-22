package net.afferolab.querywriter


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class QueryWriterTest {

    @Test
    fun `should return a simple query`() {
        val query =  QueryWriter()
            .select("*")
            .from("student")
            .query()

        val expected = "SELECT * FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with DISTINCT`() {
        val query =  QueryWriter()
            .select("*", true)
            .from("student")
            .query()

        val expected = "SELECT DISTINCT * FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with many fields`() {
        val name = "name"
        val email = "email"
        val phone = "phone"

        val query =  QueryWriter()
            .select(listOf(name, email, phone))
            .from("student")
            .query()

        val expected = "SELECT name, email, phone FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with where and equals`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").equalsValue("John")
            .query()

        val expected = "SELECT * FROM student WHERE name = 'John'"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with alias`() {
        val query = QueryWriter()
            .select("name").alias("student_name")
            .from("student")
            .query()

        val expected = "SELECT name AS student_name FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with where and not equals`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").diffValue("John")
            .query()

        val expected = "SELECT * FROM student WHERE name <> 'John'"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with where and field is null`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").isNull()
            .query()

        val expected = "SELECT * FROM student WHERE name IS NULL"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with where and field is not null`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").notNull()
            .query()

        val expected = "SELECT * FROM student WHERE name IS NOT NULL"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with AND condition`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").equalsValue("John")
            .and("lastname").equalsValue("Smith")
            .query()

        val expected = "SELECT * FROM student WHERE name = 'John' AND lastname = 'Smith'"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with OR condition`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name").equalsValue("John")
            .or("lastname").equalsValue("Smith")
            .query()

        val expected = "SELECT * FROM student WHERE name = 'John' OR lastname = 'Smith'"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with inner join statement`() {
        val query = QueryWriter()
            .select("*")
            .from("student s")
            .innerJoin("classroom cr", "s.classroomId", "cr.classId")
            .query()

        val expected = "SELECT * FROM student s INNER JOIN classroom cr ON s.classroomId = cr.classId"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with left join statement`() {
        val query = QueryWriter()
            .select("*")
            .from("student s")
            .leftJoin("classroom cr", "s.classroomId", "cr.classId")
            .query()

        val expected = "SELECT * FROM student s LEFT JOIN classroom cr ON s.classroomId = cr.classId"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with right join statement`() {
        val query = QueryWriter()
            .select("*")
            .from("student s")
            .rightJoin("classroom cr", "s.classroomId", "cr.classId")
            .query()

        val expected = "SELECT * FROM student s RIGHT JOIN classroom cr ON s.classroomId = cr.classId"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with outer join statement`() {
        val query = QueryWriter()
            .select("*")
            .from("student s")
            .outerJoin("classroom cr", "s.classroomId", "cr.classId")
            .query()

        val expected = "SELECT * FROM student s FULL OUTER JOIN classroom cr ON s.classroomId = cr.classId"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with IN statement`() {
        val query = QueryWriter()
            .select("name, phone, email")
            .from("student")
            .where("id")
            .inValues(listOf(1, 2, 3))
            .query()

        val expected = "SELECT name, phone, email FROM student WHERE id IN (1, 2, 3)"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with NOT IN statement`() {
        val query = QueryWriter()
            .select("name, phone, email")
            .from("student")
            .where("id")
            .not()
            .inValues(listOf(1, 2, 3))
            .query()

        val expected = "SELECT name, phone, email FROM student WHERE id NOT IN (1, 2, 3)"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with IN and query statement`() {

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

        val expected = "SELECT name, phone, email FROM student WHERE id IN (SELECT student_id FROM course_student)"

        assertEquals(expected, query)
    }


    @Test
    fun `should return a query with NOT IN and query statement`() {

        val internalQuery = QueryWriter()
            .select("student_id")
            .from("course_student")
            .query()

        val query = QueryWriter()
            .select("name, phone, email")
            .from("student")
            .where("id")
            .not()
            .inValues(internalQuery)
            .query()

        val expected = "SELECT name, phone, email FROM student WHERE id NOT IN (SELECT student_id FROM course_student)"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query ORDER BY`() {

        val query =  QueryWriter()
            .select("name, email, phone")
            .from("student")
            .orderBy("name")
            .query()

        val expected = "SELECT name, email, phone FROM student ORDER BY name ASC"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with ORDER BY and LIMIT`() {

        val query =  QueryWriter()
            .select("name, email, phone")
            .from("student")
            .orderBy("name")
            .limit(1, 100)
            .query()

        val expected ="SELECT name, email, phone FROM student ORDER BY name ASC LIMIT 1, 100"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query ORDER BY DESC`() {

        val query =  QueryWriter()
            .select("name, email, phone")
            .from("student")
            .orderBy("name", Sort.DESC)
            .query()

        val expected = "SELECT name, email, phone FROM student ORDER BY name DESC"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with many fields by list and ORDER BY`() {
        val name = "name"
        val email = "email"
        val phone = "phone"

        val query =  QueryWriter()
            .select(listOf(name, email, phone))
            .from("student")
            .orderBy(listOf(name, email, phone))
            .query()

        val expected = "SELECT name, email, phone FROM student ORDER BY name, email, phone ASC"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with many fields by String and ORDER BY`() {

        val query =  QueryWriter()
            .select("name, email, phone")
            .from("student")
            .orderBy("name, email, phone")
            .query()

        val expected = "SELECT name, email, phone FROM student ORDER BY name, email, phone ASC"

        assertEquals(expected, query)
    }

    @Test
    fun `should throws expect by GROUP BY without SUM or COUNT`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                    .select("*")
                    .from("student")
                    .groupBy("name")
                    .query()
        }

        val expected = "'GROUP BY' without 'COUNT', 'SUM' or 'AVG'"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `should throws exception by COUNT without GROUP BY`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .select("weight,")
                .count("*")
                .from("student")
                .query()
        }

        val expected = "'COUNT', 'SUM' or 'AVG' without 'GROUP BY'"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `should throws exception by SUM without GROUP BY`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .select()
                .sum("*", true)
                .from("student")
                .query()
        }

        val expected = "'COUNT', 'SUM' or 'AVG' without 'GROUP BY'"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `should throws exception by AVG without GROUP BY`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .select("name,")
                .avg("*")
                .from("student")
                .query()
        }

        val expected = "'COUNT', 'SUM' or 'AVG' without 'GROUP BY'"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `should return query to count`() {
        val query = QueryWriter()
            .select("weight,")
            .count("*")
            .from("student")
            .groupBy("weight")
            .query()

        val expected = "SELECT weight, COUNT(*) FROM student GROUP BY weight"

        assertEquals(expected, query )
    }

    @Test
    fun `should return a query with many fields by list and GROUP BY`() {
        val name = "name"
        val email = "email"

        val query =  QueryWriter()
            .select("name, email,")
            .count("*")
            .from("student")
            .groupBy(listOf(name, email))
            .query()

        val expected = "SELECT name, email, COUNT(*) FROM student GROUP BY name, email"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query to COUNT`() {
        val query = QueryWriter()
            .select()
            .count("*")
            .from("student")
            .query()

        val expected = "SELECT COUNT(*) FROM student"

        assertEquals(expected, query )
    }

    @Test
    fun `should return a simple query to SUM`() {
        val query = QueryWriter()
            .select()
            .sum("weight")
            .from("student")
            .query()

        val expected = "SELECT SUM(weight) FROM student"

        assertEquals(expected, query )
    }

    @Test
    fun `should return a simple query to AVG`() {
        val query = QueryWriter()
            .select()
            .avg("weight")
            .from("student")
            .query()

        val expected = "SELECT AVG(weight) FROM student"

        assertEquals(expected, query )
    }

    @Test
    fun `should return a simple query to COUNT and other field`() {
        val query = QueryWriter()
            .select()
            .count("*", true)
            .field("weight")
            .from("student")
            .groupBy("weight")
            .query()

        val expected = "SELECT COUNT(*), weight FROM student GROUP BY weight"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with MIN`() {
        val query = QueryWriter()
            .select()
            .min("weight")
            .from("student")
            .query()

        val expected = "SELECT MIN(weight) FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with MAX`() {
        val query = QueryWriter()
            .select()
            .max("weight")
            .from("student")
            .query()

        val expected = "SELECT MAX(weight) FROM student"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with WHERE and LIKE`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("name")
            .like("%[wW]%")
            .query()

        val expected = "SELECT * FROM student WHERE name LIKE %[wW]%"

        assertEquals(expected, query)
    }

    @Test
    fun `should throws exception by different type BETWEEN method`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .select("name")
                .from("student")
                .where("name")
                .between("Ana", 1)
                .query()
        }

        val expected = "firstValue and lastValue should be the same type"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `should return a simple query with BETWEEN and value as String`() {
            val query = QueryWriter()
                .select("*")
                .from("student")
                .where("city")
                .between("Florida", "New York")
                .query()

        val expected = "SELECT * FROM student WHERE city BETWEEN 'Florida' AND 'New York'"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with BETWEEN and value as Number`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("number")
            .between(1000, 2000 )
            .query()

        val expected = "SELECT * FROM student WHERE number BETWEEN 1000 AND 2000"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a simple query with NOT BETWEEN and value as Number`() {
        val query = QueryWriter()
            .select("*")
            .from("student")
            .where("number")
            .not()
            .between(1000, 2000)
            .query()

        val expected = "SELECT * FROM student WHERE number NOT BETWEEN 1000 AND 2000"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with UNION`() {
        val query = QueryWriter()
            .select("city")
            .from("Customers")
            .union()
            .select("city")
            .from("Suppliers")
            .orderBy("city")
            .query()

        val expected = "SELECT city FROM Customers UNION SELECT city FROM Suppliers ORDER BY city ASC"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with UNION ALL`() {
        val query = QueryWriter()
            .select("city")
            .from("Customers")
            .unionAll()
            .select("city")
            .from("Suppliers")
            .orderBy("city")
            .query()

        val expected = "SELECT city FROM Customers UNION ALL SELECT city FROM Suppliers ORDER BY city ASC"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with EXISTS`() {
        val internalQuery = QueryWriter()
            .select("ProductName")
            .from("Products")
            .where("Products.SupplierID").equalsField("Suppliers.supplierID")
            .and("Price").equalsValue(22)
            .query()

        val query = QueryWriter()
            .select("SupplierName")
            .from("Suppliers")
            .where()
            .exists(internalQuery)
            .query()

        val expected = "SELECT SupplierName FROM Suppliers WHERE EXISTS " +
                "(SELECT ProductName FROM Products " +
                "WHERE Products.SupplierID = Suppliers.supplierID AND Price = 22)"

        assertEquals(expected, query)
    }

    @Test
    fun `should return a query with ANY`() {
        val internalQuery = QueryWriter()
            .select("ProductName")
            .from("Products")
            .where("Products.SupplierID").equalsField("Suppliers.supplierID")
            .and("Price").equalsValue(22)
            .query()

        val query = QueryWriter()
            .select("SupplierName")
            .from("Suppliers")
            .where("City")
            .any("=", internalQuery)
            .query()

        assertEquals("SELECT SupplierName FROM Suppliers WHERE City = ANY " +
                "(SELECT ProductName FROM Products " +
                "WHERE Products.SupplierID = Suppliers.supplierID AND Price = 22)", query)
    }

    @Test
    fun `should return a query with ALL`() {
        val internalQuery = QueryWriter()
            .select("ProductName")
            .from("Products")
            .where("Products.SupplierID").equalsField("Suppliers.supplierID")
            .and("Price").equalsValue(22)
            .query()

        val query = QueryWriter()
            .select("SupplierName")
            .from("Suppliers")
            .where("City")
            .all("=", internalQuery)
            .query()

        assertEquals("SELECT SupplierName FROM Suppliers WHERE City = ALL " +
                "(SELECT ProductName FROM Products " +
                "WHERE Products.SupplierID = Suppliers.supplierID AND Price = 22)", query)
    }

    @Test
    fun `should return a insert query with values`() {
        val query = QueryWriter()
            .insert("student", "1, 'John Wick', 'johnwick@mail.com'")
            .query()

        assertEquals("INSERT INTO student VALUES(1, 'John Wick', 'johnwick@mail.com')", query)
    }

    @Test
    fun `should return a insert query with values by List`() {
        val query = QueryWriter()
            .insert("student", listOf(1, "John Wick", "johnwick@mail.com", 1000))
            .query()

        assertEquals("INSERT INTO student VALUES(1, 'John Wick', 'johnwick@mail.com', 1000)", query)
    }

    @Test
    fun `should return a insert query with fields and values`() {
        val query = QueryWriter()
            .insert("student",  "id, name, email","1, 'John Wick', 'johnwick@mail.com'")
            .query()

        assertEquals("INSERT INTO student (id, name, email) VALUES(1, 'John Wick', 'johnwick@mail.com')", query)
    }

    @Test
    fun `should return a insert query with fields and values by List`() {
        val query = QueryWriter()
            .insert("student", listOf("id", "name", "email"), listOf(1, "John Wick", "johnwick@mail.com", 1000))
            .query()

        assertEquals("INSERT INTO student (id, name, email) VALUES(1, 'John Wick', 'johnwick@mail.com', 1000)", query)
    }

    @Test
    fun `should return a query with HAVING and greaterThan`(){
        val query = QueryWriter()
            .select()
            .count("CustomerID", true)
            .field("Country")
            .from("Customers")
            .groupBy("Country")
            .having()
            .count("CustomerID")
            .greaterThan(5)
            .query()

        assertEquals("SELECT COUNT(CustomerID), Country FROM Customers GROUP BY Country HAVING COUNT(CustomerID) > 5", query)
    }

    @Test
    fun `should return a query with HAVING and lessThan`(){
        val query = QueryWriter()
            .select()
            .count("CustomerID", true)
            .field("Country")
            .from("Customers")
            .groupBy("Country")
            .having()
            .count("CustomerID")
            .lessThan(20)
            .query()

        assertEquals("SELECT COUNT(CustomerID), Country FROM Customers GROUP BY Country HAVING COUNT(CustomerID) < 20", query)
    }

    @Test
    fun `should throws exception by UPDATE without WHERE`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .update("student", "email = 'student@mail.com'")
                .query()
        }
        assertEquals("'UPDATE' or 'DELETE' without 'WHERE'", exception.message)
    }

    @Test
    fun `should return a query with update and where`() {
        val query = QueryWriter()
            .update("student", "email = 'student@mail.com'")
            .where("id").equalsValue(1)
            .query()

        assertEquals("UPDATE student SET email = 'student@mail.com' WHERE id = 1", query)
    }

    @Test
    fun `should throws exception by UPDATE with params with diff size`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .update("student", listOf("email"), listOf(1, "email@mail.com"))
                .query()
        }
        assertEquals("'UPDATE' needs the params fields and values with the size", exception.message)
    }

    @Test
    fun `should return a query with update, where and fields and values by List`() {
        val query = QueryWriter()
            .update("student", listOf("email", "student_code"), listOf("student@mail.com", 1000))
            .where("id").equalsValue(1)
            .query()

        assertEquals("UPDATE student SET email = 'student@mail.com', student_code = 1000 WHERE id = 1", query)
    }

    @Test
    fun `should throws exception by DELETE without WHERE`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .delete("student")
                .query()
        }
        assertEquals("'UPDATE' or 'DELETE' without 'WHERE'", exception.message)
    }

    @Test
    fun `should return a query with DELETE statement`() {

            val query = QueryWriter()
                .delete("student")
                .where("student_id").equalsValue(1)
                .query()

        assertEquals("DELETE student WHERE student_id = 1", query)
    }

    @Test
    fun `should throws exception by parentheses not closed`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            QueryWriter()
                .select("*")
                .from("student")
                .where("name").equalsValue("John Wick")
                .or().openParentheses()
                .field("city").equalsValue("New York")
                .and("year").equalsValue(2016)
                .query()
        }
        assertEquals("There are parentheses not closed yet", exception.message)
    }

    @Test
    fun `should return a query with parentheses opened and closed`() {
        val query = QueryWriter()
                .select("*")
                .from("student")
                .where("name").equalsValue("John Wick")
                .or().openParentheses()
                .field("city").equalsValue("New York")
                .and("year").equalsValue(2016)
                .closeParentheses()
                .query()
        
        assertEquals("SELECT * FROM student WHERE name = 'John Wick' OR ( city = 'New York' AND year = 2016 )", query)
    }

}