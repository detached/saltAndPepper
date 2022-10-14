package de.w3is.recipes.images.infra.persistence

import assertk.assertThat
import assertk.assertions.*
import de.w3is.recipes.images.ImageId
import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.infra.persistence.generated.Tables
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
        dslContext.truncate(Tables.IMAGES).execute()
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

        assertThat { imageRepository.get(imageId) }.isSuccess()

        imageRepository.delete(imageId)

        assertThat { imageRepository.get(imageId) }.isFailure()
    }

    private fun givenImageData() = this.javaClass.getResourceAsStream("/images/cake.jpg")
}