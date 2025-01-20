import React, { createContext, useState, useContext} from "react";

export const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export const AuthProvider = ({ children }) => {
  const [userData, setUserData] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));

  return (
    <AuthContext.Provider value={{ userData, setUserData, token, setToken }}>
      {children}
    </AuthContext.Provider>
  );
};
