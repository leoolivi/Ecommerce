-- Mock Data for E-Commerce Application
-- Products with realistic data for testing

-- Clear existing data (optional - uncomment if needed)
-- DELETE FROM order_product;
-- DELETE FROM _orders;
-- DELETE FROM products;
-- DELETE FROM settings;

-- Electronics Products
-- INSERT INTO products (id, name, description, price, stock_quantity, category, image_url) VALUES
-- (1, 'Wireless Bluetooth Headphones', 'Premium noise-cancelling headphones with 30-hour battery life. Crystal clear sound quality and comfortable over-ear design perfect for music lovers and professionals.', 89.99, 45, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e'),
-- (2, 'Smart Fitness Watch', 'Track your health and fitness goals with built-in heart rate monitor, GPS, and sleep tracking. Water-resistant up to 50m with 7-day battery life.', 199.99, 30, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30'),
-- (3, 'Portable Power Bank 20000mAh', 'High-capacity power bank with dual USB ports and fast charging technology. Compatible with all smartphones and tablets. Compact and lightweight design.', 34.99, 120, 'Electronics', 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5'),
-- (4, 'USB-C Charging Cable 2m', 'Durable braided charging cable with fast data transfer. Compatible with all USB-C devices. Tangle-free design with reinforced connectors.', 12.99, 200, 'Electronics', 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0'),
-- (5, '4K Webcam with Ring Light', 'Professional webcam with built-in ring light and noise-cancelling microphone. Perfect for video calls, streaming, and content creation. Auto-focus and HDR support.', 79.99, 55, 'Electronics', 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04'),
-- (6, 'Mechanical Gaming Keyboard RGB', 'Premium mechanical keyboard with customizable RGB lighting. Durable switches rated for 50 million keystrokes. Ergonomic design with wrist rest included.', 129.99, 35, 'Electronics', 'https://images.unsplash.com/photo-1595225476474-87563907a212'),
-- (7, 'Wireless Gaming Mouse', 'High-precision optical sensor with adjustable DPI up to 16000. Lightweight design with programmable buttons and RGB lighting. 70-hour battery life.', 54.99, 68, 'Electronics', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46'),
-- (8, 'Laptop Stand Aluminum', 'Ergonomic aluminum laptop stand with adjustable height and angle. Improves posture and provides better cooling for your laptop. Compatible with all laptop sizes.', 39.99, 85, 'Electronics', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46'),

-- Home & Living
-- (9, 'Smart LED Bulb Kit (4 Pack)', 'WiFi-enabled smart bulbs with 16 million color options. Control via smartphone app or voice assistant. Energy-efficient with 25,000-hour lifespan.', 44.99, 95, 'Home & Living', 'https://images.unsplash.com/photo-1563089145-599997674d42'),
-- (10, 'Stainless Steel Water Bottle 1L', 'Double-walled insulated bottle keeps drinks cold for 24h or hot for 12h. BPA-free with leak-proof lid. Perfect for gym, office, or outdoor activities.', 24.99, 150, 'Home & Living', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8'),
-- (11, 'Aromatherapy Essential Oil Diffuser', 'Ultrasonic diffuser with 7 LED color options and auto shut-off. Creates a relaxing atmosphere with whisper-quiet operation. 300ml capacity runs up to 10 hours.', 32.99, 110, 'Home & Living', 'https://images.unsplash.com/photo-1608571423902-eed4a5ad8108'),
-- (12, 'Memory Foam Pillow Set (2)', 'Contoured cervical pillows with cooling gel layer. Hypoallergenic and dust mite resistant. Includes washable bamboo covers for maximum comfort.', 49.99, 70, 'Home & Living', 'https://images.unsplash.com/photo-1631049552240-59c37f38802b'),
-- (13, 'Robot Vacuum Cleaner', 'Smart navigation with app control and scheduling. Powerful suction with HEPA filter. Auto-charging and works on all floor types. 120-minute battery life.', 249.99, 22, 'Home & Living', 'https://images.unsplash.com/photo-1558317374-067fb5f30001'),

-- Fashion & Accessories
-- (14, 'Minimalist Leather Wallet', 'Genuine leather bifold wallet with RFID blocking technology. Slim design with 8 card slots and cash compartment. Available in black and brown.', 34.99, 88, 'Fashion', 'https://images.unsplash.com/photo-1627123424574-724758594e93'),
-- (15, 'Polarized Sunglasses UV400', 'Stylish sunglasses with 100% UV protection and polarized lenses. Lightweight metal frame with spring hinges. Includes protective case and cleaning cloth.', 39.99, 102, 'Fashion', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f'),
-- (16, 'Canvas Backpack 30L', 'Durable canvas backpack with padded laptop compartment (fits up to 15.6"). Multiple pockets and USB charging port. Water-resistant and comfortable shoulder straps.', 54.99, 64, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62'),
-- (17, 'Stainless Steel Watch Classic', 'Elegant minimalist watch with sapphire crystal and Japanese quartz movement. Water-resistant to 50m. Genuine leather strap with deployment clasp.', 149.99, 40, 'Fashion', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d'),

-- Sports & Outdoors
-- (18, 'Yoga Mat Premium 6mm', 'Non-slip textured surface with extra cushioning for joints. Eco-friendly TPE material, free from harmful chemicals. Includes carrying strap and storage bag.', 29.99, 125, 'Sports', 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f'),
-- (19, 'Resistance Bands Set (5 Bands)', 'Complete resistance band set with door anchor, handles, and ankle straps. Suitable for all fitness levels from beginners to athletes. Includes workout guide.', 24.99, 145, 'Sports', 'https://images.unsplash.com/photo-1598289431512-b97b0917affc'),
-- (20, 'Camping Tent 4-Person', 'Easy setup dome tent with rainfly and mesh windows. Spacious interior with gear pockets. Waterproof and UV-resistant fabric. Includes carrying bag.', 159.99, 28, 'Sports', 'https://images.unsplash.com/photo-1478131143081-80f7f84ca84d'),
-- (21, 'Hiking Backpack 50L', 'Professional trekking backpack with adjustable harness system. Multiple compartments and external attachment points. Rain cover included. Breathable back panel.', 89.99, 42, 'Sports', 'https://images.unsplash.com/photo-1622260614153-03223fb72052'),
-- (22, 'Bicycle Helmet Adjustable', 'Lightweight cycling helmet with 18 ventilation holes. CPSC certified with adjustable fit system. Removable visor and rear LED safety light included.', 44.99, 76, 'Sports', 'https://images.unsplash.com/photo-1611689342806-0863700ce1e4'),

-- Books & Office
-- (23, 'Wireless Presenter Remote', 'Professional presentation remote with laser pointer and digital timer. USB receiver with 30m range. Rechargeable battery lasts up to 200 hours.', 29.99, 92, 'Office', 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04'),
-- (24, 'Executive Notebook Set (3)', 'Premium hardcover notebooks with dotted pages. Elastic closure and bookmark ribbon. 192 pages per notebook. Perfect for journaling or work notes.', 19.99, 180, 'Office', 'https://images.unsplash.com/photo-1517842645767-c639042777db'),
-- (25, 'Desk Organizer Bamboo', 'Sustainable bamboo desk organizer with multiple compartments. Holds pens, phone, sticky notes, and small accessories. Keeps your workspace tidy and organized.', 27.99, 105, 'Office', 'https://images.unsplash.com/photo-1586075010923-2dd4570fb338'),

-- Kitchen & Dining
-- (26, 'French Press Coffee Maker 1L', 'Borosilicate glass French press with stainless steel frame. Makes 8 cups of rich, flavorful coffee. Dishwasher safe and heat-resistant. Includes extra filters.', 34.99, 82, 'Kitchen', 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6'),
-- (27, 'Knife Set Professional 8-Piece', 'German stainless steel knives with ergonomic handles. Includes chef knife, bread knife, utility knife, paring knife, and kitchen shears. Wooden block included.', 119.99, 38, 'Kitchen', 'https://images.unsplash.com/photo-1593618998160-e34014e67546'),
-- (28, 'Blender 1200W High-Speed', 'Professional blender with 6 stainless steel blades. Variable speed control and pulse function. 2L BPA-free pitcher. Perfect for smoothies, soups, and sauces.', 79.99, 45, 'Kitchen', 'https://images.unsplash.com/photo-1585515320310-259814833e62'),
-- (29, 'Cast Iron Skillet 30cm', 'Pre-seasoned cast iron pan with even heat distribution. Oven-safe up to 260°C. Includes silicone handle cover. Perfect for searing, baking, and frying.', 44.99, 67, 'Kitchen', 'https://images.unsplash.com/photo-1606923829579-0cb981a83e2e'),
-- (30, 'Digital Kitchen Scale 5kg', 'Precision scale with tare function and unit conversion. Large LCD display and tempered glass platform. Auto-off feature to save battery. Includes AAA batteries.', 19.99, 135, 'Kitchen', 'https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae');

-- Sample Orders
-- INSERT INTO _orders (id, subtotal, shipping_address, customer_id, status, type, card_number, card_holder, expiry_month, expiry_year, cvc) VALUES
-- (1, 109.98, 'Via Roma 123, 00100 Roma, Italy', 1, 'PAID', 'CREDIT_CARD', '4532123456789012', 'Mario Rossi', '12', '2026', '123'),
-- (2, 304.97, 'Corso Buenos Aires 45, 20124 Milano, Italy', 2, 'SHIPPED', 'CREDIT_CARD', '5425123456789012', 'Laura Bianchi', '08', '2025', '456'),
-- (3, 64.98, 'Via Garibaldi 78, 50123 Firenze, Italy', 3, 'PAID', 'PAYPAL', '4716123456789012', 'Giuseppe Verdi', '03', '2027', '789'),
-- (4, 159.99, 'Piazza San Marco 1, 30124 Venezia, Italy', 1, 'CREATED', 'CREDIT_CARD', '4532987654321098', 'Mario Rossi', '11', '2026', '321');

-- Order-Product relationships
-- INSERT INTO order_product (order_id, product_id) VALUES
-- Order 1: Headphones + Charging Cable
-- (1, 1),
-- (1, 4),
-- Order 2: Fitness Watch + Webcam + Gaming Mouse
-- (2, 2),
-- (2, 5),
-- (2, 7),
-- Order 3: Water Bottle + Yoga Mat
-- (3, 10),
-- (3, 18),
-- Order 4: Camping Tent
-- (4, 20);

-- Base users
INSERT INTO users (id, email, password, role) VALUES
(1, 'mario.rossi@gmail.com', '$2a$12$me.6hrc/bQTMbRViFS0ss.WgEnmJykbbMknz7IiPMeiK0GfQHziGe', 'CUSTOMER'),
(2, 'laura.bianchi@gmail.com', '$2a$12$me.6hrc/bQTMbRViFS0ss.WgEnmJykbbMknz7IiPMeiK0GfQHziGe', 'CUSTOMER'),
(3, 'giuseppe.verdi@gmail.com', '$2a$12$me.6hrc/bQTMbRViFS0ss.WgEnmJykbbMknz7IiPMeiK0GfQHziGe', 'ADMIN');

-- Reset sequences for auto-increment (PostgreSQL syntax)
-- ALTER SEQUENCE products_seq RESTART WITH 31;
-- ALTER SEQUENCE _orders_seq RESTART WITH 5;
-- ALTER SEQUENCE settings_seq RESTART WITH 6;

-- For H2 Database (if using H2):
-- ALTER SEQUENCE PRODUCTS_SEQ RESTART WITH 31;
-- ALTER SEQUENCE _ORDERS_SEQ RESTART WITH 5;
-- ALTER SEQUENCE SETTINGS_SEQ RESTART WITH 6;