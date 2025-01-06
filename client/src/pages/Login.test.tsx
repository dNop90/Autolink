import axios from "axios";
import { fireEvent, render, screen } from '@testing-library/react';
import {api} from "../services/api";
import Login from "./Login";

jest.mock("../services/api")

test('Test Login Success', async () => {
    const mockData = {accountId: 6, username: "test", role: 3, isSuspended: false, token: "random"};
    (api.user.loginUser as jest.Mock).mockResolvedValue(mockData);

    const navigate = jest.fn();

    render(<Login/>);
    fireEvent.click(screen.getByText('Login'));

    expect(navigate).toHaveBeenCalledWith('/');
});