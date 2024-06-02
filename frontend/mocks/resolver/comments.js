import {HttpResponse} from "msw";

export const getComments = () => {
    return HttpResponse.json({
        comments: [
            {
                id: 'commentId1',
                author: {
                    id: 'authorId1',
                    name: 'Author 1'
                },
                comment: 'That is comment 1',
                createdOn: '1970-01-01',
                canDelete: true
            },
            {
                id: 'commentId2',
                author: {
                    id: 'authorId2',
                    name: 'Author 2'
                },
                comment: 'That is comment 2',
                createdOn: '1970-01-02',
                canDelete: false
            }
        ]
    });
}

export const postComment = () => {
    return HttpResponse.json(
        {
            id: 'newCommentId',
            author: {
                id: 'authorId1',
                name: 'Author 1'
            },
            comment: 'That is comment 1',
            createdOn: '1970-01-01',
            canDelete: true
        }
    )
}