import { useState } from 'react';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
import Products from './pages/Products';
import Cart from './pages/Cart';
import Orders from './pages/Orders';
import Admin from './pages/Admin';
import Layout from './components/Layout';

function AppContent() {
  const { isAuthenticated, isAdmin } = useAuth();
  const [authView, setAuthView] = useState<'login' | 'register' | 'forgot-password' | 'reset-password'>('login');
  const [currentView, setCurrentView] = useState('products');

  if (!isAuthenticated && currentView !== 'products') {
    if (authView === 'login') {
      return (
        <Login
          onSwitchToRegister={() => setAuthView('register')}
          onSwitchToForgotPassword={() => setAuthView('forgot-password')}
        />
      );
    } else if (authView === 'register') {
      return <Register onSwitchToLogin={() => setAuthView('login')} />;
    } else if (authView === 'forgot-password') {
      return (
        <ForgotPassword
          onSwitchToLogin={() => setAuthView('login')}
          onSwitchToReset={() => setAuthView('reset-password')}
        />
      );
    } else if (authView === 'reset-password') {
      return (
        <ResetPassword
          onSwitchToForgotPassword={() => setAuthView('forgot-password')}
          onSwitchToLogin={() => setAuthView('login')}
        />
      );
    }
  }

  if (!isAuthenticated) {
    return (
      <Layout currentView={currentView} onNavigate={setCurrentView}>
        <Products onNavigate={setCurrentView} />
      </Layout>
    );
  }

  return (
    <Layout currentView={currentView} onNavigate={setCurrentView}>
      {currentView === 'products' && <Products onNavigate={setCurrentView} />}
      {currentView === 'cart' && <Cart onNavigate={setCurrentView} />}
      {currentView === 'orders' && <Orders />}
      {currentView === 'admin' && isAdmin && <Admin />}
    </Layout>
  );
}

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <AppContent />
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
