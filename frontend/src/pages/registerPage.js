import React, { useState } from 'react';
import Icon from '../components/icon';
import FormField from '../components/formField';
import '../components/homePageBackground.css'; 
import '../components/card.css'; 

const SignUpPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const fields = [
    { id: 'firstName', label: 'First Name', type: 'text', required: true },
	{ id: 'lastName', label: 'Last Name', type: 'text', required: true },
	{ id: 'userName', label: 'User Name', type: 'text', required: true },
    { id: 'email', label: 'Email Address', type: 'email', required: true },
    { id: 'password', label: 'Password', type: 'password', required: true },
    { id: 'confirmPassword', label: 'Confirm Password', type: 'password', required: true },
	{id: 'profilePicture', label: 'Profile Picture', type: 'text', required: false }
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert('Passwords do not match!');
    } else {
      alert('Sign-up successful!');
    }
  };

  return (
    <div id="mainContainer" className="d-flex flex-column justify-content-center align-items-center vh-100">
      <Icon />
      <div className="card p-4 rounded" style={{ maxWidth: '600px', width: '100%' }}>
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
