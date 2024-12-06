package com.Admin.controller

import com.Admin.service.ShirtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
class ResourceController {
    @Autowired
    private val shirtSystem: ShirtService? = null

    @RequestMapping(value = ["/shirt/removeList"], method = [RequestMethod.POST])
    fun removeList(
        @RequestBody shirtShelf: ArrayList<String>,
    ): String {
        for (identity in shirtShelf) {
            val shirtIdentity = identity.substring(8)
            shirtSystem!!.removeOne(shirtIdentity.toLong())
        }

        return "delete success"
    }
}
