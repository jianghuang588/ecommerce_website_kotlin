package com.ClothStore.service.impl

import com.ClothStore.domain.*
import com.ClothStore.repository.CartItemRepository
import com.ClothStore.repository.ShirtToCartItemRepository
import com.ClothStore.service.CartItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class CartItemServiceImpl : CartItemService {
    @Autowired
    private val shoppingCartRepository: CartItemRepository? = null

    @Autowired
    private val shirtItemCartRepository: ShirtToCartItemRepository? = null

    override fun findByShoppingCart(cart: ShoppingCart?): List<CartItem?>? {
        return shoppingCartRepository?.findByShoppingCart(cart)
    }



    override fun updateCartItem(cartItem: CartItem?): CartItem? {
        var decimalValue = BigDecimal(cartItem?.shirt!!.ourPrice).multiply(BigDecimal(cartItem.quality))

        decimalValue = decimalValue.setScale(2, BigDecimal.ROUND_HALF_UP)
        cartItem.pretotal = decimalValue

        cartItem.let { shoppingCartRepository!!.save(it) }

        return cartItem
    }


    override fun addShirtToCartItem(shirt: Shirt?, user: User?, quantity: Int): CartItem? {
        val cartContents = findByShoppingCart(user?.shoppingCart)

        for (orderItem in cartContents!!) {
            if (shirt?.id === orderItem!!.shirt!!.id) {
                orderItem!!.quality = orderItem!!.quality + quantity
                if (shirt != null) {
                    orderItem!!.pretotal = BigDecimal(shirt.ourPrice).multiply(BigDecimal(quantity))
                }
                shoppingCartRepository!!.save(orderItem)
                return orderItem
            }
        }

        var cartLineItem = CartItem()
        cartLineItem.shoppingCart = user?.shoppingCart
        cartLineItem.shirt = shirt

        cartLineItem.quality = quantity
        cartLineItem.pretotal = BigDecimal(shirt!!.ourPrice).multiply(BigDecimal(quantity))
        cartLineItem = shoppingCartRepository!!.save(cartLineItem)

        val shirtToCartItem = ShirtToCartItem()
        shirtToCartItem.shirt = shirt
        shirtToCartItem.cartItem = cartLineItem
        shirtItemCartRepository?.save(shirtToCartItem)

        return cartLineItem
    }

    override fun findById(identity: Long?): CartItem {
        return shoppingCartRepository!!.findById(identity!!).orElse(null)!!
    }

    override fun removeCartItem(basketItem: CartItem?) {
        shirtItemCartRepository?.deleteByCartItem(basketItem)
        shoppingCartRepository!!.delete(basketItem!!)
    }

    override fun save(basketItem: Any): CartItem {
        return shoppingCartRepository!!.save<CartItem>(basketItem as CartItem)
    }

    override fun findByOrder(transaction: Order?): List<CartItem> {
        return shoppingCartRepository!!.findByOrder(transaction) as List<CartItem>
    }

}

