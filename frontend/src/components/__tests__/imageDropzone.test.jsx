import {render} from "@testing-library/react";
import ImageDropzone from "../imageDropzone.jsx";
import {RecipeImage} from "../../model/recipe.js";

describe("imageDropzone test", () => {
    test("when initialized with images, the images are shown in thumbnails", async () => {

        let initImages = [new RecipeImage({
            id: "1",
            url: "http://localhost/thumbnail",
            thumbnailUrl: "http://localhost/thumbnail"
        })];
        let onImagesDropped = () => {
        };
        let onImagesRemoved = () => {
        };

        let {container} = render(<ImageDropzone initImages={initImages} onImagesDropped={onImagesDropped}
                                                onImageRemoved={onImagesRemoved}/>);

        expect(container).toMatchSnapshot();
    })
});