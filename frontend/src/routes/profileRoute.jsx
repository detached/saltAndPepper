import {useTranslation} from "react-i18next";
import ProfileInfoForm from "../components/profileInfoForm";
import ChangePasswordForm from "../components/changePasswordForm";
import {useContext} from "react";
import InvitationForm from "../components/invitationForm";
import XmlImportForm from "../components/xmlImportForm";
import {ProfileContext} from "../context/profileProvider";

export default function ProfileRoute() {

    const {t} = useTranslation();
    const profile = useContext(ProfileContext);

    return <>
        <div className="header">
            <h1>{t("profile.title")}</h1>
        </div>
        <div className="content">
            <div className="content-section">
                <ProfileInfoForm profile={profile}/>
            </div>

            <div className="content-section">
                <ChangePasswordForm/>
            </div>

            {profile.isAllowedToInvite ?
                <div className="content-section">
                    <InvitationForm/>
                </div> : null
            }
            <div className="content-section">
                <XmlImportForm />
            </div>
        </div>
    </>;
}
