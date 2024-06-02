package de.w3is.recipes.images

import de.w3is.recipes.images.model.ImageId
import jakarta.inject.Singleton
import net.coobird.thumbnailator.Thumbnails
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

@Singleton
class ImageService(private val imageRepository: ImageRepository) {
    fun convertAndStoreImage(data: InputStream): ImageId {
        val output = ByteArrayOutputStream()
        data.transferTo(output)

        val imageId = ImageId.new()
        imageRepository.store(
            imageId = imageId,
            data = convertImageToPng(ByteArrayInputStream(output.toByteArray())),
            thumbnail = createThumbnailFor(ByteArrayInputStream(output.toByteArray())),
        )
        return imageId
    }

    private fun convertImageToPng(data: InputStream): InputStream {
        val output = ByteArrayOutputStream()
        val bufferedImage = ImageIO.read(data)
        ImageIO.write(bufferedImage, "png", output)
        return output.toByteArray().inputStream()
    }

    private fun createThumbnailFor(imageData: InputStream): InputStream {
        val output = ByteArrayOutputStream()

        Thumbnails.of(imageData)
            .size(160, 160)
            .outputFormat("png")
            .toOutputStream(output)

        return output.toByteArray().inputStream()
    }

    fun delete(imageId: ImageId) {
        imageRepository.delete(imageId)
    }
}
