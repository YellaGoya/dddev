import { createRoot } from 'react-dom/client';
import App from './App';

const container = document.getElementById('wrap');
const wrap = createRoot(container);

wrap.render(<App />);
