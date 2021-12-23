package de.w3is.recipes.application.users

class NoopPasswordValidator : PasswordValidator {
    override fun isPasswordValid(name: String, plainPassword: PlainPassword) = true
}