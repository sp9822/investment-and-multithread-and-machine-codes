import React, { useState, useEffect } from 'react';
import { userService } from '../services/userService';
import UserForm from './UserForm';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const usersData = await userService.getAllUsers();
      setUsers(usersData);
      setError(null);
    } catch (err) {
      setError('Failed to fetch users: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleAddUser = () => {
    setEditingUser(null);
    setShowForm(true);
  };

  const handleEditUser = (user) => {
    setEditingUser(user);
    setShowForm(true);
  };

  const handleDeleteUser = async (id) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await userService.deleteUser(id);
        fetchUsers(); // Refresh the list
      } catch (err) {
        alert('Failed to delete user: ' + err.message);
      }
    }
  };

  const handleFormSave = () => {
    setShowForm(false);
    setEditingUser(null);
    fetchUsers(); // Refresh the list
  };

  const handleFormCancel = () => {
    setShowForm(false);
    setEditingUser(null);
  };

  if (loading) {
    return <div className="loading">Loading users...</div>;
  }

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
    <div className="user-list">
      <div className="user-list-header">
        <h1>User Management</h1>
        <button onClick={handleAddUser} className="add-user-btn">
          Add New User
        </button>
      </div>

      {showForm && (
        <UserForm
          user={editingUser}
          onSave={handleFormSave}
          onCancel={handleFormCancel}
        />
      )}

      <div className="users-table">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>
                  <button 
                    onClick={() => handleEditUser(user)}
                    className="edit-btn"
                  >
                    Edit
                  </button>
                  <button 
                    onClick={() => handleDeleteUser(user.id)}
                    className="delete-btn"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default UserList;

