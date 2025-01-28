import React, { useContext, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ThemeContext } from './themeContext';
import Logout from './logout';
import './navbar.css';
import axios from 'axios';
import { useAuth } from '../Authentication/AuthContext';


// Set the base URL for the API
axios.defaults.baseURL = 'http://localhost:8080';

// Navbar component
const Navbar = ({ isAdmin }) => {
  const { token , userData } = useAuth();
  const { isDarkMode, toggleTheme } = useContext(ThemeContext); // Use ThemeContext
  const [showModal, setShowModal] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [userId, setUserId] = useState('');
  

  // State for showing the modal
  const fetchUserInfo = async () => {
    try { 
      const response = await axios.get(`/api/users/${userId}`);
      setUserInfo(response.data);
    } catch (error) {
      console.error('Error fetching user info:', error);
    }
  };

  // State for showing the modal
  const handleProfileClick = () => {
    setShowModal(true);
    fetchUserInfo();
  };

  // Close the modal
  const closeModal = () => setShowModal(false);

   // UseEffect to check if the user is an admin and set the Authorization header
   useEffect(() => {
		if (token) {
		  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
		}
		if (userData) {
      setUserId(userData.id);
		}
	  }, [token, userData]); // Set the Authorization header once on mount


  return (
    // Navbar with links to home, all movies, search, theme toggle, logout, and management
    <nav className={`navbar navbar-expand-lg ${isDarkMode ? 'navbar-dark bg-dark' : 'navbar-light bg-light'} position-fixed top-0 start-0 w-100`}>
      <div className="container-fluid">
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link className="active home-link" aria-current="page" to="/connected">
                <img src="/images/newHome.png" alt="home" style={{ width: 120, marginLeft: -20 }} />
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/allMovies">
                All movies
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/search">Search</Link>
            </li>
          </ul>
          <ul className="navbar-nav">
            <li className="nav-item">
              <button className="btn btn-primary me-2" onClick={handleProfileClick}>
                Profile
              </button>
            </li>
          </ul>
          {/* Modal */}
          {showModal && (
            <div className="custom-modal-overlay">
              <div className="custom-modal">
                <div className="custom-modal-header">
                  <h5 className="custom-modal-title">User Profile</h5>
                  <button className="custom-modal-close" onClick={closeModal}>&times;</button>
                </div>
                <div className="custom-modal-body">
                  {userInfo ? (
                    <div className="user-info">
                      {/* Profile Picture */}
                      {userInfo.profilePicture && (
                        <div className="profile-picture-container">
                          <img
                            src={userInfo.profilePicture}
                            alt={""}
                            className="profile-picture"
                          />
                        </div>
                      )}
                      {/* User Info */}
                      <p><strong>First Name:</strong> {userInfo.firstName}</p>
                      <p><strong>Last Name:</strong> {userInfo.lastName}</p>
                      <p><strong>Username:</strong> {userInfo.userName}</p>
                      <p><strong>Email:</strong> {userInfo.email}</p>
                      <p><strong>Admin:</strong> {userInfo.isAdmin ? 'Yes' : 'No'}</p>
                    </div>
                  ) : (
                    <p>Loading...</p>
                  )}
                </div>
                <div className="custom-modal-footer">
                  <button className="custom-modal-button" onClick={closeModal}>
                    Close
                  </button>
                </div>
              </div>
            </div>
          )}
          <ul className="navbar-nav">
            <li className="nav-item">
                {/* Button to toggle between light and dark mode */}
              <button
                className={`btn ${isDarkMode ? 'btn-outline-light' : 'btn-outline-dark'} me-2`}
                onClick={toggleTheme}
              >
                {/* Icon for theme toggle */}
                {isDarkMode ? 'Light Mode' : 'Dark Mode'}
              </button>
            </li>
            <li className="nav-item">
              <Logout />
            </li>
            {isAdmin && (
              <li className="nav-item">
                <Link className="btn btn-warning ms-2" to="/management">Management</Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
