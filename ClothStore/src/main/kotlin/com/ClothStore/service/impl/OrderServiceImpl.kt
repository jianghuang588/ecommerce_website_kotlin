package com.ClothStore.service.impl

import com.ClothStore.domain.*
import com.ClothStore.repository.OrderRepository
import com.ClothStore.service.CartItemService
import com.ClothStore.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl : OrderService {
    @Autowired
    private val purchaseRepository: OrderRepository? = null

    @Autowired
    private val itemInCartService: CartItemService? = null

    /*
      cart: ShoppingCart,
        deliveryAddress: ShippingAddress,
        invoiceAddress: BillingAddress,
        transaction: Payment,
        shipmentOption: String?,
        client: User?,
     */
    @Synchronized
    override fun createOrder(
        shoppingCart: ShoppingCart?,
        shippingAddress: ShippingAddress?,
        billingAddress: BillingAddress?,
        payment: Payment?,
        shipmentOption: String?,
        client: User?
    ): Order? {
        var sale = Order()
        sale.billingAddress = billingAddress
        sale.statusOfOrder = "created"
        sale.payment = payment
        sale.shippingAddress = shippingAddress
        sale.methodOfShipping = shipmentOption

        val cartEntries = itemInCartService!!.findByShoppingCart(shoppingCart)

        for (basket in cartEntries!!) {
            val shirt = basket!!.shirt
            basket.order = sale
            shirt!!.inStockNumber = shirt.inStockNumber - basket.quality
        }

        sale.cartItemList = cartEntries as List<CartItem>?
        sale.dateOfOrder = Calendar.getInstance().time
        sale.totalOfOrder = shoppingCart!!.total!!
        shippingAddress!!.order = sale
        billingAddress!!.purchaseRequest = sale
        payment!!.order = sale
        sale.user = client
        sale = purchaseRepository!!.save(sale)

        return sale
    }

    override fun findOne(id: Long?): Order? {
        return id?.let { purchaseRepository!!.findById(it).orElse(null) }
    }
}