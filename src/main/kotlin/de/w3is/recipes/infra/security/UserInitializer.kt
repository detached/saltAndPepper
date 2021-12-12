package de.w3is.recipes.infra.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import de.w3is.recipes.application.users.Role
import de.w3is.recipes.application.users.User
import de.w3is.recipes.application.users.UserRepository
import io.micronaut.context.annotation.Property
import io.micronaut.context.event.StartupEvent
import io.micronaut.core.io.ResourceResolver
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

data class UserProperties(val name: String, val password: String, val role: String)

@Deprecated("Replace with user management")
@Singleton
class UserInitializer(private val userRepository: UserRepository,
                      private val resourceResolver: ResourceResolver,
                      private val objectMapper: ObjectMapper,
                      @Property(name = "application.users") private val users: String) {

    private val logger = LoggerFactory.getLogger(UserInitializer::class.java)

    object typeReference: TypeReference<List<UserProperties>>()

    @EventListener
    fun setupUser(event: StartupEvent) {

        val users = objectMapper.readValue(resourceResolver.getResource(users).get(), typeReference)
        users.forEach {

            val user = userRepository.findUser(it.name)
            if (user == null) {
                logger.info("Add user ${it.name}")
                userRepository.store(User.createNew(it.name, it.password, Role.valueOf(it.role)))
            }
        }
    }
}