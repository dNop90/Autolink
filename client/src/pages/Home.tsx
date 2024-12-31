import React from 'react';
import * as Icon from 'react-bootstrap-icons';
import '../styles/Home.css'

const Home: React.FC = () => {
  return (
    <>
      <div className='vid'>
        <video className='vid-play' autoPlay muted loop>
          <source src='./video/BMW.mp4' type="video/mp4"/>
        </video>

        <div className='vid-context'>
          <h1>Welcome to Autolink</h1>
          <p>Explore vehicle inventories and interact with dealers</p>
        </div>
      </div>
      
      <div className='home-section'>
        <h1>How does it work?</h1>

        <div className='home-section-content'>
          <table>
            <tbody>
              <tr>
                <td>
                  <Icon.Person className='home-icon'/>
                  <span className='home-person-description'>Customer</span>
                </td>
                <td>
                  <Icon.PersonFill className='home-icon'/>
                  <span className='home-person-description'>Dealer</span>
                </td>
              </tr>
              <tr>
                <td>
                  Discover and explore the vehicle you wish to purchase using our advanced search filters to help you find your ideal option.
                </td>
                <td>
                  List your vehicle on our platform and allow potential buyers to contact you regarding the purchase.
                </td>
              </tr>
            </tbody>
          </table>
          
        </div>
      </div>
    </>
  );
};

export default Home;
