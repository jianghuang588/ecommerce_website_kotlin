package com.ClothStore.controller

import com.ClothStore.domain.Shirt
import com.ClothStore.domain.User
import com.ClothStore.service.ShirtService
import com.ClothStore.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal


@Controller
class SearchController {
    @Autowired
    private val customerService: UserService? = null

    @Autowired
    private val clothingService: ShirtService? = null


    @RequestMapping("/searchByCategory")
    fun browseByCategory(
        @RequestParam("category") type: String,
        entity: Model, mainUser: Principal?,
    ): String {
        if (mainUser != null) {
            val username = mainUser.name
            val client: User? = customerService!!.findByUsername(username)
            entity.addAttribute("user", client)
        }

        var currentCategoryClass = "active$type"
        currentCategoryClass = currentCategoryClass.replace("\\s+".toRegex(), "")
        currentCategoryClass = currentCategoryClass.replace("&".toRegex(), "")
        entity.addAttribute(currentCategoryClass, true)

        val shirtList: List<Shirt?>? = clothingService?.findByCategory(type)

        if (shirtList?.isEmpty() == true) {
            entity.addAttribute("emptyList", true)
            return "shirtShelf"
        }

        entity.addAttribute("shirtList", shirtList)

        return "shirtShelf"
    }

    @RequestMapping("/searchShirt")
    fun searchShirt(
        @ModelAttribute("keyword") identifier: String?,
        mainUser: Principal?, entity: Model,
    ): String {
        if (mainUser != null) {
            val username = mainUser.name
            val userID: User? = customerService!!.findByUsername(username)
            entity.addAttribute("user", userID)
        }

        val shirtList: List<Shirt?>? = clothingService?.blurrySearch(identifier)

        if (shirtList?.isEmpty() == true) {
            entity.addAttribute("emptyList", true)
            return "shirtShelf"
        }

        entity.addAttribute("shirtList", shirtList)

        return "shirtShelf"
    }

}
