import React, { useState, useEffect } from "react";
import "../../styles/ViewInventory.css";

import { api } from "../../services/api";
import { Link } from 'react-router-dom';


const API_LINK = process.env.REACT_APP_API_VEHICLE;

const ViewInventory: React.FC = () => {
  const [vehicles, setVehicles] = useState([]); // State to store fetched vehicles
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState<string | null>(null); // Error state
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

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value,
    });
  };

  const handleConditionChange = (value: string) => {
    setFilters({
      ...filters,
      condition: value,
    });
  };

  const handleStockChange = (value: string) => {
    setFilters({
      ...filters,
      inStock: value,
    });
  };

  const filteredVehicles = vehicles.filter((vehicle: any) => {
    return (
      vehicle.price >= filters.priceRange[0] &&
      vehicle.price <= filters.priceRange[1] &&
      (filters.make
        ? vehicle.make.toLowerCase().includes(filters.make.toLowerCase())
        : true) &&
      (filters.model
        ? vehicle.model.toLowerCase().includes(filters.model.toLowerCase())
        : true) &&
      (filters.year ? vehicle.year.toString() === filters.year : true) &&
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
      {/* Hero Section */}
      <div
        className=" rounded-3 p-4 mb-5 text-center d-flex align-items-center justify-content-center"
        style={{
          backgroundImage:
            "url('https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?fit=crop&w=1600&q=80')",
          backgroundSize: "cover",
          backgroundPosition: "50% 70%",
          textShadow: "2px 2px 5px rgba(0, 0, 0, 0.7)",
          height: "300px",
        }}
      >
        <div>
          <h1 className="fw-bolder text-white">Your Dream Vehicle Awaits</h1>
          <p>Find the perfect car to match your lifestyle and budget.</p>
        </div>
      </div>

      <h1 className="my-4">Vehicle Listings</h1>

      {loading ? (
        <p>Loading vehicles...</p>
      ) : error ? (
        <p className="text-danger">{error}</p>
      ) : (
        <div className="row">
          <div className="col-md-3">
            <div className="mb-4">
              <input
                type="text"
                name="make"
                placeholder="Make"
                onChange={handleFilterChange}
                className="form-control mb-2"
              />
              <input
                type="text"
                name="model"
                placeholder="Model"
                onChange={handleFilterChange}
                className="form-control mb-2"
              />

              <div className="row mb-2">
                <div className="col">
                  <input
                    type="number"
                    name="minPrice"
                    placeholder="Min Price"
                    onChange={(e) =>
                      setFilters({
                        ...filters,
                        priceRange: [
                          e.target.value ? Number(e.target.value) : 0,
                          filters.priceRange[1],
                        ],
                      })
                    }
                    className="form-control"
                  />
                </div>
                <div className="col">
                  <input
                    type="number"
                    name="maxPrice"
                    placeholder="Max Price"
                    onChange={(e) =>
                      setFilters({
                        ...filters,
                        priceRange: [
                          filters.priceRange[0],
                          e.target.value ? Number(e.target.value) : 100000,
                        ],
                      })
                    }
                    className="form-control"
                  />
                </div>
              </div>

              <input
                type="text"
                name="year"
                placeholder="Year"
                onChange={handleFilterChange}
                className="form-control mb-2"
              />

              <div className="mb-2">
                <label htmlFor="condition" className="form-label">
                  Condition
                </label>
                <select
                  id="condition"
                  className="form-select"
                  onChange={(e) => handleConditionChange(e.target.value)}
                  value={filters.condition}
                >
                  <option value="">Select Condition</option>
                  <option value="New">New</option>
                  <option value="Used">Used</option>
                </select>
              </div>

              <div className="mb-2">
                <label htmlFor="inStock" className="form-label">
                  In Stock
                </label>
                <select
                  id="inStock"
                  className="form-select"
                  onChange={(e) => handleStockChange(e.target.value)}
                  value={filters.inStock}
                >
                  <option value="">Select Stock Status</option>
                  <option value="true">In Stock</option>
                  <option value="false">Out of Stock</option>
                </select>
              </div>
            </div>
          </div>

          <div className="col-md-9">
            <div className="row">
              {filteredVehicles.map((vehicle: any) => (
                <div className="col-md-4 mb-4" key={vehicle.vehicleId}>
                  <Link to={`/vehicle/${vehicle.vehicleId}`}>
                <div className="card bg-dark text-light">
                  <img
                    src="https://images.pexels.com/photos/35967/mini-cooper-auto-model-vehicle.jpg?cs=srgb&dl=pexels-pixabay-35967.jpg&fm=jpg"
                    className="card-img-top"
                    alt={vehicle.model}
                  />
                  <div className="card-body">
                  
                    <h5 className="card-title">
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
                </div>
                </Link>
              </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ViewInventory;
