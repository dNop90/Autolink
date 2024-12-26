import React, { useState, useEffect } from 'react';

const UserProfile: React.FC = () => {
  const [profile, setProfile] = useState({ name: '', email: '' });
  const [message, setMessage] = useState('');
  
  // Fetch user information when the component loads
    const fetchProfile = async () => {
      try {
        const response = await fetch('/api/user/info');
        console.log(response);  // Log the response object
        if (response.ok) {
          const data = await response.json();
          setProfile({ name: data.name, email: data.email });
        } else {
          setMessage('Failed to load profile information.');
        }
      } catch (error) {
        setMessage('An error occurred while fetching the profile.');
        console.error(error);  // Log error for debugging
      }
    };
    
  // Handle input changes
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  // Submit updated profile
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch('/api/user/edit-profile', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Add authentication headers if necessary
          // Example: Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(profile),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.success) {
          setMessage('Profile updated successfully!');
        } else {
          setMessage(data.message || 'An error occurred.');
        }
      } else {
        setMessage('Failed to update profile.');
      }
    } catch (error) {
      setMessage('An error occurred while updating the profile.');
      console.error(error);  // Log error for debugging
    }
  };

  return (
    <div>
      <h1>Your Profile</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Name:
          <input
            type="text"
            name="name"
            value={profile.name}
            onChange={handleInputChange}
          />
        </label>
        <br />
        <label>
          Email:
          <input
            type="email"
            name="email"
            value={profile.email}
            onChange={handleInputChange}
          />
        </label>
        <br />
        <button type="submit">Update Profile</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default UserProfile;
