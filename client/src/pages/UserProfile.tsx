import React, { useState, useEffect, FormEvent } from "react";
import { useCookie } from "../contexts/CookieContext";
import { useAuth } from "../contexts/AuthContext";
import { api } from "../services/api";
import { Button, Col, Form, Row } from "react-bootstrap";
import * as Icon from 'react-bootstrap-icons';

const UserProfile: React.FC = () => {
  const [profileFormInput, setProfileFormInput] = useState({
    email: "",
    firstName: "",
    lastName: "",
    phone: ""
  });
  const [type, setType] = useState("password");
  const [formInput, setFormInput] = useState({
    currentPassword: "",
    password: "",
    confirmPassword: ""
  });
  const [formError, setFormError] = useState({
    currentPassword: "",
    password: "",
    confirmPassword: "",
    errorMessage: "",
  });


  const cookie = useCookie();
  const token = cookie.cookieData.token;
  const context = useAuth();
  const username = context.user?.username;

  useEffect(() => {
    fetchProfile();
  }, [])

  // Fetch the user's profile information
  async function fetchProfile() {
    if (!username) return;
    let res = await api.user.fetchProfile(token, username)
    await setProfileFormInput({
      email: res?.email,
      firstName: res?.firstName,
      lastName: res?.lastName,
      phone: res?.phone
    })
    console.log(res);
  }

  // Handle the form submission to update profile data
  function handleProfileSubmit(e: FormEvent) {
    e.preventDefault();
    if (!username) return;
    api.user.updateProfile(token, username, profileFormInput.email, profileFormInput.firstName, profileFormInput.lastName, profileFormInput.phone).then((response) => {
      console.log(response);
      alert("Account Updated");
      fetchProfile();
    }).catch((error) => {
      console.log(error);
    })
  };

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
  // Handle the password change submission
  function handlePasswordSubmit(e: FormEvent) {
    e.preventDefault();
    let inputError = {
      currentPassword: "",
      password: "",
      confirmPassword: "",
      errorMessage: "",
    };

    if (!formInput.currentPassword) {
      setFormError({
        ...inputError,
        currentPassword: "Current password cannot be empty"
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
    if (formInput.password !== formInput.confirmPassword) {
      setFormError({
        ...inputError,
        confirmPassword: "Passwords do not match"
      });
      return;
    }
    setFormError(inputError);
    let currentPassword = formInput.currentPassword;
    let password = formInput.password;
    if (!username) return;
    api.user.updatePassword(token, username, currentPassword, password).then((response) => {
      console.log(response);
      alert("Password updated");
    }).catch((error) => {
      console.log(error);
    })
  };

  return (
    <>
      <Form className="loginform" onSubmit={handleProfileSubmit}>
        <Form.Group>
          <Form.Label>
            Email:
            <Form.Control autoComplete="new-username" type='text' value={profileFormInput.email} onChange={(e: any) => setProfileFormInput({ ...profileFormInput, email: e.target.value })} ></Form.Control>
          </Form.Label>
        </Form.Group>
        <Form.Group>
          <Form.Label>
            First Name:
            <Form.Control type="text" value={profileFormInput.firstName} onChange={(e: any) => setProfileFormInput({ ...profileFormInput, firstName: e.target.value })} placeholder='First name'></Form.Control>
          </Form.Label>
        </Form.Group>
        <Form.Group>
          <Form.Label>
            Last Name:
            <Form.Control type="text" value={profileFormInput.lastName} onChange={(e: any) => setProfileFormInput({ ...profileFormInput, lastName: e.target.value })} placeholder='Last name'></Form.Control>
          </Form.Label>
        </Form.Group>
        <Form.Group>
          <Form.Label>
            Phone:
            <Form.Control type="text" value={profileFormInput.phone} onChange={(e: any) => setProfileFormInput({ ...profileFormInput, phone: e.target.value })} placeholder='Phone'></Form.Control>
          </Form.Label>
        </Form.Group>
        <Button type="submit">Update</Button>
      </Form>

      <Form className="loginform" onSubmit={handlePasswordSubmit}>
        <Form.Group>
          <Form.Label>
            Current Password:
            <Form.Control autoComplete="new-password" type="password" value={formInput.currentPassword} onChange={(e: any) => setFormInput({ ...formInput, currentPassword: e.target.value })} placeholder='Password'></Form.Control>
            <Form.Text>{formError.currentPassword}</Form.Text>
          </Form.Label>
        </Form.Group>
        <Form.Group>
          <Form.Label>
            Password:
            <div>
              <Row>
                <Col className="new-password">
                  <Form.Control autoComplete="new-password" type={type} value={formInput.password} onChange={(e: any) => handlePassword(e.target.value)} placeholder='Password'></Form.Control>
                  {type === "password" ? (
                    <span className="new-password-eye"
                      onClick={() => setType("text")}>
                      <Icon.EyeSlash />
                    </span>
                  ) : (
                    <span className="new-password-eye"
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
            </div>
          </Form.Label>
        </Form.Group>
        <Form.Group>
          <Form.Label>
            Confirm Password:
            <Form.Control type={type} value={formInput.confirmPassword} onChange={(e: any) => setFormInput({ ...formInput, confirmPassword: e.target.value })} placeholder='Confirm Password'></Form.Control>
            <Form.Text>{formError.confirmPassword}</Form.Text>
          </Form.Label>
        </Form.Group>
        <Button type="submit">Submit</Button>
      </Form>
    </>
  );
};
export default UserProfile;
