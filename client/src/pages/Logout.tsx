import React, { useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext';
import { Navigate } from 'react-router-dom';

function Logout() {
  const context = useAuth();

  useEffect(() => {
    context.logout();
  }, []);
    
  return (
    <Navigate to="/" replace />
  )
}

export default Logout