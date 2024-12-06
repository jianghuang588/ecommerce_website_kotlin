package com.Admin.controller

import com.Admin.domain.User
import com.Admin.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


// it is web controller for specific page that allow user to
// create there navigation for that page.
@Controller
class HomeController {
    @Autowired
    private val Client: UserService? = null

    @RequestMapping("/")
    fun index(): String {
        return "redirect:/home"
    }

    @GetMapping("/home")
    fun home(entity: Model): String {
        val endUser: List<User?>? = Client?.getAllUsers()
        entity.addAttribute("userList", endUser)
        return "home"
    }
    @RequestMapping("/login")
    fun login(): String {
        return "login"
    }

    @PutMapping("/lockUser/{id}")
    fun lockUser(@PathVariable id: Long?): ResponseEntity<*> {
        try {
            Client?.lockUserById(id)
            return ResponseEntity.ok().build<Any>()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something is wrong with lock user")
        }
    }

    @PutMapping("/unlockUser/{id}")
    fun unlockUser(@PathVariable id: Long?): ResponseEntity<*> {
        try {
            Client?.unlockUserById(id)
            return ResponseEntity.ok("Unlocked user process")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something wrong with unlock user")
        }
    }

    @PutMapping("/lockUsers")
    fun lockUsers(@RequestBody payload: Map<String?, List<Long?>?>): ResponseEntity<*> {
        val identity = payload["userIds"]
        Client?.lockUsersByIds(identity)
        return ResponseEntity.ok().build<Any>()
    }

    @PutMapping("/unlockUsers")
    fun unlockUsers(@RequestBody payload: Map<String?, List<Long?>?>): ResponseEntity<*> {
        val identity = payload["userIds"]
        Client?.unlockUsersByIds(identity)
        return ResponseEntity.ok().build<Any>()
    }


}