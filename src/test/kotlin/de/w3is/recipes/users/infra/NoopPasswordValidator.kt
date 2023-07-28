package de.w3is.recipes.users.infra

import de.w3is.recipes.users.model.PlainPassword

class NoopPasswordValidator : PasswordValidator {
    override fun isPasswordValid(name: String, plainPassword: PlainPassword) = true
}
