import { useTranslation } from "react-i18next";
import UsersList from "../components/usersList.jsx";

export default function UsersRoute() {
  const { t } = useTranslation();

  return (
    <div className="pure-g">
      <div className="pure-u-1">
        <div className="header">
          <h2>{t("users.title")}</h2>
        </div>
        <UsersList />
      </div>
    </div>
  );
}
