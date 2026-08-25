package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantRequest {

    @NotBlank(message = "가게 이름은 필수입니다")
    @Size(max = 100, message = "가게 이름은 100자 이내여야 합니다")
    private String name;

    @NotBlank(message = "주소는 필수입니다")
    @Size(max = 200, message = "주소는 200자 이내여야 합니다")
    private String address;

    @Size(max = 200)
    private String roadAddress;     // 선택

    @NotBlank(message = "지역은 필수입니다")
    @Size(max = 50)
    private String region;

    @NotNull(message = "경도는 필수입니다")
    @DecimalMin(value = "124.0", message = "한국 범위를 벗어난 좌표입니다")
    @DecimalMax(value = "132.0", message = "한국 범위를 벗어난 좌표입니다")
    private Double longitude;

    @NotNull(message = "위도는 필수입니다")
    @DecimalMin(value = "33.0", message = "한국 범위를 벗어난 좌표입니다")
    @DecimalMax(value = "39.0", message = "한국 범위를 벗어난 좌표입니다")
    private Double latitude;



}
