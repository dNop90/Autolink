import React, { useState, useEffect } from "react";
import { authenticate, fetchData } from "../../services/apiService";
import "../../styles/AddVehicle.css";

const AddVehicle: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    year: "",
    make: "",
    model: "",
    engineType: "",
    color: "",
    price: 0, // Numeric input
    inStock: false, // Boolean input
    condition: "Used", // Dropdown for "Used" or "New"
  });

  type Model = {
    id: number;
    name: string;
    make_id: number;
  };

  type EngineType = {

    drive_type: string;
    engine_type: string;
    fuel_type: string;
    horsepower_hp: number;
    id: number;
    transmission: string;
  };

  type Color = {
    id: number,
    name: string,
    rgb: string;
  }
  const [years, setYears] = useState<string[]>([]);
  const [makes, setMakes] = useState<string[]>([]);
  //const [models, setModels] = useState<string[]>([]);
  const [engine, setEngine] = useState<EngineType[]>([]);
  const [models, setModels] = useState<Model[]>([]);
  const [colors, setColor] = useState<Color[]>([]);



  useEffect(() => {
    // Authenticate on component mount
    const authenticateUser = async () => {
      const jwt = await authenticate();
      if (jwt) {
        setIsAuthenticated(true);
      }
    };

    authenticateUser();
  }, []);

  useEffect(() => {
    // Fetch years after authentication
    const loadYears = async () => {
      if (isAuthenticated) {
        const data = await fetchData("years");
        if (Array.isArray(data)) {
          setYears(data);
        } else {
          console.error("Invalid response structure for years:", data);
        }
      }
    };

    loadYears();
  }, [isAuthenticated]);

  const handleYearChange = async (year: string) => {
    setFormData({ ...formData, year });


    try {
      const response = await fetchData(`makes`);


      if (response && Array.isArray(response.data)) {
        // Map over the data array to extract make names
        setMakes(response.data.map((make: { id: number; name: string }) => make.name));
        setModels([]); // Reset models when year changes
        setEngine([]); // Reset trims when year changes
      } else {
        console.error("Invalid response structure for makes:", response);
      }
    } catch (error) {
      console.error("Error fetching makes:", error);
    }
  };


  const handleMakeChange = async (make: string) => {
    setFormData({ ...formData, make });

    try {
      const response = await fetchData(`models?year=${formData.year}&make=${make}`);
      if (response && Array.isArray(response.data)) {
        // Store objects with id and name to ensure uniqueness
        setModels(response.data.map((model: { id: number; name: string; make_id: number; }) => model));
        setEngine([]); // Reset trims when make changes
      } else {
        console.error("Invalid response structure for models:", response);
      }
    } catch (error) {
      console.error("Error fetching models:", error);
    }
  };



  const handleModelChange = async (model: string) => {
    setFormData({ ...formData, model });

    try {
      const data = await fetchData(`engines?year=${formData.year}&make=${formData.make}&model=${formData.model}`);
      if (Array.isArray(data.data)) {
        setEngine(data.data);
      } else {
        console.error("Invalid response structure for trims:", data);
      }
      // Fetch exterior colors
      const colorResponse = await fetchData(
        `exterior-colors?year=${formData.year}&make=${formData.make}&model=${model}`
      );

      if (Array.isArray(colorResponse.data)) {
        setColor(colorResponse.data);
      } else {
        console.error("Invalid response structure for colors:", colorResponse);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  // const handleSubmit = (e: React.FormEvent) => {
  //   e.preventDefault();
  //   console.log("Form submitted:", formData);
  // };
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Form to be submitted: ", formData)
    try {
      const response = await fetch("http://localhost:8080/api/vehicles", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData), // Send the formData object
      });

      if (response.ok) {
        const savedVehicle = await response.json();
        console.log("Vehicle saved successfully:", savedVehicle);
        alert("Vehicle added successfully!");
      } else {
        console.error("Failed to save vehicle:", response.statusText);
        alert("Failed to add vehicle. Please try again.");
      }
    } catch (error) {
      console.error("Error saving vehicle:", error);
      alert("An error occurred. Please try again.");
    }
  };


  return (
    <div className="add-vehicle-container">
      <h1>Add Vehicle</h1>
      {!isAuthenticated ? (
        <p>Authenticating...</p>
      ) : (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="year">Year</label>
            <select
              id="year"
              value={formData.year}
              onChange={(e) => handleYearChange(e.target.value)}
              required
            >
              <option value="">Select Year</option>
              {years.map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="make">Make</label>
            <select
              id="make"
              value={formData.make}
              onChange={(e) => handleMakeChange(e.target.value)}
              required
            >
              <option value="">Select Make</option>
              {makes.map((make) => (
                <option key={make} value={make}>
                  {make}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="model">Model</label>
            <select
              id="model"
              value={formData.model}
              onChange={(e) => handleModelChange(e.target.value)}
              required
            >
              <option value="">Select Model</option>
              {models.map((model: { id: number; name: string; make_id: number; }) => (
                <option key={model.id} value={model.name}>
                  {model.name}
                </option>
              ))}
            </select>
          </div>


          <div className="form-group">
            <label htmlFor="engine">Engine Type</label>
            <select
              id="engine"
              value={formData.engineType}
              onChange={(e) => setFormData({ ...formData, engineType: e.target.value })}
              required
            >
              <option value="">Select Engine Type</option>
              {Array.from(new Set(engine.map((e) => e.engine_type))).map((type, index) => (
                <option key={index} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="color">Color</label>
            <select
              id="color"
              value={formData.color}
              onChange={(e) => setFormData({ ...formData, color: e.target.value })}
              required
            >
              <option value="">Select color</option>
              {Array.from(new Set(colors.map((e) => e.name))).map((type, index) => (
                <option key={index} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="price">Price</label>
            <input type="number" className="form-control" id="price" placeholder="Enter Price"
              value={formData.price} onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })} />
          </div>
          <div className="form-group">
            <label htmlFor="inStock">In Stock</label>
            <input
              type="checkbox"
              id="inStock"
              checked={formData.inStock}
              onChange={(e) => setFormData({ ...formData, inStock: e.target.checked })}
            />
          </div>
          <div className="form-group">
            <label htmlFor="condition">Condition</label>
            <select
              id="condition"
              value={formData.condition}
              onChange={(e) => setFormData({ ...formData, condition: e.target.value })}
              required
            >
              <option value="Used">Used</option>
              <option value="New">New</option>
            </select>
          </div>




          <button type="submit" className="btn btn-primary">
            Add Vehicle
          </button>
        </form>
      )}
    </div>
  );
};

export default AddVehicle;
