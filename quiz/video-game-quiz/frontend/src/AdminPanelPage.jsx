import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminPanelPage.css';

function AdminPanelPage() {
  const navigate = useNavigate();

  const [users, setUsers] = useState([
    { id: 1, username: 'GraczMaster99', email: 'gracz.master@example.com', status: 'Aktywny' },
    { id: 4, username: 'AnonimowyGracz', email: 'anon@example.com', status: 'Zbanowany' },
    { id: 5, username: 'QuizFanatic', email: 'quiz.fan@example.com', status: 'Aktywny' },
  ]);

  const [showBanModal, setShowBanModal] = useState(false);
  const [showPunishModal, setShowPunishModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [banConfirmed, setBanConfirmed] = useState(false);
  const [punishDuration, setPunishDuration] = useState('1'); 
  const [punishConfirmed, setPunishConfirmed] = useState(false);

  useEffect(() => {
    // Tutaj możesz umieścić logikę pobierania użytkowników z API
    // lub sprawdzania uprawnień administratora, jak wcześniej sugerowano
  }, [navigate]);

  const openBanModal = (user) => {
    setSelectedUser(user);
    setBanConfirmed(false);
    setShowBanModal(true);
  };

  const closeBanModal = () => {
    setShowBanModal(false);
    setSelectedUser(null);
    setBanConfirmed(false);
  };

  const confirmBan = () => {
    if (selectedUser && banConfirmed) {
      setUsers(prevUsers =>
        prevUsers.map(user =>
          user.id === selectedUser.id ? { ...user, status: 'Zbanowany' } : user
        )
      );
      alert(`Użytkownik ${selectedUser.username} (ID: ${selectedUser.id}) został ZBANOWANY.`);
      closeBanModal();
      // fetch(`/api/admin/banUser/${selectedUser.id}`, { method: 'POST' });
    } else if (!banConfirmed) {
      alert('Musisz potwierdzić chęć zbanowania użytkownika.');
    }
  };

  const openPunishModal = (user) => {
    setSelectedUser(user);
    setPunishDuration('1');
    setPunishConfirmed(false);
    setShowPunishModal(true);
  };

  const closePunishModal = () => {
    setShowPunishModal(false);
    setSelectedUser(null);
    setPunishDuration('1');
    setPunishConfirmed(false);
  };

  const confirmPunishment = () => {
    if (selectedUser && punishConfirmed) {
      alert(`Nałożono KARĘ (${punishDuration} dni) na użytkownika ${selectedUser.username} (ID: ${selectedUser.id}).`);
      closePunishModal();
      // fetch(`/api/admin/punish/${selectedUser.id}`, {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify({ duration: punishDuration })
      // });
    } else if (!punishConfirmed) {
      alert('Musisz potwierdzić chęć nałożenia kary.');
    }
  };

  return (
    <div className="admin-panel-container">
      <h1 className="admin-panel-title">Admin Panel</h1>
      <p className="admin-panel-description">
        Manage users, quizzes, and application settings.
      </p>

      <section className="admin-section user-management-section">
        <h2 className="section-title">User Management</h2>
        <div className="users-table-container">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map(user => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td className={user.status === 'Zbanowany' ? 'status-banned' : 'status-active'}>
                    {user.status}
                  </td>
                  <td>
                    <button
                      className="action-btn ban-btn"
                      onClick={() => openBanModal(user)}
                      disabled={user.status === 'Zbanowany'}
                    >
                      Ban
                    </button>
                    <button
                      className="action-btn punish-btn"
                      onClick={() => openPunishModal(user)}
                    >
                      Kara
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <button className="back-to-home-btn" onClick={() => navigate('/')}>
        Back to Homepage
      </button>

      {/* Modal dla Bana */}
      {showBanModal && selectedUser && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <h3 className="modal-title">Confirm Ban</h3>
            <p className="modal-message">
              Are you sure you want to **permanently ban** user **{selectedUser.username}**?
              This action cannot be undone.
            </p>
            <div className="modal-checkbox-group">
              <input
                type="checkbox"
                id="confirmBan"
                checked={banConfirmed}
                onChange={(e) => setBanConfirmed(e.target.checked)}
              />
              <label htmlFor="confirmBan">I understand and confirm this action.</label>
            </div>
            <div className="modal-actions">
              <button className="modal-btn cancel-btn" onClick={closeBanModal}>Cancel</button>
              <button
                className="modal-btn confirm-btn"
                onClick={confirmBan}
                disabled={!banConfirmed}
              >
                Confirm Ban
              </button>
            </div>
          </div>
        </div>
      )}

      {showPunishModal && selectedUser && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <h3 className="modal-title">Apply Punishment</h3>
            <p className="modal-message">
              Select the duration for the punishment for user **{selectedUser.username}**.
            </p>
            <div className="modal-input-group">
              <label htmlFor="punishDuration">Punishment Duration:</label>
              <select
                id="punishDuration"
                value={punishDuration}
                onChange={(e) => setPunishDuration(e.target.value)}
              >
                <option value="1">1 </option>
                <option value="3">3 Days</option>
                <option value="7">7 Days</option>
                <option value="30">30 Days</option>
              </select>
            </div>
            <div className="modal-checkbox-group">
              <input
                type="checkbox"
                id="confirmPunish"
                checked={punishConfirmed}
                onChange={(e) => setPunishConfirmed(e.target.checked)}
              />
              <label htmlFor="confirmPunish">Nałóż kare.</label>
            </div>
            <div className="modal-actions">
              <button className="modal-btn cancel-btn" onClick={closePunishModal}>Cancel</button>
              <button
                className="modal-btn confirm-btn"
                onClick={confirmPunishment}
                disabled={!punishConfirmed}
              >
                Potwierdź
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminPanelPage;