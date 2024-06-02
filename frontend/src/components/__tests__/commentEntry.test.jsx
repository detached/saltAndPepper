import { render } from "@testing-library/react";
import CommentEntry from "../commentEntry";
import { CommentAuthor, CommentResponse } from "../../api/model";

describe("commentEntry snapshot tests", () => {
  test("with delete button", () => {
    const comment = new CommentResponse({
      id: "commentId1",
      author: new CommentAuthor({
        id: "authorId1",
        name: "Author Name",
      }),
      comment: "That is a comment",
      createdOn: new Date("1970-01-01"),
      canDelete: true,
    });

    const { container } = render(<CommentEntry comment={comment} />);

    expect(container).toMatchSnapshot();
  });

  test("without delete button", () => {
    const comment = new CommentResponse({
      id: "commentId1",
      author: new CommentAuthor({
        id: "authorId1",
        name: "Author Name",
      }),
      comment: "That is a comment",
      createdOn: new Date("1970-01-01"),
      canDelete: false,
    });

    const { container } = render(<CommentEntry comment={comment} />);

    expect(container).toMatchSnapshot();
  });
});
