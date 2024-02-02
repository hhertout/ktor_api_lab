package api.dao

import api.models.Customer

interface CustomerDao {
    suspend fun addCustomer(name: String, email:String): Customer?
    suspend fun getCustomers(): List<Customer>
    suspend fun findCustomerById(id: Int): Customer?
    suspend fun updateCustomer(id: Int, name: String, email: String): Boolean
    suspend fun deleteCustomer(id: Int): Boolean
}