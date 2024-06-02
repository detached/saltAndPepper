import { createContext, useContext, useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper.js";
import PropTypes from "prop-types";

export const ProfileContext = createContext({});

export const ProfileProvider = ({ children }) => {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    SaltAndPepper.getProfile().then((response) => {
      setProfile(response);
    });
  }, [setProfile]);

  return (
    <ProfileContext.Provider value={profile}>
      {children}
    </ProfileContext.Provider>
  );
};

ProfileProvider.propTypes = {
  children: PropTypes.any,
};

export const useProfile = () => {
  return useContext(ProfileContext);
};
