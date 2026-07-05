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

- **Maven**: `io.github.hisondev:api-link:2.0.0` / Spring Boot 3.3.5 BOM, jakarta / 의존: spring-boot-starter-websocket, data-model 2.0.0 / MIT
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

## 알려진 이슈 (수정 금지 — 추후 소유자와 재정리 예정)

1. **🔴 Boot 3 자동 등록 깨짐 의심**: AutoConfiguration.imports가 존재하지 않는 v1 클래스명(WebSocketConfig, ApiController)을 참조. 올바른 클래스명은 spring.factories에만 있으나 Boot 3는 이를 무시 → 자동 등록 실패/기동 오류 가능성, 실동작 검증 필요
2. @HisonService v2 필수 요구사항이 어떤 문서에도 없음
3. 사이트의 커스텀 핸들러 예시 `handle()` 메서드는 v2 인터페이스에 없음 (실제 훅: beforeHandleRequest/handleAuthority/handleLog/afterHandleRequest/예외 4종)
4. status 기본 메시지 사이트와 상이 ("...ready and running." vs 실제 "...is running.")
5. 사소: javadoc @version 2.0.1 vs pom 2.0.0, 메서드명 오타 `respones`
6. demo 프로젝트는 hisonjv 1.0.4(구버전) 사용 — v2 기능/요구사항 검증에 부적합

## 작업 규칙

- 이 저장소의 소스/README 수정은 사용자의 명시적 지시가 있을 때만 진행 (프로젝트 루트 CLAUDE.md 규칙 준수)
