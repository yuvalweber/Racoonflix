import React, { useState } from 'react';
import '../components/homePageBackground.css'; 
import '../components/card.css'; 
import Icon from '../components/icon';
import FormField from '../components/formField'; 
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios'; // Import axios for making HTTP requests
import { useAuth } from '../Authentication/AuthContext';

// Login page component
const LoginPage = () => {
  const { setUserData, setToken } = useAuth();
  const [formData, setFormData] = useState({
    userName: '',
    password: ''
  });

  // Use the navigate hook to redirect to the home page
  const navigate = useNavigate();

  // Fields configuration for the form
  const fields = [
    { id: 'userName', label: 'User Name', type: 'text', required: true },
    { id: 'password', label: 'Password', type: 'password', required: true }
  ];

  // Handle form field changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Sending login data to the API via POST request
      const response = await axios.post('http://localhost:8080/api/tokens', {
        userName: formData.userName,
        password: formData.password
      });

      if (response.status === 200) {
        // Assuming the response contains a token
		    localStorage.setItem('token', response.data.token); // Save the token in local storage
		    setToken(response.data.token); // Set the user data in the context
		    axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`; // Set the Authorization header
		    const userInfo = await axios.get('/api/tokens'); // get user information
		    setUserData(userInfo.data);
		    navigate('/connected'); // Redirect to the home page  
      }
    } catch (err) {
      console.error('Error logging in:', err);
      alert(`Login failed. ${err.response.data.errors}`);
    }
  };

  return (
    // Login form
    <div id="mainContainer" className="d-flex flex-column justify-content-center align-items-center vh-100">
      <Icon />
      <div className="card p-4 rounded" style={{ maxWidth: '600px', width: '100%' }}>
        <h3 className="card-title text-center mb-4">Log In</h3>
        <form onSubmit={handleSubmit}>
          {fields.map((field) => (
            <FormField
              key={field.id}
              id={field.id}
              label={field.label}
              type={field.type}
              value={formData[field.id]}
              onChange={handleChange}
              required={field.required}
            />
          ))}
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
