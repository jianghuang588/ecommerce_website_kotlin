package com.ClothStore.controller

import com.ClothStore.domain.*
import com.ClothStore.service.*
import com.ClothStore.utility.MailConstructor
import com.ClothStore.utility.USConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal
import java.time.LocalDate
import java.util.*

@Controller
class CheckoutController {
    private val deliveryAddress = ShippingAddress()
    private val statementAddress = BillingAddress()
    private val transaction = Payment()

    @Autowired
    private val userManagement: UserService? = null

    @Autowired
    private val cartOperations: CartItemService? = null

    @Autowired
    private val addressManagementService: ShippingAddressService? = null

    @Autowired
    private val transactionService: PaymentService? = null

    @Autowired
    private val addressVerificationService: BillingAddressService? = null

    @Autowired
    private val shippingOperations: UserShippingService? = null

    @Autowired
    private val userRemittanceService: UserPaymentService? = null

    @Autowired
    private val mailService: JavaMailSender? = null

    @Autowired
    private val cartProcessingService: ShoppingCartService? = null

    @Autowired
    private val purchaseService: OrderService? = null

    @Autowired
    private val emailBuilder: MailConstructor? = null

    @RequestMapping("/checkout")
    fun paymentProcess(
        @RequestParam("id") cartIdentifier: Long,
        @RequestParam(value = "missingRequiredField", required = false) incompleteInput: Boolean, Entity: Model,
        userIdentity: Principal,
    ): String {
        val client = userManagement!!.findByUsername(userIdentity.name)

        if (cartIdentifier !== client!!.shoppingCart!!.identity) {
            return "badRequestPage"
        }

        val purchaseList = cartOperations!!.findByShoppingCart(client!!.shoppingCart)

        if (purchaseList!!.isEmpty()) {
            Entity.addAttribute("emptyCart", true)
            return "forward:/shoppingCart/cart"
        }

        for (BasketItem in purchaseList) {
            if (BasketItem?.shirt!!.inStockNumber < BasketItem.quality) {
                Entity.addAttribute("notEnoughStock", true)
                return "forward:/shoppingCart/cart"
            }
        }

        val userDeliveryList: List<UserShipping>? = client.userShippingList
        val userTransactionList: List<UserPayment>? = client.userPaymentList

        Entity.addAttribute("userShippingList", userDeliveryList)
        Entity.addAttribute("userPaymentList", userTransactionList)

        if (userTransactionList!!.size == 0) {
            Entity.addAttribute("emptyPaymentList", true)
        } else {
            Entity.addAttribute("emptyPaymentList", false)
        }

        if (userDeliveryList!!.size == 0) {
            Entity.addAttribute("emptyShippingList", true)
        } else {
            Entity.addAttribute("emptyShippingList", false)
        }

        for (userShipping in userDeliveryList) {
            if (userShipping.isUserShippingDefault) {
                addressManagementService!!.setByUserShipping(userShipping, deliveryAddress)
            }
        }

        for (deliveryInformation in userTransactionList) {
            if (deliveryInformation.isDefaultPayment) {
                transactionService!!.setByUserPayment(deliveryInformation, transaction)
                addressVerificationService!!.setByUserBilling(deliveryInformation.userBilling, statementAddress)
            }
        }

        Entity.addAttribute("shippingAddress", deliveryAddress)
        Entity.addAttribute("payment", transaction)
        Entity.addAttribute("billingAddress", statementAddress)
        Entity.addAttribute("listItem", purchaseList)
        Entity.addAttribute("shoppingCart", client.shoppingCart)

        val ListOfStates = USConstants.listOfUSStatesCode
        Collections.sort(ListOfStates)
        Entity.addAttribute("stateList", ListOfStates)

        Entity.addAttribute("classActiveShipping", true)

        if (incompleteInput) {
            Entity.addAttribute("missingRequiredField", true)
        }

        return "checkout"
    }


