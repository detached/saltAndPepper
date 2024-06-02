import { useTranslation } from "react-i18next";
import { useCallback, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper.js";
import { useAuth } from "../context/authProvider.jsx";
import Snowflakes from "../components/snowflakes.jsx";
import logo from "../res/saltAndPepper.png";
import "./loginRoute.css";

export default function LoginRoute() {
  const { t } = useTranslation();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const authentication = useAuth();

  const handleSubmit = useCallback(
    (e) => {
      e.preventDefault();
      if (isLoading) {
        return;
      }
      setIsLoading(true);
      SaltAndPepper.login(username, password)
        .then((authResponse) => {
          authentication.onLogin(authResponse);
        })
        .catch((error) => {
          setErrorMessage(error.message);
        })
        .finally(() => {
          setIsLoading(false);
        });
    },
    [
      isLoading,
      setIsLoading,
      setErrorMessage,
      username,
      password,
      authentication,
    ],
  );

  function isWintertime() {
    let currentMonth = new Date().getMonth();
    return currentMonth === 11 || currentMonth === 0;
  }

  return (
    <div className="login-container">
      {isWintertime() ? <Snowflakes /> : null}

      <div className="login-content pure-g">
        <img className="pure-img pure-u-1" src={logo} alt="logo" />
        <form
          className="pure-form pure-form-stacked pure-u-1"
          onSubmit={(e) => handleSubmit(e)}
        >
          <fieldset className="pure-control-group">
            <input
              type="text"
              placeholder={t("login.username")}
              className="pure-input-1"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder={t("login.password")}
              className="pure-input-1"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </fieldset>
          <input
            type="submit"
            disabled={isLoading}
            value={t("login.submit")}
            className="pure-button pure-button-primary pure-input-1"
          />
        </form>
        {errorMessage !== "" ? (
          <span className="form-error-message pure-u-1">
            {t("login.failed")}
          </span>
        ) : null}
      </div>
    </div>
  );
}
