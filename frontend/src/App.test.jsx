import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it, vi } from 'vitest'
import App from './App.jsx'

const initialMessages = [
  { id: 1, text: 'Hallo aus der Datenbank!' },
  { id: 2, text: 'Zweite Nachricht' },
]

function mockBackend({ messages = initialMessages } = {}) {
  const stored = [...messages]
  return vi.spyOn(globalThis, 'fetch').mockImplementation(async (url, options = {}) => {
    if (url.endsWith('/hello')) {
      return new Response('Hallo vom Backend!')
    }
    if (url.endsWith('/messages') && options.method === 'POST') {
      const { text } = JSON.parse(options.body)
      const message = { id: stored.length + 1, text }
      stored.push(message)
      return new Response(JSON.stringify(message), { status: 201 })
    }
    if (url.endsWith('/messages')) {
      return new Response(JSON.stringify(stored))
    }
    throw new Error(`unerwartete URL ${url}`)
  })
}

describe('App', () => {
  it('lädt beim Start die Nachrichten aus dem Backend', async () => {
    mockBackend()
    render(<App />)

    expect(await screen.findByText('Hallo aus der Datenbank!')).toBeInTheDocument()
    expect(screen.getByText('Zweite Nachricht')).toBeInTheDocument()
  })

  it('zeigt nach Klick auf den Button den String vom Backend an', async () => {
    const fetchMock = mockBackend()
    render(<App />)

    await userEvent.click(screen.getByRole('button', { name: 'Backend fragen' }))

    expect(await screen.findByText('Hallo vom Backend!')).toBeInTheDocument()
    expect(fetchMock).toHaveBeenCalledWith('http://localhost:8080/api/hello')
  })

  it('speichert eine neue Nachricht und lädt die Liste neu', async () => {
    const fetchMock = mockBackend()
    render(<App />)

    await userEvent.type(screen.getByPlaceholderText('Neue Nachricht'), 'Aus dem Test')
    await userEvent.click(screen.getByRole('button', { name: 'Speichern' }))

    expect(await screen.findByText('Aus dem Test')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Neue Nachricht')).toHaveValue('')
    expect(fetchMock).toHaveBeenCalledWith(
      'http://localhost:8080/api/messages',
      expect.objectContaining({ method: 'POST', body: JSON.stringify({ text: 'Aus dem Test' }) }),
    )
  })

  it('speichert keine leeren Nachrichten', async () => {
    const fetchMock = mockBackend()
    render(<App />)

    await userEvent.type(screen.getByPlaceholderText('Neue Nachricht'), '   ')
    await userEvent.click(screen.getByRole('button', { name: 'Speichern' }))

    expect(fetchMock).not.toHaveBeenCalledWith(expect.anything(), expect.objectContaining({ method: 'POST' }))
  })

  it('zeigt eine Fehlermeldung, wenn das Backend nicht erreichbar ist', async () => {
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(new TypeError('Failed to fetch'))
    render(<App />)

    expect(await screen.findByText('Backend nicht erreichbar: Failed to fetch')).toBeInTheDocument()
  })
})
