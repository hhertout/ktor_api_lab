package api.dao

import api.config.DatabaseFactory.dbQuery
import api.models.Customer
import api.models.Customers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class CustomerDaoImpl: CustomerDao {
    override suspend fun addCustomer(name: String, email:String): Customer? = dbQuery {
        val insert = Customers.insert {
            it[Customers.name] = name
            it[Customers.email]= email
        }
        insert.resultedValues?.singleOrNull()?.let(::resultRowToCustomer)
    }

    override suspend fun getCustomers(): List<Customer> = dbQuery {
        Customers.selectAll().map(::resultRowToCustomer)
    }

    override suspend fun findCustomerById(id: Int): Customer? = dbQuery {
        Customers.select { Customers.id eq id}
            .map(::resultRowToCustomer)
            .singleOrNull()
    }

    private fun resultRowToCustomer(row : ResultRow) = Customer(
        id = row[Customers.id],
        name = row[Customers.name],
        email = row[Customers.email]
    )
}