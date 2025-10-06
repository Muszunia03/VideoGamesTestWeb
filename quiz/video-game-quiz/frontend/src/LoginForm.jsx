import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NotificationPopup from './NotificationPopup'; 
import './LoginForm.css';

function LoginForm({ onLoginSuccess }) { 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPopup, setShowPopup] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');
  const [popupType, setPopupType] = useState('');
  const [redirectAfterClose, setRedirectAfterClose] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8090/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json(); 
        const loggedInUsername = data.username || username; 
        
        setPopupMessage('Zalogowano pomyślnie!');
        setPopupType('success');
        setShowPopup(true);
        setRedirectAfterClose(true); 

        if (onLoginSuccess) {
          onLoginSuccess(loggedInUsername); 
        }

      } else {
        const data = await response.json();
        setPopupMessage(data.detail || 'Błąd logowania. Spróbuj ponownie.');
        setPopupType('error');
        setShowPopup(true);
        setRedirectAfterClose(false);
      }
    } catch (error) {
      setPopupMessage('Błąd sieci lub serwer jest offline. Spróbuj ponownie później.');
      setPopupType('error');
      setShowPopup(true);
      setRedirectAfterClose(false);
    }
  };

  const handleClosePopup = () => {
    setShowPopup(false);
    if (redirectAfterClose) {
      navigate('/quiz');
    }
    setPopupMessage('');
    setPopupType('');
    setRedirectAfterClose(false);
  };

  return (
    <div className="form-page-wrapper">
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Zaloguj się</h2>

        <div className="input-group">
          <input
            type="text"
            placeholder="Nazwa użytkownika"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="input-group">
          <input
            type="password"
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className="button-group">
          <button type="submit" className="primary-btn">Zaloguj się</button>
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