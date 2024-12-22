import React, { useEffect, useState } from 'react';
import '../styles/ProductList.css';

const ProductList = () => {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetch('/api/products')
            .then((response) => response.json())
            .then((data) => setProducts(data))
            .catch((error) => console.error('Error fetching products:', error));
    }, []);

    return (
        <div className="product-list">
            <h2>상품 목록</h2>
            {products.length === 0 ? (
                <p>상품을 불러오는 중입니다...</p>
            ) : (
                <ul>
                    {products.map((product) => (
                        <li key={product.id}>
                            <span>상품 ID: {product.id}</span> | <span>이름: {product.name}</span> |{' '}
                            <span>가격: {product.price}원</span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ProductList;
