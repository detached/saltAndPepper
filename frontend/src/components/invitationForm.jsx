import { useTranslation } from "react-i18next";
import { useCallback, useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";

export default function InvitationForm() {
  const { t } = useTranslation();
  const [invitationLink, setInvitationLink] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  function createInviteLink(code) {
    return (
      window.location.protocol +
      "//" +
      window.location.host +
      "/#/invite/" +
      code
    );
  }

  useEffect(() => {
    SaltAndPepper.getInvitationCode().then((result) => {
      if (result) {
        setInvitationLink(createInviteLink(result.code));
      }
    });
  }, [setInvitationLink]);

  const createInvitationLink = useCallback(
    (e) => {
      e.preventDefault();
      if (isLoading) {
        return;
      }
      setIsLoading(true);

      SaltAndPepper.createInvitationCode()
        .then((result) => {
          setInvitationLink(createInviteLink(result.code));
        })
        .finally(() => {
          setIsLoading(false);
        });
    },
    [isLoading, setInvitationLink, setIsLoading],
  );

  function copyLinkToClipboard(e) {
    e.preventDefault();
    navigator.clipboard.writeText(invitationLink);
  }

  return (
    <>
      <h3>{t("profile.invitation.title")}</h3>
      <p>{t("profile.invitation.text")}</p>

      <form className="pure-form pure-form-aligned">
        <div className="pure-control-group">
          <fieldset>
            <input
              className="pure-button"
              type="submit"
              id="createInvitation"
              name="createInvitation"
              value={t("profile.createInvitation")}
              onClick={(e) => createInvitationLink(e)}
              disabled={invitationLink !== ""}
            />
          </fieldset>
          {invitationLink !== "" ? (
            <fieldset>
              <input
                className="pure-input-2-3"
                type="text"
                id="invitationLink"
                name="invitationLink"
                value={invitationLink}
                readOnly
              />
              <button
                className="pure-button"
                onClick={(e) => copyLinkToClipboard(e)}
              >
                {t("copyToClipboard")}
              </button>
            </fieldset>
          ) : null}
        </div>
      </form>
    </>
  );
}
