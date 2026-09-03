import { initialMockDb } from './mockData'

const STORAGE_KEY = 'recruiting-api-mock-db-v1'

function clone(value) {
  return structuredClone(value)
}

function migrate(db) {
  const initialScoreByPostingId = new Map(
    initialMockDb.postings.map((posting) => [posting.id, posting.relevanceScore]),
  )
  let changed = false

  db.postings?.forEach((posting) => {
    if (posting.relevanceScore != null) return

    const initialScore = initialScoreByPostingId.get(posting.id)

    if (initialScore != null) {
      posting.relevanceScore = initialScore
      changed = true
    }
  })

  if (changed) {
    saveDb(db)
  }

  return db
}

export function getDb() {
  const raw = localStorage.getItem(STORAGE_KEY)

  if (!raw) {
    const db = clone(initialMockDb)
    saveDb(db)
    return db
  }

  try {
    return migrate(JSON.parse(raw))
  } catch {
    const db = clone(initialMockDb)
    saveDb(db)
    return db
  }
}

export function saveDb(db) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(db))
}

export function resetDb() {
  localStorage.removeItem(STORAGE_KEY)
}

export function nextId(prefix) {
  return `${prefix}-${crypto.randomUUID()}`
}
