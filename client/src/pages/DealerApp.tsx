import React, { FormEvent, useEffect, useState } from 'react'
import { Button, Form, Table } from 'react-bootstrap'
import { useCookie } from '../contexts/CookieContext';
import { useAuth } from '../contexts/AuthContext';
import { api } from '../services/api';
import { useNavigate } from 'react-router-dom';

function DealerApp() {
    const context = useAuth();
    const cookie = useCookie();
    const navigate = useNavigate();
    const [formInput, setFormInput] = useState({
        firstName: "",
        lastName: "",
        email: "",
        businessLicense: "",
        street: "",
        city: "",
        state: "",
        zipCode: ""
    });
    const [applications, setApplications] = useState<any[]>([]);
    useEffect(() => {
        listApplications();
    }, [])
    function listApplications() {
        if (context.user == null || context.user.userid == null) {
            navigate("/login");
            return;
        }
        api.application.listApplicantApplication(cookie.cookieData.token, context.user.userid).then((response) => {
            console.log(response);
            setApplications(response);
        }).catch((error) => {
            console.log(error);
        })
    }

    function handleSubmit(event: FormEvent) {
        event.preventDefault();
        if (context.user == null || context.user.userid == null) {
            navigate("/login");
            return;
        }
        let accountId = context.user.userid;
        let email = formInput.email;
        let firstName = formInput.firstName;
        let lastName = formInput.lastName;
        let businessLicense = formInput.businessLicense;
        let street = formInput.street;
        let city = formInput.city;
        let state = formInput.state;
        let zipCode =+ formInput.zipCode;
        api.application.submitApplication(cookie.cookieData.token, accountId, email,
            firstName, lastName, businessLicense,
            street, city, state, zipCode)
            .then((response) => {
                console.log(response);
                listApplications();
            }).catch((error) => {
                console.log(error);
            })
    }
    return (
        <>
            <Form className="loginform" onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>
                        First Name:
                        <Form.Control type='text' value={formInput.firstName} onChange={(e: any) => setFormInput({ ...formInput, firstName: e.target.value })} placeholder='First Name'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Last Name:
                        <Form.Control type="text" value={formInput.lastName} onChange={(e: any) => setFormInput({ ...formInput, lastName: e.target.value })} placeholder='Last Name'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Email:
                        <Form.Control type="text" value={formInput.email} onChange={(e: any) => setFormInput({ ...formInput, email: e.target.value })} placeholder='Email'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Business License:
                        <Form.Control type="text" value={formInput.businessLicense} onChange={(e: any) => setFormInput({ ...formInput, businessLicense: e.target.value })} placeholder='Business License'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Street:
                        <Form.Control type="text" value={formInput.street} onChange={(e: any) => setFormInput({ ...formInput, street: e.target.value })} placeholder='Street'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        City:
                        <Form.Control type="text" value={formInput.city} onChange={(e: any) => setFormInput({ ...formInput, city: e.target.value })} placeholder='City'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        State:
                        <Form.Control type="text" value={formInput.state} onChange={(e: any) => setFormInput({ ...formInput, state: e.target.value })} placeholder='State'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Zip Code:
                        <Form.Control type="text" value={formInput.zipCode} onChange={(e: any) => setFormInput({ ...formInput, zipCode: e.target.value })} placeholder='Zip Code'></Form.Control>
                    </Form.Label>
                </Form.Group>
                <Button type="submit">Submit</Button>
            </Form>
            <h4>Submitted Applications</h4>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ApplicationId</th>
                        <th>ApplicantId</th>
                        <th>Email</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Business License</th>
                        <th>Street Address</th>
                        <th>City</th>
                        <th>State</th>
                        <th>Zip Code</th>
                        <th>Status</th>
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
                                        <td>{application.status}</td>
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

export default DealerApp