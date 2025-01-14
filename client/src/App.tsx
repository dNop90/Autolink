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
import AdminUserDashboard from './pages/Admin/AdminUserDashboard';
import AdminDealerDashboard from './pages/Admin/AdminDealerDashboard';
import VehicleDetails from './pages/Vehicle/VehicleDetails';
import DealerVehicleList from './pages/Vehicle/DealerVehicleList';
import UpdateVehicle from './pages/Vehicle/UpdateVehicle';
import MessageSystem from './components/message/MessageSystem';
import DealerApp from './pages/DealerApp';


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
          {/* testing for Car api request */}
          <Route path="/addvehicle" element={<AddVehicle />} />

          <Route path="/vehicle/:vehicleId" element={<VehicleDetails dLer={true}/>} />
          <Route path="/dealerVehicle/:vehicleId" element={<VehicleDetails dLer={false}/>} />
          
          <Route element={<UserRouteGuard />}>
            <Route path="/dealer-dashboard" element={<DealerDashboard />} />
            <Route path="/user/profile" element={<UserProfile />} />

            <Route element={<DashboardRoute />}>
              <Route path="/dashboard/dealer/add" element={<AddVehicle />} />
              <Route path="/dashboard" element={<DealerVehicleList dLer={false} />} />
              <Route path="/dashboard/dealerVehicleList" element={<DealerVehicleList dLer={true}/>} />
              <Route path="/dashboard/vehiclelist/update-vehicle/:vehicleId" element={<UpdateVehicle />} />

              <Route path="/dashboard/admin/user" element={<AdminUserDashboard />} />
              <Route path="/dashboard/admin/dealer" element={<AdminDealerDashboard />} />
              <Route path="/dashboard/user/dealer" element={<DealerApp />} />
            </Route>
          </Route>
        </Routes>

        <MessageSystem />
      </AuthProvider>
    </CookieProvider>
  );
};

export default App;
