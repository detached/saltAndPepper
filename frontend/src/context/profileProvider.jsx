import {createContext, useContext, useEffect, useState} from "react";
import {SaltAndPepper} from "../api/saltAndPepper";

export const ProfileContext = createContext({});

export const ProfileProvider = ({ children }) => {

    const [profile, setProfile] = useState(null);

    useEffect(() => {
        SaltAndPepper.getProfile().then(response => {
                setProfile(response);
            }
        )
    }, [setProfile]);

    return (
        <ProfileContext.Provider value={profile}>
            {children}
        </ProfileContext.Provider>
    );
};
export const useProfile = () => {
    return useContext(ProfileContext);
};