import { useTranslation } from "react-i18next";
import PropTypes from "prop-types";
import { Profile } from "../api/model.js";

export default function ProfileInfoForm({ profile }) {
  const { t } = useTranslation();

  return (
    <form className="pure-form pure-form-aligned">
      <div className="pure-control-group">
        <fieldset>
          <label htmlFor="name">{t("profile.name")}:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={profile.name}
            readOnly
          />
        </fieldset>
        <fieldset>
          <label htmlFor="role">{t("profile.role")}:</label>
          <input
            type="text"
            id="role"
            name="role"
            value={profile.role}
            readOnly
          />
        </fieldset>
      </div>
    </form>
  );
}
ProfileInfoForm.propTypes = {
  profile: PropTypes.instanceOf(Profile),
};
