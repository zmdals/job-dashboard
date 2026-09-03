import { initialMockDb } from './mockData'

const STORAGE_KEY = 'recruiting-api-mock-db-v1'

function clone(value) {
  return structuredClone(value)
}

function migrate(db) {
  const storedVersion = db.mockDataVersion ?? 1
  const initialScoreByPostingId = new Map(
    initialMockDb.postings.map((posting) => [posting.id, posting.relevanceScore]),
  )
  let changed = false

  if (storedVersion < 2) {
    const mergeMissingById = (storedItems = [], initialItems = []) => {
      const storedIds = new Set(storedItems.map((item) => item.id))
      return [
        ...storedItems,
        ...initialItems.filter((item) => !storedIds.has(item.id)).map(clone),
      ]
    }

    db.companies = mergeMissingById(db.companies, initialMockDb.companies)
    db.postings = mergeMissingById(db.postings, initialMockDb.postings)
    db.applications = mergeMissingById(db.applications, initialMockDb.applications)
    db.postingInfoById = {
      ...clone(initialMockDb.postingInfoById),
      ...db.postingInfoById,
    }
    changed = true
  }

  if (storedVersion < 3) {
    db.applications?.forEach((application) => {
      if (application.status === 'IN_PROGRESS') {
        application.status = 'FIRST_INTERVIEW'
      }
    })
    changed = true
  }

  if (storedVersion < initialMockDb.mockDataVersion) {
    db.mockDataVersion = initialMockDb.mockDataVersion
    changed = true
  }

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
