import { useTranslation } from "react-i18next";
import ProfileInfoForm from "../components/profileInfoForm.jsx";
import ChangePasswordForm from "../components/changePasswordForm.jsx";
import InvitationForm from "../components/invitationForm.jsx";
import XmlImportForm from "../components/xmlImportForm.jsx";
import { useProfile } from "../context/profileProvider.jsx";

export default function ProfileRoute() {
  const { t } = useTranslation();
  const profile = useProfile();

  const content = () => {
    return (
      <div className="content">
        <div className="content-section">
          <ProfileInfoForm profile={profile} />
        </div>

        <div className="content-section">
          <ChangePasswordForm />
        </div>

        {profile.isAllowedToInvite ? (
          <div className="content-section">
            <InvitationForm />
          </div>
        ) : null}
        <div className="content-section">
          <XmlImportForm />
        </div>
      </div>
    );
  };

  return (
    <>
      <div className="header">
        <h1>{t("profile.title")}</h1>
      </div>
      {profile ? content() : null}
    </>
  );
}
