import { render, screen } from "@testing-library/react";
import Comments from "../comments";
import { userEvent } from "@testing-library/user-event";
import { act } from "react";
import { http, HttpResponse } from "msw";
import { server } from "../../../mocks/node";
import { postComment } from "../../../mocks/resolver/comments";

describe("comments integration test", () => {
  test("should render fetched comments", async () => {
    render(<Comments recipeId={"123"} />);

    await screen.findByText("Author 1");

    const allComments = screen.getAllByRole("listitem");
    expect(allComments).toHaveLength(2);

    expect(screen.getByText("Author 1")).toBeInTheDocument();
    expect(screen.getByText("That is comment 1")).toBeInTheDocument();
    expect(screen.getByText("1970-01-01")).toBeInTheDocument();

    expect(screen.getByText("Author 2")).toBeInTheDocument();
    expect(screen.getByText("That is comment 2")).toBeInTheDocument();
    expect(screen.getByText("1970-01-02")).toBeInTheDocument();
  });

  test("should fetch comments again when adding new", async () => {
    const user = userEvent.setup();
    const recipeId = "newCommentTest";
    mockServerReturnsNoComments(recipeId);

    render(<Comments recipeId={recipeId} />);

    const newComment = "new comment";
    mockServerReturnsComment(recipeId, newComment);

    const commentInput = screen.getByRole("textbox");
    await act(() => user.type(commentInput, newComment));
    expect(commentInput).toHaveValue(newComment);
    await act(() => user.click(screen.getByRole("button")));

    expect(
      await screen.findByText(newComment, {}, { timeout: 2000 }),
    ).toBeInTheDocument();
  });

  function mockServerReturnsNoComments(recipeId) {
    server.resetHandlers(
      http.get("/api/comments/recipe/" + recipeId, () => {
        return HttpResponse.json({ comments: [] });
      }),
    );
  }

  function mockServerReturnsComment(recipeId, commentText) {
    server.resetHandlers(
      ...[
        http.post("/api/comments/recipe/" + recipeId, postComment),
        http.get("/api/comments/recipe/" + recipeId, () => {
          return HttpResponse.json({
            comments: [
              {
                id: "commentId",
                author: {
                  id: "authorId",
                  name: "Author",
                },
                comment: commentText,
                createdOn: "1970-01-02",
                canDelete: true,
              },
            ],
          });
        }),
      ],
    );
  }
});
