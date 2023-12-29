import { http } from 'msw'
import {getComments, postComment} from "./resolver/comments";

export const handlers = [
    http.get('/api/comments/recipe/*', getComments),
    http.post("/api/comments/recipe/*", postComment)
];