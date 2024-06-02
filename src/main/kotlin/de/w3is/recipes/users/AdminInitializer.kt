package de.w3is.recipes.users

import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.Role
import de.w3is.recipes.users.model.User
import io.micronaut.context.annotation.Property
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton

@Singleton
class AdminInitializer(
    private val userRepository: UserRepository,
    @Property(name = "application.initialAdminPassword") private val initialAdminPassword: String,
) {
    @EventListener
    @Suppress("UNUSED_PARAMETER")
    fun setupInitialAdmin(event: StartupEvent) {
        val admin = userRepository.findUser("admin")
        if (admin == null) {
            userRepository.store(User.createNew("admin", PlainPassword(initialAdminPassword), Role.ADMIN))
        }
    }
}
