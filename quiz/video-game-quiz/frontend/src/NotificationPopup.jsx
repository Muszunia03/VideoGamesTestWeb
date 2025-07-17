// NotificationPopup.js
import React, { useEffect } from 'react';
import './NotificationPopup.css'; // Link to its dedicated CSS

function NotificationPopup({ message, type, onClose }) {
  useEffect(() => {
    // Automatically close the pop-up after a few seconds for success messages
    // Or you can make it close always after some time
    const timer = setTimeout(() => {
      onClose();
    }, 3000); // Auto-closes after 3 seconds

    return () => clearTimeout(timer); // Cleanup the timer on unmount
  }, [onClose, type]); // Re-run effect if onClose or type changes

  return (
    <div className={`notification-popup-overlay ${type}`}>
      <div className={`notification-popup-content ${type}`}>
        <div className="popup-icon">
          {type === 'success' ? '✅' : '❌'} {/* Dynamic icon based on type */}
        </div>
        <p className="popup-message">{message}</p>
        <button className="popup-close-btn" onClick={onClose}>
          {type === 'success' ? 'Continue' : 'Close'} {/* Dynamic button text */}
        </button>
      </div>
    </div>
  );
}

export default NotificationPopup;