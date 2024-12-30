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
import ViewInventory from './pages/Vehicle/ViewInventory';
import { CookieProvider } from './contexts/CookieContext';
import Logout from './pages/Logout';
import AddVehicle from './pages/Vehicle/AddVehicle';
import DashboardRoute from './routeguards/DashboardRoute';
import Dashboard_Home from './pages/Dashboard_Home';
import VehicleDetails from './pages/Vehicle/VehicleDetails';
import DelerVehicleList from './pages/Vehicle/DealerVehicleList';
import UpdateVehicle from './pages/Vehicle/UpdateVehicle';


const App: React.FC = () => {
  return (
    <CookieProvider>
      <AuthProvider>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/register" element={<Register />} />
          <Route path="/explore" element={<ViewInventory />} />
          <Route path="/vehicle/:vehicleId" element={<VehicleDetails/>} />

          <Route element={<UserRouteGuard />}>
            <Route path="/dealer-dashboard" element={<DealerDashboard />} />
            <Route path="/user/profile" element={<UserProfile />} />

            <Route element={<DashboardRoute />}>
              <Route path="/dashboard" element={<Dashboard_Home />} />
              {/* testing for Car api request */}
              
              <Route path="/dashboard/dealer/add" element={<AddVehicle />} />
              <Route path="/dashboard/vehiclelist" element={<DelerVehicleList />} />
              <Route path="/dashboard/vehiclelist/update-vehicle/:vehicleId" element={<UpdateVehicle />} />
              
            </Route>
          </Route>
        </Routes>
      </AuthProvider>
    </CookieProvider>
  );
};

export default App;
