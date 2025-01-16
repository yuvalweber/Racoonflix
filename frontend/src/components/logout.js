import { useNavigate } from 'react-router-dom';

const Logout = () => {
	const navigate = useNavigate();
	const logoutUser = () => {
		// Clear user data from local storage or context
		localStorage.removeItem('token');
		navigate("/");
	}

	return (
		<button className="btn btn-danger" onClick={logoutUser}>Logout</button>
	);
};

export default Logout;