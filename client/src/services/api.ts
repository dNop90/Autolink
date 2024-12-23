import axios from 'axios';

const API_URL = 'http://your-backend-api-url'; // Replace with your backend URL

export const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Example function to login a user
export const loginUser = async (username: string, password: string) => {
  const response = await api.post('/login', { username, password });
  return response.data;
};
