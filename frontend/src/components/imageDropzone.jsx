import {FaTrashAlt} from "react-icons/fa";
import {useDropzone} from "react-dropzone";
import {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import PropTypes from "prop-types";
import {RecipeImage} from "../model/recipe.js";
import "./imageDropzone.css";

export default function ImageDropzone({initImages, onImagesDropped, onImageRemoved}) {
    const { t } = useTranslation();
    const [images, setImages] = useState(initImages ? initImages : [],);
    const { getRootProps, getInputProps } = useDropzone({
        accept: {
            "image/jpeg": [".jpg", ".jpeg"],
            "image/png": [".png"],
        },
        onDrop: (acceptedFiles) => {
            addImages(acceptedFiles)
        },
    });
    useEffect(() => {
        // Make sure to revoke the data uris to avoid memory leaks, will run on unmount
        return () =>
            images.forEach((file) => {
                if (file.isNew) {
                    URL.revokeObjectURL(file.preview);
                }
            });
    }, [images]);

    function addImages(acceptedFiles) {
        let newImages = acceptedFiles.map((file) =>
            Object.assign(file, {
                isNew: true,
                thumbnailUrl: URL.createObjectURL(file),
            }),
        );
        setImages(images.concat(newImages));
        onImagesDropped(acceptedFiles);
    }

    function removeImage(imageToRemove) {
        onImageRemoved(imageToRemove, imageToRemove.isNew);
        setImages(images.filter((image) => image !== imageToRemove));
    }

    const thumbs = images.map((file) => (
        <div
            className="edit-recipe-images-thumb"
            key={file.name ? file.name : file.id}
        >
            <div onClick={() => removeImage(file)}>
                <img
                    id="image"
                    src={file.thumbnailUrl}
                    onLoad={() => {
                        if (file.isNew) {
                            URL.revokeObjectURL(file.thumbnailUrl);
                        }
                    }}
                    alt="thumbnail"
                />
                <div id="overlay">
                    <FaTrashAlt />
                </div>
            </div>
        </div>
    ));

    return (
        <section className="edit-recipe-images-container">
            <div {...getRootProps({className: "dropzone"})}>
                <input {...getInputProps()} />
                <p>{t("newRecipe.dropzone")}</p>
            </div>
            <aside className="edit-recipe-images-thumbs-container">
                {thumbs}
            </aside>
        </section>
    );
}
ImageDropzone.propTypes = {
    initImages: PropTypes.arrayOf(PropTypes.instanceOf(RecipeImage)),
    onImagesDropped: PropTypes.func,
    onImageRemoved: PropTypes.func
}