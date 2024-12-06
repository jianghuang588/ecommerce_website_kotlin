package com.ClothStore

import com.ClothStore.domain.User
import com.ClothStore.domain.security.Role
import com.ClothStore.domain.security.UserRole
import com.ClothStore.service.UserService
import com.ClothStore.utility.SecurityUtility.passwordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class ClothStoreApplication : CommandLineRunner {
	@Autowired
	private val userService: UserService? = null

	@Throws(Exception::class)
	override fun run(vararg args: String) {
		val Client: User = User()
		Client.firstName = "Huang"
		Client.lastName = "Jiang"
		Client.username = "j"
		Client.password = passwordEncoder().encode("p")
		Client.email = "jianghuangsecond@gmail.com"
		val userEdit: MutableSet<UserRole?> = HashSet()
		val permissions: Role = Role()
		permissions.roleId = 1
		permissions.name = "ROLE_USER"
		userEdit.add(UserRole(Client, permissions))
		userService!!.createUser(Client, userEdit)
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(ClothStoreApplication::class.java, *args)
		}
	}
}
