import React, { useEffect, useState } from 'react'
import { useAuth } from '../../contexts/AuthContext';
import { useCookie } from '../../contexts/CookieContext';
import { Button, Table } from 'react-bootstrap';
import { api } from '../../services/api';
import { error } from 'console';

function AdminDealerDashboard() {
  const context = useAuth();
  const cookie = useCookie();
  const [applications, setApplications] = useState<any[]>([]);
  useEffect(() => {
    api.application.listPendingApplications(cookie.cookieData.token).then((response) => {
      console.log(response);
      setApplications(response);
    }).catch((error) => {
      console.log(error);
    })
  },[])

  function resolve(i: number, applicationId: number, status: string) {
    api.application.updateApplication(cookie.cookieData.token, applicationId, status).then((response) => {
      console.log(response);
      removeApplication(i);
    }).catch((error) => {
      console.log(error);
    })
    
  }
  function removeApplication(indexToDelete: number) {
    const updatedApplications = [...applications.slice(0, indexToDelete), ...applications.slice(indexToDelete + 1)];
    setApplications(updatedApplications);
  }
  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ApplicationId</th>
            <th>ApplicantId</th>
            <th>Email</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Street Address</th>
            <th>City</th>
            <th>State</th>
            <th>Zip Code</th>
          </tr>
        </thead>
        <tbody>
          {
            applications ? applications.map(
              (application, i) => {
                return (
                  <tr key={i}>
                    <td>{application.applicationId}</td>
                    <td>{application.applicantId}</td>
                    <td>{application.applicantEmail}</td>
                    <td>{application.applicantFirstName}</td>
                    <td>{application.applicantLastName}</td>
                    <td>{application.businessLicense}</td>
                    <td>{application.street}</td>
                    <td>{application.city}</td>
                    <td>{application.state}</td>
                    <td>{application.zipCode}</td>
                    <td>{<Button variant='success' type="submit" onClick={() => resolve(i, application.applicationId, "Approved")}> Approve </Button>}{<Button variant='success' type="submit" onClick={() => resolve(i, application.applicationId, "Denied")}> Deny </Button>}</td>
                  </tr>
                )
              }
            ) : null

          }
        </tbody>
      </Table>
    </>
  )
}

export default AdminDealerDashboard