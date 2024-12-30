import React, { useState, useEffect } from "react";
import axios from "axios";

const UserProfile: React.FC = () => {
  const [profile, setProfile] = useState({ name: "", email: "" });
  const [message, setMessage] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const token = localStorage.getItem("jwtToken");
  const API_LINK = process.env.REACT_APP_API_USER;

  // Fetch the user's profile information
  const fetchProfile = async () => {
    try {
      const response = await axios.get(`${API_LINK}/user/info`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200) {
        setProfile(response.data); // Set the profile data from API
      } else {
        setMessage("Failed to load profile information.");
      }
    } catch (error) {
      setMessage("An error occurred while fetching the profile.");
      console.error(error);
    }
  };

  // Handle the form submission to update profile data
  const handleProfileSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `${API_LINK}/user/update`,
        profile,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200) {
        setMessage("Profile updated successfully!");
      } else {
        setMessage("Failed to update profile.");
      }
    } catch (error) {
      setMessage("An error occurred while updating the profile.");
      console.error(error);
    }
  };

  // Handle the password change submission
  const handlePasswordChange = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      setMessage("New passwords do not match.");
      return;
    }

    try {
      const response = await axios.post(
        `${API_LINK}/user/change-password`,
        { currentPassword, newPassword },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200) {
        setMessage("Password updated successfully!");
        setCurrentPassword("");
        setNewPassword("");
        setConfirmPassword("");
      } else {
        setMessage("Failed to update password.");
      }
    } catch (error) {
      setMessage("An error occurred while updating the password.");
      console.error(error);
    }
  };

  // Handle changes in input fields
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  useEffect(() => {
    if (token) {
      fetchProfile();
    } else {
      setMessage("You need to log in to access this page.");
    }
  }, [token]);

  return (
    <div className="profile-container">
      <h1 className="profile-title">Your Profile</h1>

      {message && <div className="message">{message}</div>}

      <form onSubmit={handleProfileSubmit} className="profile-form">
        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            name="name"
            value={profile.name}
            onChange={handleInputChange}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            value={profile.email}
            onChange={handleInputChange}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <button type="submit" className="btn btn-primary">
            Update Profile
          </button>
        </div>
      </form>

      <h2 className="password-title">Change Password</h2>
      <form onSubmit={handlePasswordChange} className="password-form">
        <div className="form-group">
          <label htmlFor="currentPassword">Current Password</label>
          <input
            type="password"
            id="currentPassword"
            name="currentPassword"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="newPassword">New Password</label>
          <input
            type="password"
            id="newPassword"
            name="newPassword"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm New Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <button type="submit" className="btn btn-primary">
            Change Password
          </button>
        </div>
      </form>
    </div>
  );
};

export default UserProfile;
