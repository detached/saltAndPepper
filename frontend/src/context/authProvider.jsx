import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import {getToken, removeToken, storeToken} from "../service/tokenStore";
import { saltAndPepperClient } from "../config/axiosConfig";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(getToken());
  const location = useLocation();
  const navigateTo = useNavigate();
  const unauthenticatedPaths = ["/login", "/invite"];

  const handleLogin = (token) => {
    storeToken(token);
    setToken(token);
    navigateTo("/");
  };

  const handleLogout = useCallback(() => {
    removeToken();
    setToken("");
    navigateTo("/");
  }, [setToken, navigateTo]);

  function isLoggedIn() {
    return token !== "";
  }

  useEffect(() => {
    saltAndPepperClient.interceptors.response.clear();
    saltAndPepperClient.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        if (error.config.url !== "/login" && error.response.status === 401) {
          handleLogout();
        }
        return Promise.reject(error);
      }
    );
  }, [handleLogout]);

  const value = {
    isLoggedIn: isLoggedIn(),
    onLogin: handleLogin,
    onLogout: handleLogout,
  };

  function doesntNeedAuthentication(pathname) {
    for (let i = 0; i < unauthenticatedPaths.length; i++) {
      if (pathname.startsWith(unauthenticatedPaths[i])) {
        return true;
      }
    }
    return false;
  }

  if (doesntNeedAuthentication(location.pathname) || isLoggedIn()) {
    return (
      <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
    );
  } else {
    return <Navigate to="/login" />;
  }
};

export const useAuth = () => {
  return useContext(AuthContext);
};
