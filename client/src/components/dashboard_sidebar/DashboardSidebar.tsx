import React from 'react'
import './DashboardSidebar.css'
import { useAuth } from '../../contexts/AuthContext'
import { Link } from 'react-router-dom';

function DashboardSidebar() {
    const auth = useAuth();
    const role = auth.user?.role || 1;

    return (
        <div className="dashboard-sidebar">
            {
                (role === 1 || role >= 3) && 
                <ul>
                    <li><Link to="/dashboard">Vehicle List</Link></li>
                    <li><Link to="/dashboard/user/dealer">Become Dealer</Link></li>
                </ul>
            }
            
            {
                (role === 2 || role >= 3) && 
                <ul>
                    <li><Link to="/dashboard/dealerVehicleList">Dealer Vehicle List</Link></li>
                    <li><Link to="/dashboard/dealer/add">Add New Vehicle</Link></li>
                </ul>
            }

            {
                (role >= 3) && 
                <ul>
                    <li><Link to="/dashboard/admin/user">User Administrative</Link></li>
                    <li><Link to="/dashboard/admin/dealer">Dealer Administrative</Link></li>
                </ul>
            }

            
        </div>
    )
}

export default DashboardSidebar