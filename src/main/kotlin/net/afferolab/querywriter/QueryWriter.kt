package net.afferolab.querywriter

import java.lang.IllegalArgumentException
import java.util.logging.Logger

class QueryWriter {

    private var queryStatement = ""

    fun baseQuery(query: String): QueryWriter {
        queryStatement += query
        return this;
    }

    fun select(fields: String = "", distinct: Boolean = false): QueryWriter {
        queryStatement += "${select(distinct)} ${verifyField(fields)}"
        return this
    }

    fun select(fields: List<String>, distinct: Boolean = false): QueryWriter {
        queryStatement += "${select(distinct)} ${fields.joinToString { it }} "
        return this
    }

    private fun select(distinct: Boolean) = "SELECT${if (distinct) " DISTINCT" else ""}"

    fun count(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "COUNT($field)${comma(insertComma)}"
        return this
    }

    fun sum(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "SUM($field)${comma(insertComma)}"
        return this
    }

    fun avg(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "AVG($field)${comma(insertComma)}"
        return this
    }

    fun min(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "MIN($field)${comma(insertComma)}"
        return this
    }

    fun max(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "MAX($field)${comma(insertComma)}"
        return this
    }

    fun alias(alias: String): QueryWriter {
        queryStatement += "AS $alias "
        return this
    }

    fun from(table: String): QueryWriter {
        queryStatement += "FROM $table "
        return this
    }

    fun innerJoin(tableToInnerWithAlias: String, onFieldFirstTable: String, onFieldSecondTable: String): QueryWriter {
        queryStatement += "INNER JOIN $tableToInnerWithAlias ON $onFieldFirstTable = $onFieldSecondTable "
        return this
    }

    fun leftJoin(tableToInnerWithAlias: String, onFieldFirstTable: String, onFieldSecondTable: String): QueryWriter {
        queryStatement += "LEFT JOIN $tableToInnerWithAlias ON $onFieldFirstTable = $onFieldSecondTable "
        return this
    }

    fun rightJoin(tableToInnerWithAlias: String, onFieldFirstTable: String, onFieldSecondTable: String): QueryWriter {
        queryStatement += "RIGHT JOIN $tableToInnerWithAlias ON $onFieldFirstTable = $onFieldSecondTable "
        return this
    }

    fun outerJoin(tableToInnerWithAlias: String, onFieldFirstTable: String, onFieldSecondTable: String): QueryWriter {
        queryStatement += "FULL OUTER JOIN $tableToInnerWithAlias ON $onFieldFirstTable = $onFieldSecondTable "
        return this
    }



    fun where(field: String = ""): QueryWriter {
        queryStatement += "WHERE ${verifyField(field)}"
        return this
    }

    fun field(field: String, insertComma: Boolean = false): QueryWriter {
        queryStatement += "$field${comma(insertComma)}"
        return this
    }

    fun equalsValue(value: Any): QueryWriter {
        queryStatement += "= ${verifyValue(value)} "
        return this
    }

    fun equalsField(field: String): QueryWriter {
        queryStatement += "= $field "
        return this
    }

    fun diffValue(value: Any): QueryWriter {
        queryStatement += "<> ${verifyValue(value)} "
        return this
    }

    fun diffField(field: Any): QueryWriter {
        queryStatement += "<> $field "
        return this
    }

    fun isNull(field: String = ""): QueryWriter {
        queryStatement += "${verifyField(field)}IS NULL "
        return this
    }

    fun notNull(field: String = ""): QueryWriter {
        queryStatement += "${verifyField(field)}IS NOT NULL "
        return this
    }

    fun greaterThan(value: Any): QueryWriter {
        queryStatement += "> ${verifyValue(value)} "
        return this
    }

    fun lessThan(value: Any): QueryWriter {
        queryStatement += "< ${verifyValue(value)} "
        return this
    }

