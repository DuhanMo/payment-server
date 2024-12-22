import React from 'react';
import ProductList from './components/ProductList';
import OrderForm from './components/OrderForm';
import './App.css';

const App = () => {
    return (
        <div className="App">
            <header>
                <h1>주문 관리 시스템</h1>
            </header>
            <main>
                <ProductList />
                <OrderForm />
            </main>
        </div>
    );
};

export default App;
