import React, { useState, useEffect } from 'react';
import { userService } from '../services/userService';

const UserForm = ({ user, onSave, onCancel }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [showPasswordChange, setShowPasswordChange] = useState(false);
  const [newPassword, setNewPassword] = useState('');

  useEffect(() => {
    if (user) {
      setFormData({
        name: user.name || '',
        email: user.email || '',
        password: '' // Don't pre-fill password
      });
      setIsEditing(true);
    } else {
      setFormData({
        name: '',
        email: '',
        password: ''
      });
      setIsEditing(false);
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        // For editing, don't include password in the update
        const { password, ...updateData } = formData;
        await userService.updateUser(user.id, updateData);
      } else {
        // For creating, include password
        await userService.createUser(formData);
      }
      onSave();
    } catch (error) {
      console.error('Error saving user:', error);
      alert('Error saving user: ' + error.message);
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    if (!newPassword.trim()) {
      alert('Please enter a new password');
      return;
    }
    try {
      await userService.changePassword(user.id, newPassword);
      setNewPassword('');
      setShowPasswordChange(false);
      alert('Password changed successfully');
    } catch (error) {
      console.error('Error changing password:', error);
      alert('Error changing password: ' + error.message);
    }
  };

  return (
    <div className="user-form">
      <h2>{isEditing ? 'Edit User' : 'Add New User'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        
        {!isEditing && (
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
        )}
        
        <div className="form-actions">
          <button type="submit">{isEditing ? 'Update' : 'Create'}</button>
          <button type="button" onClick={onCancel}>Cancel</button>
        </div>
      </form>

      {isEditing && (
        <div className="password-change-section">
          <h3>Change Password</h3>
          {!showPasswordChange ? (
            <button 
              type="button" 
              onClick={() => setShowPasswordChange(true)}
              className="change-password-btn"
            >
              Change Password
            </button>
          ) : (
            <form onSubmit={handlePasswordChange}>
              <div className="form-group">
                <label htmlFor="newPassword">New Password:</label>
                <input
                  type="password"
                  id="newPassword"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="submit">Change Password</button>
                <button 
                  type="button" 
                  onClick={() => {
                    setShowPasswordChange(false);
                    setNewPassword('');
                  }}
                >
                  Cancel
                </button>
              </div>
            </form>
          )}
        </div>
      )}
    </div>
  );
};

export default UserForm;
