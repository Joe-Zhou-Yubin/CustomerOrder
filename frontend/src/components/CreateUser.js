import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateUser() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    mobile: '',
    roles: [],
  });

  const handleRoleChange = (e) => {
    const { value } = e.target;
    const updatedRoles = [...formData.roles];

    if (updatedRoles.includes(value)) {
      // If the role is already in the array, remove it
      updatedRoles.splice(updatedRoles.indexOf(value), 1);
    } else {
      // If the role is not in the array, add it
      updatedRoles.push(value);
    }

    setFormData({ ...formData, roles: updatedRoles });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/api/auth/signup', formData);

      console.log('User created:', response.data);
      navigate('/user');
    } catch (error) {
      console.error('Error creating user:', error);
    }
  };

  return (
    <div className="container mt-5">
        <button className="btn btn-primary mb-3" onClick={() => navigate('/user')}>
        Back to User Dashboard
      </button>
      <h2>Create User</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">
            Username
          </label>
          <input
            type="text"
            className="form-control"
            id="username"
            name="username"
            value={formData.username}
            onChange={(e) => setFormData({ ...formData, username: e.target.value })}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            Email
          </label>
          <input
            type="email"
            className="form-control"
            id="email"
            name="email"
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label">
            Password
          </label>
          <input
            type="password"
            className="form-control"
            id="password"
            name="password"
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="mobile" className="form-label">
            Mobile
          </label>
          <input
            type="text"
            className="form-control"
            id="mobile"
            name="mobile"
            value={formData.mobile}
            onChange={(e) => setFormData({ ...formData, mobile: e.target.value })}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Roles</label>
          <div className="form-check">
            <input
              className="form-check-input"
              type="checkbox"
              value="member"
              id="member"
              checked={formData.roles.includes('member')}
              onChange={handleRoleChange}
            />
            <label className="form-check-label" htmlFor="member">
              Member
            </label>
          </div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="checkbox"
              value="admin"
              id="admin"
              checked={formData.roles.includes('admin')}
              onChange={handleRoleChange}
            />
            <label className="form-check-label" htmlFor="admin">
              Admin
            </label>
          </div>
          {/* Add more role checkboxes as needed */}
        </div>
        <button type="submit" className="btn btn-primary">
          Create User
        </button>
      </form>
    </div>
  );
}

export default CreateUser;
