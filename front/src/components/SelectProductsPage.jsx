import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function SelectProductsPage() {
    const [products, setProducts] = useState([]);
    const [cart, setCart] = useState([]);
    const navigate = useNavigate();

    // 서버에서 상품 목록 가져오기
    useEffect(() => {
        (async () => {
            try {
                const response = await fetch("/api/products");
                const data = await response.json();
                setProducts(data);

                // 초기 장바구니 상태 설정
                setCart(
                    data.map((product) => ({
                        productId: product.id,
                        name: product.name,
                        price: product.price,
                        quantity: 0,
                    }))
                );
            } catch (error) {
                console.error("상품 데이터를 불러오는 중 오류 발생:", error);
            }
        })();
    }, []);

    // 수량 업데이트
    const updateQuantity = (productId, increment) => {
        setCart((prevCart) =>
            prevCart.map((item) =>
                item.productId === productId
                    ? { ...item, quantity: Math.max(item.quantity + increment, 0) }
                    : item
            )
        );
    };

    // 구매하기 처리
    const handlePurchase = () => {
        const selectedProducts = cart.filter((item) => item.quantity > 0);
        if (selectedProducts.length === 0) {
            alert("상품을 선택해주세요!");
            return;
        }

        // 구매 페이지로 이동
        navigate("/purchase", {
            state: {
                userId: 1, // 사용자 ID를 설정 (예: 로그인 정보를 기반으로 설정)
                products: selectedProducts,
            },
        });
    };

    return (
        <div>
            <h1>상품 선택</h1>
            {products.map((product) => (
                <div key={product.id}>
                    <h3>{product.name}</h3>
                    <p>가격: {product.price.toLocaleString()}원</p>
                    <div>
                        <button onClick={() => updateQuantity(product.id, -1)}>-</button>
                        <span>
              {
                  cart.find((item) => item.productId === product.id)?.quantity ||
                  0
              }
            </span>
                        <button onClick={() => updateQuantity(product.id, 1)}>+</button>
                    </div>
                </div>
            ))}
            <button onClick={handlePurchase}>구매하기</button>
        </div>
    );
}

export default SelectProductsPage;
