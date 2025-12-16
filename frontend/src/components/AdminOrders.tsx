import { useState, useEffect } from 'react';
import { api, Order } from '../lib/api';
import { Trash2, Package } from 'lucide-react';

export default function AdminOrders() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const data = await api.adminGetAllOrders();
      setOrders(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this order?')) return;

    try {
      await api.adminDeleteOrder(id);
      await loadOrders();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete order');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PAID':
        return 'bg-green-100 text-green-800';
      case 'SHIPPED':
        return 'bg-blue-100 text-blue-800';
      case 'CANCELED':
        return 'bg-red-100 text-red-800';
      case 'PAYING':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-slate-100 text-slate-800';
    }
  };

  if (loading) {
    return <div className="text-center py-8 text-slate-600">Loading...</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-semibold text-slate-900">All Orders</h2>
        <div className="text-sm text-slate-600">{orders.length} total orders</div>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
          {error}
        </div>
      )}

      {orders.length === 0 ? (
        <div className="text-center py-12">
          <Package className="w-16 h-16 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-600">No orders found</p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div
              key={order.id}
              className="border border-slate-200 rounded-lg overflow-hidden hover:border-slate-300 transition"
            >
              <div className="p-4 bg-slate-50 border-b border-slate-200">
                <div className="flex items-center justify-between">
                  <div>
                    <span className="font-semibold text-slate-900">Order #{order.id}</span>
                    <span className="mx-2 text-slate-400">•</span>
                    <span className="text-sm text-slate-600">Customer ID: {order.customerId}</span>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                    <button
                      onClick={() => handleDelete(order.id)}
                      className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </div>

              <div className="p-4">
                <div className="grid md:grid-cols-2 gap-4 mb-4 text-sm">
                  <div>
                    <p className="text-slate-500 mb-1">Shipping Address</p>
                    <p className="text-slate-900">{order.shippingAddress}</p>
                  </div>
                  <div>
                    <p className="text-slate-500 mb-1">Payment</p>
                    <p className="text-slate-900">
                      {order.payment.type} •••• {order.payment.cardNumber.slice(-4)}
                    </p>
                  </div>
                </div>

                <div className="border-t border-slate-200 pt-4">
                  <p className="text-sm text-slate-500 mb-2">Products ({order.products.length})</p>
                  <div className="flex flex-wrap gap-2">
                    {order.products.map((product, index) => (
                      <span key={index} className="text-xs bg-slate-100 text-slate-700 px-2 py-1 rounded">
                        {product.name}
                      </span>
                    ))}
                  </div>
                  <p className="text-right font-bold text-slate-900 mt-3">
                    Total: €{order.subtotal.toFixed(2)}
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
