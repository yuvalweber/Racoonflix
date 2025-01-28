// Import necessary modules from React
import React, { createContext, useState, useContext } from "react";

// Create a context for authentication
export const AuthContext = createContext();

// Custom hook to use the AuthContext
export function useAuth() {
  return useContext(AuthContext);
}

// Provider component to wrap the application with AuthContext
export const AuthProvider = ({ children }) => {
  // State to store user data
  const [userData, setUserData] = useState(null);
  // State to store token, initialized from localStorage
  const [token, setToken] = useState(localStorage.getItem("token"));

  return (
    <AuthContext.Provider value={{ userData, setUserData, token, setToken }}>
      {children}
    </AuthContext.Provider>
  );
};
