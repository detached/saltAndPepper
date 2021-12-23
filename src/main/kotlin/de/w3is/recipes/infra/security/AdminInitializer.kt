package de.w3is.recipes.infra.security

import de.w3is.recipes.application.users.PlainPassword
import de.w3is.recipes.application.users.Role
import de.w3is.recipes.application.users.User
import de.w3is.recipes.application.users.UserRepository
import io.micronaut.context.annotation.Property
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton

@Singleton
class AdminInitializer(
    private val userRepository: UserRepository,
    @Property(name = "application.initialAdminPassword") private val initialAdminPassword: String
) {

    @EventListener
    fun setupInitialAdmin(event: StartupEvent) {

        val admin = userRepository.findUser("admin")
        if (admin == null) {
            userRepository.store(User.createNew("admin", PlainPassword(initialAdminPassword), Role.ADMIN))
        }
    }
}