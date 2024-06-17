import {HttpResponse} from "msw";
import fs from "node:fs";
import path from "node:path";

export const getTestImage = () => {
    let buffer = fs.readFileSync(
        path.resolve(process.cwd(), 'mocks/assets', 'image.jpg')
    );
    return HttpResponse.arrayBuffer(buffer, { headers: { 'Content-Type': 'image/jpg'}});
}