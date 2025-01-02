import React, { FormEvent, useState } from 'react'
import { Button, Form, Table } from 'react-bootstrap';
import { api } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useCookie } from '../../contexts/CookieContext';

function AdminUserDashboard() {
    const context = useAuth();
    const cookie = useCookie();
    const [username, setUsername] = useState<string>("");
    const [account, setAccount] = useState<any>(null);

    function handleSubmit(event: FormEvent) {
        event.preventDefault();
        updateTable();
    }
    function updateTable() {
        api.user.searchUser(cookie.cookieData.token, username).then((response) => {
            console.log(response);
            setAccount(response);
        }).catch((error) => {
            console.log(error);
        })
    }
    function promote(username: string) {
        api.user.promoteUser(cookie.cookieData.token, username).then((response) => {
            console.log(response);
            updateTable();
        }).catch((error) => {
            console.log(error);
        })
    }
    function suspend(username: string) {
        api.user.suspendUser(cookie.cookieData.token, username, !account.isSuspended).then((response) => {
            console.log(response);
            updateTable();
        }).catch((error) => {
            console.log(error);
        })
    }
    return (
        <>
            <Form className="loginform" onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>
                        Username: <Form.Control autoComplete="new-username" type='text' value={username} onChange={(e: any) => setUsername(e.target.value)} placeholder='Username'></Form.Control> <Button type="submit">Search</Button>
                    </Form.Label>
                </Form.Group>
            </Form>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>AccountId</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>isSuspended</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        account &&
                        <tr key={account.accountId}>
                            <td>{account.accountId}</td>
                            <td>{account.username}</td>
                            <td>{account.role === 3 ? "Manager" : "User"}</td>
                            <td>{account.isSuspended ? "Yes" : "No"}</td>
                            <td>{account.role === 3 ? null : <Button variant='success' type="submit" onClick={() => promote(account.username)}> Promote </Button>
                            }{account.role === 3 ? null : <Button variant='success' type="submit" onClick={() => suspend(account.username)}> {
                                account.isSuspended ? "Activate" : "Suspend"} </Button>}</td>
                        </tr>
                    }
                </tbody>
            </Table>
        </>


    )
}

export default AdminUserDashboard