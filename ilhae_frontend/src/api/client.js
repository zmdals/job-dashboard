const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";

export class ApiError extends Error {
  constructor(message, { status, code } = {}) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.code = code;
  }
}

function getAccessToken() {
  return (
    localStorage.getItem("accessToken") ||
    sessionStorage.getItem("accessToken")
  );
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {});
  const token = getAccessToken();

  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  if (response.status === 204) {
    return null;
  }

  const body = await response.json().catch(() => null);

  if (!response.ok) {
    throw new ApiError(body?.message || `HTTP ${response.status}`, {
      status: response.status,
      code: body?.code || "HTTP_ERROR",
    });
  }

  // Spring controllers return ApiResponse<T>, while the local mocks still
  // return T directly. Supporting both keeps stores independent of transport.
  if (
    body &&
    typeof body === "object" &&
    typeof body.success === "boolean" &&
    Object.hasOwn(body, "data")
  ) {
    if (!body.success) {
      throw new ApiError(body.message || "요청에 실패했습니다.", {
        status: response.status,
        code: body.code || "API_ERROR",
      });
    }

    return body.data;
  }

  return body;
}

export const api = {
  // =========================
  // Auth
  // =========================

  login(payload) {
    return request("/auth/login", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  signup(payload) {
    return request("/auth/signup", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // =========================
  // Postings
  // =========================

  // 전체 공고 조회
  getPostings(page = 0, size = 10) {
    const query = new URLSearchParams({
      page: String(page),
      size: String(size),
    });

    return request(`/postings?${query}`);
  },

  // 공고 상세 조회
  // JobRequirement(지원자격)도 상세 응답에 포함한다고 가정
  getPosting(postingId) {
    return request(`/postings/${postingId}`);
  },

  // 현재 사용자와 공고의 AI 적합도 분석
  getPostingRelevance(postingId) {
    return request(`/postings/${postingId}/relevance`);
  },

  // 공고 등록 (companyId, title 필수)
  createPosting(payload) {
    return request("/postings", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 공고 수정 (companyId, title 필수, 회사 연결은 서버에서 유지)
  updatePosting(postingId, payload) {
    return request(`/postings/${postingId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },

  // 공고 삭제
  deletePosting(postingId) {
    return request(`/postings/${postingId}`, {
      method: "DELETE",
    });
  },

  // 공고 및 회사 관련 부가 정보
  getPostingInfo(postingId) {
    return request(`/postings/${postingId}/info`);
  },

  // =========================
  // My Specs
  // =========================

  // 학력 전체 조회
  getEducations() {
    return request("/me/educations");
  },

  // 학력 상세 조회
  getEducation(educationId) {
    return request(`/me/educations/${educationId}`);
  },

  // 학력 추가
  addEducation(payload) {
    return request("/me/educations", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 학력 수정
  updateEducation(educationId, payload) {
    return request(`/me/educations/${educationId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },

  // 학력 삭제
  deleteEducation(educationId) {
    return request(`/me/educations/${educationId}`, {
      method: "DELETE",
    });
  },

  // 경력 전체 조회
  getCareers() {
    return request("/me/careers");
  },

  // 경력 상세 조회
  getCareer(careerId) {
    return request(`/me/careers/${careerId}`);
  },

  // 경력 추가
  addCareer(payload) {
    return request("/me/careers", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 경력 수정
  updateCareer(careerId, payload) {
    return request(`/me/careers/${careerId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },

  // 경력 삭제
  deleteCareer(careerId) {
    return request(`/me/careers/${careerId}`, {
      method: "DELETE",
    });
  },

  // 프로젝트 전체 조회
  getProjects() {
    return request("/me/projects");
  },

  // 프로젝트 상세 조회
  getProject(projectId) {
    return request(`/me/projects/${projectId}`);
  },

  // 프로젝트 추가
  addProject(payload) {
    return request("/me/projects", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 프로젝트 수정
  updateProject(projectId, payload) {
    return request(`/me/projects/${projectId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },

  // 프로젝트 삭제
  deleteProject(projectId) {
    return request(`/me/projects/${projectId}`, {
      method: "DELETE",
    });
  },

  // 내 스펙 전체 조회
  getMySpecs() {
    return request("/me/specs");
  },

  // =========================
  // Applications
  // =========================

  // 내 지원 현황 조회
  getMyApplications() {
    return request("/me/applications");
  },

  // 지원 상태 PREPARING으로 생성
  applyPosting(postingId) {
    return request(`/postings/${postingId}/applications`, {
      method: "POST",
    });
  },

  // 지원 상태 및 메모 변경
  updateApplicationStatus(applicationId, status, memo = null) {
    return request(`/applications/${applicationId}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status, memo }),
    });
  },

  // 지원 내역 삭제
  deleteApplication(applicationId) {
    return request(`/applications/${applicationId}`, {
      method: "DELETE",
    });
  },

  // =========================
  // Starred Postings
  // =========================

  // 즐겨찾기 조회
  getMyStarredPostings() {
    return request("/me/starred-postings");
  },

  // 즐겨찾기 추가
  starPosting(postingId) {
    return request(`/me/starred-postings/${postingId}`, {
      method: "POST",
    });
  },

  // 즐겨찾기 삭제
  unstarPosting(postingId) {
    return request(`/me/starred-postings/${postingId}`, {
      method: "DELETE",
    });
  },

  // =========================
  // User
  // =========================

  // 내 정보 조회
  getMe() {
    return request("/users/me");
  },

  // 자격증 전체 조회
  getCertificates() {
    return request("/me/certificates");
  },

  // 자격증 상세 조회
  getCertificate(certificateId) {
    return request(`/me/certificates/${certificateId}`);
  },

  // 자격증 추가
  addCertificate(payload) {
    return request("/me/certificates", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 자격증 수정
  updateCertificate(certificateId, payload) {
    return request(`/me/certificates/${certificateId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },

  // 자격증 삭제
  deleteCertificate(certificateId) {
    return request(`/me/certificates/${certificateId}`, {
      method: "DELETE",
    });
  },

  // =========================
  // Cover Letters
  // =========================

  // 자소서 작성
  createCoverLetter(applicationId, payload) {
    return request(`/applications/${applicationId}/cover-letters`, {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },

  // 자소서 목록 조회
  getCoverLetters(applicationId) {
    return request(`/applications/${applicationId}/cover-letters`);
  },

  // =========================
  // AI Mock API
  // =========================

  // 지원서 매칭 분석 요청
  requestApplicationAnalysis(applicationId) {
    return request(`/applications/${applicationId}/ai/analysis`, {
      method: "POST",
    });
  },

  // 지원서 매칭 분석 결과 조회
  getApplicationAnalysis(applicationId) {
    return request(`/applications/${applicationId}/ai/analysis`);
  },

  // 자소서 피드백 요청
  requestCoverLetterFeedback(coverLetterId) {
    return request(`/cover-letters/${coverLetterId}/ai/feedback`, {
      method: "POST",
    });
  },

  // 면접 질문 생성
  generateInterviewQuestions(applicationId) {
    return request(
      `/applications/${applicationId}/ai/interview-questions`,
      {
        method: "POST",
      }
    );
  },
};
