import axios from "axios";
import { useState, FormEvent } from "react";
import { Button, Form, Modal, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import * as Icon from 'react-bootstrap-icons';
import '../styles/Login.css'

const API_LINK = process.env.REACT_APP_API_USER;

function Register() {
    const [type, setType] = useState("password");
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
    const handleClose = () => {
        setShow(false);
        navigate("/login");
    }
    const [lowerVal, setLowerVal] = useState(false);
    const [upperVal, setUpperVal] = useState(false);
    const [numberVal, setNumberVal] = useState(false);
    const [specialVal, setSpecialVal] = useState(false);
    const [lengthVal, setLengthVal] = useState(false);

    function handlePassword(value: string) {
        const lower = new RegExp('(?=.*[a-z])');
        const upper = new RegExp('(?=.*[A-Z])');
        const number = new RegExp('(?=.*[0-9])');
        const special = new RegExp('(?=.*[!@#\$%\^\&\*])');
        const length = new RegExp('(?=.{8,})');
        if (lower.test(value)) setLowerVal(true);
        else setLowerVal(false);
        if (upper.test(value)) setUpperVal(true);
        else setUpperVal(false);
        if (number.test(value)) setNumberVal(true);
        else setNumberVal(false);
        if (special.test(value)) setSpecialVal(true);
        else setSpecialVal(false);
        if (length.test(value)) setLengthVal(true);
        else setLengthVal(false);
        setFormInput({ ...formInput, password: value });
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
                password: "Password does not meet the requirements"
            });
            return;
        }
        if (!formInput.username) {
            setFormError({
                ...inputError,
                username: "Username cannot be empty"
            });
            if (!lowerVal || !upperVal || !numberVal || !specialVal || !lengthVal) {
                setFormError({
                    ...inputError,
                    password: "Password does not meet the requirements"
                });
            };
            return;
        }
        if (!formInput.email) {
            setFormError({
                ...inputError,
                email: "Email cannot be empty",
            });
            return;
        }
        if (!formInput.password || !lowerVal || !upperVal || !numberVal || !specialVal || !lengthVal) {
            setFormError({
                ...inputError,
                password: "Password does not meet the requirements"
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
            <Form className="loginform" onSubmit={handleSubmit}>
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
                        <Container>
                            <Row>
                                <Col>
                                    <Form.Control autoComplete="new-password" type={type} value={formInput.password} onChange={(e: any) => handlePassword(e.target.value)} placeholder='Password'></Form.Control>
                                </Col>
                                <Col>
                                    {type === "password" ? (
                                        <span
                                            onClick={() => setType("text")}>
                                            <Icon.EyeSlash />
                                        </span>
                                    ) : (
                                        <span
                                            onClick={() => setType("password")}>
                                            <Icon.Eye />
                                        </span>
                                    )}
                                </Col>
                            </Row>
                            <Row>
                                <Row>
                                    <Col>
                                        {
                                            lowerVal ? (
                                                <Icon.CheckCircle />
                                            ) : (
                                                <Icon.XCircle />
                                            )
                                        }
                                    </Col>
                                    <Col>
                                        At least one lowercase letter
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        {
                                            upperVal ? (
                                                <Icon.CheckCircle />
                                            ) : (
                                                <Icon.XCircle />
                                            )
                                        }
                                    </Col>
                                    <Col>
                                        At least one uppercase letter
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        {
                                            numberVal ? (
                                                <Icon.CheckCircle />
                                            ) : (
                                                <Icon.XCircle />
                                            )
                                        }
                                    </Col>
                                    <Col>
                                        At least one number
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        {
                                            specialVal ? (
                                                <Icon.CheckCircle />
                                            ) : (
                                                <Icon.XCircle />
                                            )
                                        }
                                    </Col>
                                    <Col>
                                        At least one special character
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        {
                                            lengthVal ? (
                                                <Icon.CheckCircle />
                                            ) : (
                                                <Icon.XCircle />
                                            )
                                        }
                                    </Col>
                                    <Col>
                                        At least 8 characters
                                    </Col>
                                </Row>
                            </Row>
                            <Row>
                                <Form.Text>{formError.password}</Form.Text>
                            </Row>
                        </Container>
                    </Form.Label>
                </Form.Group>
                <Form.Group>
                    <Form.Label>
                        Confirm Password:
                        <Form.Control type={type} value={formInput.confirmPassword} onChange={(e: any) => setFormInput({ ...formInput, confirmPassword: e.target.value })} placeholder='Confirm Password'></Form.Control>
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