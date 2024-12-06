package com.ClothStore.controller

import com.ClothStore.domain.*
import com.ClothStore.domain.security.PasswordResetToken
import com.ClothStore.domain.security.Role
import com.ClothStore.domain.security.UserRole
import com.ClothStore.service.*
import com.ClothStore.service.impl.UserSecurityService
import com.ClothStore.utility.MailConstructor
import com.ClothStore.utility.SecurityUtility.passwordEncoder
import com.ClothStore.utility.SecurityUtility.randomPassword
import com.ClothStore.utility.USConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam


// it is web controller for specific page that allow user to
// create there navigation for that page.
@Controller
class HomeController {

    @Autowired
    private val clientService: UserService? = null

    @Autowired
    private val accountSecurityService: UserSecurityService? = null

    @Autowired
    private val mailBuilder: MailConstructor? = null

    @Autowired
    private val mailDispatcher: JavaMailSender? = null

    @Autowired
    private val shirtService: ShirtService? = null

    @Autowired
    private val accountPaymentService: UserPaymentService? = null

    @Autowired
    private val consumerShippingService: UserShippingService? = null

    @Autowired
    private val purchaseService: OrderService? = null

    @Autowired
    private val checkoutItemService: CartItemService? = null

    // give desire url name and return to the user select url
    @RequestMapping("/")
    fun home(): String {
        return "home"
    }

    @RequestMapping("/hours")
    fun hours(): String {
        return "hours"
    }

    @RequestMapping("/faq")
    fun faq(): String {
        return "faq"
    }

    @RequestMapping("/login")
    fun login(model: Model): String {
        // model is the container that hold data that user want to pass to the html
        // allow user to see the login page when user first open it
        model.addAttribute("classActiveLogin", true)
        return "myAccount"
    }


    @RequestMapping("/forgetPassword")
    fun resetPassword(
        inquiry: HttpServletRequest,
        @ModelAttribute("email") electronicMail: String?,
        entity: Model,
    ): String {
        entity.addAttribute("classActiveForgetPassword", true)

        val client: User? = clientService?.findByEmail(electronicMail)!!

        if (client == null) {
            entity.addAttribute("emailNotExist", true)
            return "myAccount"
        }

        val securityKey = randomPassword()

        val encodedPassword = passwordEncoder().encode(securityKey)
        client.password = encodedPassword
        clientService.save(client)

        val accessToken = UUID.randomUUID().toString()
        clientService?.createPasswordResetTokenForUser(client, accessToken)

        val appEndpoint = "http://" + inquiry.serverName + ":" + inquiry.serverPort + inquiry.contextPath
        val updatedEmail: SimpleMailMessage = mailBuilder!!.constructResetTokenEmail(
            appEndpoint, inquiry.locale, accessToken, client,
            securityKey
        )

        mailDispatcher?.send(updatedEmail)

        entity.addAttribute("forgetPasswordEmailSent", "true")

        return "myAccount"
    }

