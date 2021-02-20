import { LitElement, html, css } from 'lit-element'
import { getImagesOfRecipe, addImageToRecipe, removeImageFromRecipe } from '../api'

export class ImageGallery extends LitElement {

    static get styles() {
        return css`

        .image-container {
            display:inline-block;
            letter-spacing:normal;
            word-spacing:normal;
            vertical-align:top;
            text-rendering:auto;
            position: relative;
            width: 20%;
            margin: 5px;
        }

        .image-button {
            position: absolute;
            top: 8px;
            right: 16px;
            opacity: 0;
            cursor: pointer;
        }

        .upload-button {
             cursor: pointer;
        }
        
        img {
            max-width:100%;
            height:auto;
            display:block;
            transition: .2s ease;
        }
        
        .image-container:hover img {
            opacity: 0.3;
        }
        
        .image-container:hover a {
            opacity: 1;
        }
        `;
    }

    static get properties() {
        return {
            recipeId: {},

            toggleUploadButtonLabel: {},
            uploadImageLabel: {},
            allowUpload: {},

            images: { attribute: false },
            selectedFile: { attribute: false },
            isUploadHidden: { attribute: false }
        };
    }

    constructor() {
        super();
        this.images = [];
        this.isUploadHidden = true;
    }

    connectedCallback() {
        super.connectedCallback()

        getImagesOfRecipe(this.recipeId)
            .then(response => response.json())
            .then(i => this.images = i);
    }

    render() {
        return html`
            <div>
                ${this.images.map(image => html`
                    <div class="image-container">
                        <img src=${image.url} />
                        <a @click=${this._removeImage(image.id)} class="image-button">X</a>
                    </div>
                    `)}
            </div>
            ${ this.allowUpload == "true" ? html`
                <a @click=${this._toggleUploadButton} class="upload-button">${this.toggleUploadButtonLabel}</a>
                <div ?hidden=${this.isUploadHidden}>
                    <input type="file" name="image" accept="image/png, image/jpeg" @change=${this._imageSelected} />
                    <button @click=${this._uploadImage} >${this.uploadImageLabel}</button>
                </div>` : null}
        `;
    }

    _imageSelected(e) {
        this.selectedFile = e.target.files[0];
    }

    _uploadImage() {
        const image = this.selectedFile;
        if (image) {
            addImageToRecipe(this.recipeId, image)
                .then(response => response.json())
                .then(newImage => this.images = [...this.images, newImage]);
        }
    }

    _removeImage(imageId) {
        return () => {
            removeImageFromRecipe(this.recipeId, imageId)
                .then(response => {
                    if (response.ok) {
                        this.images = this.images.filter((image) => image.id !== imageId);
                    }
                });
        };
    }

    _toggleUploadButton() {
        this.isUploadHidden = !this.isUploadHidden;
    }
}

window.customElements.define('image-gallery', ImageGallery);