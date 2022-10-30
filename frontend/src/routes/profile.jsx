import {useTranslation} from "react-i18next";

export default function Profile() {

    const {t} = useTranslation();

    return <>
        <div className="header">
            <h1>{t("profile.title")}</h1>
        </div>
        <div className="content">
            <div className="content-section">
                <form className="pure-form pure-form-aligned">
                    <div className="pure-control-group">
                        <fieldset>
                            <label htmlFor="name">{t("profile.name")}:</label>
                            <input type="text" id="name" name="name" value="$profile.name" readOnly/>
                        </fieldset>
                        <fieldset>
                            <label htmlFor="role">{t("profile.role")}:</label>
                            <input type="text" id="role" name="role" value="$profile.role" readOnly/>
                        </fieldset>
                    </div>
                </form>
            </div>

            <div className="content-section">
                <h3>{t("profile.changePassword.title")}</h3>
                <form className="pure-form pure-form-aligned" method="post" action="/profile/changePassword">
                    <div className="pure-control-group">
                        <fieldset>
                            <label htmlFor="oldPassword">
                                {t("profile.changePassword.oldPassword")}:
                            </label>
                            <input type="password" id="oldPassword" name="oldPassword"
                                   placeholder={t("profile.changePassword.oldPassword")}/>
                        </fieldset>
                        <fieldset>
                            <label htmlFor="newPassword">
                                {t("profile.changePassword.newPassword")}:
                            </label>
                            <input type="password" id="newPassword" name="newPassword"
                                   placeholder={t("profile.changePassword.newPassword")}/>
                        </fieldset>
                    </div>
                    <p>{t("password.rules")}</p>
                    <button className="pure-button pure-button-primary" type="submit">
                        {t("profile.changePassword.save")}
                    </button>
                    {/*                    #if($passwordChangeSuccess)
                    <p>$translations.get("profile.changePassword.success")</p>
                    #end*/}
                </form>
                {/*
                #if($passwordChangeError)
                <span className="form-error-message">$passwordChangeError</span>
                #end*/}
            </div>

            {/*#if($allowedToInvite)*/}
            <div className="content-section">

                <h3>{t("profile.invitation.title")}</h3>
                <p>{t("profile.invitation.text")}</p>

                <form className="pure-form pure-form-aligned" method="post" action="/profile/invitation">
                    <div className="pure-control-group">
                        {/*#if($invitationLink)*/}

                        <fieldset>
                            <input className="pure-button" type="submit" id="createInvitation" name="createInvitation"
                                   value={t("profile.createInvitation")} disabled/>
                        </fieldset>
                        <fieldset>
                            <input className="pure-input-2-3" type="text" id="invitationLink" name="invitationLink"
                                   value="$invitationLink" readOnly/>
                            <button className="pure-button" onClick="copyToClipboard('invitationLink');">
                                {t("copyToClipboard")}
                            </button>
                        </fieldset>
                        {/*
                        #else

                        <input className="pure-button" type="submit" id="createInvitation" name="createInvitation"
                               value="$translations.get('profile.createInvitation')"/>

                        #end
                        */}
                    </div>
                </form>
            </div>
            {/*#end*/}
            <div className="content-section">
                <h3>{t("import.gourmet")}</h3>
                <form method="post" action="/import">
                    <input type="file" name="file" id="file"/>
                    <input type="submit" value={t("import.submit")}/>
                </form>
                {/* #if ($error)
                <div class="error">
                    $translations.get("import.error")
                </div>
                #end*/}
            </div>
        </div>
    </>;
}
