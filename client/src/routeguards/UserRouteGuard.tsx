import React from 'react'
import { useAuth } from '../contexts/AuthContext'
import Navbar from '../components/navbar/Navbar';
import { Navigate, Outlet } from 'react-router-dom';

function UserRouteGuard() {
    const authContext = useAuth();
    const user = authContext.user;

    if(!user)
    {
        return <Navigate to="/" replace/>;
    }

    return (
        <>
            <Outlet/>
        </>
    )
}

export default UserRouteGuard