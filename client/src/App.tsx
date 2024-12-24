import React from 'react';
import { Route, Routes } from 'react-router-dom';

import { AuthProvider } from './contexts/AuthContext';
import DealerDashboard from './pages/DealerDashboard';
import Home from './pages/Home';
import UserProfile from './pages/UserProfile';
import Navbar from './components/navbar/Navbar';
import UserRouteGuard from './routeguards/UserRouteGuard';


const App: React.FC = () => {
  return (
    <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />

        <Route element={<UserRouteGuard/>}>
          <Route path="/dealer-dashboard" element={<DealerDashboard />} />
          <Route path="/user-profile" element={<UserProfile />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
};

export default App;
