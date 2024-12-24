import axios from "axios";
import { useState, FormEvent } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

const API_LINK = process.env.REACT_APP_API_USER;

function Register() {
    const [formInput, setFormInput] = useState({
        username: "",
        email: "",
        password: "",
        confirmPassword: ""
    });
    const [formError, setFormError] = useState({
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        errorMessage: "",
        toLoginLink: false
    });
    const [show, setShow] = useState(false);
    const context = useAuth()
    if (!context) throw new Error("Register must be used with an AuthProvider");
    const navigate = useNavigate();
    const handleClose = () =>  {
        setShow(false);
        navigate("/login");
    }
    function handleSubmit(event: FormEvent) {
        event.preventDefault();
        let inputError = {
            username: "",
            email: "",
            password: "",
            confirmPassword: "",
            errorMessage: "",
            toLoginLink: false
        };
        if (!formInput.username && !formInput.email && !formInput.password) {
            setFormError({
                ...inputError,
                username: "Username cannot be empty",
                email: "Email cannot be empty",
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
        if (!formInput.email) {
            setFormError({
                ...inputError,
                email: "Email cannot be empty",
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
        if (!formInput.password !== !formInput.confirmPassword) {
            setFormError({
                ...inputError,
                confirmPassword: "Passwords do not match"
            });
            return;
        }
        setFormError(inputError);
        let username = formInput.username;
        let email = formInput.email;
        let password = formInput.password;
        axios
            .post(`${API_LINK}/register`, {
                username,
                email,
                password
            }).then((response) => {
                console.log(response);
                setShow(true);
            }).catch((error) => {
                console.log(error.response.data);
                let errorData = error.response.data;
                if (errorData == "Username taken") {
                    setFormError({
                        ...inputError,
                        username: error.response.data
                    });
                }
                if (errorData == "Email already registered") {
                    setFormError({
                        ...inputError,
                        errorMessage: `${errorData}. Click here to `,
                        toLoginLink: true
                    });
                }
                
                return;
            })
    }

    return (
        <>
            <Form onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>
                        Username:
                        <Form.Control type='text' value={formInput.username} onChange={(e: any) => setFormInput({ ...formInput, username: e.target.value })} placeholder='Username'></Form.Control>
                        <Form.Text>{formError.username}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Email:
                        <Form.Control type='email' pattern="[^@\s]+@[^@\s]+\.[^@\s]+" value={formInput.email} onChange={(e: any) => setFormInput({ ...formInput, email: e.target.value })} placeholder='Email'></Form.Control>
                        <Form.Text>{formError.email}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Password:
                        <Form.Control autoComplete="new-password" type="password" value={formInput.password} onChange={(e: any) => setFormInput({ ...formInput, password: e.target.value })} placeholder='Password'></Form.Control>
                        <Form.Text>{formError.password}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Confirm Password:
                        <Form.Control type="password" value={formInput.confirmPassword} onChange={(e: any) => setFormInput({ ...formInput, confirmPassword: e.target.value })} placeholder='Confirm Password'></Form.Control>
                        <Form.Text>{formError.confirmPassword}</Form.Text>
                    </Form.Label>
                </Form.Group>
                <Button type="submit">Register</Button>
                <Form.Group>
                    <Form.Text>{formError.errorMessage}
                        {
                            formError.toLoginLink &&
                            <Link to="/login">Login</Link>
                        }
                    </Form.Text>
                </Form.Group>
            </Form>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Success</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Successfully Registered</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={handleClose}>Close</Button>
                </Modal.Footer>
            </Modal>
        </>

    )
}

export default Register