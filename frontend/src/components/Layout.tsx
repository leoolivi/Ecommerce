import { ReactNode } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';
import { ShoppingBag, ShoppingCart, LogOut, LogIn } from 'lucide-react';

interface LayoutProps {
  children: ReactNode;
  currentView: string;
  onNavigate: (view: string) => void;
}

export default function Layout({ children, currentView, onNavigate }: LayoutProps) {
  const { user, logout, isAdmin, isAuthenticated } = useAuth();
  const { itemCount } = useCart();

  return (
    <div className="min-h-screen bg-slate-50">
      <nav className="bg-white border-b border-slate-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-8">
              <button
                onClick={() => onNavigate('products')}
                className="flex items-center space-x-2 group"
              >
                <div className="bg-slate-900 p-2 rounded-lg group-hover:bg-slate-800 transition">
                  <ShoppingBag className="w-5 h-5 text-white" />
                </div>
                <span className="font-bold text-slate-900">ShopHub</span>
              </button>

              <div className="hidden md:flex space-x-1">
                <button
                  onClick={() => onNavigate('products')}
                  className={`px-4 py-2 rounded-lg font-medium transition ${
                    currentView === 'products'
                      ? 'bg-slate-100 text-slate-900'
                      : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
                  }`}
                >
                  Products
                </button>
                {isAuthenticated && (
                  <>
                    <button
                      onClick={() => onNavigate('orders')}
                      className={`px-4 py-2 rounded-lg font-medium transition ${
                        currentView === 'orders'
                          ? 'bg-slate-100 text-slate-900'
                          : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
                      }`}
                    >
                      Orders
                    </button>
                    {isAdmin && (
                      <button
                        onClick={() => onNavigate('admin')}
                        className={`px-4 py-2 rounded-lg font-medium transition ${
                          currentView === 'admin'
                            ? 'bg-slate-100 text-slate-900'
                            : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
                        }`}
                      >
                        Admin
                      </button>
                    )}
                  </>
                )}
              </div>
            </div>

            <div className="flex items-center space-x-4">
              {isAuthenticated && (
                <button
                  onClick={() => onNavigate('cart')}
                  className="relative p-2 text-slate-600 hover:text-slate-900 hover:bg-slate-100 rounded-lg transition"
                >
                  <ShoppingCart className="w-5 h-5" />
                  {itemCount > 0 && (
                    <span className="absolute -top-1 -right-1 bg-slate-900 text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                      {itemCount}
                    </span>
                  )}
                </button>
              )}

              <div className="flex items-center space-x-3 pl-3 border-l border-slate-200">
                {isAuthenticated ? (
                  <>
                    <span className="text-sm text-slate-600 hidden sm:block">
                      {user?.email}
                    </span>
                    <button
                      onClick={logout}
                      className="p-2 text-slate-600 hover:text-slate-900 hover:bg-slate-100 rounded-lg transition"
                      title="Logout"
                    >
                      <LogOut className="w-5 h-5" />
                    </button>
                  </>
                ) : (
                  <button
                    onClick={() => onNavigate('login')}
                    className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition"
                  >
                    <LogIn className="w-4 h-4" />
                    <span className="hidden sm:inline">Sign In</span>
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
    </div>
  );
}
