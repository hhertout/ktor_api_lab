package api.dao

import api.models.Customer

interface CustomerDao {
    suspend fun addCustomer(name: String, email:String): Customer?
    suspend fun getCustomers(): List<Customer>
}