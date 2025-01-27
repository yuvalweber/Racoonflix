import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Authentication/AuthContext';
import { useTheme } from '../components/themeContext'; // Import ThemeContext

const Logout = () => {
  const { setUserData, setToken } = useAuth();
  const { resetTheme } = useTheme(); // Consume resetTheme
  const navigate = useNavigate();

  const logoutUser = () => {
    // Clear user data and reset theme
    localStorage.removeItem('token');
    setToken(null);
    setUserData(null);
    resetTheme(); // Reset theme to dark mode
    navigate('/');
  };

  return (
    <button className="btn btn-danger" onClick={logoutUser}>
      Logout
    </button>
  );
};

export default Logout;
