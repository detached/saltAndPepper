import { useTranslation } from "react-i18next";
import { useCallback, useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";

export default function InvitationForm() {
  const { t } = useTranslation();
  const [invitationLink, setInvitationLink] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    SaltAndPepper.getInvitationLink().then((result) => {
      if (result) {
        setInvitationLink(result.invitationLink);
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

      SaltAndPepper.createInvitationLink()
        .then((result) => {
          setInvitationLink(result.invitationLink);
        })
        .finally(() => {
          setIsLoading(false);
        });
    },
    [isLoading, setInvitationLink, setIsLoading]
  );

  return (
    <>
      <h3>{t("profile.invitation.title")}</h3>
      <p>{t("profile.invitation.text")}</p>

      <form
        className="pure-form pure-form-aligned"
        onSubmit={(e) => createInvitationLink(e)}
      >
        <div className="pure-control-group">
          <fieldset>
            <input
              className="pure-button"
              type="submit"
              id="createInvitation"
              name="createInvitation"
              value={t("profile.createInvitation")}
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
                onClick={() => {
                  navigator.clipboard.writeText(invitationLink);
                }}
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
