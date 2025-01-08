import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

export function SuccessPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        // 쿼리 파라미터 값이 결제 요청할 때 보낸 데이터와 동일한지 반드시 확인하세요.
        // 클라이언트에서 결제 금액을 조작하는 행위를 방지할 수 있습니다.
        const requestData = {
            paymentType: searchParams.get("paymentType"),
            orderId: searchParams.get("orderId"),
            amount: searchParams.get("amount"),
            paymentKey: searchParams.get("paymentKey"),
        };

        async function handleSuccess() {
            try {
                // 1) AUTH (사전 검증) 호출
                const authResponse = await fetch("/api/orders/pay/auth", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(requestData),
                });

                if (!authResponse.ok) {
                    // AUTH 호출 실패 시 -> 결제 실패로 간주
                    const errorJson = await authResponse.json();
                    navigate(
                        `/fail?message=${encodeURIComponent(errorJson.message)}&code=${encodeURIComponent(
                            errorJson.code
                        )}`
                    );
                    return;
                }

                const authData = await authResponse.json();
                // pgStatus 확인
                if (authData.pgStatus !== "AUTH_SUCCESS") {
                    // AUTH_SUCCESS가 아니면 -> 결제 실패 처리
                    navigate(
                        `/fail?message=${encodeURIComponent("AUTH 단계에서 실패하였습니다.")}&code=AUTH_FAILED`
                    );
                    return;
                }

                // 2) AUTH_SUCCESS라면 최종 결제 확정(confirm) 호출
                const confirmResponse = await fetch("/api/orders/pay/confirm", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(requestData),
                });

                if (!confirmResponse.ok) {
                    // 결제 확정 실패 -> 결제 실패 로직
                    const failData = await confirmResponse.json();
                    navigate(
                        `/fail?message=${encodeURIComponent(failData.message)}&code=${encodeURIComponent(
                            failData.code
                        )}`
                    );
                    return;
                }

                // 결제 성공 비즈니스 로직을 여기에 추가
                // 예: 주문 상세 페이지 이동 / 성공 메시지 표시 등
                // navigate("/order/completed") 등...

            } catch (error) {
                console.error("결제 처리 중 에러:", error);
                // 예상치 못한 예외 발생 시에도 결제 실패로 처리
                navigate(
                    `/fail?message=${encodeURIComponent("결제 처리 중 오류가 발생했습니다.")}&code=UNKNOWN_ERROR`
                );
            }
        }
        handleSuccess();
    }, []);

    return (
        <div className="result wrapper">
            <div className="box_section">
                <h2>
                    결제 성공
                </h2>
                <p>{`주문번호: ${searchParams.get("orderId")}`}</p>
                <p>{`결제 금액: ${Number(
                    searchParams.get("amount")
                ).toLocaleString()}원`}</p>
                <p>{`paymentKey: ${searchParams.get("paymentKey")}`}</p>
            </div>
        </div>
    );
}