package de.w3is.recipes.infra.views.model

class Menu(private val activeItem: Site) {
    fun isActive(site: String) = activeItem == Site.valueOf(site)
    fun activeItem() = activeItem.name
}