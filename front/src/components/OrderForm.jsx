import React, { useState } from 'react';
import '../styles/OrderForm.css';

const OrderForm = () => {
    const [userId, setUserId] = useState('');
    const [orders, setOrders] = useState([{ productId: '', quantity: '' }]);

    const handleOrderChange = (index, field, value) => {
        const updatedOrders = [...orders];
        updatedOrders[index][field] = value;
        setOrders(updatedOrders);
    };

    const addOrderField = () => {
        setOrders([...orders, { productId: '', quantity: '' }]);
    };

    const removeOrderField = (index) => {
        const updatedOrders = orders.filter((_, i) => i !== index);
        setOrders(updatedOrders);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payload = {
            userId: Number(userId),
            products: orders.map((order) => ({
                productId: Number(order.productId),
                quantity: Number(order.quantity),
            })),
        };

        try {
            const response = await fetch('/api/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert('주문이 성공적으로 생성되었습니다!');
            } else {
                const error = await response.json();
                console.error('Error creating order:', error);
                alert('주문 생성에 실패했습니다.');
            }
        } catch (error) {
            console.error('Network error:', error);
            alert('네트워크 에러가 발생했습니다.');
        }
    };

    return (
        <div className="order-form">
            <h2>주문 생성</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        유저 ID:
                        <input
                            type="number"
                            value={userId}
                            onChange={(e) => setUserId(e.target.value)}
                            required
                        />
                    </label>
                </div>
                <h3>주문 상품</h3>
                {orders.map((order, index) => (
                    <div key={index} className="order-item">
                        <label>
                            상품 식별자:
                            <input
                                type="number"
                                value={order.productId}
                                onChange={(e) => handleOrderChange(index, 'productId', e.target.value)}
                                required
                            />
                        </label>
                        <label>
                            수량:
                            <input
                                type="number"
                                value={order.quantity}
                                onChange={(e) => handleOrderChange(index, 'quantity', e.target.value)}
                                required
                            />
                        </label>
                        {orders.length > 1 && (
                            <button type="button" onClick={() => removeOrderField(index)}>
                                삭제
                            </button>
                        )}
                    </div>
                ))}
                <button type="button" onClick={addOrderField}>
                    + 상품 추가
                </button>
                <div>
                    <button type="submit">주문 생성</button>
                </div>
            </form>
        </div>
    );
};

export default OrderForm;
