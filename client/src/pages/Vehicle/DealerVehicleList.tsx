import React, { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useCookie } from "../../contexts/CookieContext";

const API_LINK = process.env.REACT_APP_API_VEHICLE;

interface Vehicle {
  vehicleId: string; // Ensure vehicleId is included in the interface
  year: string; // Change to string if you want to allow empty strings
  make: string;
  model: string;
  engineType: string;
  color: string;
  price: number; // Numeric input
  inStock: boolean; // Boolean input
  condition: "Used" | "New"; // Dropdown for "Used" or "New"
}

const DealerVehicleList: React.FC = () => {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]); // State to store fetched vehicles
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState<string | null>(null); // Error state
  const cookie = useCookie();
  const navigate = useNavigate();

  const [filters, setFilters] = useState({
    priceRange: [0, 100000],
    make: "",
    model: "",
    year: "",
    condition: "",
    inStock: "", // State for inStock filter
  });

  // Fetch vehicles from the backend
  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/inventory`);
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        setVehicles(data); // Update state with fetched vehicles
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, []);


  const handleUpdate = (vehicleId: string) => {
    navigate(`/dashboard/vehiclelist/update-vehicle/${vehicleId}`);
  };

  
  const handleDelete = async (vehicleId: string) => {
    if (window.confirm("Are you sure you want to delete this vehicle?")) {
      try {
        const response = await fetch(`${API_LINK}/${vehicleId}`, {
          method: 'DELETE',
          headers: {
            "Content-Type": "application/json",
            "Authorization": cookie.cookieData.token
          },
        });

        if (!response.ok) {
          throw new Error("Failed to delete vehicle");
        }

        // Remove the deleted vehicle from the state
        setVehicles((prevVehicles) => 
          prevVehicles.filter(vehicle => vehicle.vehicleId !== vehicleId) // Correctly filter by vehicleId
        );
      } catch (err: any) {
        setError(err.message);
      }
    }
  };

  const filteredVehicles = vehicles.filter((vehicle: Vehicle) => {
    return (
      vehicle.price >= filters.priceRange[0] &&
      vehicle.price <= filters.priceRange[1] &&
      (filters.make
        ? vehicle.make.toLowerCase().includes(filters.make.toLowerCase())
        : true) &&
      (filters.model
        ? vehicle.model.toLowerCase().includes(filters.model.toLowerCase())
        : true) &&
      (filters.year ? vehicle.year === filters.year : true) &&
      (filters.condition
        ? vehicle.condition.toLowerCase() === filters.condition.toLowerCase()
        : true) &&
      (filters.inStock
        ? filters.inStock === "true"
          ? vehicle.inStock === true
          : vehicle.inStock === false
        : true)
    );
  });

  return (
    <div style={{ padding: "35px" }}>
      {loading ? (
        <p>Loading vehicles...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : (
        <div className="row">
          <div className="col-md-12">
            <div className="row">
              {filteredVehicles.map((vehicle: Vehicle) => (
                <div className="col-md-4 mb-4" key={vehicle.vehicleId}>
                  <div className="card bg-dark text-light">
                    <Link to={`/vehicle/${vehicle.vehicleId}`}>
                      <img
                        src="https://images.pexels.com/photos/35967/mini-cooper-auto-model-vehicle.jpg?cs=srgb &dl=pexels-pixabay-35967.jpg&fm=jpg"
                        className="card-img-top"
                        alt={vehicle.model}
                      />
                      <div className="card-body">
                        <h5 className="card-title text-light">
                          {vehicle.make} {vehicle.model}
                        </h5>
                        <p className="card-text">Price: ${vehicle.price}</p>
                        <p className="card-text">Year: {vehicle.year}</p>
                        <p className="card-text">
                          Condition: {vehicle.condition}
                        </p>
                        <p className="card-text">
                          In Stock: {vehicle.inStock ? "Yes" : "No"}
                        </p>
                      </div>
                    </Link>
                    <button className="btn btn-warning ms-2 me-2" onClick={() => handleUpdate(vehicle.vehicleId)}>Update</button>
                    <button className="btn btn-danger ms-2 m-2" onClick={() => handleDelete(vehicle.vehicleId)}>Delete</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DealerVehicleList;