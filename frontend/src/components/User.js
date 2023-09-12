import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Users() {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    setCurrentUser(user);
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/auth/users');
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      await axios.delete(`http://localhost:8080/api/auth/delete/${userId}`);
      fetchUsers();
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // Custom role name mapping
  const customRoleNames = {
    ROLE_MEMBER: 'Member',
    ROLE_ADMIN: 'Admin',
    // Add more mappings as needed
  };

  return (
    <div className="container mt-4">
      <div className="mb-3">
        <Link to="/home" className="btn btn-primary">
          Back to Home
        </Link>
        <Link to="/createuser" className="btn btn-success">
          Create User
        </Link>
      </div>
      {currentUser ? (
        <div className="card">
          <div className="card-header d-flex justify-content-between align-items-center">
            <div>User Dashboard</div>
          </div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>User ID</th>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Mobile</th>
                  <th>Roles</th>
                  <th>Delete</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.userId}</td>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td>{user.mobile}</td>
                    <td>
                      {user.roles.map((role) => customRoleNames[role.name] || role.name).join(', ')}
                    </td>
                    <td>
                      {user.username !== currentUser.username && (
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDeleteUser(user.userId)}
                        >
                          Delete
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="alert alert-danger">
          You are not authenticated. Please log in to view this page.
        </div>
      )}
    </div>
  );
}

export default Users;
