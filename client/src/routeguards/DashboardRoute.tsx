import React from 'react'
import { Outlet } from 'react-router-dom'
import './DashboardRoute.css'
import DashboardSidebar from '../components/dashboard_sidebar/DashboardSidebar'

function DashboardRoute() {
    return (
        <div className="dashboard">
            <DashboardSidebar/>

            <div className="dashboard-content">
                <Outlet/>
            </div>
        </div>
    )
}

export default DashboardRoute