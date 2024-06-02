import { useTranslation } from "react-i18next";
import { FaRegCommentAlt, FaRegTrashAlt } from "react-icons/fa";
import { useCallback, useState } from "react";
import Modal from "./modal.jsx";
import { SaltAndPepper } from "../api/saltAndPepper.js";
import "./commentEntry.css";
import PropTypes from "prop-types";

/**
 * @param recipeId {String}
 * @param comment {CommentResponse}
 * @param onDelete {function}
 * @returns {JSX.Element}
 * @constructor
 */
export default function CommentEntry({ recipeId, comment, onDelete }) {
  const { t } = useTranslation();
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);

  const askForDeleteConfirmation = useCallback(() => {
    setShowDeleteConfirmation(true);
  }, [setShowDeleteConfirmation]);

  const closeDeleteConfirmation = useCallback(() => {
    setShowDeleteConfirmation(false);
  }, [setShowDeleteConfirmation]);

  const deleteComment = useCallback(() => {
    setShowDeleteConfirmation(false);
    SaltAndPepper.deleteComment(recipeId, comment.id).then(() => {
      onDelete();
    });
  }, [recipeId, comment, setShowDeleteConfirmation, onDelete]);

  return (
    <>
      {showDeleteConfirmation ? (
        <Modal
          headerText={t("deleteComment.title")}
          contentText={t("deleteComment.text")}
          buttonOneText={t("deleteComment.yes")}
          buttonOneCallback={deleteComment}
          buttonTwoText={t("deleteComment.no")}
          buttonTwoCallback={closeDeleteConfirmation}
        />
      ) : null}
      <li key={comment.id} className="pure-g comment-entry-comment">
        <div className="comment-entry-left">
          <FaRegCommentAlt className="comment-entry-icon" />
        </div>
        <div className="">
          <div className="comment-entry-content-title">
            <div className="comment-entry-author">{comment.author.name}</div>
            <div className="comment-entry-date">
              {t("intlDateTime", { val: comment.createdOn })}
            </div>
            {comment.canDelete ? (
              <FaRegTrashAlt
                className="comment-entry-control"
                onClick={askForDeleteConfirmation}
              />
            ) : null}
          </div>
          <p className="comment-entry-text">{comment.comment}</p>
        </div>
      </li>
    </>
  );
}
CommentEntry.propTypes = {
  recipeId: PropTypes.string,
  comment: PropTypes.object,
  onDelete: PropTypes.func,
};
