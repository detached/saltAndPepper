package de.w3is.recipes.images.infra.persistence

import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.images.model.ImageId
import de.w3is.recipes.infra.persistence.generated.tables.Images.Companion.IMAGES
import jakarta.inject.Singleton
import org.jooq.DSLContext
import java.io.InputStream

@Singleton
class JooqImageRepository(private val dslContext: DSLContext) : ImageRepository {
    override fun store(
        imageId: ImageId,
        data: InputStream,
        thumbnail: InputStream,
    ) {
        dslContext.newRecord(IMAGES).apply {
            this.imageId = imageId.value
            this.data = data.readAllBytes()
            this.thumbnail = thumbnail.readAllBytes()
        }.store()
    }

    override fun get(imageId: ImageId): InputStream {
        return dslContext.select(IMAGES.DATA)
            .from(IMAGES)
            .where(IMAGES.IMAGE_ID.eq(imageId.value))
            .fetchOne(IMAGES.DATA)?.inputStream() ?: error("image with id $imageId not found")
    }

    override fun getThumbnail(imageId: ImageId): InputStream =
        dslContext.select(IMAGES.THUMBNAIL)
            .from(IMAGES)
            .where(IMAGES.IMAGE_ID.eq(imageId.value))
            .fetchOne(IMAGES.THUMBNAIL)?.inputStream() ?: error("thumbnail with id $imageId not found")

    override fun delete(imageId: ImageId) {
        dslContext.delete(IMAGES)
            .where(IMAGES.IMAGE_ID.eq(imageId.value))
            .execute()
    }
}
