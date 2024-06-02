import { useTranslation } from "react-i18next";
import { useCallback, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";
import { OperationState } from "../model/operationState";
import { ChangePasswordRequest } from "../api/model";

export default function ChangePasswordForm() {
  const { t } = useTranslation();
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [passwordChangeState, setPasswordChangeState] = useState(
    OperationState.NONE,
  );
  const [errorReason, setErrorReason] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const changePassword = useCallback(
    (e) => {
      e.preventDefault();
      if (isLoading) {
        return;
      }
      setIsLoading(true);
      setPasswordChangeState(OperationState.NONE);

      SaltAndPepper.changePassword(
        new ChangePasswordRequest({ oldPassword, newPassword }),
      )
        .then(() => {
          setPasswordChangeState(OperationState.SUCCESSFUL);
        })
        .catch((error) => {
          setPasswordChangeState(OperationState.ERROR);
          setErrorReason(error.message);
        })
        .finally(() => {
          setIsLoading(false);
        });
    },
    [
      isLoading,
      setIsLoading,
      newPassword,
      oldPassword,
      setPasswordChangeState,
      setErrorReason,
    ],
  );

  function passwordChangedResult() {
    switch (passwordChangeState) {
      case OperationState.SUCCESSFUL:
        return <p>{t("profile.changePassword.success")}</p>;
      case OperationState.ERROR:
        return <p className="form-error-message">{errorReason}</p>;
      default:
        return null;
    }
  }

  return (
    <>
      <h3>{t("profile.changePassword.title")}</h3>
      <form
        className="pure-form pure-form-aligned"
        onSubmit={(e) => changePassword(e)}
      >
        <div className="pure-control-group">
          <fieldset>
            <label htmlFor="oldPassword">
              {t("profile.changePassword.oldPassword")}:
            </label>
            <input
              type="password"
              id="oldPassword"
              name="oldPassword"
              placeholder={t("profile.changePassword.oldPassword")}
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
            />
          </fieldset>
          <fieldset>
            <label htmlFor="newPassword">
              {t("profile.changePassword.newPassword")}:
            </label>
            <input
              type="password"
              id="newPassword"
              name="newPassword"
              placeholder={t("profile.changePassword.newPassword")}
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </fieldset>
        </div>
        <p>{t("password.rules")}</p>
        <button className="pure-button" type="submit" disabled={isLoading}>
          {t("profile.changePassword.save")}
        </button>
      </form>
      {passwordChangedResult()}
    </>
  );
}
