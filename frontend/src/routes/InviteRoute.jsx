import { useTranslation } from "react-i18next";
import { useCallback, useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";
import { useNavigate, useParams } from "react-router-dom";
import "./inviteRoute.css";

export default function InviteRoute() {
  const { t } = useTranslation();
  const { code } = useParams();
  const [invitingUser, setInvitingUser] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isRegistered, setIsRegistered] = useState(false);
  const navigateTo = useNavigate();

  useEffect(() => {
    SaltAndPepper.getInvitationInfo(code)
      .then((result) => {
        setInvitingUser(result.invitingUser);
      })
      .catch(() => {
        navigateTo("/")
      });
  }, [code, setInvitingUser, navigateTo]);

  const handleSubmit = useCallback(
    (e) => {
      e.preventDefault();
      if (isLoading) {
        return;
      }
      setIsLoading(true);
      SaltAndPepper.registerByInvitation(code, username, password)
        .then(() => {
          setIsRegistered(true);
        })
        .catch((error) => {
          setErrorMessage(error.message);
        })
        .finally(() => {
          setIsLoading(false);
        });
    },
    [code, username, password, isLoading, setIsLoading, setIsRegistered]
  );

  function successMessage() {
    return (
      <div>
        <h2>{t("userRegistered.title", { username: username })}</h2>
        <p>{t("userRegistered.text")}</p>
        <button className="pure-button" onClick={() => navigateTo("/")}>
          {t("userRegistered.link")}
        </button>
      </div>
    );
  }

  function registerForm() {
    return (
      <>
        <h1>{t("inviteRegistration.title")}</h1>
        <p>
          {t("inviteRegistration.text", { invitingUser: invitingUser })}
          <br />
          {t("inviteRegistration.text2")}
        </p>
        <form
          className="pure-form pure-form-stacked pure-u-1 invite-form"
          onSubmit={(e) => handleSubmit(e)}
        >
          <div className="pure-control-group">
            <fieldset>
              <input
                type="text"
                id="name"
                name="name"
                className="pure-input-1"
                placeholder={t("inviteRegistration.name")}
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <input
                type="password"
                id="password"
                name="password"
                className="pure-input-1"
                placeholder={t("inviteRegistration.password")}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </fieldset>
          </div>
          <p>{t("password.rules")}</p>

          <button
            className="pure-button pure-button-primary"
            type="submit"
            disabled={isLoading}
          >
            {t("inviteRegistration.register")}
          </button>
        </form>
      </>
    );
  }

  return (
    <div className="content invite-container">
      {isRegistered ? successMessage() : registerForm()}
      {errorMessage ? (
        <span className="form-error-message">{errorMessage}</span>
      ) : null}
    </div>
  );
}
