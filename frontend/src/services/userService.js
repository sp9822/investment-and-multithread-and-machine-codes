const API_BASE_URL = 'http://localhost:8080/api';

export const userService = {
  getAllUsers: async () => {
    const response = await fetch(`${API_BASE_URL}/users`);
    if (!response.ok) {
      throw new Error('Failed to fetch users');
    }
    return response.json();
  },

  getUserById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch user');
    }
    return response.json();
  },

  createUser: async (userData) => {
    const response = await fetch(`${API_BASE_URL}/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    if (!response.ok) {
      throw new Error('Failed to create user');
    }
    return response.json();
  },

  updateUser: async (id, userData) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    if (!response.ok) {
      throw new Error('Failed to update user');
    }
    return response.json();
  },

  changePassword: async (id, newPassword) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}/password`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ password: newPassword }),
    });
    if (!response.ok) {
      throw new Error('Failed to change password');
    }
    return response.json();
  },

  deleteUser: async (id) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error('Failed to delete user');
    }
  },
};

