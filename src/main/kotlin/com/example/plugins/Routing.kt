package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: String,
)

@Serializable
data class Client(
    val id: Int,
    val name: String,
)

fun Application.configureRouting() {
    // local database
    val productsDatabase = mutableListOf<Product>()
    val clientsDataBase = mutableListOf<Client>()
    routing {
        get("/products") {
            call.respond(productsDatabase)
        }
        post("/new-product") {
            val newProduct = call.receive<Product>()
            productsDatabase.add(newProduct)
            call.respondText("product created successfully")
        }
        put ("/update-product/{id}") {
            // busca o id enviado na url - precisa do {} para identificar a variavel
            val id = call.parameters["id"]!!.toInt()
            // com o id busca o produto na lista
            val product = productsDatabase.find { it.id == id }
            // com o produto buscamos a posição dele na lista
            val index = productsDatabase.indexOf(product)
            // pega os dados atualizados do produto que foram enviado via json
            val newProduct = call.receive<Product>()

            // atualiza o produto na posição correta
            productsDatabase[index] = newProduct

            call.respondText("product updated")
        }
        delete ("/remove-product/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val product = productsDatabase.find { it.id == id }
            productsDatabase.remove(product)
            call.respondText("product was removed")
        }
        get ("/clients"){
            call.respond(clientsDataBase)
        }
        post ("/new-client"){
            val newClient = call.receive<Client>()
            clientsDataBase.add(newClient)
            call.respondText("new client add")
        }
        put ("/update-client/{id}"){
            val id = call.parameters["id"]!!.toInt()
            val client = clientsDataBase.find { it.id == id }
            val index = clientsDataBase.indexOf(client)
            val newClient = call.receive<Client>()

            clientsDataBase[index] = newClient

            call.respondText ("client was updated")
        }
        delete ("remove-client/{id}"){
            val id = call.parameters["id"]!!.toInt()
            val client = clientsDataBase.find {it.id == id}
            clientsDataBase.remove(client)
            call.respondText("Client was removed")
        }
    }
}
