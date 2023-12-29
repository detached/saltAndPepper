import { useCallback, useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";
import CommentEntry from "./commentEntry";
import NewCommentForm from "./newCommentForm";
import "./comments.css";

export default function Comments({ recipeId }) {
  const [comments, setComments] = useState([]);

  const updateComments = useCallback(() => {
    SaltAndPepper.getAllComments(recipeId).then((comments) => {
      setComments(comments);
    });
  }, [recipeId, setComments]);

  useEffect(() => {
    updateComments();
  }, [updateComments]);

  return (
    <div className="comments">
      <NewCommentForm recipeId={recipeId} onNew={updateComments} />
      <ol>
        {comments.map((comment) => (
          <CommentEntry
            key={comment.id}
            recipeId={recipeId}
            comment={comment}
            onDelete={updateComments}
          />
        ))}
      </ol>
    </div>
  );
}
