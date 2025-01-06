import { useLocation } from "react-router-dom";
import { CheckoutPage } from "./CheckoutPage";

function PurchasePage() {
    const location = useLocation();
    const { userId, products } = location.state || {};

    // 총 결제 금액 계산
    const totalAmount = products.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
    );

    return (
        <div>
            <h1>주문 확인</h1>
            <ul>
                {products.map((product) => (
                    <li key={product.productId}>
                        {product.name} - 수량: {product.quantity}개 - 가격:{" "}
                        {(product.price * product.quantity).toLocaleString()}원
                    </li>
                ))}
            </ul>
            <h2>총 결제 금액: {totalAmount.toLocaleString()}원</h2>

            {/* CheckoutPage 호출 */}
            <CheckoutPage userId={userId} products={products} amount={totalAmount} />
        </div>
    );
}

export default PurchasePage;