    @RequestMapping("/newUser")
    fun newUser(region: Locale?, @RequestParam("token") credential: String?, entity: Model): String {
        val submitToken: PasswordResetToken? = clientService!!.getPasswordResetToken(credential)

        if (submitToken == null) {
            val note = "incorrect note"
            entity.addAttribute("message", note)
            return "redirect:/badRequest"
        }

        val client = submitToken.user
        val userID = client!!.getUsername()

        val memberDetails: UserDetails = accountSecurityService!!.loadUserByUsername(userID)

        val verification: Authentication = UsernamePasswordAuthenticationToken(
            memberDetails, memberDetails.password,
            memberDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = verification

        entity.addAttribute("user", client)

        entity.addAttribute("classActiveEdit", true)

        return "myProfile"
    }



    // create new  user
    @RequestMapping(value = ["/newUser"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun newAccountPost(
        inquiry: HttpServletRequest, @ModelAttribute("email") clientEmail: String?,
        @ModelAttribute("username") loginName: String?, entity: Model,
    ): String {
        entity.addAttribute("classActiveNewAccount", true)
        entity.addAttribute("email", clientEmail)
        entity.addAttribute("username", loginName)

        if (clientService?.findByUsername(loginName) != null) {
            entity.addAttribute("usernameExists", true)

            return "myAccount"
        }

        if (clientService?.findByEmail(clientEmail) != null) {
            entity.addAttribute("emailExists", true)

            return "myAccount"
        }

        val account: User = User()

        account.username = loginName.toString()

        account.email = clientEmail.toString()
        val securityKey = randomPassword()

        val hashedPassword = passwordEncoder().encode(securityKey)
        account.password = hashedPassword

        val task: Role = Role()
        task.roleId = 1

        task.name = "ROLE_USER"
        val clientRoles: MutableSet<UserRole> = HashSet()
        clientRoles.add(UserRole(account, task))

        clientService?.createUser(account, clientRoles)

        val authenticationToken = UUID.randomUUID().toString()
        clientService!!.createPasswordResetTokenForUser(account, authenticationToken)

        val serviceUrl = "http://" + inquiry.serverName + ":" + inquiry.serverPort + inquiry.contextPath

        val electronicMail: SimpleMailMessage = mailBuilder!!.constructResetTokenEmail(
            serviceUrl, inquiry.locale, authenticationToken, account,
            securityKey
        )

        mailDispatcher!!.send(electronicMail)

        entity.addAttribute("emailSent", "true")

        return "myAccount"
    }


    @RequestMapping("/myProfile")
    fun myInformation(entity: Model, mainUser: Principal): String {
        val client = clientService!!.findByUsername(mainUser.name)
        entity.addAttribute("user", client)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)
        val userDeliever: UserShipping = UserShipping()
        entity.addAttribute("userShipping", userDeliever)
        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("listOfShippingAddresses", true)
        val areaUSA: List<String> = USConstants.listOfUSStatesCode
        Collections.sort(areaUSA)
        entity.addAttribute("stateList", areaUSA)
        entity.addAttribute("classActiveEdit", true)

        return "myProfile"
    }

    @RequestMapping("/listOfCreditCards")
    fun userCredit(entity: Model, mainCharacter: Principal): String {
        val client = clientService!!.findByUsername(mainCharacter.name)
        entity.addAttribute("user", client)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("classActiveBilling", true)
        entity.addAttribute("listOfShippingAddresses", true)

        return "myProfile"
    }

    @RequestMapping("/addNewCreditCard")
    fun userCreditCardAdd(entity: Model, mainUser: Principal): String {
        val client = clientService!!.findByUsername(mainUser.name)
        entity.addAttribute("user", client)

        entity.addAttribute("addNewCreditCard", true)
        entity.addAttribute("classActiveBilling", true)
        entity.addAttribute("listOfShippingAddresses", true)

        val userCharge = UserBilling()
        val userCost = UserPayment()

        entity.addAttribute("userBilling", userCharge)
        entity.addAttribute("userPayment", userCost)

        val zipUSA = USConstants.listOfUSStatesCode
        Collections.sort(zipUSA)
        entity.addAttribute("stateList", zipUSA)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }

    @RequestMapping("/addNewShippingAddress")
    fun storeNewShippingAddress(entity: Model, mainUser: Principal): String {
        val client = clientService!!.findByUsername(mainUser.name)
        entity.addAttribute("user", client)

        entity.addAttribute("addNewShippingAddress", true)
        entity.addAttribute("classActiveShipping", true)
        entity.addAttribute("listOfCreditCards", true)

        val clientShipping = UserShipping()

        entity.addAttribute("userShipping", clientShipping)

        val areaList = USConstants.listOfUSStatesCode
        Collections.sort(areaList)

        entity.addAttribute("stateList", areaList)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }


    @RequestMapping("/shirtshelf")
    fun clothingShelf(entity: Model, mainUser: Principal?): String {
        if (mainUser != null) {
            val username = mainUser.name
            val client = clientService?.findByUsername(username)
            entity.addAttribute("user", client)
        }

        val shirtList = shirtService?.findAll()

        entity.addAttribute("shirtList", shirtList)
        entity.addAttribute("activeAll", true)

        return "shirtShelf"
    }

    @RequestMapping("/shirtDetail")
    fun outfitDetail(@PathParam("id") id: Long?, entity: Model, mainUser: Principal?): String {
        if (mainUser != null) {
            val userID = mainUser.name
            val client = clientService!!.findByUsername(userID)
            entity.addAttribute("user", client)
        }

        val shirt: Shirt? = shirtService?.findOne(id)

        entity.addAttribute("shirt", shirt)

        val countList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        entity.addAttribute("qtyList", countList)
        entity.addAttribute("qty", 1)

        return "shirtDetail"
    }

    @RequestMapping(value = ["/addNewCreditCard"], method = [RequestMethod.POST])
    fun registerNewCreditCard(
        @ModelAttribute("userPayment") clientPayment: UserPayment?,
        @ModelAttribute("userBilling") memberBilling: UserBilling?, mainUser: Principal, entity: Model,
    ): String {
        val client = clientService!!.findByUsername(mainUser.name)
        clientService.updateUserBilling(memberBilling, clientPayment, client)

        entity.addAttribute("user", client)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("classActiveBilling", true)
        entity.addAttribute("listOfShippingAddresses", true)
        entity.addAttribute("orderList", client?.orderList)
        return "myProfile"
    }

    @RequestMapping("/removeCreditCard")
    fun deleteCreditCard(@ModelAttribute("id") identification: Long?, mainUser: Principal, entity: Model): String {
        val client = clientService!!.findByUsername(mainUser.name)
        val customerTransaction: UserPayment? = accountPaymentService?.findById(identification)

        if (client?.id !== customerTransaction?.user!!.id) {
            return "badRequestPage"
        } else {
            entity.addAttribute("user", client)
            accountPaymentService?.removeById(identification)

            entity.addAttribute("listOfCreditCards", true)
            entity.addAttribute("classActiveBilling", true)
            entity.addAttribute("listOfShippingAddresses", true)

            entity.addAttribute("userPaymentList", client?.userPaymentList)
            entity.addAttribute("userShippingList", client?.userShippingList)

            return "myProfile"
        }
    }

    @RequestMapping(value = ["/setDefaultPayment"], method = [RequestMethod.POST])
    fun setPrimaryPayment(
        @ModelAttribute("defaultUserPaymentId") standardPaymentId: Long?, mainUser: Principal,
        entity: Model,
    ): String {
        val client = clientService!!.findByUsername(mainUser.name)
        clientService.setUserDefaultPayment(standardPaymentId, client)

        entity.addAttribute("user", client)
        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("classActiveBilling", true)
        entity.addAttribute("listOfShippingAddresses", true)

        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }

    @RequestMapping("/listOfShippingAddresses")
    fun userShipping(entity: Model, mainUser: Principal): String {
        val client = clientService!!.findByUsername(mainUser.name)
        entity.addAttribute("user", client)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("classActiveShipping", true)
        entity.addAttribute("listOfShippingAddresses", true)

        return "myProfile"
    }

    @RequestMapping(value = ["/addNewShippingAddress"], method = [RequestMethod.POST])
    fun insertNewShippingAddressPost(
        @ModelAttribute("userShipping") userShippingData: UserShipping?,
        mainUser: Principal, entity: Model,
    ): String {
        val client = clientService!!.findByUsername(mainUser.name)
        clientService.updateUserShipping(userShippingData, client)

        entity.addAttribute("user", client)
        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("listOfShippingAddresses", true)
        entity.addAttribute("classActiveShipping", true)
        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }

    @RequestMapping("/updateUserShipping")
    fun editUserShippingInformation(
        @ModelAttribute("id") indentification: Long?,
        mainUser: Principal,
        entity: Model,
    ): String {
        val client = clientService!!.findByUsername(mainUser.name)
        val userDeliever: UserShipping? = consumerShippingService?.findById(indentification)

        if (client?.id !== userDeliever?.user!!.id) {
            return "badRequestPage"
        } else {
            entity.addAttribute("user", client)

            entity.addAttribute("userShipping", userDeliever)

            val zipUSA = USConstants.listOfUSStatesCode
            Collections.sort(zipUSA)
            entity.addAttribute("stateList", zipUSA)

            entity.addAttribute("addNewShippingAddress", true)
            entity.addAttribute("classActiveShipping", true)
            entity.addAttribute("listOfCreditCards", true)

            entity.addAttribute("userPaymentList", client?.userPaymentList)
            entity.addAttribute("userShippingList", client?.userShippingList)

            return "myProfile"
        }
    }

    @RequestMapping(value = ["/setDefaultShippingAddress"], method = [RequestMethod.POST])
    fun setPrimaryShippingAddress(
        @ModelAttribute("defaultShippingAddressId") mainShippingId: Long?,
        mainUser: Principal, entity: Model,
    ): String {
        val client = clientService!!.findByUsername(mainUser.name)
        clientService.setUserDefaultShipping(mainShippingId, client)

        entity.addAttribute("user", client)
        entity.addAttribute("listOfCreditCards", true)
        entity.addAttribute("classActiveShipping", true)
        entity.addAttribute("listOfShippingAddresses", true)

        entity.addAttribute("userPaymentList", client?.userPaymentList)
        entity.addAttribute("userShippingList", client?.userShippingList)
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }

    @RequestMapping("/removeUserShipping")
    fun deleteUserShipping(@ModelAttribute("id") identification: Long?, mainUser: Principal, entity: Model): String {
        val client = clientService!!.findByUsername(mainUser.name)
        val memberShipping = consumerShippingService!!.findById(identification)

        if (client?.id !== memberShipping!!.user!!.id) {
            return "badRequestPage"
        } else {
            entity.addAttribute("user", client)

            consumerShippingService.removeById(identification)

            entity.addAttribute("listOfShippingAddresses", true)
            entity.addAttribute("classActiveShipping", true)

            entity.addAttribute("userPaymentList", client?.userPaymentList)
            entity.addAttribute("userShippingList", client?.userShippingList)
            entity.addAttribute("orderList", client?.orderList)

            return "myProfile"
        }
    }

    @RequestMapping(value = ["/updateUserInfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun modifyUserInfo(
        @ModelAttribute("user") client: User, @ModelAttribute("newPassword") updatedPassword: String?,
        entity: Model,
    ): String {
        val existingUser: User = clientService?.findById(client.id) ?: throw java.lang.Exception("User did not exist")

        /* confirm the existence of the email */
        if (clientService!!.findByEmail(client.email) != null) {
            if (clientService.findByEmail(client.email)!!.id !== existingUser.id) {
                entity.addAttribute("emailExists", true)
                return "myProfile"
            }
        }

        /* verify the current username */
        if (clientService.findByUsername(client.username) != null) {
            if (clientService.findByUsername(client.username)!!.id !== existingUser.id) {
                entity.addAttribute("usernameExists", true)
                return "myProfile"
            }
        }

        /* Change the password */
        if (updatedPassword != null && !updatedPassword.isEmpty() && (updatedPassword != "")) {
            val credentialHasher = passwordEncoder()
            val databaseAccessKey = existingUser.password
            if (credentialHasher.matches(client.password, databaseAccessKey)) {
                existingUser.password = credentialHasher.encode(updatedPassword)
            } else {
                entity.addAttribute("incorrectPassword", true)

                return "myProfile"
            }
        }

        existingUser.firstName = client.firstName
        existingUser.lastName = client.lastName
        existingUser.username = client.username
        existingUser.email = client.email
        clientService.save(existingUser)

        entity.addAttribute("updateSuccess", true)

        entity.addAttribute("user", existingUser)
        entity.addAttribute("classActiveEdit", true)

        entity.addAttribute("listOfShippingAddresses", true)
        entity.addAttribute("listOfCreditCards", true)

        val profileDetails = accountSecurityService!!.loadUserByUsername(existingUser.username)

        val accessAuthentication: Authentication = UsernamePasswordAuthenticationToken(
            profileDetails, profileDetails.password,
            profileDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = accessAuthentication
        entity.addAttribute("orderList", client?.orderList)

        return "myProfile"
    }

    @RequestMapping("/orderDetail")
    fun purchaseDetail(@RequestParam("id") identification: Long?, mainUser: Principal, entity: Model): String {
        val client = clientService!!.findByUsername(mainUser.name)
        val transaction: Order = purchaseService?.findOne(identification)!!

        if (transaction.user?.id !== client?.id) {
            return "badRequestPage"
        } else {
            val cartContents: List<CartItem?>? = checkoutItemService!!.findByOrder(transaction)
            entity.addAttribute("listItem", cartContents)
            entity.addAttribute("user", client)
            entity.addAttribute("order", transaction)

            entity.addAttribute("userPaymentList", client?.userPaymentList)
            entity.addAttribute("userShippingList", client?.userShippingList)
            entity.addAttribute("orderList", client?.orderList)

            val memberShipping = UserShipping()
            entity.addAttribute("userShipping", memberShipping)

            val regionList = USConstants.listOfUSStatesCode
            Collections.sort(regionList)
            entity.addAttribute("stateList", regionList)

            entity.addAttribute("listOfShippingAddresses", true)
            entity.addAttribute("classActiveOrders", true)
            entity.addAttribute("listOfCreditCards", true)
            entity.addAttribute("displayOrderDetail", true)

            return "myProfile"
        }
    }

    @RequestMapping("/shirtOne")
    fun shirtOne(): String {
        return "shirtOne"
    }

    @RequestMapping("/shirtTwo")
    fun shirtTwo(): String {
        return "shirtTwo"
    }

    @RequestMapping("/shirtThree")
    fun shirtThree(): String {
        return "shirtThree"
    }

    @RequestMapping("/shirtFour")
    fun shirtFour(): String {
        return "shirtFour"
    }

    @RequestMapping("/shirtFive")
    fun shirtFive(): String {
        return "shirtFive"
    }

    @RequestMapping("/shirtSix")
    fun shirtSix(): String {
        return "shirtSix"
    }

    @RequestMapping("/shirtSeven")
    fun shirtSeven(): String {
        return "shirtSeven"
    }

    @RequestMapping("/shirtEight")
    fun shirtEight(): String {
        return "shirtEight"
    }

    @RequestMapping("/shirtNine")
    fun shirtNine(): String {
        return "shirtNine"
    }

    @RequestMapping("/shirtTen")
    fun shirtTen(): String {
        return "shirtTen"
    }

    @RequestMapping("/shirtEleven")
    fun shirtEleven(): String {
        return "shirtEleven"
    }

    @RequestMapping("/shirtTwelve")
    fun shirtTwelve(): String {
        return "shirtTwelve"
    }

    @RequestMapping("/shirtThirteen")
    fun shirtThirteen(): String {
        return "shirtThirteen"
    }

    @RequestMapping("/shirtFourteen")
    fun shirtFourteen(): String {
        return "shirtFourteen"
    }

    @RequestMapping("/shirtFifteen")
    fun shirtFifteen(): String {
        return "shirtFifteen"
    }

    @RequestMapping("/shirtSixteen")
    fun shirtSixteen(): String {
        return "shirtSixteen"
    }

    @RequestMapping("/shirtSeventeen")
    fun shirtSeventeen(): String {
        return "shirtSeventeen"
    }

    @RequestMapping("/shirtEighteen")
    fun shirtEighteen(): String {
        return "shirtEighteen"
    }




}