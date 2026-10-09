import { useEffect, useState } from 'react'
import './App.css'

// Backend läuft auf Port 8080, das Frontend (Vite) auf 5173.
// Damit der Browser die Antwort akzeptiert, erlaubt das Backend CORS (WebConfig.java).
const API_URL = 'http://localhost:8080/api'

function App() {
  const [hello, setHello] = useState('')
  const [messages, setMessages] = useState([])
  const [text, setText] = useState('')
  const [error, setError] = useState('')

  async function loadHello() {
    try {
      const res = await fetch(`${API_URL}/hello`)
      setHello(await res.text())
      setError('')
    } catch (e) {
      setError(`Backend nicht erreichbar: ${e.message}`)
    }
  }

  async function loadMessages() {
    try {
      const res = await fetch(`${API_URL}/messages`)
      setMessages(await res.json())
      setError('')
    } catch (e) {
      setError(`Backend nicht erreichbar: ${e.message}`)
    }
  }

  async function addMessage(event) {
    event.preventDefault()
    if (!text.trim()) return
    await fetch(`${API_URL}/messages`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text }),
    })
    setText('')
    loadMessages()
  }

  useEffect(() => {
    loadMessages()
  }, [])

  return (
    <main>
      <h1>my first message line</h1>

      <section>
        <h2>Schicht 3 → Schicht 2</h2>
        <button onClick={loadHello}>Backend fragen</button>
        <span className="result">{hello}</span>
      </section>

      <section>
        <h2>Schicht 3 → Schicht 2 → Schicht 1</h2>
        <form onSubmit={addMessage}>
          <input value={text} onChange={(e) => setText(e.target.value)} placeholder="Neue Nachricht" />
          <button type="submit">Speichern</button>
        </form>
        <ul>
          {messages.map((m) => (
            <li key={m.id}>{m.text}</li>
          ))}
        </ul>
      </section>

      {error && <p className="error">{error}</p>}
    </main>
  )
}

export default App
