import React, { FormEvent, useState } from 'react'
import { Button, Form } from 'react-bootstrap';

function AdminUserDashboard() {
    const [username, setUsername] = useState<string>("");
    function handleSubmit(e: FormEvent) {
        
    }

    return (
        <Form className="loginform" onSubmit={handleSubmit}>
            <Form.Group>
                <Form.Label>
                    Username: <Form.Control autoComplete="new-username" type='text' value={username} onChange={(e: any) => setUsername( e.target.value )} placeholder='Username'></Form.Control> <Button type="submit">Search</Button>
                </Form.Label>
            </Form.Group>
        </Form>
        
    )
}

export default AdminUserDashboard