package com.Admin.controller

import com.Admin.domain.Shirt
import com.Admin.service.ShirtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/shirt")
class ShirtController {

    @Autowired
    private val shirtSystem: ShirtService? = null

    @RequestMapping(value = ["/add"], method = [RequestMethod.GET])
    fun addBook(entity: Model): String {
        val shirt = Shirt()
        entity.addAttribute("shirt", shirt)
        return "addShirt"
    }

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addBookPost(@ModelAttribute("shirt") shirt: Shirt, request: HttpServletRequest?): String {
        shirtSystem?.save(shirt)

        val shirtImage = shirt.shirtImage

        try {
            val bit = shirtImage!!.bytes
            val image = shirt.id.toString() + ".png"
            val bufferedOutputStream = BufferedOutputStream(
                FileOutputStream(File("src/main/resources/static/image/shirt/$image"))
            )
            bufferedOutputStream.write(bit)
            bufferedOutputStream.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return "redirect:shirtList"
    }

    @RequestMapping("/shirtList")
    fun shirtList(entity: Model): String {
        val shirtShelf: List<Shirt?>? = shirtSystem?.findAll()
        entity.addAttribute("shirtList", shirtShelf)
        return "shirtList"
    }


    @RequestMapping("/shirtInfo")
    fun shirtInfo(@RequestParam("id") id: Long?, entity: Model): String {
        val shirt: Shirt? = shirtSystem?.findOne(id)
        entity.addAttribute("shirt", shirt)
        return "shirtInfo"
    }

    @RequestMapping("/updateShirt")
    fun updateBook(@RequestParam("id") id: Long?, entity: Model): String {
        val shirt = shirtSystem!!.findOne(id)
        entity.addAttribute("shirt", shirt)

        return "updateShirt"
    }


    @RequestMapping(value = ["/updateShirt"], method = [RequestMethod.POST])
    fun updateShirtPost(@ModelAttribute("shirt") shirt: Shirt, request: HttpServletRequest?): String {
        shirtSystem!!.save(shirt)

        val shirtImage = shirt.shirtImage

        if (!shirtImage!!.isEmpty) {
            try {
                val bit = shirtImage!!.bytes
                val image = shirt.id.toString() + ".png"

                Files.delete(Paths.get("src/main/resources/static/image/shirt/$image"))

                val stream = BufferedOutputStream(
                    FileOutputStream(File("src/main/resources/static/image/shirt/$image"))
                )
                stream.write(bit)
                stream.close()
            } catch (exception: java.lang.Exception) {
                exception.printStackTrace()
            }
        }

        return "redirect:/shirt/shirtInfo?id=" + shirt.id
    }


    @RequestMapping(value = ["/remove"], method = [RequestMethod.POST])
    fun remove(@ModelAttribute("id") id: String, entity: Model): String {
        val digitSegement = id.substring("oneShirt-".length)

        val shirtIdentiy = digitSegement.toLong()

        shirtSystem?.removeOne(shirtIdentiy)

        val shirtShelf = shirtSystem!!.findAll()
        entity.addAttribute("shirtList", shirtShelf)

        return "redirect:/shirt/shirtList"
    }


}