import { useState } from 'react'
import { sculptureList } from './data'
import './index.css'

export default function App() {
  const [index, setIndex] = useState(0)
  const [showMore, setShowMore] = useState(false)

  const sculpture = sculptureList[index]

  function handleNext() {
    setIndex((i) => (i + 1) % sculptureList.length)
  }

  return (
    <div className="app">
      <div className="controls">
        <button onClick={handleNext}>Next</button>
        <button onClick={() => setShowMore((s) => !s)}>
          {showMore ? 'Hide' : 'Show'} details
        </button>
      </div>
      <h2>
        <i>{sculpture.name}</i> by {sculpture.artist}
      </h2>
      <h3>
        ({index + 1} of {sculptureList.length})
      </h3>
      <img src={sculpture.url} alt={sculpture.alt} className="sculpture" />
      {showMore && <p className="desc">{sculpture.description}</p>}
    </div>
  )
}
