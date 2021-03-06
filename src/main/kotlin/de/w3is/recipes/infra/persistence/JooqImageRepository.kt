package de.w3is.recipes.infra.persistence

import de.w3is.recipes.domain.ImageRepository
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.infra.persistence.generated.public_.Tables.IMAGES
import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.Cacheable
import org.jooq.DSLContext
import java.io.InputStream
import javax.inject.Singleton

@Singleton
class JooqImageRepository(private val dslContext: DSLContext) : ImageRepository {

    override fun store(imageId: ImageId, data: InputStream, thumbnail: InputStream) {

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
            .fetchOne(IMAGES.DATA).inputStream()
    }

    override fun getThumbnail(imageId: ImageId): InputStream =
            dslContext.select(IMAGES.THUMBNAIL)
                    .from(IMAGES)
                    .where(IMAGES.IMAGE_ID.eq(imageId.value))
                    .fetchOne(IMAGES.THUMBNAIL).inputStream()

    override fun delete(imageId: ImageId) {
        dslContext.delete(IMAGES)
                .where(IMAGES.IMAGE_ID.eq(imageId.value))
                .execute()
    }
}