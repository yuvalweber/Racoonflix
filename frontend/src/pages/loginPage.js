import React, { useState } from 'react';
import '../components/homePageBackground.css'; 
import '../components/card.css'; 
import Icon from '../components/icon';
import FormField from '../components/formField'; 
import { Link } from 'react-router-dom';
import axios from 'axios'; // Import axios for making HTTP requests

const LoginPage = () => {
  const [formData, setFormData] = useState({
    userName: '',
    password: ''
  });

  // Fields configuration for the form
  const fields = [
    { id: 'userName', label: 'User Name', type: 'text', required: true },
    { id: 'password', label: 'Password', type: 'password', required: true }
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

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
        alert('Login successful!');
        console.log('Token:', response.data); // Save or use the token as needed
        // You can store the token in localStorage, state, or use it for further requests
      }
    } catch (err) {
      console.error('Error logging in:', err);
      alert(`Login failed. ${err.response.data.errors}`);
    }
  };

  return (
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
