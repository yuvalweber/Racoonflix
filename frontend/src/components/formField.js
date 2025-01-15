import React from 'react';

const FormField = ({ id, label, type, value, onChange, required }) => (
  <div className="mb-3">
    <label htmlFor={id} className="form-label">{label}</label>
    <input
      type={type}
      className="form-control cardField"
      id={id}
      name={id}
      value={value}
      onChange={onChange}
      required={required}
    />
  </div>
);

export default FormField;
