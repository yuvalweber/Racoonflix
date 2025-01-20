import { useAuth } from "./AuthContext";
import { Navigate } from "react-router-dom";

export const ProtectedRoute = ({ Component }) => {
  const { token } = useAuth();

  // If there's no token, redirect to login page
  if (!token) {
    return <Navigate to="/login" />;
  }

  // If authenticated, render the protected component
  return Component;
};
