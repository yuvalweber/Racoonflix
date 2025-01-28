import { useAuth } from "./AuthContext";
import { Navigate } from "react-router-dom";
import axios from "axios";

// Set the base URL for the axios requests
axios.defaults.baseURL = "http://localhost:8080";

// Function to create a protected route
export const ProtectedRoute = ({ Component }) => {
  const { token, userData, setUserData } = useAuth();

  // If there's no token, redirect to login page
  if (!token) {
    return <Navigate to="/login" />;
  }

  // if token still works and user data is not fetched, fetch user data
  if(!userData){
    // Set the token in the Authorization header
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    const response = axios.get("/api/tokens");
    response.then((res) => {
      // Set the user data in the AuthContext
      setUserData(res.data);
    }).catch((error) => {
      localStorage.removeItem("token");
      return <Navigate to="/login" />;
    });

  }

  // If authenticated, render the protected component
  return Component;
};
