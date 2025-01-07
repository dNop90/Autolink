import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { messageManager } from "../../services/MessageManager";
import '../../styles/VehicleDetails.css'; // Import the CSS file
import { Account, Vehicle } from "../../services/EntitiesEnterface";
import { useAuth } from '../../contexts/AuthContext';
import { useCookie } from "../../contexts/CookieContext";


function VehicleDetail(props: { dLer: boolean }) {
  const { vehicleId } = useParams<{ vehicleId: string }>();
  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reviews, setReviews] = useState<any[]>([]); // Initialize as an empty array
  const [newReview, setNewReview] = useState<string>("");
  const cookie = useCookie();

  const API_LINK = process.env.REACT_APP_API_VEHICLE;

      const authContext = useAuth();
      const user = authContext.user;

  
  interface Review {
    reviewId: number;
    comment: string;
    vehicle: Vehicle;
    reviewer: Account;
  }

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
        
        // Fetch reviews for the vehicle
        const reviewsResponse = await fetch(`${API_LINK}/${vehicleId}/reviews`);
        if (!reviewsResponse.ok) {
          throw new Error("Failed to fetch reviews");
        }
        const reviewsData: Review[] = await reviewsResponse.json();
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
    console.log("This is review: ", )
    if (!newReview.trim()) return; // Prevent empty reviews

    try {
      const response = await fetch(`${API_LINK}/${vehicleId}/reviews`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": cookie.cookieData.token,
        },
        body: JSON.stringify({
          comment: newReview,
          vehicle: { vehicleId }, // Include the vehicle ID
          reviewer: { user } // Replace with actual current user ID
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to add review");
      }

      const addedReview: Review = await response.json();
      setReviews((prevReviews) => [...prevReviews, addedReview]); // Update reviews state
      setNewReview(""); // Clear the input
    } catch (err: any) {
      setError(err.message);
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div className="vehicle-detail-container">
      <h1 className="vehicle-title">{vehicle?.make} {vehicle?.model}</h1>

      <img
        src={vehicle?.imgUrl && vehicle.imgUrl.trim() !== "" ? vehicle.imgUrl : "/AutoLinkNoImage.png"}
        className="vehicle-image"
        alt={vehicle?.model || "No Image Available"}
      />

      <div className="details-section">
        <h3>Details</h3>
        <p><strong>Price:</strong> ${vehicle?.price.toLocaleString()}</p>
        <p><strong>Year:</strong> {vehicle?.year}</p>
        <p><strong>Condition:</strong> {vehicle?.condition}</p>
        <p><strong>Description:</strong> {vehicle?.description}</p>
        
        {vehicle?.dealer && (
          <div className="dealer-info-container">
            <div className="dealer-info">
              <h4>Dealer Information</h4>
              <p><strong> Username:</strong> {vehicle.dealer.username}</p>
            </div>
            {props.dLer && (
              <button
                className="chat-button"
                onClick={(e) => {
                  let dealerid = e.currentTarget.getAttribute("vehicle-dealerid");
                  let dealerusername = e.currentTarget.getAttribute("vehicle-dealerusername");

                  if (dealerid && dealerusername)
                    messageManager.createMessageChat(parseInt(dealerid), dealerusername);
                }}
                vehicle-dealerid={vehicle.dealer ? vehicle.dealer.accountId : -1}
                vehicle-dealerusername={vehicle.dealer ? vehicle.dealer.username : null}
              >
                💬
              </button>
            )}
          </div>
        )}
      </div>

      <div className="reviews-section">
        <h3>Reviews</h3>
        {reviews.length > 0 ? (
          reviews.map((review) => (
            <div key={review.reviewId} className="review">
              <p><strong>{review.reviewer.username}</strong> : {review.comment}</p>
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