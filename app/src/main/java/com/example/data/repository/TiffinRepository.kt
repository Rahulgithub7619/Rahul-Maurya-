package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TiffinRepository {

    // Current App Role
    private val _currentRole = MutableStateFlow(AppRole.CUSTOMER)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    // Auth state
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // User Profile
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Language state (false = English, true = Hindi)
    private val _isHindi = MutableStateFlow(false)
    val isHindi: StateFlow<Boolean> = _isHindi.asStateFlow()

    // Theme state (false = Light Mode, true = Dark Mode)
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Meals Menu
    private val initialMeals = listOf(
        Meal(
            id = "m1",
            titleEn = "Executive Shahi Thali",
            titleHi = "एग्जीक्यूटिव शाही थाली",
            descriptionEn = "Paneer Butter Masala, Dal Makhani, Jeera Rice, 4 Butter Tandoori Roti, Gulab Jamun, Salad & Papad",
            descriptionHi = "पनीर बटर मसाला, दाल मखनी, जीरा राइस, 4 तंदूरी बटर रोटी, गुलाब जामुन, सलाद और पापड़",
            price = 180.0,
            calories = 680,
            isVeg = true,
            rating = 4.9f,
            reviewsCount = 340,
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500&q=80",
            category = MealCategory.SPECIAL_THALI,
            mealType = MealType.LUNCH,
            isAvailable = true,
            isTodaySpecial = true,
            itemsIncludedEn = listOf("Paneer Masala", "Dal Makhani", "Jeera Rice", "4 Roti", "Gulab Jamun", "Papad"),
            itemsIncludedHi = listOf("पनीर मसाला", "दाल मखनी", "जीरा राइस", "4 रोटी", "गुलाब जामुन", "पापड़")
        ),
        Meal(
            id = "m2",
            titleEn = "Standard Homestyle Veg Tiffin",
            titleHi = "होमस्टाइल वेज टिफिन",
            descriptionEn = "Aloo Gobi Dry, Panchmel Dal, Steamed Rice, 4 Phulka Roti with Desi Ghee, Cucumber Salad & Pickle",
            descriptionHi = "आलू गोभी सूखी, पंचमेल दाल, स्टीम्ड राइस, देसी घी वाली 4 फुलका रोटी, खीरा सलाद और अचार",
            price = 120.0,
            calories = 520,
            isVeg = true,
            rating = 4.8f,
            reviewsCount = 210,
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500&q=80",
            category = MealCategory.VEG_THALI,
            mealType = MealType.LUNCH,
            isAvailable = true,
            isTodaySpecial = false,
            itemsIncludedEn = listOf("Aloo Gobi", "Panchmel Dal", "Rice", "4 Ghee Roti", "Salad", "Pickle"),
            itemsIncludedHi = listOf("आलू गोभी", "पंचमेल दाल", "चावल", "4 घी रोटी", "सलाद", "अचार")
        ),
        Meal(
            id = "m3",
            titleEn = "Aloo Paratha Breakfast Combo",
            titleHi = "आलू पराठा नाश्ता कॉम्बो",
            descriptionEn = "2 Stuffed Aloo Parathas with Amul Butter, Fresh Curd, Green Chutney & Hot Masala Chai",
            descriptionHi = "2 आलू पराठे अमूल बटर के साथ, ताजा दही, हरी चटनी और गरमा गरम मसाला चाय",
            price = 90.0,
            calories = 450,
            isVeg = true,
            rating = 4.9f,
            reviewsCount = 415,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500&q=80",
            category = MealCategory.BREAKFAST,
            mealType = MealType.BREAKFAST,
            isAvailable = true,
            isTodaySpecial = true,
            itemsIncludedEn = listOf("2 Aloo Parathas", "Fresh Curd", "Amul Butter", "Masala Chai"),
            itemsIncludedHi = listOf("2 आलू पराठे", "ताजा दही", "अमूल बटर", "मसाला चाय")
        ),
        Meal(
            id = "m4",
            titleEn = "Gujarati Special Thali",
            titleHi = "गुजराती स्पेशल थाली",
            descriptionEn = "Sweet Kadhai Sev Tamatar, Gujarati Kadi, Plain Rice, 5 Soft Rotli, Dhokla Piece & Pickle",
            descriptionHi = "सेव टमाटर, गुजराती कढ़ी, सादा चावल, 5 रोटली, ढोकला और अचार",
            price = 150.0,
            calories = 580,
            isVeg = true,
            rating = 4.7f,
            reviewsCount = 180,
            imageUrl = "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=500&q=80",
            category = MealCategory.SPECIAL_THALI,
            mealType = MealType.DINNER,
            isAvailable = true,
            isTodaySpecial = false,
            itemsIncludedEn = listOf("Sev Tamatar", "Gujarati Kadi", "Rice", "5 Rotli", "Dhokla"),
            itemsIncludedHi = listOf("सेव टमाटर", "गुजराती कढ़ी", "चावल", "5 रोटली", "ढोकला")
        ),
        Meal(
            id = "m5",
            titleEn = "Mini Express Tiffin",
            titleHi = "मिनी एक्सप्रेस टिफिन",
            descriptionEn = "Rajma Masala / Chole, Steamed Rice, 3 Soft Rotis & Onion Salad",
            descriptionHi = "राजमा मसाला / छोले, स्टीम्ड राइस, 3 सॉफ्ट रोटी और प्याज का सलाद",
            price = 85.0,
            calories = 410,
            isVeg = true,
            rating = 4.6f,
            reviewsCount = 145,
            imageUrl = "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=500&q=80",
            category = MealCategory.MINI_TIFFIN,
            mealType = MealType.LUNCH,
            isAvailable = true,
            isTodaySpecial = false,
            itemsIncludedEn = listOf("Rajma / Chole", "Rice", "3 Roti", "Salad"),
            itemsIncludedHi = listOf("राजमा / छोले", "चावल", "3 रोटी", "सलाद")
        ),
        Meal(
            id = "m6",
            titleEn = "South Indian Breakfast Platter",
            titleHi = "साउथ इंडियन ब्रेकफास्ट प्लेटर",
            descriptionEn = "2 Crispy Idlis, 1 Medu Vada, Plain Dosa with Hot Sambhar & Coconut Chutney",
            descriptionHi = "2 इडली, 1 मेदु वड़ा, प्लेन डोसा गरम सांभर और नारियल चटनी के साथ",
            price = 110.0,
            calories = 390,
            isVeg = true,
            rating = 4.8f,
            reviewsCount = 290,
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500&q=80",
            category = MealCategory.BREAKFAST,
            mealType = MealType.BREAKFAST,
            isAvailable = true,
            isTodaySpecial = false,
            itemsIncludedEn = listOf("2 Idlis", "1 Vada", "Plain Dosa", "Sambhar", "Chutney"),
            itemsIncludedHi = listOf("2 इडली", "1 वड़ा", "डोसा", "सांभर", "चटनी")
        ),
        Meal(
            id = "m7",
            titleEn = "Special Moong Dal Halwa",
            titleHi = "स्पेशल मूंग दाल हलवा",
            descriptionEn = "Rich Desi Ghee Moong Dal Halwa loaded with Almonds and Cashews (200g)",
            descriptionHi = "देसी घी से भरपूर बादाम और काजू वाला मूंग दाल हलवा (200g)",
            price = 80.0,
            calories = 320,
            isVeg = true,
            rating = 5.0f,
            reviewsCount = 510,
            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=500&q=80",
            category = MealCategory.SWEET_DESSERT,
            mealType = MealType.ALL,
            isAvailable = true,
            isTodaySpecial = true,
            itemsIncludedEn = listOf("Moong Dal Halwa 200g"),
            itemsIncludedHi = listOf("मूंग दाल हलवा 200g")
        ),
        Meal(
            id = "m8",
            titleEn = "Extra Tandoori Butter Roti (Set of 4)",
            titleHi = "एक्स्ट्रा तंदूरी बटर रोटी (4 का सेट)",
            descriptionEn = "Hot, fresh whole wheat tandoori rotis brushed with pure butter",
            descriptionHi = "गरमा गरम तंदूरी बटर रोटी",
            price = 40.0,
            calories = 240,
            isVeg = true,
            rating = 4.8f,
            reviewsCount = 95,
            imageUrl = "https://images.unsplash.com/photo-1626074353765-517a681e40be?w=500&q=80",
            category = MealCategory.ADD_ONS,
            mealType = MealType.ALL,
            isAvailable = true,
            isTodaySpecial = false,
            itemsIncludedEn = listOf("4 Butter Rotis"),
            itemsIncludedHi = listOf("4 बटर रोटी")
        )
    )

    private val _meals = MutableStateFlow(initialMeals)
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    // Tiffin Plans Available
    val availablePlans = listOf(
        TiffinPlan(
            id = "p_daily",
            nameEn = "Trial Daily Tiffin Plan",
            nameHi = "डेली ट्रायल टिफिन प्लान",
            duration = PlanDuration.DAILY,
            pricePerMeal = 130.0,
            totalPrice = 130.0,
            discountPercent = 5,
            descriptionEn = "Perfect for tasting our authentic homestyle food before long-term subscription.",
            descriptionHi = "लंबी सदस्यता से पहले हमारे घर जैसे भोजन का स्वाद लेने के लिए उत्तम।",
            featuresEn = listOf("1 Meal (Lunch or Dinner)", "Fresh Desi Ghee Rotis", "Free Delivery Included", "Cancel Anytime"),
            featuresHi = listOf("1 भोजन (दोपहर या रात)", "ताजा देसी घी रोटी", "मुफ्त डिलीवरी शामिल", "कभी भी रद्द करें")
        ),
        TiffinPlan(
            id = "p_weekly",
            nameEn = "Weekly Smart Saver",
            nameHi = "साप्ताहिक स्मार्ट सेवर",
            duration = PlanDuration.WEEKLY,
            pricePerMeal = 110.0,
            totalPrice = 770.0,
            discountPercent = 15,
            descriptionEn = "7 Days complete meal plan with daily rotating menu and weekend sweet dish treat.",
            descriptionHi = "प्रतिदिन बदलते मेनू और सप्ताहांत मीठे व्यंजन के साथ 7 दिनों की भोजन योजना।",
            featuresEn = listOf("7 Days Meal Coverage", "Choose Lunch or Dinner slot", "Skip any day with 2hr notice", "Weekend Sweet Dish Free"),
            featuresHi = listOf("7 दिन भोजन कवरेज", "लंच या डिनर स्लॉट चुनें", "2 घंटे की सूचना पर दिन स्किप करें", "वीकेंड स्वीट डिश फ्री"),
            isPopular = true
        ),
        TiffinPlan(
            id = "p_monthly",
            nameEn = "Monthly Master Subscription",
            nameHi = "मासिक मास्टर सब्स्क्रिप्शन",
            duration = PlanDuration.MONTHLY,
            pricePerMeal = 95.0,
            totalPrice = 2850.0,
            discountPercent = 25,
            descriptionEn = "30 Days comprehensive plan for students and working professionals. Maximum savings!",
            descriptionHi = "छात्रों और कामकाजी पेशेवरों के लिए 30 दिनों का सर्वश्रेष्ठ प्लान। सर्वाधिक बचत!",
            featuresEn = listOf("30 Days Lunch/Dinner", "Pause/Resume subscription anytime", "Holiday Calendar manager", "Free Special Sunday Thali", "Dedicated Delivery Boy"),
            featuresHi = listOf("30 दिन लंच/डिनर", "कभी भी सब्स्क्रिप्शन पॉज़/रिज्यूम करें", "हॉलिडे कैलेंडर मैनेजर", "मुफ्त स्पेशल संडे थाली", "समर्पित डिलीवरी बॉय")
        )
    )

    // Subscriptions
    private val initialSubscriptions = mutableListOf(
        Subscription(
            id = "SUB-8821",
            userId = "USR-101",
            planId = "p_monthly",
            planNameEn = "Monthly Master Subscription",
            planNameHi = "मासिक मास्टर सब्स्क्रिप्शन",
            duration = PlanDuration.MONTHLY,
            startDate = "2026-07-01",
            endDate = "2026-07-31",
            deliveryTimeSlot = "Lunch (1:00 PM - 2:00 PM)",
            autoRenew = true,
            status = SubscriptionStatus.ACTIVE,
            skippedDates = mutableListOf("2026-07-15", "2026-07-22"),
            address = "Flat 302, Green Heights, Mansarovar, Jaipur",
            totalAmount = 2850.0
        )
    )
    private val _subscriptions = MutableStateFlow<List<Subscription>>(initialSubscriptions)
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()

    // Cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Applied Coupon Code
    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    val availableCoupons = listOf(
        Coupon("BALAJI20", 20, 100.0, 200.0, "Get 20% OFF on orders above ₹200", "₹200 से अधिक के ऑर्डर पर 20% की छूट पाएं", "2026-12-31"),
        Coupon("WELCOME50", 50, 150.0, 150.0, "Flat 50% OFF on first order", "पहले ऑर्डर पर फ्लैट 50% की छूट", "2026-12-31"),
        Coupon("FESTIVAL100", 25, 100.0, 300.0, "Festival Special ₹100 Discount", "त्योहार विशेष ₹100 की छूट", "2026-08-15")
    )

    // Orders
    private val initialOrders = mutableListOf(
        Order(
            id = "BALAJI-91024",
            userId = "USR-101",
            items = listOf(
                CartItem(initialMeals[0], 1, "Less spicy please")
            ),
            itemTotal = 180.0,
            discount = 36.0,
            deliveryFee = 0.0,
            totalAmount = 144.0,
            deliveryAddress = "Flat 302, Green Heights, Mansarovar, Jaipur",
            status = OrderStatus.OUT_FOR_DELIVERY,
            paymentMethod = PaymentMethod.UPI,
            paymentStatus = "PAID",
            createdAt = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date()),
            estimatedDeliveryTime = "15-20 Mins",
            driverName = "Suresh Sharma",
            driverPhone = "+91 98290 11223"
        ),
        Order(
            id = "BALAJI-88410",
            userId = "USR-101",
            items = listOf(
                CartItem(initialMeals[1], 2),
                CartItem(initialMeals[6], 1)
            ),
            itemTotal = 320.0,
            discount = 50.0,
            deliveryFee = 20.0,
            totalAmount = 290.0,
            deliveryAddress = "Flat 302, Green Heights, Mansarovar, Jaipur",
            status = OrderStatus.DELIVERED,
            paymentMethod = PaymentMethod.WALLET,
            paymentStatus = "PAID",
            createdAt = "24 Jul, 01:15 PM"
        )
    )
    private val _orders = MutableStateFlow<List<Order>>(initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Wallet Transactions
    private val initialTransactions = listOf(
        WalletTransaction("TXN-9011", "Added via GPay UPI", "25 Jul, 10:30 AM", 500.0, TransactionType.CREDIT),
        WalletTransaction("TXN-8812", "Paid for Order #BALAJI-88410", "24 Jul, 01:15 PM", 290.0, TransactionType.DEBIT),
        WalletTransaction("TXN-7740", "Referral Bonus Received", "20 Jul, 04:00 PM", 240.0, TransactionType.CREDIT)
    )
    private val _walletTransactions = MutableStateFlow(initialTransactions)
    val walletTransactions: StateFlow<List<WalletTransaction>> = _walletTransactions.asStateFlow()

    // Notifications
    private val initialNotifications = listOf(
        NotificationItem(
            id = "n1",
            titleEn = "Order Out For Delivery! 🚴‍♂️",
            titleHi = "ऑर्डर डिलीवरी के लिए निकल चुका है! 🚴‍♂️",
            messageEn = "Delivery Partner Suresh Sharma is on his way with your hot Executive Shahi Thali.",
            messageHi = "डिलीवरी पार्टनर सुरेश शर्मा आपकी गरमा गरम शाही थाली लेकर आ रहे हैं।",
            timestamp = "10 Mins ago",
            isRead = false
        ),
        NotificationItem(
            id = "n2",
            titleEn = "20% OFF Festival Offer Enabled 🎉",
            titleHi = "20% की छूट का ऑफर शुरू 🎉",
            messageEn = "Use code BALAJI20 at checkout for instant 20% discount on all thalis.",
            messageHi = "सभी थाली पर तुरंत 20% छूट के लिए चेकआउट पर BALAJI20 कोड का उपयोग करें।",
            timestamp = "2 Hours ago",
            isRead = false
        ),
        NotificationItem(
            id = "n3",
            titleEn = "Subscription Renewal Notice",
            titleHi = "सदस्यता नवीनीकरण सूचना",
            messageEn = "Your Monthly Tiffin Subscription will auto-renew on 1st Aug. Tap to modify dates.",
            messageHi = "आपकी मासिक टिफिन सदस्यता 1 अगस्त को ऑटो-रिन्यू होगी। तिथियां बदलने के लिए टैप करें।",
            timestamp = "1 Day ago",
            isRead = true
        )
    )
    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Reviews
    private val initialReviews = listOf(
        Review(mealTitle = "Executive Shahi Thali", userName = "Ankit Verma", rating = 5.0f, comment = "Pure homestyle taste! Paneer was super soft and rotis had fresh desi ghee. Highly recommended!", date = "23 Jul 2026"),
        Review(mealTitle = "Aloo Paratha Breakfast", userName = "Priya Saini", rating = 4.8f, comment = "Chai was boiling hot and parathas were stuffed generously. On-time delivery!", date = "22 Jul 2026"),
        Review(mealTitle = "Standard Homestyle Veg", userName = "Vikram Singh", rating = 5.0f, comment = "Best tiffin service in Jaipur. Clean packing and hygienic stainless feel boxes.", date = "21 Jul 2026")
    )
    private val _reviews = MutableStateFlow(initialReviews)
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    // Functions

    fun setRole(role: AppRole) {
        _currentRole.value = role
    }

    fun toggleLanguage() {
        val newVal = !_isHindi.value
        _isHindi.value = newVal
        _userProfile.value = _userProfile.value.copy(isHindi = newVal)
    }

    fun toggleDarkMode() {
        val newVal = !_isDarkMode.value
        _isDarkMode.value = newVal
        _userProfile.value = _userProfile.value.copy(isDarkMode = newVal)
    }

    fun addToCart(meal: Meal, note: String = "") {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.meal.id == meal.id }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
        } else {
            current.add(CartItem(meal, 1, note))
        }
        _cartItems.value = current
    }

    fun removeFromCart(mealId: String) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.meal.id == mealId }
        if (index >= 0) {
            if (current[index].quantity > 1) {
                current[index] = current[index].copy(quantity = current[index].quantity - 1)
            } else {
                current.removeAt(index)
            }
        }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    fun applyCoupon(coupon: Coupon) {
        _appliedCoupon.value = coupon
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    fun placeOrder(address: String, paymentMethod: PaymentMethod): Order {
        val items = _cartItems.value
        val subtotal = items.sumOf { it.meal.price * it.quantity }
        val coupon = _appliedCoupon.value
        var discount = 0.0
        if (coupon != null && subtotal >= coupon.minOrderAmount) {
            discount = (subtotal * coupon.discountPercent / 100).coerceAtMost(coupon.maxDiscount)
        }
        val deliveryFee = if (subtotal > 200) 0.0 else 25.0
        val finalAmount = subtotal - discount + deliveryFee

        val newOrder = Order(
            items = items,
            itemTotal = subtotal,
            discount = discount,
            deliveryFee = deliveryFee,
            totalAmount = finalAmount,
            deliveryAddress = address,
            paymentMethod = paymentMethod,
            paymentStatus = "PAID",
            createdAt = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
        )

        // Deduct wallet if paid via wallet
        if (paymentMethod == PaymentMethod.WALLET) {
            val profile = _userProfile.value
            profile.walletBalance = (profile.walletBalance - finalAmount).coerceAtLeast(0.0)
            val newTxn = WalletTransaction(
                title = "Order #${newOrder.id}",
                date = "Just now",
                amount = finalAmount,
                type = TransactionType.DEBIT
            )
            _walletTransactions.value = listOf(newTxn) + _walletTransactions.value
        }

        val updatedOrders = listOf(newOrder) + _orders.value
        _orders.value = updatedOrders
        clearCart()

        // Notification
        val notif = NotificationItem(
            titleEn = "Order Placed Successfully! 🍛",
            titleHi = "ऑर्डर सफलतापूर्वक दर्ज किया गया! 🍛",
            messageEn = "Order #${newOrder.id} for ₹${finalAmount.toInt()} has been confirmed.",
            messageHi = "₹${finalAmount.toInt()} का आपका ऑर्डर #${newOrder.id} कन्फर्म हो गया है।",
            timestamp = "Just now"
        )
        _notifications.value = listOf(notif) + _notifications.value

        return newOrder
    }

    fun createSubscription(plan: TiffinPlan, deliverySlot: String, startDate: String, address: String): Subscription {
        val newSub = Subscription(
            planId = plan.id,
            planNameEn = plan.nameEn,
            planNameHi = plan.nameHi,
            duration = plan.duration,
            startDate = startDate,
            endDate = "2026-08-31",
            deliveryTimeSlot = deliverySlot,
            address = address,
            totalAmount = plan.totalPrice
        )
        _subscriptions.value = listOf(newSub) + _subscriptions.value
        return newSub
    }

    fun togglePauseSubscription(subId: String) {
        val current = _subscriptions.value.map { sub ->
            if (sub.id == subId) {
                val newStatus = if (sub.status == SubscriptionStatus.ACTIVE) SubscriptionStatus.PAUSED else SubscriptionStatus.ACTIVE
                sub.copy(status = newStatus)
            } else sub
        }
        _subscriptions.value = current
    }

    fun skipSubscriptionDate(subId: String, dateStr: String) {
        val current = _subscriptions.value.map { sub ->
            if (sub.id == subId) {
                val updatedSkipped = sub.skippedDates.toMutableList()
                if (updatedSkipped.contains(dateStr)) {
                    updatedSkipped.remove(dateStr)
                } else {
                    updatedSkipped.add(dateStr)
                }
                sub.copy(skippedDates = updatedSkipped)
            } else sub
        }
        _subscriptions.value = current
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val updated = _orders.value.map { order ->
            if (order.id == orderId) order.copy(status = newStatus) else order
        }
        _orders.value = updated
    }

    fun addMoneyToWallet(amount: Double) {
        val profile = _userProfile.value
        profile.walletBalance += amount
        val newTxn = WalletTransaction(
            title = "Added to Wallet via UPI",
            date = "Just now",
            amount = amount,
            type = TransactionType.CREDIT
        )
        _walletTransactions.value = listOf(newTxn) + _walletTransactions.value
    }

    fun addMealByAdmin(meal: Meal) {
        _meals.value = listOf(meal) + _meals.value
    }

    fun toggleMealAvailability(mealId: String) {
        _meals.value = _meals.value.map {
            if (it.id == mealId) it.copy(isAvailable = !it.isAvailable) else it
        }
    }

    fun deleteMealByAdmin(mealId: String) {
        _meals.value = _meals.value.filter { it.id != mealId }
    }

    fun addReview(mealTitle: String, rating: Float, comment: String) {
        val review = Review(
            mealTitle = mealTitle,
            userName = _userProfile.value.name,
            rating = rating,
            comment = comment,
            date = "Today"
        )
        _reviews.value = listOf(review) + _reviews.value
    }

    fun addAddress(address: UserAddress) {
        val current = _userProfile.value.addresses
        current.add(address)
    }

    fun sendPushNotificationByAdmin(titleEn: String, titleHi: String, bodyEn: String, bodyHi: String) {
        val item = NotificationItem(
            titleEn = titleEn,
            titleHi = titleHi,
            messageEn = bodyEn,
            messageHi = bodyHi,
            timestamp = "Just now"
        )
        _notifications.value = listOf(item) + _notifications.value
    }
}
