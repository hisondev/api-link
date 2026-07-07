# api-link — hisondev cmd 기반 API 라우팅 라이브러리

hisondev 생태계의 핵심 Java artifact. 컨트롤러 작성 없이 `cmd`("서비스명.메서드명")로 `@Service` 메서드를 직접 호출.
hisonjv 통합 artifact에 포함되며, 단독 사용도 가능 (`io.github.hisondev:api-link`).

## 구조 (중첩 디렉토리 주의)

```
jv/api-link/            ← git 저장소 루트 (README, LICENSE)
└─ api-link/            ← 실제 Maven 프로젝트 루트 (pom.xml)
   └─ src/main/
      ├─ java/io/github/hison/api/
      │  ├─ controller/        ApiLink(핵심 317줄), ApiLinkController(자동 등록용)
      │  ├─ controllerhandler/ ApiHandler(Default/Factory) — 요청 훅·예외 처리
      │  ├─ caching/           CachingWebSocket, ApiLinkWebSocket(자동 등록용), CachingWebSocketSessionManager
      │  ├─ cachinghandler/    CachingHandler(Default/Factory), CachingWebSocketHandler
      │  ├─ exception/         ApiException, ServiceRuntimeException (code 필드)
      │  └─ util/              @HisonService(마커), MethodHandleUtil, CorsValidator
      └─ resources/META-INF/   spring.factories, spring/...AutoConfiguration.imports (⚠️ 이슈 1 참조)
```

## 핵심 사실

- **Maven**: `io.github.hisondev:api-link:2.0.2`(2026-07-06 보완, 배포 대기) / Spring Boot 3.3.5 BOM, jakarta / 의존: spring-boot-starter-websocket, **data-model 2.0.1** / MIT
- **패키지는 `io.github.hison.api.*`**
- **⚠️ v2 필수**: cmd로 호출될 서비스 빈에 `@HisonService`(io.github.hison.api.util) 부착 필수 — 없으면 APIERROR0007. 사이트/README/demo에 미문서화된 v2 신규 요구사항
- 요청 흐름: beforeHandleRequest → handleAuthority → handleLog → cmd 파싱(빈 이름 = 서비스명 첫 글자 소문자) → 메서드 호출 → afterHandleRequest
- 메서드 해석: `method(DataWrapper)`/`method()` 우선(MethodHandle), 폴백 리플렉션은 DataWrapper/HttpServletRequest/String(파라미터명=키, `-parameters` 필요) 조합 지원
- 반환 허용: DataWrapper(→200) / ResponseEntity<DataWrapper> / null(→204)
- 에러코드: APIERROR0001~0009 (cmd 없음/형식 오류/빈 없음/.../@HisonService 미부착=0007)
- 예외 기본 응답: HTTP 500 + `{status:"error", code, message}` (ServiceRuntimeException만 실제 메시지 노출)
- properties: `hison.link.api.path`(기본 /hison-api-link), cors 3종, `hison.link.api.status.message`, `hison.link.websocket.endpoint`(콤마 복수 가능). `{path}/status` 헬스체크 존재
- WebSocket 푸시: `CachingWebSocketSessionManager.getInstance().notifyAllSessions(msg)`
- 커스터마이징 패턴(공통): XxxDefault 상속 → `XxxFactory.setCustomHandler()` → main()에서 register()

## 상세 문서

- 가이드: `../../../md/hisondev-api-link.md` (소스 검증 완료 — 요청 흐름/에러코드/훅 목록 포함)
- 전체 메서드 표: `../../../md/hisondev-hisonjv.md`의 ApiLink 섹션
- 생태계 전체: `../../../md/hisondev-ecosystem.md`

## 보완 이력 (v2.0.1 — 2026-07-06 완료, 배포 대기)

원본 의도 + nonoshow 포크 개선 종합. 상세 = `../../../md/hisondev-api-link.md` 11절 / README Changelog. 컴파일·빌드 통과.
- 🔴 **Boot3 자동등록 복구**(imports 클래스명 정정 ApiLinkWebSocket/ApiLinkController) + @ConditionalOnMissingBean override (D1)
- 🔴 **핸들러 빈순서 근본해결**: ApiLink가 핸들러를 첫 요청 시 지연조회(빈 우선→정적팩토리 폴백). @DependsOn 불필요 (D3)
- **@ApiLinkService 신설**(@Service 통합 — 단일 어노테이션으로 빈 등록+노출), @HisonService는 @Deprecated 별칭 (D2)
- null 응답 계약 정합(null body 반환) / cmd 검증 강화 / null-safety (포크 개선)
- 캐싱 모듈 명확화(A): "캐시 무효화 신호 채널, 채팅 아님" + `hison.link.websocket.enabled` on/off
- data-model 2.0.1 의존, pom·README·Changelog
- ⚠️ 남은 것(github.io 단계): 사이트 handle() 예시·status 메시지·`respones` 오타 / demo는 hisonjv 1.0.4라 v2 검증 부적합
- ⚠️ 자동등록 Boot3 실동작은 nonoshow 의존성 교체(9단계) 때 최종 검증

## nonoshow 포크와의 관계

- nonoshow `backend/common/api/*`는 이 api-link의 **포크(최신 테스트본)**. 이번 2.0.1이 그 개선을 원본에 반영한 것.
- **9단계(마지막)**: nonoshow가 포크를 걷어내고 이 원본 2.0.1 의존으로 교체 예정. 단 nonoshow의 `@ApiLinkService`(현재 포크 util)도 원본 것으로 통일.

## 작업 규칙

- 이 저장소의 소스/README 수정은 사용자의 명시적 지시가 있을 때만 진행 (프로젝트 루트 CLAUDE.md 규칙 준수)
