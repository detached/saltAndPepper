import { useCallback, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";
import { useTranslation } from "react-i18next";
import "./newCommentForm.css";

export default function NewCommentForm({ recipeId, onNew }) {
  const { t } = useTranslation();
  const [newComment, setNewComment] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [hasInputFocus, setHasInputFocus] = useState(false);

  const addNewComment = useCallback(
    (e) => {
      e.preventDefault();
      if (!newComment || isLoading) {
        return;
      }
      setIsLoading(true);

      SaltAndPepper.createComment(recipeId, newComment)
        .then(() => {
          onNew();
        })
        .finally(() => {
          setNewComment("");
          setIsLoading(false);
        });
    },
    [isLoading, newComment, recipeId, onNew],
  );

  return (
    <form className="new-comment-form pure-form pure-form-stacked">
      <fieldset>
        <input
          id="newCommment"
          data-testid="newCommentInput"
          name="newComment"
          type="text"
          placeholder={t("comments.input")}
          value={newComment}
          onChange={(e) => {
            setNewComment(e.target.value);
          }}
          onFocus={() => {
            setHasInputFocus(true);
          }}
          onBlur={() => {
            setHasInputFocus(false);
          }}
        ></input>
        <button
          className={
            hasInputFocus || newComment
              ? "pure-button"
              : "new-comment-button-hidden pure-button"
          }
          onClick={(e) => {
            addNewComment(e);
          }}
        >
          {t("comments.send")}
        </button>
      </fieldset>
    </form>
  );
}
