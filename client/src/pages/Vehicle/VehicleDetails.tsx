import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from '../../contexts/AuthContext';
import { messageManager } from "../../services/MessageManager";

function VehicleDetail(props: { dLer: boolean }) {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const [vehicle, setVehicle] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reviews, setReviews] = useState<any[]>([]); // Initialize as an empty array
  const [newReview, setNewReview] = useState<string>("");

  const API_LINK = process.env.REACT_APP_API_VEHICLE;

  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_LINK}/${vehicleId}`);
        console.log("response is this: ", response)
        if (!response.ok) {
          throw new Error("Failed to fetch vehicle details");
        }
        
        const data = await response.json();
        setVehicle(data);
        
        // Fetch reviews for the vehicle
        const reviewsResponse = await fetch(`${API_LINK}/${vehicleId}/reviews`);
        if (!reviewsResponse.ok) {
          throw new Error("Failed to fetch reviews");
        }
        const reviewsData = await reviewsResponse.json();
        setReviews(reviewsData);
        
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicle();
  }, [vehicleId]);

  const handleAddReview = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newReview.trim()) return; // Prevent empty reviews

    try {
      const response = await fetch(`${API_LINK}/${vehicleId}/reviews`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ comment: newReview }),
      });

      if (!response.ok) {
        throw new Error("Failed to add review");
      }

      const addedReview = await response.json();
      setReviews((prevReviews) => [...prevReviews, addedReview]); // Update reviews state
      setNewReview(""); // Clear the input
    } catch (err: any) {
      setError(err.message);
    }
  };
  console.log("THis is review: ", reviews)

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div style={{
      padding: "35px", maxWidth: "800px", margin: "auto", marginTop: "60px", marginBottom: "60px", position: "relative", boxShadow: "var(--color-text-link)",
      background: "var(--color-background)", lineHeight: "1", fontFamily: "Open Sans"
    }}>
      <h1 className="text-center mb-4" style={{textTransform: "uppercase"}}>{vehicle.make} {vehicle.model}</h1>

      <img
        src={vehicle.imgUrl && vehicle.imgUrl.trim() !== "" ? vehicle.imgUrl : "/AutoLinkNoImage.png"}
        className="card-img-top"
        alt={vehicle.model || "No Image Available"}
      />

      <div className="mt-4">
        <h3 style={{textTransform: "uppercase", marginBottom: "20px", fontWeight: 300}}>Details</h3>
        <p style={{fontWeight: 300}}><strong>Price:</strong> ${vehicle.price.toLocaleString()}</p>
        <p style={{fontWeight: 300}}><strong>Year:</strong> {vehicle.year}</p>
        <p style={{fontWeight: 300}}><strong>Condition:</strong> {vehicle.condition}</p>
        <p style={{fontWeight: 300}}><strong>Description :</strong> {vehicle.description}</p>
      </div>

      <div className="mt-4">
        <h3 style={{textTransform: "uppercase", marginBottom: "20px", fontWeight: 300}}>Reviews</h3>
        {reviews.length > 0 ? (
          reviews.map((review) => (
            <div key={review.id} className="review">
              
              <p>{review.comment}</p>
            </div>
          ))
        ) : (
          <p>No reviews available.</p>
        )}
      </div>

      <form onSubmit={handleAddReview} className="mt-4">
        <textarea
          value={newReview}
          onChange={(e) => setNewReview(e.target.value)}
          placeholder="Write a review..."
          rows={4}
          className="form-control"
        />
        <button type="submit" className="btn btn-primary mt-2">Add Review</button>
      </form>
    </div>
  );
}

export default VehicleDetail;