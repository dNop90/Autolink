import React from 'react';
import { Route, Routes } from 'react-router-dom';

import { AuthProvider } from './contexts/AuthContext';
import DealerDashboard from './pages/DealerDashboard';
import Home from './pages/Home';
import UserProfile from './pages/UserProfile';
import Navbar from './components/navbar/Navbar';
import UserRouteGuard from './routeguards/UserRouteGuard';
import Login from './pages/Login';
import Register from './pages/Register';


const App: React.FC = () => {
  return (
    <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />}></Route>
        <Route path="/register" element={<Register />}></Route>
        <Route element={<UserRouteGuard/>}>
          <Route path="/dealer-dashboard" element={<DealerDashboard />} />
          <Route path="/user-profile" element={<UserProfile />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
};

export default App;
