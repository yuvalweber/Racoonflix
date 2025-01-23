import { useAuth } from "./AuthContext";
import { Navigate } from "react-router-dom";
import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080";

export const ProtectedRoute = ({ Component }) => {
  const { token, userData, setUserData } = useAuth();

  // If there's no token, redirect to login page
  if (!token) {
    return <Navigate to="/login" />;
  }

  // if token still works and user data is not fetched, fetch user data
  if(!userData){
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    const response = axios.get("/api/tokens");
    response.then((res) => {
      setUserData(res.data);
    }).catch((error) => {
      localStorage.removeItem("token");
      return <Navigate to="/login" />;
    });

  }

  // If authenticated, render the protected component
  return Component;
};
