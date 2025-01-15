import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Icon from '../components/icon';
import FormField from '../components/formField';
import '../components/homePageBackground.css'; 
import '../components/card.css'; 
import '../components/scrollbar.css';

const SignUpPage = () => {
  const [formData, setFormData] = useState({
    firstName: '',
	lastName: '',
	userName: '',
	email: '',
	password: '',
	confirmPassword: '',
	profilePicture: ''
  });

  const scrollContainerRef = useRef(null); // Ref for the scroll container

  useEffect(() => {
    // Scroll to the top when the component loads
    if (scrollContainerRef.current) {
      scrollContainerRef.current.scrollTop = 0;
    }
  }, []);

  const fields = [
    { id: 'firstName', label: 'First Name', type: 'text', required: true },
    { id: 'lastName', label: 'Last Name', type: 'text', required: true },
    { id: 'userName', label: 'User Name', type: 'text', required: true },
    { id: 'email', label: 'Email Address', type: 'email', required: true },
    { id: 'password', label: 'Password', type: 'password', required: true },
    { id: 'confirmPassword', label: 'Confirm Password', type: 'password', required: true },
    { id: 'profilePicture', label: 'Profile Picture', type: 'text', required: false }
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
  
	if (formData.password !== formData.confirmPassword) {
	  alert('Passwords do not match!');
	  return;
	}
  
	try {
	  // remove the confirmPassword key before sending the data
	  const { confirmPassword, ...data } = formData;
	  const response = await axios.post('http://localhost:8080/api/users', data);
  
	  if (response.status === 201) {
		alert('Sign-up successful!');
		console.log('Server response:', response.data);
	  } else {
		alert(`Error: ${response.data || 'Something went wrong'}`);
	  }
	} catch (err) {
	  console.error('Error:', err);
	  if (err.response) {
		// Response error from server
		alert(`Error: ${err.response.data.errors || 'Server error occurred'}`);
	  } else {
		// Network or other error
		alert('An error occurred while signing up.');
	  }
	}
  };

  return (
    <div id="mainContainer" className="d-flex flex-column justify-content-center align-items-center vh-100">
      <Icon />
      <div
        className="card p-4 rounded scroll-menu"
        style={{ maxWidth: '600px', width: '100%', maxHeight: '70vh', overflowY: 'auto' }}
        ref={scrollContainerRef}
      >
        <h3 className="card-title text-center mb-4">Sign Up</h3>
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
          <button type="submit" className="btn btn-primary w-100">Sign Up</button>
        </form>
      </div>
    </div>
  );
};

export default SignUpPage;
