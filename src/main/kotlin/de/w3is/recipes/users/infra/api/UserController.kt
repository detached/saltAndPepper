package de.w3is.recipes.users.infra.api

import de.w3is.recipes.common.api.toPageViewModel
import de.w3is.recipes.common.model.PageRequest
import de.w3is.recipes.common.model.PageResponse
import de.w3is.recipes.users.UserRepository
import de.w3is.recipes.users.model.User
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController(
    private val userRepository: UserRepository,
) {
    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getAllUsers(
        @QueryValue(defaultValue = "0") page: Int,
        @QueryValue(defaultValue = "50") limit: Int,
    ): UsersResponseViewModel {
        val sanitizedPage = maxOf(page, 0)
        val sanitizedLimit = if(limit < 0 || limit > 50) { 50 } else { limit }

        val request = PageRequest(
            limit = sanitizedLimit,
            page = sanitizedPage
        )
        return userRepository.getAllUsers(request).toViewModel()
    }
}

private fun PageResponse<User>.toViewModel(): UsersResponseViewModel {
    val users =
        this.results.map { user ->
            UserViewModel(
                id = user.id.value,
                name = user.name,
                role = user.role,
            )
        }
    return UsersResponseViewModel(
        users = users,
        page = this.page.toPageViewModel(),
    )
}
