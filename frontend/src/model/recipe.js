export class Recipe {
    id;
    author;
    title;
    category;
    cuisine;
    yields;
    ingredients;
    instructions;
    modifications;
    images;

    constructor(title, category, cuisine, yields, ingredients, instructions, modifications) {
        this.title = title;
        this.category = category;
        this.cuisine = cuisine;
        this.yields = yields;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.modifications = modifications;
    }

    addNewImages(images) {
        if (this.newImages === undefined) {
            this.newImages = [];
        }
        this.newImages = this.newImages.concat(images);
    }
    removeNewImage(imageFile) {
        if (this.newImages === undefined) {
            return
        }
        this.newImages = this.newImages.filter((image) => image !== imageFile);
    }

    removeExistingImage(imageId) {
        this.images = this.images.filter((image) => image.id !== imageId);
    }
}

export class RecipeImage {
    id;
    url;
    thumbnailUrl;

    constructor(id, url, thumbnailUrl) {
        this.id = id;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }
}

export class Author {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }
}