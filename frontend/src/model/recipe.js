import { ArrayModel, Model } from "objectmodel";

export class RecipeImage extends Model({
  id: String,
  url: String,
  thumbnailUrl: String,
}) {}

export class Author extends Model({
  id: String,
  name: String,
}) {}

export class Recipe extends Model({
  id: [String],
  author: [Author],
  title: String,
  category: String,
  cuisine: String,
  yields: String,
  ingredients: String,
  instructions: String,
  modifications: String,
  images: [ArrayModel(RecipeImage)],
  newImages: [ArrayModel(RecipeImage)],
}) {
  addNewImages(images) {
    if (this.newImages === undefined) {
      this.newImages = [];
    }
    this.newImages = this.newImages.concat(images);
  }

  removeNewImage(imageFile) {
    if (this.newImages === undefined) {
      return;
    }
    this.newImages = this.newImages.filter((image) => image !== imageFile);
  }

  removeExistingImage(imageId) {
    this.images = this.images.filter((image) => image.id !== imageId);
  }
}
