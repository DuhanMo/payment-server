import { loadTossPayments } from "@tosspayments/tosspayments-sdk";
import { useEffect, useState } from "react";

const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";

export function CheckoutPage({ userId, products, amount }) {
    const [ready, setReady] = useState(false);
    const [widgets, setWidgets] = useState(null);
    const customerKey = "9999"; // TODO userId;

    // -------- 1. TossPayments 위젯 초기화 --------
    useEffect(() => {
        async function initWidgets() {
            const tossPayments = await loadTossPayments(clientKey);
            const paymentWidgets = tossPayments.widgets({ customerKey });
            setWidgets(paymentWidgets);
        }

        initWidgets();
    }, []);

    // -------- 2. 위젯 렌더링 --------
    useEffect(() => {
        if (!widgets) return;

        async function renderWidgets() {
            // 결제금액 설정
            await widgets.setAmount({
                currency: "KRW",
                value: amount, // 부모 컴포넌트로부터 전달받은 금액
            });

            // 결제수단 렌더링
            await widgets.renderPaymentMethods({
                selector: "#payment-method",
                variantKey: "DEFAULT",
            });

            // 이용약관 렌더링
            await widgets.renderAgreement({
                selector: "#agreement",
                variantKey: "AGREEMENT",
            });

            setReady(true);
        }

        renderWidgets();
    }, [widgets, amount]);

    // -------- 3. 주문 생성 후 결제 요청 --------
    const handlePayment = async () => {
        try {
            // (1) 서버에 주문 생성
            //     userId, products 를 포함해 서버에서 필요한 데이터를 모두 전송
            const response = await fetch("/api/orders", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userId: userId,
                    products: products.map((p) => ({
                        productId: p.productId,
                        quantity: p.quantity,
                    })),
                    // 서버 측에서 가격을 검증한다면 amount를 보내도 되고,
                    // 서버에서 직접 상품 가격을 조회해 totalAmount를 계산하는 방식도 가능합니다.
                }),
            });

            if (!response.ok) {
                // 주문 생성 실패 시 처리
                throw new Error("주문 생성 실패");
            }

            // 서버가 반환하는 Order 객체 예시: { id: number, ... }
            const orderData = await response.json();
            // orderId 추출 (예시: orderData.id 가 서버에서 내려주는 주문 번호라고 가정)

            // (2) 결제창 띄우기
            //     orderId를 TossPayments로 전달하여 결제 요청
            await widgets.requestPayment({
                // 서버에서 받은 orderId
                orderId: orderData.pgOrderId,
                // 화면 등에 표시되는 주문명
                orderName: orderData.description,
                // 결제 완료 후 이동할 URL
                successUrl: window.location.origin + "/success",
                // 결제 실패 시 이동할 URL
                failUrl: window.location.origin + "/fail",
                // 그 외 고객 정보
                customerEmail: "customer123@gmail.com",
                customerName: "김토스",
                customerMobilePhone: "01012341234",
            });
        } catch (error) {
            console.error("결제 요청 중 오류 발생:", error);
            alert("결제 요청에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
    };

    // -------- 4. JSX --------
    return (
        <div className="wrapper">
            <div className="box_section">
                {/* 결제수단 UI */}
                <div id="payment-method" />
                {/* 이용약관 UI */}
                <div id="agreement" />

                {/* 결제하기 버튼 */}
                <button
                    className="button"
                    disabled={!ready}
                    onClick={handlePayment}
                >
                    결제하기
                </button>
            </div>
        </div>
    );
}