    fun inValues(values: List<Any>): QueryWriter {
        queryStatement += "IN (${values.toList()
            .map { verifyValue(it) }
            .joinToString { it }}) "
        return this
    }

    fun inValues(sql: String): QueryWriter {
        queryStatement += "IN ($sql) "
        return this
    }

    fun like(pattern: String): QueryWriter {
        queryStatement += "LIKE $pattern "
        return this
    }

    fun between(firstValue: Any, lastValue: Any): QueryWriter {
        if (firstValue.javaClass !== lastValue.javaClass) {
            throw IllegalArgumentException("firstValue and lastValue should be the same type")
        }
        queryStatement += "BETWEEN ${verifyValue(firstValue)} AND ${verifyValue(lastValue)} "
        return this
    }

    fun not(): QueryWriter {
        queryStatement += "NOT "
        return this
    }

    fun and(field: String = ""): QueryWriter {
        queryStatement += "AND ${verifyField(field)}"
        return this
    }

    fun openParentheses(): QueryWriter {
        queryStatement += "( "
        return this
    }

    fun closeParentheses(): QueryWriter {
        queryStatement += ")"
        return this
    }

    fun or(field: String = ""): QueryWriter {
        queryStatement += "OR ${verifyField(field)}"
        return this
    }

    private fun comma(insertComma: Boolean) = if(insertComma) ", " else " "

    fun orderBy(fields: String, sort: Sort = Sort.ASC): QueryWriter {
        queryStatement += "ORDER BY $fields ${sort.name} "
        return this
    }

    fun orderBy(fields: List<String>, sort: Sort = Sort.ASC): QueryWriter {
        queryStatement += "ORDER BY ${fields.joinToString()} ${sort.name} "
        return this
    }

    fun having(): QueryWriter {
        queryStatement += "HAVING "
        return this
    }

    fun exists(sql: String): QueryWriter {
        queryStatement += "EXISTS ($sql) "
        return this
    }

    fun any(operator: String, sql: String): QueryWriter {
        queryStatement += "$operator ANY ($sql) "
        return this
    }

    fun all(operator: String, sql: String): QueryWriter {
        queryStatement += "$operator ALL ($sql) "
        return this
    }

    fun groupBy(fields: String): QueryWriter {
        queryStatement += "GROUP BY $fields "
        return this
    }

    fun groupBy(fields: List<String>): QueryWriter {
        queryStatement += "GROUP BY ${fields.joinToString()} "
        return this
    }

    fun limit(offset: Int, quantity: Int): QueryWriter {
        queryStatement += "LIMIT $offset, $quantity"
        return this
    }

    fun union(): QueryWriter {
        queryStatement += "UNION "
        return this
    }

    fun unionAll(): QueryWriter {
        queryStatement += "UNION ALL "
        return this
    }

    fun insert(table: String, fields: String, values: String): QueryWriter {
        queryStatement += "INSERT INTO $table ($fields) VALUES($values) "
        return this
    }

    fun insert(table: String, fields: List<String>, values: List<Any>): QueryWriter {
        queryStatement += "INSERT INTO $table (${fields.joinToString()}) VALUES(${values.joinToString { verifyValue(it) }}) "
        return this
    }

    fun insert(table: String, values: String): QueryWriter {
        queryStatement += "INSERT INTO $table VALUES($values) "
        return this
    }

    fun insert(table: String, values: List<Any>): QueryWriter {
        queryStatement += "INSERT INTO $table VALUES(${ values.joinToString { verifyValue(it) } }) "
        return this
    }

    fun update(table: String, setValues: String): QueryWriter {
        queryStatement += "UPDATE $table SET $setValues "
        return this
    }

    fun update(table: String, fields: List<String>, values: List<Any>): QueryWriter {
        if(fields.size != values.size) {
            throw IllegalArgumentException("'UPDATE' needs the params fields and values with the size")
        }

        queryStatement += "UPDATE $table SET "

        for(i in fields.indices) {
            queryStatement += "${fields[i]} = ${verifyValue(values[i])}, "
        }

        queryStatement = queryStatement.substring(0, queryStatement.length - 2) + " "

        return this
    }

    fun delete(table: String): QueryWriter {
        queryStatement += "DELETE $table "
        return this
    }
        
    private fun verifyField(field: String) = if(field.isEmpty()) "" else "$field "

    private fun verifyValue(value: Any) = if (value is Number)  "$value" else "'$value'"

    fun query(): String {
        validateQuery()
        return queryStatement.trim()
    }

    private fun validateQuery() {
        val openedParentheses = queryStatement
            .filter { it == '(' }
            .length

        val closedParentheses = queryStatement
            .filter { it == ')' }
            .length

        when {
            queryStatement.contains("GROUP BY")
                    && !queryStatement.contains(Regex("COUNT|SUM|AVG")) ->
                throw IllegalArgumentException("'GROUP BY' without 'COUNT', 'SUM' or 'AVG'")

            !queryStatement.contains("GROUP BY") &&
                    queryStatement.contains(Regex("(, (COUNT|SUM|AVG)\\(.+\\)|(COUNT|SUM|AVG)\\(.+\\),)")) ->
                throw IllegalArgumentException("'COUNT', 'SUM' or 'AVG' without 'GROUP BY'")

            queryStatement.contains(Regex("UPDATE|DELETE"))
                    && !queryStatement.contains("WHERE") ->
                throw IllegalArgumentException("'UPDATE' or 'DELETE' without 'WHERE'")

            openedParentheses != closedParentheses -> throw IllegalArgumentException("There are parentheses not closed yet")
        }
    }

}
