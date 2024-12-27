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
import ViewInventory from './pages/ViewInventory';
import { CookieProvider } from './contexts/CookieContext';
import Logout from './components/Logout';


const App: React.FC = () => {
  return (
    
    <CookieProvider>
      <AuthProvider>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout/>}/>
          <Route path="/register" element={<Register />} />
          <Route path="/explore" element={<ViewInventory />}/>

          <Route element={<UserRouteGuard/>}>
            <Route path="/dealer-dashboard" element={<DealerDashboard />} />
            <Route path="/user-profile" element={<UserProfile />} />
          </Route>
        </Routes>
      </AuthProvider>
    </CookieProvider>
  );
};

export default App;
