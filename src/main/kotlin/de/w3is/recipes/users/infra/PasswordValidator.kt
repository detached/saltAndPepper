package de.w3is.recipes.users.infra

import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton

interface PasswordValidator {
    fun isPasswordValid(name: String, plainPassword: PlainPassword): Boolean
}

// TODO: implement with passay
@Singleton
class SimplePasswordValidator(
    @Property(name = "application.minPasswordLength") private val minPasswordLength: Int
) : PasswordValidator {

    override fun isPasswordValid(name: String, plainPassword: PlainPassword): Boolean =
        plainPassword.value.isNotBlank()
                && plainPassword.value.length >= minPasswordLength
                && plainPassword.value.trim() != name
}