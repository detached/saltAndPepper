import { createContext, useContext, useEffect, useState } from "react";
import { storeToken, deleteToken, getToken } from "../service/sessionService";
import { Navigate, useLocation, useNavigate } from "react-router-dom";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const location = useLocation();
  const navigateTo = useNavigate();

  useEffect(() => {
    const storedToken = getToken();
    if (storedToken) {
      setToken(storedToken);
    }
  }, [setToken]);

  const handleLogin = (token) => {
    setToken(token);
    storeToken(token);
    navigateTo("/");
  };

  const handleLogout = () => {
    setToken(null);
    deleteToken();
    navigateTo("/");
  };

  const value = {
    token,
    onLogin: handleLogin,
    onLogout: handleLogout,
  };

  if (location.pathname === "/login" || token) {
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
