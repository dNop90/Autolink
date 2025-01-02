import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useCookie } from "../../contexts/CookieContext";

const API_LINK = process.env.REACT_APP_API_VEHICLE;

interface Vehicle {
  vehicleId: string;
  year: string;
  make: string;
  model: string;
  engineType: string;
  color: string;
  price: number;
  condition: "Used" | "New";
  imgUrl?: string | null;
}

const UpdateVehicle: React.FC = () => {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const navigate = useNavigate();
  const cookie = useCookie();

  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/${vehicleId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch vehicle details");
        }
        const data = await response.json();
        setVehicle(data);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicle();
  }, [vehicleId]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setVehicle((prev) => ({
      ...prev!,
      [name]: name === "price" ? Number(value) : value,
    }));
  };


  // const handleSubmit = async (e: React.FormEvent) => {
  //   e.preventDefault();
  //   try {
  //     const response = await fetch(`${API_LINK}/${vehicleId}`, {
  //       method: "PUT",
  //       headers: {
  //         "Content-Type": "application/json",
  //         "Authorization": cookie.cookieData.token,
  //       },
  //       body: JSON.stringify(vehicle),
  //     });

  //     if (!response.ok) {
  //       throw new Error("Failed to update vehicle");
  //     }

  //     alert("Vehicle updated successfully!");
  //     navigate("/");
  //   } catch (err: any) {
  //     setError(err.message);
  //   }
  // };
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      console.log("Submitting vehicle data:", vehicle); // Log the vehicle data being submitted
      const response = await fetch(`${API_LINK}/${vehicleId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": cookie.cookieData.token,
        },
        body: JSON.stringify(vehicle),
      });
  
      if (!response.ok) {
        throw new Error("Failed to update vehicle");
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
      <form onSubmit={handleSubmit}>
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
          <label htmlFor="imgUrl" className="form-label">Image Url</label>
          <input
            type="text"
            className="form-control"
            id="imgUrl"
            name="imgUrl"
            value={vehicle?.imgUrl || ""}
            onChange={handleInputChange}
          />
        </div>


        <button type="submit" className="btn btn-primary">Update Vehicle</button>
      </form>
    </div>
  );
};

export default UpdateVehicle;