    @RequestMapping(value = ["/checkout"], method = [RequestMethod.POST])
    fun orderSubmission(
        @ModelAttribute("shippingAddress") deliveryDestination: ShippingAddress,
        @ModelAttribute("billingAddress") registeredAddress: BillingAddress,
        @ModelAttribute("payment") transaction: Payment,
        @ModelAttribute("billingSameAsShipping") billingMatchesDeliveryAddress: String,
        @ModelAttribute("shippingMethod") shippingChoice: String,
        coreUser: Principal,
        entity: Model,
    ): String {
        val orderCart = userManagement!!.findByUsername(coreUser.name)!!.shoppingCart

        val basketItemList = cartOperations!!.findByShoppingCart(orderCart)
        entity.addAttribute("listItem", basketItemList)

        if (billingMatchesDeliveryAddress == "true") {
            registeredAddress.billingRecipientName = deliveryDestination.nameOfShippingAddress
            registeredAddress.billingStreetAddress1 = deliveryDestination.streetOneOfShippingAddress
            registeredAddress.billingStreetAddress2 = deliveryDestination.streetTwoOfShippingAddress
            registeredAddress.billingCityName = deliveryDestination.cityOfShippingAddress
            registeredAddress.billingStateCode = deliveryDestination.stateOfShippingAddress
            registeredAddress.billingCountryName = deliveryDestination.countryOfShippingAddress
            registeredAddress.billingPostalCode = deliveryDestination.zipcodeOfShippingAddress
        }

        if (deliveryDestination.streetOneOfShippingAddress!!.isEmpty() || deliveryDestination.cityOfShippingAddress!!.isEmpty()
            || deliveryDestination.stateOfShippingAddress!!.isEmpty()
            || deliveryDestination.nameOfShippingAddress!!.isEmpty()
            || deliveryDestination.zipcodeOfShippingAddress!!.isEmpty() || transaction.cardNumber!!.isEmpty()
            || transaction.cvc == 0 || registeredAddress.billingStreetAddress1!!.isEmpty()
            || registeredAddress.billingCityName!!.isEmpty() || registeredAddress.billingStateCode!!.isEmpty()
            || registeredAddress.billingRecipientName!!.isEmpty()
            || registeredAddress.billingPostalCode!!.isEmpty()
        ) return "redirect:/checkout?id=" + orderCart!!.identity + "&missingRequiredField=true"

        val clientUser = userManagement.findByUsername(coreUser.name)

        val checkoutOrder = purchaseService!!.createOrder(
            orderCart, deliveryDestination, registeredAddress, transaction,
            shippingChoice, clientUser
        )

        mailService!!.send(
            emailBuilder!!.constructOrderConfirmationEmail(
                clientUser!!,
                checkoutOrder!!,
                Locale.ENGLISH
            )
        )

        cartProcessingService!!.clearShoppingCart(orderCart)

        val currentDay = LocalDate.now()

        val anticipatedDeliveryDate = if (shippingChoice == "groundShipping") {
            currentDay.plusDays(5)
        } else {
            currentDay.plusDays(3)
        }

        entity.addAttribute("estimatedDeliveryDate", anticipatedDeliveryDate)

        return "orderSubmittedPage"
    }


    @RequestMapping("/setShippingAddress")
    fun configureShippingAddress(
        @RequestParam("userShippingId") deliveryAddressId: Long?, coreUser: Principal,
        entity: Model,
    ): String {
        val subscriber = userManagement!!.findByUsername(coreUser.name)

        val deliveryDetails = shippingOperations!!.findById(deliveryAddressId)

        if (deliveryDetails!!.user!!.id !== subscriber!!.id) {
            return "badRequestPage"
        } else {
            addressManagementService!!.setByUserShipping(deliveryDetails, deliveryAddress)

            val orderItemList = cartOperations!!.findByShoppingCart(subscriber!!.shoppingCart)

            entity.addAttribute("shippingAddress", deliveryAddress)
            entity.addAttribute("payment", transaction)
            entity.addAttribute("billingAddress", statementAddress)
            entity.addAttribute("listItem", orderItemList)
            entity.addAttribute("shoppingCart", subscriber.shoppingCart)

            val geographicList = USConstants.listOfUSStatesCode
            Collections.sort(geographicList)
            entity.addAttribute("stateList", geographicList)

            val userDeliveryList: List<UserShipping>? = subscriber.userShippingList
            val paymentOptionList: List<UserPayment>? = subscriber.userPaymentList

            entity.addAttribute("userShippingList", userDeliveryList)
            entity.addAttribute("userPaymentList", paymentOptionList)

            entity.addAttribute("shippingAddress", deliveryAddress)

            entity.addAttribute("classActiveShipping", true)

            if (paymentOptionList!!.size == 0) {
                entity.addAttribute("emptyPaymentList", true)
            } else {
                entity.addAttribute("emptyPaymentList", false)
            }

            entity.addAttribute("emptyShippingList", false)

            return "checkout"
        }
    }

    @RequestMapping("/setPaymentMethod")
    fun configurePaymentMethod(
        @RequestParam("userPaymentId") consumerPaymentId: Long?, primaryUser: Principal,
        entity: Model,
    ): String {
        val client = userManagement!!.findByUsername(primaryUser.name)
        val consumerPayment = userRemittanceService!!.findById(consumerPaymentId)
        val clientBilling = consumerPayment!!.userBilling

        if (consumerPayment.user!!.id !== client!!.id) {
            return "badRequestPage"
        } else {
            transactionService!!.setByUserPayment(consumerPayment, transaction)

            val purchaseItemList = cartOperations!!.findByShoppingCart(client!!.shoppingCart)

            addressVerificationService!!.setByUserBilling(clientBilling, statementAddress)

            entity.addAttribute("shippingAddress", deliveryAddress)
            entity.addAttribute("payment", transaction)
            entity.addAttribute("billingAddress", statementAddress)
            entity.addAttribute("listItem", purchaseItemList)
            entity.addAttribute("shoppingCart", client.shoppingCart)

            val categoryList = USConstants.listOfUSStatesCode
            Collections.sort(categoryList)
            entity.addAttribute("stateList", categoryList)

            val subscriberShippingList: List<UserShipping>? = client.userShippingList
            val participantPaymentList: List<UserPayment>? = client.userPaymentList

            entity.addAttribute("userShippingList", subscriberShippingList)
            entity.addAttribute("userPaymentList", participantPaymentList)

            entity.addAttribute("shippingAddress", deliveryAddress)

            entity.addAttribute("classActivePayment", true)

            entity.addAttribute("emptyPaymentList", false)

            if (subscriberShippingList!!.size == 0) {
                entity.addAttribute("emptyShippingList", true)
            } else {
                entity.addAttribute("emptyShippingList", false)
            }

            return "checkout"
        }
    }
}