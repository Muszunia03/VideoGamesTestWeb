import React, { useEffect, useState } from "react";
import "./QuizCategoryPage.css";

function AdminPanelPage() {
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    loadUsers();
  }, []);

  async function loadUsers() {
    try {
      const res = await fetch("http://localhost:8080/api/users/all");
      if (res.ok) {
        const data = await res.json();
        setUsers(data);
      }
    } catch (error) {
      console.error(error);
    }
  }

  async function banUser(username) {
    try {
      await fetch(`http://localhost:8080/api/users/ban/${username}`, { method: "POST" });
      setMessage(`User ${username} has been banned.`);
      loadUsers();
    } catch (e) {
      console.error(e);
    }
  }

  async function unbanUser(username) {
    try {
      await fetch(`http://localhost:8080/api/users/unban/${username}`, { method: "POST" });
      setMessage(`User ${username} has been unbanned.`);
      loadUsers();
    } catch (e) {
      console.error(e);
    }
  }

  const tableHeaderStyle = {
    padding: '15px',
    borderBottom: '2px solid #00E0FF',
    color: '#ffeb3b',
    fontSize: '1.1em',
    textTransform: 'uppercase',
    letterSpacing: '1px'
  };

  const tableCellStyle = {
    padding: '15px',
    borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
    color: '#e0e0e0',
    fontSize: '1em'
  };

  return (
    <div className="quiz-category-page-container">
      
      <h1 className="quiz-category-title">Admin Panel</h1>
      
      <p className="quiz-category-description">
        Manage users, view statistics, and control access permissions.
      </p>

      <div 
        className="quiz-placeholder-content" 
        style={{ maxWidth: '1000px', padding: '30px', overflowX: 'auto' }}
      >
        <h2 style={{ marginBottom: '20px', textShadow: '0 0 10px rgba(0, 224, 255, 0.5)' }}>
          User List
        </h2>

        {message && (
          <p className="result-message" style={{ marginBottom: '20px' }}>
            {message}
          </p>
        )}

        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
          <thead>
            <tr>
              <th style={tableHeaderStyle}>Username</th>
              <th style={tableHeaderStyle}>Email</th>
              <th style={tableHeaderStyle}>Status</th>
              <th style={tableHeaderStyle}>Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.username} style={{ transition: 'background 0.3s' }}>
                <td style={{...tableCellStyle, fontWeight: 'bold', color: '#fff'}}>{u.username}</td>
                <td style={tableCellStyle}>{u.email}</td>
                <td style={tableCellStyle}>
                  <span style={{
                    color: u.isBanned ? '#e74c3c' : '#1abc9c',
                    fontWeight: 'bold',
                    textShadow: `0 0 5px ${u.isBanned ? 'rgba(231, 76, 60, 0.5)' : 'rgba(26, 188, 156, 0.5)'}`
                  }}>
                    {u.isBanned ? "BANNED" : "ACTIVE"}
                  </span>
                </td>
                <td style={tableCellStyle}>
                  <button
                    className="primary-btn"
                    onClick={() => u.isBanned ? unbanUser(u.username) : banUser(u.username)}
                    style={{
                      padding: '8px 16px',
                      fontSize: '0.9em',
                      minWidth: '100px',
                      borderColor: u.isBanned ? '#1abc9c' : '#e74c3c',
                      backgroundColor: u.isBanned ? 'rgba(26, 188, 156, 0.1)' : 'rgba(231, 76, 60, 0.1)',
                      color: '#fff',
                      boxShadow: 'none'
                    }}
                    onMouseEnter={(e) => {
                       e.currentTarget.style.backgroundColor = u.isBanned ? '#1abc9c' : '#e74c3c';
                       e.currentTarget.style.color = '#000';
                    }}
                    onMouseLeave={(e) => {
                       e.currentTarget.style.backgroundColor = u.isBanned ? 'rgba(26, 188, 156, 0.1)' : 'rgba(231, 76, 60, 0.1)';
                       e.currentTarget.style.color = '#fff';
                    }}
                  >
                    {u.isBanned ? "UNBAN" : "BAN"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        
        {users.length === 0 && (
           <p style={{ marginTop: '20px', color: '#aaa' }}>Loading users...</p>
        )}
      </div>
    </div>
  );
}

export default AdminPanelPage;