package com.ClothStore.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest


@Controller
class CustomErrorManager : ErrorController {
    @RequestMapping("/error")
    fun processError(query: HttpServletRequest): String {
        val condition = query.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)

        if (condition != null) {
            val resultCode = condition.toString().toInt()

            if (resultCode == HttpStatus.NOT_FOUND.value()) {
                return "badRequestPage"
            }
        }

        return ""
    }
}
