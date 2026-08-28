package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.dto.KakaoLocalResponse;
import com.kalchelin.kalchelin_road.dto.RestaurantSearchResponse;
import com.kalchelin.kalchelin_road.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestClient kakaoRestClient;

    private static final String SEARCH_FAILED = "가게 검색에 실패했습니다. 잠시 후 다시 시도해 주세요.";
    private static final int PAGE_SIZE = 15;    // 카카오 최대값
    private static final String RESTAURANT_CATEGORY = "FD6";    // 음식점

    // 특별자치도만 정식 명칭으로 온다. 나머지 시, 도는 이미 축약형
    private static final Map<String, String> SIDO = Map.of(
            "강원특별자치도", "강원",
            "제주특별자치도", "제주",
            "전북특별자치도", "전북"
    );

    /**
     * DB를 건드리지 않으므로 @Transactional이 없다
     * 붙이면 네트워크를 기다리는 동안 DB 커넥션을 붙잡게 되어 오히려 해롭다.
     */
    public List<RestaurantSearchResponse> search(String region, String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        // 지역을 앞에 붙이면 카카오가 위치로 인식해 그 지역을 상위로 올려준다
        // 다만 잘라내지는 않으므로 인근 지역이 섞일 수 있다
        String fullQuery = (region == null || region.isBlank())
                ? query
                : region + " " + query;

        KakaoLocalResponse response = callKakao(fullQuery);

        if (response == null || response.documents() == null) {
            return List.of();
        }

        return response.documents().stream()
                .map(this::toSearchResponse)
                .toList();
    }

    private KakaoLocalResponse callKakao(String query) {
        try {
            return kakaoRestClient.get()
                    .uri(builder -> builder
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", query)
                            .queryParam("size", PAGE_SIZE)
                            .queryParam("category_group_code", RESTAURANT_CATEGORY)
                            .build())
                    .retrieve()
                    .body(KakaoLocalResponse.class);
        } catch (RestClientResponseException e) {
            // 응답은 왔는데 실패 - 401(키) 403(권한) 429(한도)
            log.warn("카카오 장소 검색 실패 status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException(SEARCH_FAILED);
        } catch (RestClientException e) {
            // 응답 자체가 못 옴 - 타임아웃, 연결 실패
            log.warn("카카오 장소 검색 호출 실패", e);
            throw new ExternalApiException(SEARCH_FAILED);
        }
    }

    private RestaurantSearchResponse toSearchResponse(KakaoLocalResponse.Document doc) {
        return new RestaurantSearchResponse(
                doc.placeName(),
                doc.addressName(),
                blankToNull(doc.roadAddressName()),
                toRegion(doc.addressName()),
                Double.parseDouble(doc.x()),
                Double.parseDouble(doc.y()),
                doc.placeUrl()
        );
    }

    // 카카오는 값이 없을 때 null이 아니라 빈 문자열을 준다
    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    /**
     * "서울 강남구 역삼동 732-17" -> "서울 강남구"
     * 카카오가 시,도를 이미 축약형으로 주므로 변환표가 필요 없다.
     */
    private String toRegion(String address) {
        if (address == null || address.isBlank()) return "";

        String[] tokens = address.split(" ");
        if (tokens.length < 2) return address;

        return SIDO.getOrDefault(tokens[0], tokens[0]) + " " + tokens[1];
    }
}
