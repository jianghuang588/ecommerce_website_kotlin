package com.ClothStore.service.impl

import com.ClothStore.domain.ShoppingCart
import com.ClothStore.repository.ShoppingCartRepository
import com.ClothStore.service.CartItemService
import com.ClothStore.service.ShoppingCartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class ShoppingCartServiceImpl : ShoppingCartService {

    @Autowired
    private val checkoutItemService: CartItemService? = null

    @Autowired
    private val cartStorage: ShoppingCartRepository? = null


    override fun updateShoppingCart(shoppingCart: ShoppingCart?): ShoppingCart? {
        var total = BigDecimal(0)

        val checkoutItems = checkoutItemService!!.findByShoppingCart(shoppingCart)

        for (item in checkoutItems!!) {
            if (item!!.shirt!!.inStockNumber > 0) {
                checkoutItemService!!.updateCartItem(item)
                total = total.add(item!!.pretotal)
            }
        }

        shoppingCart?.total = total

        if (shoppingCart != null) {
            cartStorage!!.save(shoppingCart)
        }

        return shoppingCart
    }

    override fun clearShoppingCart(shoppingCart: ShoppingCart?) {
        val selectedCartItems = checkoutItemService!!.findByShoppingCart(shoppingCart)

        for (cartItem in selectedCartItems!!) {
            cartItem!!.shoppingCart = null
            checkoutItemService.save(cartItem)
        }

        shoppingCart?.total = BigDecimal(0)

        shoppingCart?.let { cartStorage!!.save(it) }
    }


}
