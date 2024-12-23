import React from 'react';
import { Route, Routes } from 'react-router-dom';

import { AuthProvider } from './contexts/AuthContext';
import DealerDashboard from './pages/DealerDashboard';
import Home from './pages/Home';
import UserProfile from './pages/UserProfile';
import Navbar from './components/navbar/Navbar';


const App: React.FC = () => {
  return (
    <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dealer-dashboard" element={<DealerDashboard />} />
        <Route path="/user-profile" element={<UserProfile />} />
      </Routes>
    </AuthProvider>
  );
};

export default App;
