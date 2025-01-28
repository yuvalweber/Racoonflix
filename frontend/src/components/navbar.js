import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { ThemeContext } from './themeContext';
import Logout from './logout';
import './navbar.css';

// Navbar component
const Navbar = ({ isAdmin }) => {
  const { isDarkMode, toggleTheme } = useContext(ThemeContext); // Use ThemeContext

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
