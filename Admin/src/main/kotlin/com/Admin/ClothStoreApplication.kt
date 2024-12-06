package com.Admin

import com.Admin.domain.User
import com.Admin.domain.security.Role
import com.Admin.domain.security.UserRole
import com.Admin.service.UserService
import com.Admin.utility.SecurityUtility.passwordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class AdminApplication : CommandLineRunner {
	@Autowired
	private val userService: UserService? = null

	@Throws(Exception::class)
	override fun run(vararg args: String) {
		val Client = User()
		Client.username = "admin"
		Client.password = passwordEncoder().encode("admin")
		Client.email = "admin@gmail.com"
		val userEdit: MutableSet<UserRole?> = HashSet()
		val permissions = Role()
		permissions.roleId = 0
		permissions.name = "ROLE_USER"
		userEdit.add(UserRole(Client, permissions))
		userService!!.createUser(Client, userEdit)
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(AdminApplication::class.java, *args)
		}
	}
}
