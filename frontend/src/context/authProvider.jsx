import { createContext, useCallback, useContext, useEffect } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import {
  getRefreshToken,
  removeRefreshToken,
  storeRefreshToken,
  getAccessToken,
  removeAccessToken,
  storeAccessToken,
} from "../service/tokenStore";
import { SaltAndPepper } from "../api/saltAndPepper";
import { saltAndPepperClient } from "../config/axiosConfig";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const location = useLocation();
  const navigateTo = useNavigate();
  const unauthenticatedPaths = ["/login", "/invite"];

  const handleLogin = (authResponse) => {
    storeRefreshToken(authResponse.refreshToken);
    storeAccessToken(authResponse.accessToken);
    navigateTo("/");
  };

  const handleLogout = useCallback(() => {
    removeAccessToken();
    removeRefreshToken();
    navigateTo("/");
  }, [navigateTo]);

  function isLoggedIn() {
    return getAccessToken() !== "";
  }

  const addErrorHandlerInterceptor = useCallback(() => {
    const refreshInterceptor = saltAndPepperClient.interceptors.response.use(
      (response) => response,
      (error) => {
        console.log("errorInterceptor");
        const config = error?.config;

        if (
          error?.config?.url !== "/login" &&
          error?.response?.status === 401 &&
          !config?.sent
        ) {
          config.sent = true;
          saltAndPepperClient.interceptors.response.eject(refreshInterceptor);

          return SaltAndPepper.refreshToken(getRefreshToken())
            .then((authResponse) => {
              storeAccessToken(authResponse.accessToken);
              storeRefreshToken(authResponse.refreshToken);

              error.response.config.headers["Authorization"] =
                "Bearer " + authResponse.accessToken;
              return saltAndPepperClient(error.response.config);
            })
            .catch((refreshError) => {
              handleLogout();
              return Promise.reject(refreshError);
            })
            .finally(addErrorHandlerInterceptor);
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

  saltAndPepperClient.interceptors.response.clear();
  addErrorHandlerInterceptor();

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
