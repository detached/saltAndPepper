package de.w3is.recipes.images.infra.persistence

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.images.model.ImageId
import de.w3is.recipes.infra.persistence.generated.tables.Images.Companion.IMAGES
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class JooqImageRepositoryTest {
    @Inject
    private lateinit var imageRepository: ImageRepository

    @Inject
    private lateinit var dslContext: DSLContext

    @BeforeEach
    internal fun setUp() {
        dslContext.truncate(IMAGES).execute()
    }

    @Test
    fun `store and get image`() {
        val imageId = ImageId.new()

        imageRepository.store(imageId, givenImageData(), givenImageData())

        assertThat(imageRepository.get(imageId).readAllBytes()).isEqualTo(givenImageData().readAllBytes())
        assertThat(imageRepository.getThumbnail(imageId).readAllBytes()).isEqualTo(givenImageData().readAllBytes())
    }

    @Test
    fun `delete image`() {
        val imageId = ImageId.new()
        val data = givenImageData()
        val thumbnail = givenImageData()

        imageRepository.store(imageId, data, thumbnail)

        assertThat(imageRepository.get(imageId))

        imageRepository.delete(imageId)

        assertFailure { imageRepository.get(imageId) }
    }

    private fun givenImageData() = this.javaClass.getResourceAsStream("/images/cake.jpg")!!
}
