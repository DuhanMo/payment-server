import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SelectProductsPage from "./components/SelectProductsPage";
import PurchasePage from "./components/PurchasePage";
import {FailPage} from "./components/FailPage";
import {SuccessPage} from "./components/SuccessPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<SelectProductsPage />} />
                <Route path="/purchase" element={<PurchasePage />} />
                {/* 성공 및 실패 페이지 추가 */}
                <Route path="/success" element={<SuccessPage />} />
                <Route path="/fail" element={<FailPage />} />
            </Routes>
        </Router>
    );
}

export default App;
