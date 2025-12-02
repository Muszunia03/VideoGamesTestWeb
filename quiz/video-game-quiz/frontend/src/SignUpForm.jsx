import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NotificationPopup from './NotificationPopup';
import './SignUpForm.css'; 

function SignUpForm() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [usernameError, setUsernameError] = useState('');
  const [emailError, setEmailError] = useState('');

  const [showPopup, setShowPopup] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');
  const [popupType, setPopupType] = useState('');
  const [redirectAfterClose, setRedirectAfterClose] = useState(false);

  const navigate = useNavigate();

  const validateForm = () => {
    let isValid = true;
    setUsernameError('');
    setEmailError('');

    if (username.length < 3) {
      setUsernameError('Nazwa użytkownika musi mieć co najmniej 3 znaki.');
      isValid = false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
      setEmailError('Wprowadź prawidłowy adres e-mail.');
      isValid = false;
    }

    if (password !== confirmPassword) {
      setPopupMessage('Hasła muszą być identyczne.');
      setPopupType('error');
      setShowPopup(true);
      isValid = false;
    }

    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password }),
      });

      const data = await response.json();

      if (data.status === 'success') {
        setPopupMessage('Konto zostało pomyślnie utworzone! Możesz się teraz zalogować.');
        setPopupType('success');
        setShowPopup(true);
        setRedirectAfterClose(true);
      } else {
        setPopupMessage(data.message || 'Rejestracja nie powiodła się.');
        setPopupType('error');
        setShowPopup(true);
      }
    } catch (error) {
      setPopupMessage('Błąd sieci. Spróbuj ponownie później.');
      setPopupType('error');
      setShowPopup(true);
    }
  };

  const handleClosePopup = () => {
    setShowPopup(false);
    if (redirectAfterClose) navigate('/login');
  };

  return (
    <div className="form-page-wrapper">
      <form className="sign-up-form" onSubmit={handleSubmit}>
        <h2>Stwórz swoje konto</h2>

        <div className="input-group">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className={emailError ? 'input-error' : ''}
          />
          {emailError && <p className="error-message">{emailError}</p>}
        </div>

        <div className="input-group">
          <input
            type="text"
            placeholder="Nazwa użytkownika"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className={usernameError ? 'input-error' : ''}
          />
          {usernameError && <p className="error-message">{usernameError}</p>}
        </div>

        <div className="input-group password-input-group">
          <input
            type={showPassword ? 'text' : 'password'}
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <span
            className="password-toggle"
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? '👁️' : '👁️‍🗨️'}
          </span>
        </div>

        <div className="input-group password-input-group">
          <input
            type={showConfirmPassword ? 'text' : 'password'}
            placeholder="Potwierdź hasło"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
          <span
            className="password-toggle"
            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
          >
            {showConfirmPassword ? '👁️' : '👁️‍🗨️'}
          </span>
        </div>

        <div className="button-group">
          <button type="submit" className="primary-btn">Zarejestruj się</button>
          <button type="button" onClick={() => navigate('/login')} className="secondary-btn">
            Masz już konto? Zaloguj się
          </button>
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

export default SignUpForm;