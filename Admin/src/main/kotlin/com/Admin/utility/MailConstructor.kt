package com.Admin.utility

import com.Admin.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import java.util.*


@Component
class MailConstructor {
    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val templateRenderer: TemplateEngine? = null


    fun constructResetTokenEmail(
        relativePath: String, region: Locale?, key: String, client: User,
        passwordToken: String,
    ): SimpleMailMessage {
        // url


        val path = "$relativePath/newUser?token=$key"

        val text = ("""
     
     To verify your email and update your profile, click the link provided. Your password is: 
     $passwordToken
     """.trimIndent())
        val mail = SimpleMailMessage()
        mail.setTo(client.email)
        mail.subject = "Huang's ClothStore - New User"
        mail.text = path + text
        mail.from = environment!!.getProperty("support.email")
        return mail
    }
    /*
    fun constructOrderConfirmationEmail(
        client: User,
        transaction: Order,
        localizationSetting: Locale?,
    ): MimeMessagePreparator {
        val scope: Context = Context()
        scope.setVariable("order", transaction)
        scope.setVariable("user", client)
        scope.setVariable("cartItemList", transaction.getCartItemList())
        val message = templateRenderer!!.process("orderConfirmationEmailTemplate", scope)

        val messageBuilder =
            MimeMessagePreparator { formattedMessage ->
                val mail = MimeMessageHelper(formattedMessage)
                mail.setTo(client.getEmail())
                mail.setSubject("Order Confirmation - " + transaction.getIdentifyer())
                mail.setText(message, true)
                mail.setFrom(InternetAddress("jianghuang588@gmail.com"))
            }

        return messageBuilder
    }
    */


}