export const APPLICATION_STATUS_OPTIONS = [
  { value: 'PREPARING', label: '서류준비' },
  { value: 'APPLIED', label: '지원완료' },
  { value: 'CODING_TEST', label: '코딩테스트 (필기시험)' },
  { value: 'FIRST_INTERVIEW', label: '1차면접' },
  { value: 'SECOND_INTERVIEW', label: '2차면접' },
  { value: 'FINAL_INTERVIEW', label: '최종면접' },
  { value: 'ACCEPTED', label: '최종합격' },
  { value: 'REJECTED', label: '탈락' },
]

export const APPLICATION_STATUS_VALUES = APPLICATION_STATUS_OPTIONS.map(({ value }) => value)

export const APPLICATION_STATUS_TRANSITIONS = {
  PREPARING: ['APPLIED', 'REJECTED'],
  APPLIED: ['CODING_TEST', 'FIRST_INTERVIEW', 'REJECTED'],
  CODING_TEST: ['FIRST_INTERVIEW', 'REJECTED'],
  FIRST_INTERVIEW: ['SECOND_INTERVIEW', 'FINAL_INTERVIEW', 'ACCEPTED', 'REJECTED'],
  SECOND_INTERVIEW: ['FINAL_INTERVIEW', 'ACCEPTED', 'REJECTED'],
  FINAL_INTERVIEW: ['ACCEPTED', 'REJECTED'],
  ACCEPTED: [],
  REJECTED: [],
}

export const APPLICATION_STATUS_GROUPS = {
  진행중: ['PREPARING', 'APPLIED', 'CODING_TEST'],
  '면접 예정': ['FIRST_INTERVIEW', 'SECOND_INTERVIEW', 'FINAL_INTERVIEW'],
  최종합격: ['ACCEPTED'],
}
