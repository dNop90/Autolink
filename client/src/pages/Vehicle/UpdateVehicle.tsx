import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useCookie } from "../../contexts/CookieContext";
import { useAuth } from "../../contexts/AuthContext";
import { Account, Vehicle } from "../../services/EntitiesEnterface";

const API_LINK = process.env.REACT_APP_API_VEHICLE;



const UpdateVehicle: React.FC = () => {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const navigate = useNavigate();
  const cookie = useCookie();
  const authContext = useAuth();
  const currentUser = authContext.user;

  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [users, setUsers] = useState<Account[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        setLoading(true);

        const vehicleResponse = await fetch(`${API_LINK}/${vehicleId}`);
        const userResponse = await fetch(`${process.env.REACT_APP_API_USER}/all`);

        if (!vehicleResponse.ok || !userResponse.ok) {
          throw new Error("Failed to fetch data");
        }

        const vehicleData = await vehicleResponse.json();
        const userData = await userResponse.json();

        setUsers(userData);
        setVehicle({ ...vehicleData, dealer: currentUser }); // Add current user as dealer
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicle();
  }, [vehicleId, currentUser]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    if (name === "buyerId") {
      // Find the selected user by accountId
      const selectedUser = users.find(user => user.accountId === Number(value));

      // Update the vehicle state with the selected buyer
      setVehicle((prev) => ({
        ...prev!,
        buyer: selectedUser || null, // Set buyer to selected account or null
        inStock: selectedUser ? false : true,
      }));
    } else {
      setVehicle((prev) => ({
        ...prev!,
        [name]: name === "price" ? Number(value) : value,
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (!vehicle) {
        setError("Vehicle data is missing");
        return;
      }

      console.log("Submitting vehicle data:", vehicle); // Log the vehicle object being sent

      const response = await fetch(`${API_LINK}/${vehicleId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": cookie.cookieData.token,
        },
        body: JSON.stringify(vehicle), // Submit the vehicle object
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to update vehicle");
      }

      alert("Vehicle updated successfully!");
      navigate("/");
    } catch (err: any) {
      setError(err.message);
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div>
      <h1>Update Vehicle</h1>
      <form className="loginform" onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="make" className="form-label">Make</label>
          <input
            type="text"
            className="form-control"
            id="make"
            name="make"
            value={vehicle?.make || ""}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="model" className="form-label">Model</label>
          <input
            type="text"
            className="form-control"
            id="model"
            name="model"
            value={vehicle?.model || ""}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="year" className="form-label">Year</label>
          <input
            type="text"
            className="form-control"
            id="year"
            name="year"
            value={vehicle?.year || ""}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="price" className="form-label">Price</label>
          <input
            type="number"
            className="form-control"
            id="price"
            name="price"
            value={vehicle?.price || 0}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="condition" className="form-label">Condition</label>
          <select
            className="form-select"
            id="condition"
            name="condition"
            value={vehicle?.condition || ""}
            onChange={handleInputChange}
          >
            <option value="New">New</option>
            <option value="Used">Used</option>
          </select>
        </div>

        <div className="mb-3">
          <label htmlFor="imgUrl" className="form-label">Image URL</label>
          <input
            type="text"
            className="form-control"
            id="imgUrl"
            name="imgUrl"
            value={vehicle?.imgUrl || ""}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="buyerId" className="form-label">Buyer</label>
          <select
            className="form-select"
            id="buyerId"
            name="buyerId"
            value={vehicle?.buyer?.accountId || ""}
            onChange={handleInputChange}
          >
            <option value="">Select a buyer</option>
            {users.map((user) => (
              <option key={user.accountId} value={user.accountId}>
                {user.username}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn btn-primary">Update Vehicle</button>
      </form>
    </div>
  );
};

export default UpdateVehicle;
