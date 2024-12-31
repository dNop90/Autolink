import React, { FormEvent, useEffect, useState } from 'react'
import { useAuth } from '../contexts/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Form } from 'react-bootstrap'
import '../styles/Login.css'
import { api } from '../services/api';
import { useCookie } from '../contexts/CookieContext';

function Login() {
    const [formInput, setFormInput] = useState({
        username: "",
        password: "",
    });
    const [formError, setFormError] = useState({
        username: "",
        password: "",
        errorMessage: "",
        linkToReg: false
    });
    const [account, setAccount] = useState<any | null>(null);
    const context = useAuth();
    const cookie = useCookie();

    if (!context) throw new Error("Login must be used with an AuthProvider");
    const navigate = useNavigate();
    let username = formInput.username;
    let password = formInput.password;
    useEffect(() => {
        if(account) {
            //console.log(account);
            let accountId = account.accountId;
            let role = account.role;
            let imgeUrl = account.imgeUrl;
            if (context) {
                context.login(accountId, username, role, imgeUrl);
                navigate('/');
            }
        }
    },[account] );

    function handleSubmit(event: FormEvent) {
        event.preventDefault();
        let inputError = {
            username: "",
            password: "",
            errorMessage: "",
            linkToReg: false
        };
        if (!formInput.username && !formInput.password) {
            setFormError({
                ...inputError,
                username: "Username cannot be empty",
                password: "Password cannot be empty"
            });
            return;
        }
        if (!formInput.username) {
            setFormError({
                ...inputError,
                username: "Username cannot be empty"
            });
            return;
        }
        if (!formInput.password) {
            setFormError({
                ...inputError,
                password: "Password cannot be empty"
            });
            return;
        }
        setFormError(inputError);
        api.user.loginUser(username, password).then((response) => {
                //console.log(response);
                cookie.setToken(response.token);
                setAccount(response);
            })
            .catch((error) => {
                console.log(error);
                console.log(error.response.data);
                let errorData = error.response.data;
                if (errorData === "No account with username") {
                    setFormError({
                        ...inputError,
                        errorMessage: `${errorData} ${username}. Click here to `,
                        linkToReg: true
                    });
                    return;
                }
                setFormError({
                    ...inputError,
                    errorMessage: "Invalid Login",
                    linkToReg: false
                });
                return;
            })
            
    }

    return (
        <>
            <Form className="loginform" onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>
                        Username:
                        <Form.Control autoComplete="new-username" type='text' value={formInput.username} onChange={(e: any) => setFormInput({ ...formInput, username: e.target.value })} placeholder='Username'></Form.Control>
                        <Form.Text>{formError.username}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Password:
                        <Form.Control autoComplete="new-password" type="password" value={formInput.password} onChange={(e: any) => setFormInput({ ...formInput, password: e.target.value })} placeholder='Password'></Form.Control>
                        <Form.Text>{formError.password}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Button type="submit">Login</Button>
                <Form.Group>
                    <Form.Text>{formError.errorMessage}
                        {
                            formError.linkToReg &&
                            <Link to="/register">Register</Link>
                        }
                    </Form.Text>
                </Form.Group>
            </Form>
        </>

    )
}

export default Login