package com.jobdashboard.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobdashboard.backend.dto.ai.*;
import com.jobdashboard.backend.dto.matchanalysis.MatchAnalysisReq;
import com.jobdashboard.backend.dto.matchanalysis.MatchAnalysisRes;
import com.jobdashboard.backend.entity.Application;
import com.jobdashboard.backend.entity.CoverLetter;
import com.jobdashboard.backend.entity.InterviewQuestion;
import com.jobdashboard.backend.entity.MatchAnalysis;
import com.jobdashboard.backend.entity.enums.AiStatus;
import com.jobdashboard.backend.entity.enums.InterviewCategory;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiMockService {

    private final ApplicationRepository applicationRepository;
    private final MatchAnalysisRepository matchAnalysisRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final CoverLetterRepository coverLetterRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ============================================================
    // 1. 적합도 (비저장, 모달용)
    //    → 나중에 AI 붙이면 유저 스펙 vs 공고 요건 비교 로직으로 교체
    // ============================================================
    public RelevanceRes getRelevance(Long postingId) {
        return RelevanceRes.builder()
                .score(78)
                .summary("지원자의 컴퓨터공학 전공과 Spring Boot 프로젝트 경험이 해당 공고 요건에 부합합니다.")
                .strengths(List.of(
                        "컴퓨터공학 전공 (필수 요건 충족)",
                        "정보처리기사 보유",
                        "Spring Boot 기반 프로젝트 경험 2건"
                ))
                .weaknesses(List.of(
                        "관련 직무 경력 부족 (인턴 경험만 보유)",
                        "Python 활용 경험 없음"
                ))
                .build();
    }

    // ============================================================
    // 2. 매칭 분석 생성 (저장형, 덮어쓰기)
    //    → includeCoverLetter=true면 점수 높게, false면 낮게
    // ============================================================
    @Transactional
    public MatchAnalysisRes createAnalysis(Long applicationId, Long userId, MatchAnalysisReq req) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다."));

        // 기존 분석 있으면 삭제 (덮어쓰기)
        // flush를 안 하면 Hibernate가 삭제보다 아래쪽의 insert를 먼저 실행해서
        // application_id unique 제약(OneToOne)에 걸려 재분석 시 500이 남
        matchAnalysisRepository.deleteByApplicationId(applicationId);
        matchAnalysisRepository.flush();

        boolean withCoverLetter = req.getIncludeCoverLetter() != null && req.getIncludeCoverLetter();
        int mockScore = withCoverLetter ? 84 : 72;

        String strengths = toJson(withCoverLetter
                ? List.of("컴퓨터공학 전공", "정보처리기사 보유", "Spring Boot 프로젝트 경험", "자소서에서 직무 이해도 높음")
                : List.of("컴퓨터공학 전공", "정보처리기사 보유", "Spring Boot 프로젝트 경험"));

        String weaknesses = toJson(withCoverLetter
                ? List.of("관련 직무 경력 부족")
                : List.of("관련 직무 경력 부족", "자소서 미제출로 직무 적합성 판단 제한"));

        String recommendation = withCoverLetter
                ? "자소서의 프로젝트 경험 서술이 직무 요건과 잘 맞습니다. 기술 면접 대비를 추천합니다."
                : "자소서를 작성하면 더 정밀한 분석이 가능합니다. 현재 스펙만으로는 경력 부족이 주요 약점입니다.";

        MatchAnalysis analysis = MatchAnalysis.builder()
                .application(application)
                .totalScore(BigDecimal.valueOf(mockScore))
                .passProbability(BigDecimal.valueOf(mockScore - 5))
                .summary("종합 적합도 " + mockScore + "% — " + (withCoverLetter ? "자소서 포함 분석" : "스펙 기반 분석"))
                .strengths(strengths)
                .weaknesses(weaknesses)
                .recommendation(recommendation)
                .includedCoverLetter(withCoverLetter)
                .aiStatus(AiStatus.COMPLETED)
                .build();

        MatchAnalysis saved = matchAnalysisRepository.save(analysis);
        return toMatchAnalysisRes(saved);
    }

    // ============================================================
    // 3. 매칭 분석 조회
    // ============================================================
    public MatchAnalysisRes getAnalysis(Long applicationId, Long userId) {
        applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다."));

        MatchAnalysis analysis = matchAnalysisRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("분석 결과가 없습니다. 먼저 AI 분석을 요청해주세요."));

        return toMatchAnalysisRes(analysis);
    }

    // ============================================================
    // 4. 자소서 AI 피드백 (비저장)
    //    → 나중에 AI 붙이면 자소서 내용 분석 로직으로 교체
    // ============================================================
    public CoverLetterFeedbackRes getFeedback(Long coverLetterId) {
        CoverLetter coverLetter = coverLetterRepository.findById(coverLetterId)
                .orElseThrow(() -> new ResourceNotFoundException("자소서를 찾을 수 없습니다."));

        return CoverLetterFeedbackRes.builder()
                .coverLetterId(coverLetter.getId())
                .score(76)
                .summary("전반적으로 직무에 대한 이해도가 잘 드러나지만, 구체적인 성과 수치가 부족합니다.")
                .strengths(List.of(
                        "직무 지원 동기가 명확함",
                        "프로젝트 경험을 직무와 연결지어 서술",
                        "문장 구성이 논리적"
                ))
                .improvements(List.of(
                        "성과를 수치로 표현하면 설득력 향상 (예: 'DAU 20% 증가')",
                        "지원 회사에 대한 구체적 언급 추가 필요",
                        "마지막 문단에 입사 후 포부를 더 구체적으로"
                ))
                .build();
    }

    // ============================================================
    // 5. 면접 질문 생성 (저장형, 덮어쓰기)
    // ============================================================
    @Transactional
    public List<InterviewQuestionRes> createInterviewQuestions(Long applicationId, Long userId) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다."));

        // 기존 질문 있으면 삭제
        interviewQuestionRepository.deleteAllByApplicationId(applicationId);

        List<InterviewQuestion> questions = List.of(
                buildQuestion(application, InterviewCategory.PERSONALITY,
                        "팀 프로젝트에서 의견 충돌이 있었던 경험과 해결 방법을 말씀해주세요.",
                        "부스트캠프에서 기술 스택 선정 시 팀원과 의견이 갈렸고, 각 기술의 장단점을 표로 정리해 공유한 뒤 투표로 결정했습니다."),
                buildQuestion(application, InterviewCategory.TECHNICAL,
                        "Spring Boot에서 @Transactional의 동작 원리와 주의할 점을 설명해주세요.",
                        "프록시 기반 AOP로 동작하며, 같은 클래스 내 메서드 호출 시 트랜잭션이 적용되지 않는 self-invocation 문제에 주의해야 합니다."),
                buildQuestion(application, InterviewCategory.TECHNICAL,
                        "JPA의 N+1 문제가 무엇이고 어떻게 해결하나요?",
                        "연관 엔티티를 지연 로딩할 때 쿼리가 N번 추가 발생하는 문제입니다. Fetch Join이나 @EntityGraph로 해결할 수 있습니다."),
                buildQuestion(application, InterviewCategory.JOB_FIT,
                        "이 직무에 지원한 이유와 본인이 적합하다고 생각하는 근거를 말씀해주세요.",
                        "컴퓨터공학 전공과 Spring Boot 프로젝트 경험을 통해 백엔드 개발 역량을 쌓았고, 이 직무에서 요구하는 기술 스택과 일치합니다."),
                buildQuestion(application, InterviewCategory.EXPERIENCE,
                        "가장 도전적이었던 프로젝트 경험과 그 과정에서 배운 점을 이야기해주세요.",
                        "취업 대시보드 프로젝트에서 13개 엔티티 설계와 AI Mock API 구현을 3일 내에 완료한 경험이 있으며, 우선순위 설정과 MVP 개발의 중요성을 배웠습니다.")
        );

        List<InterviewQuestion> saved = interviewQuestionRepository.saveAll(questions);
        return saved.stream().map(InterviewQuestionRes::from).toList();
    }

    // ============================================================
    // 6. 면접 질문 조회
    // ============================================================
    public List<InterviewQuestionRes> getInterviewQuestions(Long applicationId, Long userId) {
        applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다."));

        List<InterviewQuestion> questions = interviewQuestionRepository
                .findAllByApplicationIdOrderByCreatedAtDesc(applicationId);

        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("면접 질문이 없습니다. 먼저 AI 질문 생성을 요청해주세요.");
        }

        return questions.stream().map(InterviewQuestionRes::from).toList();
    }

    // ============================================================
    // Helper Methods
    // ============================================================
    private InterviewQuestion buildQuestion(Application application, InterviewCategory category,
                                            String question, String sampleAnswer) {
        return InterviewQuestion.builder()
                .application(application)
                .category(category)
                .question(question)
                .sampleAnswer(sampleAnswer)
                .aiStatus(AiStatus.COMPLETED)
                .build();
    }

    private MatchAnalysisRes toMatchAnalysisRes(MatchAnalysis analysis) {
        return MatchAnalysisRes.builder()
                .id(analysis.getId())
                .applicationId(analysis.getApplication().getId())
                .score(analysis.getTotalScore().intValue())
                .summary(analysis.getSummary())
                .strengths(parseJson(analysis.getStrengths()))
                .weaknesses(parseJson(analysis.getWeaknesses()))
                .recommendation(analysis.getRecommendation())
                .includedCoverLetter(analysis.getIncludedCoverLetter())
                .build();
    }

    private static List<String> parseJson(String json) {
        if (json == null) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    private static String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }
}