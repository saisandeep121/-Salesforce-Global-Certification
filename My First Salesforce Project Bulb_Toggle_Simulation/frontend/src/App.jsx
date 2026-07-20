import { useState } from 'react'
import './App.css'

function App() {
  const [date, setDate] = useState('2026-01-01');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSimulate = async (dateToSimulate = date) => {
    setLoading(true);
    setError('');
    try {
      const response = await fetch(`http://localhost:8080/api/simulate?date=${dateToSimulate}`);
      if (!response.ok) {
        throw new Error('Failed to fetch from server');
      }
      const data = await response.json();
      if (data.error) throw new Error(data.error);
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  const adjustDate = (days) => {
    const d = new Date(date);
    d.setDate(d.getDate() + days);
    const newDateStr = d.toISOString().split('T')[0];
    setDate(newDateStr);
    handleSimulate(newDateStr);
  }

  const defaultBulbs = Array.from({length: 7}, (_, i) => ({ 
    id: i + 1, 
    state: (i + 1) % 2 !== 0 ? 'ON' : 'OFF' // 1,3,5,7 ON by default
  }));
  
  const currentBulbs = result ? result.bulbs : defaultBulbs;

  return (
    <div className="container">
      <header>
        <h1>7-Bulb Lighting Simulation</h1>
        <p>A beautiful visual representation of the bulb scheduling logic.</p>
      </header>

      <div className="bulbs-container">
        {currentBulbs.map((bulb) => (
          <div key={bulb.id} className="bulb-wrapper">
            <div className={`bulb ${bulb.state === 'ON' ? 'on' : 'off'}`}>
              <div className="filament"></div>
            </div>
            <span className="bulb-label">Bulb {bulb.id}</span>
          </div>
        ))}
      </div>

      <div className="controls">
        <button onClick={() => adjustDate(-1)} disabled={loading} className="nav-btn">
          &laquo; Prev Day
        </button>
        <input 
          type="date" 
          value={date} 
          onChange={(e) => setDate(e.target.value)} 
          className="date-input"
        />
        <button onClick={() => adjustDate(1)} disabled={loading} className="nav-btn">
          Next Day &raquo;
        </button>
        <button onClick={() => handleSimulate(date)} disabled={loading} className="sim-btn">
          {loading ? 'Simulating...' : 'Simulate'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {result && (
        <div className="result-panel">
          <div className="info-cards">
          <div className="card">
            <h3>Holiday</h3>
            <p className={result.holiday !== 'None' ? 'holiday-text' : ''}>
              {result.holiday !== 'None' ? 'Yes' : 'No'}
            </p>
          </div>
          <div className="card">
            <h3>Reason</h3>
            <p>{result.reason}</p>
          </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default App
