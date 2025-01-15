import React, { useState } from 'react';
import '../components/homePageBackground.css'; 
import '../components/card.css'; 
import { Link } from 'react-router-dom';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // כאן תוכל להוסיף את הלוגיקה של כניסת המשתמש (לדוגמה, שליחה לשרת)
    alert('Login successful!');
  };

  return (
    <div id="mainContainer" className="d-flex flex-column justify-content-center align-items-center vh-100">
      <div className="card p-4 rounded" style={{ maxWidth: '600px', width: '100%' }}>
        <h3 className="card-title text-center mb-4">Log In</h3>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">Email Address</label>
            <input
              type="email"
              className="form-control cardField"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">Password</label>
            <input
              type="password"
              className="form-control cardField"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">Log In</button>
        </form>

        <div className="mt-3 text-center">
          <p>Don't have an account?</p>
          <Link to="/signup" className="btn btn-danger">Sign Up</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
