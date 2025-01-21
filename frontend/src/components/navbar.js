import React from 'react';
import { Link } from 'react-router-dom';
import Logout from './logout';
import './navbar.css';

const Navbar = ({ isAdmin }) => {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark position-fixed top-0 start-0 w-100">
      <div className="container-fluid">
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
             <Link className="nav-link active home-link" aria-current="page" to="/connected">
             <img src="/images/newHome.png" alt="home" style={{ width: 120, marginLeft: -20 }}/>
             </Link>
            </li>

            <li className="nav-item dropdown">
              <Link className="nav-link dropdown-toggle" to="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Movies by Category
              </Link>
              <ul className="scroll-menu dropdown-menu" aria-labelledby="navbarDropdown">
                <li><Link className="dropdown-item" to="/movies/action">Action</Link></li>
                <li><Link className="dropdown-item" to="/movies/comedy">Comedy</Link></li>
                <li><Link className="dropdown-item" to="/movies/drama">Drama</Link></li>
                <li><Link className="dropdown-item" to="/movies/horror">Horror</Link></li>
                <li><Link className="dropdown-item" to="/movies/romance">Romance</Link></li>
                <li><Link className="dropdown-item" to="/movies/thriller">Thriller</Link></li>
                <li><Link className="dropdown-item" to="/movies/scifi">Sci-Fi</Link></li>
                <li><Link className="dropdown-item" to="/movies/fantasy">Fantasy</Link></li>
                <li><Link className="dropdown-item" to="/movies/documentary">Documentary</Link></li>
              </ul>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/search">Search</Link>
            </li>
          </ul>
          <ul className="navbar-nav">
            <li className="nav-item">
              <button className="btn btn-outline-light me-2">Dark/Light Mode</button>
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
