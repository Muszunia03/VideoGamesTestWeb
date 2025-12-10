import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NotificationPopup from './NotificationPopup'; 
import './LoginForm.css';

function LoginForm({ onLoginSuccess }) { 
  const [login, setLogin] = useState(''); 
  const [password, setPassword] = useState('');
  const [showPopup, setShowPopup] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');
  const [popupType, setPopupType] = useState('');
  const [redirectAfterClose, setRedirectAfterClose] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login, password }),
      });

      const data = await response.json();

      if (data.status === 'success') {
        setPopupMessage('✅ You have successfully logged in!');
        setPopupType('success');
        setShowPopup(true);
        setRedirectAfterClose(true);

        if (onLoginSuccess) {
          onLoginSuccess(login);
        }
      } else {
        setPopupMessage(data.message || '❌ Incorrect login credentials.');
        setPopupType('error');
        setShowPopup(true);
      }
    } catch (error) {
      setPopupMessage('⚠️ Network error. Please try again later.');
      setPopupType('error');
      setShowPopup(true);
    }
  };

  const handleClosePopup = () => {
    setShowPopup(false);
    if (redirectAfterClose) navigate('/quiz');
  };

  return (
    <div className="form-page-wrapper">
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Log In</h2>

        <div className="input-group">
          <input
            type="text"
            placeholder="Email or username"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
        </div>

        <div className="input-group">
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className="button-group">
          <button type="submit" className="primary-btn">Log In</button>
        </div>
      </form>

      {showPopup && (
        <NotificationPopup
          message={popupMessage}
          type={popupType}
          onClose={handleClosePopup}
        />
      )}
    </div>
  );
}

export default LoginForm;