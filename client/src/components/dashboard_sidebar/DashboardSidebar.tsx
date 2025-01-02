import React from 'react'
import './DashboardSidebar.css'
import { useAuth } from '../../contexts/AuthContext'
import { Link } from 'react-router-dom';
import MessageSystem from '../message/MessageSystem';

function DashboardSidebar() {
    const auth = useAuth();
    const role = auth.user?.role;

    return (
        <div className="dashboard-sidebar">
            <ul>
                <li><Link to="/dashboard/vehiclelist">Vehicle List</Link></li>
                <li><Link to="/dashboard/user/dealer">Become Dealer</Link></li>
            </ul>
            
            <ul>
                <li><Link to="/dashboard/dealer/list">Dealer Vehicle List</Link></li>
                <li><Link to="/dashboard/dealer/add">Add New Vehicle</Link></li>
            </ul>

            <ul>
                <li><Link to="/dashboard/admin/user">User Administrative</Link></li>
                <li><Link to="/dashboard/admin/dealer">Dealer Administrative</Link></li>
            </ul>
        </div>
    )
}

export default DashboardSidebar