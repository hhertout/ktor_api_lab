package api.dao

import api.config.DatabaseFactory.dbQuery
import api.models.Customer
import api.models.Customers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CustomerDaoImpl : CustomerDao {
    override suspend fun addCustomer(name: String, email: String): Customer? = dbQuery {
        val insert = Customers.insert {
            it[Customers.name] = name
            it[Customers.email] = email
        }
        insert.resultedValues?.singleOrNull()?.let(::resultRowToCustomer)
    }

    override suspend fun getCustomers(): List<Customer> = dbQuery {
        Customers.selectAll().map(::resultRowToCustomer)
    }

    override suspend fun findCustomerById(id: Int): Customer? = dbQuery {
        Customers.select { Customers.id eq id }
            .map(::resultRowToCustomer)
            .singleOrNull()
    }

    override suspend fun updateCustomer(id: Int, name: String, email: String): Boolean = dbQuery {
        Customers.update({ Customers.id eq id }) {
            it[Customers.name] = name
            it[Customers.email] = email
        } > 0
    }

    override suspend fun deleteCustomer(id: Int): Boolean {
        return dbQuery {
            Customers.deleteWhere { Customers.id eq id }
        } > 0
    }

    private fun resultRowToCustomer(row: ResultRow) = Customer(
        id = row[Customers.id],
        name = row[Customers.name],
        email = row[Customers.email]
    )
}