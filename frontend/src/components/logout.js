import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Authentication/AuthContext';

const Logout = () => {
	const { setUserData, setToken } = useAuth();
	const navigate = useNavigate();
	const logoutUser = () => {
		// Clear user data from local storage or context
		localStorage.removeItem('token');
		setToken(null);
		setUserData(null);
		navigate("/");
	}

	return (
		<button className="btn btn-danger" onClick={logoutUser}>Logout</button>
	);
};

export default Logout;