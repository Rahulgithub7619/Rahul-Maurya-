package com.example.data.models

import java.util.UUID

enum class AppRole {
    CUSTOMER, ADMIN, DELIVERY_BOY
}

enum class MealCategory {
    ALL, BREAKFAST, VEG_THALI, SPECIAL_THALI, MINI_TIFFIN, SWEET_DESSERT, ADD_ONS
}

enum class MealType {
    ALL, BREAKFAST, LUNCH, DINNER
}

data class Meal(
    val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val price: Double,
    val calories: Int,
    val isVeg: Boolean = true,
    val rating: Float = 4.8f,
    val reviewsCount: Int = 120,
    val imageUrl: String,
    val category: MealCategory,
    val mealType: MealType,
    val isAvailable: Boolean = true,
    val isTodaySpecial: Boolean = false,
    val itemsIncludedEn: List<String> = emptyList(),
    val itemsIncludedHi: List<String> = emptyList()
)

enum class PlanDuration {
    DAILY, WEEKLY, MONTHLY
}

data class TiffinPlan(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val duration: PlanDuration,
    val pricePerMeal: Double,
    val totalPrice: Double,
    val discountPercent: Int,
    val descriptionEn: String,
    val descriptionHi: String,
    val featuresEn: List<String>,
    val featuresHi: List<String>,
    val isPopular: Boolean = false
)

enum class SubscriptionStatus {
    ACTIVE, PAUSED, EXPIRED, CANCELLED
}

data class Subscription(
    val id: String = "SUB-" + (1000..9999).random(),
    val userId: String = "USR-101",
    val planId: String,
    val planNameEn: String,
    val planNameHi: String,
    val duration: PlanDuration,
    val startDate: String,
    val endDate: String,
    val deliveryTimeSlot: String, // e.g. "Lunch (1:00 PM - 2:00 PM)"
    val autoRenew: Boolean = true,
    var status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    val skippedDates: MutableList<String> = mutableListOf(),
    val address: String,
    val totalAmount: Double
)

data class CartItem(
    val meal: Meal,
    var quantity: Int = 1,
    var customNotes: String = ""
)

enum class OrderStatus {
    CONFIRMED, PREPARING, PACKED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
}

enum class PaymentMethod {
    UPI, WALLET, CASH_ON_DELIVERY
}

data class Order(
    val id: String = "BALAJI-" + (10000..99999).random(),
    val userId: String = "USR-101",
    val items: List<CartItem>,
    val itemTotal: Double,
    val discount: Double,
    val deliveryFee: Double,
    val totalAmount: Double,
    val deliveryAddress: String,
    var status: OrderStatus = OrderStatus.CONFIRMED,
    val paymentMethod: PaymentMethod = PaymentMethod.UPI,
    val paymentStatus: String = "PAID",
    val createdAt: String,
    val estimatedDeliveryTime: String = "30-45 Mins",
    val driverName: String = "Ramesh Kumar",
    val driverPhone: String = "+91 98765 43210",
    val driverRating: Float = 4.9f
)

data class UserAddress(
    val id: String = UUID.randomUUID().toString(),
    val label: String, // Home, Office, Other
    val houseNo: String,
    val street: String,
    val landmark: String,
    val city: String = "Jaipur",
    val pincode: String = "302001",
    val isDefault: Boolean = false
)

data class UserProfile(
    val id: String = "USR-101",
    val name: String = "Rahul Sharma",
    val phone: String = "+91 98123 45678",
    val email: String = "rahul.sharma@example.com",
    var isHindi: Boolean = false,
    var isDarkMode: Boolean = false,
    var walletBalance: Double = 450.0,
    val referralCode: String = "BALAJI-RAHUL99",
    var loyaltyPoints: Int = 280,
    val addresses: MutableList<UserAddress> = mutableListOf(
        UserAddress("1", "Home", "Flat 302, Green Heights", "Mansarovar", "Near City Park", "Jaipur", "302020", true),
        UserAddress("2", "Office", "Tower B, IT Park", "Sitapura Industrial Area", "Opposite Genpact", "Jaipur", "302022", false)
    )
)

data class Coupon(
    val code: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val minOrderAmount: Double,
    val descriptionEn: String,
    val descriptionHi: String,
    val expiryDate: String
)

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val mealTitle: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val photoUrl: String? = null
)

enum class TransactionType {
    CREDIT, DEBIT
}

data class WalletTransaction(
    val id: String = "TXN-" + (10000..99999).random(),
    val title: String,
    val date: String,
    val amount: Double,
    val type: TransactionType
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleHi: String,
    val messageEn: String,
    val messageHi: String,
    val timestamp: String,
    var isRead: Boolean = false
)
