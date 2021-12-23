package de.w3is.recipes.application.users

interface UserRepository {
    fun findUser(userName: String): User?
    fun store(user: User)
    fun update(user: User)
    fun getUser(userId: UserId): User
}