import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Icon from '../components/icon';
import FormField from '../components/formField';
import '../components/homePageBackground.css';
import '../components/card.css';

const SignUpPage = () => {
  // State to hold form data
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    userName: '',
    email: '',
    password: '',
    confirmPassword: '',
    profilePicture: null,
  });

  // Hook to navigate programmatically
  const navigate = useNavigate();

  // Ref for the scroll container
  const scrollContainerRef = useRef(null);

  useEffect(() => {
    if (scrollContainerRef.current) {
      scrollContainerRef.current.scrollTop = 0;
    }
  }, []);

  // Handle input change
  const handleChange = (e) => {
    const { name, type, value, files } = e.target;
    if (type === 'file') {
      setFormData({
        ...formData,
        [name]: files[0], // Store the file object in state
      });
    } else {
      setFormData({
        ...formData,
        [name]: value, // Store text inputs normally
      });
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      alert('Passwords do not match!');
      return;
    }

    try {
      const { confirmPassword, ...data } = formData;
      const formDataToSubmit = new FormData();

      // Append all form data to FormData
      Object.keys(data).forEach((key) => {
        formDataToSubmit.append(key, data[key]);
      });

      const response = await axios.post('http://localhost:8080/api/users', formDataToSubmit, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      if (response.status === 201) {
        alert('Sign-up successful!');
        navigate('/login');
      } else {
        alert(`Error: ${response.data || 'Something went wrong'}`);
      }
    } catch (err) {
      console.error('Error:', err);
      alert('An error occurred while signing up.');
    }
  };

  const fields = [
    { id: 'firstName', label: 'First Name', type: 'text', required: true },
    { id: 'lastName', label: 'Last Name', type: 'text', required: true },
    { id: 'userName', label: 'User Name', type: 'text', required: true },
    { id: 'email', label: 'Email Address', type: 'email', required: true },
    { id: 'password', label: 'Password', type: 'password', required: true },
    { id: 'confirmPassword', label: 'Confirm Password', type: 'password', required: true },
    { id: 'profilePicture', label: 'Profile Picture', type: 'file', required: false }, // File input
  ];

  return (
    <div
      id="mainContainer"
      className="d-flex flex-column justify-content-center align-items-center vh-100"
    >
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
              value={field.type !== 'file' ? formData[field.id] : undefined} // Don't set value for file input
              onChange={handleChange}
              required={field.required}
            />
          ))}
          <button type="submit" className="btn btn-primary w-100">
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
};

export default SignUpPage;