package com.ClothStore.controller

import com.ClothStore.domain.CartItem
import com.ClothStore.domain.Shirt
import com.ClothStore.domain.ShoppingCart
import com.ClothStore.domain.User
import com.ClothStore.service.CartItemService
import com.ClothStore.service.ShirtService
import com.ClothStore.service.ShoppingCartService
import com.ClothStore.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal


@Controller
@RequestMapping("/shoppingCart")
class ShoppingCartController {

    @Autowired
    private val customerSupportService: UserService? = null

    @Autowired
    private val cartContentService: CartItemService? = null

    @Autowired
    private val cartManagementService: ShoppingCartService? = null

    @Autowired
    private val shirtService: ShirtService? = null


    @RequestMapping("/cart")
    fun shoppingCart(entity: Model, mainUser: Principal): String {
        val client: User? = customerSupportService!!.findByUsername(mainUser.name)
        val cart: ShoppingCart? = client?.shoppingCart

        val listOfItem: List<CartItem?>? = cartContentService?.findByShoppingCart(cart)

        cartManagementService?.updateShoppingCart(cart)

        entity.addAttribute("listItem", listOfItem)
        entity.addAttribute("shoppingCart", cart)

        return "shoppingCart"
    }

    @RequestMapping("/addItem")
    fun insertItem(
        @ModelAttribute("shirt") newShirt: Shirt?,
        @ModelAttribute("qty") amount: String,
        entity: Model, mainUser: Principal,
    ): String {
        var newShirt = newShirt
        val client: User? = customerSupportService!!.findByUsername(mainUser.name)
        newShirt = shirtService!!.findOne(newShirt!!.id)

        if (amount.toInt() > newShirt!!.inStockNumber) {
            entity.addAttribute("notEnoughStock", true)
            return "forward:/shirtDetail?id=" + newShirt.id
        }

        val cartItem: CartItem? = cartContentService?.addShirtToCartItem(newShirt, client, amount.toInt())
        entity.addAttribute("addshirtSuccess", true)

        return "forward:/shirtDetail?id=" + newShirt.id
    }


    @RequestMapping("/updateCartItem")
    fun editShoppingCart(
        @ModelAttribute("id") identity: Long?,
        @ModelAttribute("qty") amount: Int,
    ): String {
        val shoppingCartList: CartItem? = cartContentService?.findById(identity)
        shoppingCartList?.quality = amount
        cartContentService?.updateCartItem(shoppingCartList)

        return "forward:/shoppingCart/cart"
    }


    @RequestMapping("/removeItem")
    fun deleteItem(@RequestParam("id") identity: Long?): String {
        cartContentService?.removeCartItem(cartContentService.findById(identity))

        return "forward:/shoppingCart/cart"
    }


}